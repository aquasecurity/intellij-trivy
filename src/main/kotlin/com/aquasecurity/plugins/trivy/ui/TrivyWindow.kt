package com.aquasecurity.plugins.trivy.ui

import com.aquasecurity.plugins.trivy.model.commercial.AssuranceReport
import com.aquasecurity.plugins.trivy.model.commercial.Result as CommercialResult
import com.aquasecurity.plugins.trivy.model.report.Location
import com.aquasecurity.plugins.trivy.model.report.Report
import com.aquasecurity.plugins.trivy.model.report.Result
import com.aquasecurity.plugins.trivy.settings.TrivyProjectSettingState
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.aquasecurity.plugins.trivy.ui.treenodes.FileTreeNode
import com.aquasecurity.plugins.trivy.ui.treenodes.LocationTreeNode
import com.aquasecurity.plugins.trivy.ui.treenodes.PolicyTreeNode
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ui.JBColor
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.AsyncProcessIcon
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Paths
import java.util.function.Consumer
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class TrivyWindow(project: Project) : SimpleToolWindowPanel(false, true) {
  private val project: Project
  private val findingsHelper: FindingsHelper
  private var findings: Tree? = null
  private var assurancePolicies: Tree? = null
  private val projectSettings: TrivyProjectSettingState

  init {

    this.background = JBColor.PanelBackground
    this.project = project
    this.findingsHelper = FindingsHelper()
    this.projectSettings = TrivyProjectSettingState.getInstance(project)
    configureToolbar()
  }

  private fun configureToolbar() {
    val actionManager = ActionManager.getInstance()

    val actionGroup = DefaultActionGroup("ACTION_GROUP", false)

    actionGroup.add(
        actionManager.getAction("com.aquasecurity.plugins.trivy.actions.RunScannerAction"))
    actionGroup.add(
        actionManager.getAction("com.aquasecurity.plugins.trivy.actions.ClearResultsAction"))
    actionGroup.add(
        actionManager.getAction("com.aquasecurity.plugins.trivy.actions.ShowTrivySettingsAction"))

    val actionToolbar = actionManager.createActionToolbar("ACTION_TOOLBAR", actionGroup, true)
    actionToolbar.orientation = SwingConstants.VERTICAL
    this.toolbar = actionToolbar.component
  }

  override fun getContent(): JComponent? {
    return this.component
  }

  fun showRunning() {
    this.removeAll()
    this.findingsHelper.setHelp(null, null)
    this.add(runningPanel())
    this.validate()
    this.repaint()
  }

  private fun runningPanel(): JComponent {
    val panel = JPanel(FlowLayout(FlowLayout.LEFT))
    val margin = 10
    panel.border = BorderFactory.createEmptyBorder(margin, margin, margin, margin)
    val spinner = AsyncProcessIcon("Running")
    val label = JBLabel("Scanning...")

    panel.add(spinner, BorderLayout.WEST)
    panel.add(label, BorderLayout.CENTER)

    return panel
  }

  fun updateFindings(findings: Report?) {
    if (findings == null || findings.results == null) {
      this.findings = null
      return
    }
    val fileNodes: MutableList<FileTreeNode> = ArrayList()
    val rootNode = DefaultMutableTreeNode("Findings by type")

    findings.results.forEach(
        Consumer { f: Result ->
          if ((f.misconfigurations != null && f.misconfigurations!!.isNotEmpty()) ||
              (f.vulnerabilities != null && f.vulnerabilities!!.isNotEmpty()) ||
              (f.secrets != null && f.secrets!!.isNotEmpty()) ||
              (f.sasts != null && f.sasts!!.isNotEmpty())) {
            addOrUpdateTreeNode(f, fileNodes)
          }
        })

    fileNodes.forEach(Consumer { newChild: FileTreeNode? -> rootNode.add(newChild) })

    this.findings = Tree(rootNode)
    this@TrivyWindow.findings?.putClientProperty("JTree.lineStyle", "Horizontal")
    this@TrivyWindow.findings?.isRootVisible = false
    this@TrivyWindow.findings?.cellRenderer = FindingTreeRenderer()
    this@TrivyWindow.findings?.addMouseListener(
        object : MouseAdapter() {
          override fun mouseClicked(me: MouseEvent) {
            doMouseClicked(me, this@TrivyWindow.findings)
          }
        })
  }

  fun getHighlightSeverity(severity: String): HighlightSeverity {
    return when (severity) {
      "CRITICAL" -> HighlightSeverity.ERROR
      "HIGH" -> HighlightSeverity.ERROR
      "MEDIUM" -> HighlightSeverity.WARNING
      "LOW" -> HighlightSeverity.WEAK_WARNING
      else -> HighlightSeverity.INFORMATION
    }
  }

  fun updateAssuranceResults(assuranceReport: AssuranceReport?) {
    if (assuranceReport == null) {
      this.assurancePolicies = null
      return
    }

    val results = assuranceReport.results
    val report = assuranceReport.report

    if (results.isEmpty()) {
      this.assurancePolicies = null
      return
    }

    val rootNode = DefaultMutableTreeNode("Assurance Policies")

    val visitedTitle = mutableListOf<String>()

    results
        .sortedBy { it.filename }
        .sortedByDescending { it.severity }
        .forEach(
            Consumer { cr: CommercialResult ->
              if (cr.policyResults != null) {
                for (result in
                    cr.policyResults.filter {
                      it.controlResult != null && it.controlResult.size > 0
                    }) {
                  if (result.controlResult != null && !visitedTitle.contains(result.policyName)) {
                    visitedTitle.add(result.policyName)
                    rootNode.add(PolicyTreeNode(result.policyName, result))
                  }
                  val policyNode =
                      rootNode.children().toList().find {
                        it is PolicyTreeNode && it.title == result.policyName
                      } as PolicyTreeNode
                  result.controlResult?.forEach { ctl ->
                    val visitedKey = ctl.matchedData + cr.filename
                    if (!visitedTitle.contains(visitedKey)) {
                      policyNode.addControlResult(ctl, result, report, cr)
                      visitedTitle.add(visitedKey)
                    }
                  }
                }
              }
            })

    this.assurancePolicies = Tree(rootNode)
    this.assurancePolicies?.putClientProperty("JTree.lineStyle", "Horizontal")
    this.assurancePolicies?.isRootVisible = false
    this.assurancePolicies?.cellRenderer = FindingTreeRenderer()
    this.assurancePolicies?.addMouseListener(
        object : MouseAdapter() {
          override fun mouseClicked(me: MouseEvent) {
            doMouseClicked(me, this@TrivyWindow.assurancePolicies)
          }
        })

    return
  }

  private fun addOrUpdateTreeNode(finding: Result, fileNodes: MutableList<FileTreeNode>) {
    val match =
        fileNodes.stream().filter { r: FileTreeNode -> r.target == finding.target }.findFirst()
    if (match.isPresent) {
      match.get().update(finding)
      return
    }

    val f = FileTreeNode(finding)
    fileNodes.add(f)
  }

  fun doMouseClicked(e: MouseEvent, tree: Tree?) {
    if (tree == null) {
      return
    }
    // clear the help panel
    findingsHelper.setHelp(null, null)
    val lastSelectedNode = tree.lastSelectedPathComponent ?: return
    if (lastSelectedNode is LocationTreeNode) {
      val node = lastSelectedNode
      if (node.childCount == 0) {
        val findingLocation = node.location
        findingsHelper.setHelp(node.getHelpObject(), findingLocation.filename)
        openFileLocation(findingLocation)
        return
      }
    }
    val path: TreePath? = tree.getPathForLocation(e.x, e.y)
    if (path != null) {
      if (tree.isExpanded(path)) {
        tree.collapsePath(path)
      } else {
        tree.expandPath(path)
      }
    }
  }

  private fun openFileLocation(findingLocation: Location?) {
    if (findingLocation == null) {
      return
    }
    val file =
        VirtualFileManager.getInstance()
            .refreshAndFindFileByNioPath(Paths.get(project.basePath, findingLocation.filename))
    if (file == null || !file.exists()) {
      TrivyNotificationGroup.notifyInformation(
          project, String.format("File %s cannot be opened", findingLocation.filename))
      return
    }

    val startLine = maxOf(findingLocation.startLine ?: 0, 1)
    val endLine = maxOf(findingLocation.endLine ?: 0, 1)
    val ofd = OpenFileDescriptor(project, file, startLine - 1, 0)

    val editor = FileEditorManager.getInstance(project).openTextEditor(ofd, true) ?: return
    editor.selectionModel.setBlockSelection(
        LogicalPosition(startLine - 1, 0), LogicalPosition(endLine - 1, 1000))
  }

  fun redraw() {
    this.removeAll()
    if (this.findings != null) {
      updateView()
    }
    configureToolbar()
    this.validate()
    this.repaint()
  }

  private fun updateView() {
    val tabPane = JBTabbedPane()
    val findingsPane = JBScrollPane(this.findings)
    findingsPane.border = null
    tabPane.addTab("Findings", findingsPane)

    if (this.assurancePolicies != null) {
      val assurancePane = JBScrollPane(this.assurancePolicies)
      assurancePane.border = null
      tabPane.addTab("Assurance Policies", assurancePane)
    }

    val splitter = JBSplitter(true, 0.6f)
    splitter.isShowDividerControls = true
    splitter.setDividerWidth(3)
    splitter.border = null
    splitter.firstComponent = tabPane
    splitter.divider.background = JBColor.namedColor("ScrollBar.thumb", JBColor.GRAY)

    val helper = JBScrollPane(this.findingsHelper)
    helper.border = null
    splitter.secondComponent = helper

    this.add(splitter)
  }
}
