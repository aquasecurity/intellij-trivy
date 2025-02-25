package com.aquasecurity.plugins.trivy.ui

import com.aquasecurity.plugins.trivy.model.Findings
import com.aquasecurity.plugins.trivy.model.Location
import com.aquasecurity.plugins.trivy.model.Result
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.aquasecurity.plugins.trivy.ui.treenodes.FileTreeNode
import com.aquasecurity.plugins.trivy.ui.treenodes.LocationTreeNode
import com.aquasecurity.plugins.trivy.ui.treenodes.SecretTreeNode
import com.aquasecurity.plugins.trivy.ui.treenodes.VulnerabilityTreeNode
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.treeStructure.Tree
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Paths
import java.util.function.Consumer
import javax.swing.JComponent
import javax.swing.JSplitPane
import javax.swing.SwingConstants
import javax.swing.tree.DefaultMutableTreeNode

class TrivyWindow(project: Project) : SimpleToolWindowPanel(false, true) {
    private val project: Project
    private val findingsHelper: FindingsHelper
    private var root: Tree? = null


    init {
        this.background = JBColor.PanelBackground
        this.project = project
        this.findingsHelper = FindingsHelper()
        configureToolbar()
    }

    private fun configureToolbar() {
        val actionManager = ActionManager.getInstance()

        val actionGroup = DefaultActionGroup("ACTION_GROUP", false)

        actionGroup.add(actionManager.getAction("com.aquasecurity.plugins.trivy.actions.RunScannerAction"))
        actionGroup.add(actionManager.getAction("com.aquasecurity.plugins.trivy.actions.ClearResultsAction"))
        actionGroup.add(actionManager.getAction("com.aquasecurity.plugins.trivy.actions.ShowTrivySettingsAction"))

        val actionToolbar = actionManager.createActionToolbar("ACTION_TOOLBAR", actionGroup, true)
        actionToolbar.orientation = SwingConstants.VERTICAL
        this.toolbar = actionToolbar.component
    }

    override fun getContent(): JComponent? {
        return this.component
    }


    fun updateFindings(findings: Findings?) {
        if (findings == null) {
            this.root = null
            return
        }
        val fileNodes: MutableList<FileTreeNode> = ArrayList()
        findings.setBasePath(project.basePath)
        val rootNode = DefaultMutableTreeNode("Findings by type")


        findings.results!!.forEach(Consumer { f: Result ->
            if (f.misconfigurations != null && f.misconfigurations!!.size > 0 || f.vulnerabilities != null && f.vulnerabilities!!.size > 0 || f.secrets != null && f.secrets!!.size > 0) {
                addOrUpdateTreeNode(f, fileNodes)
            }
        })

        fileNodes.forEach(Consumer { newChild: FileTreeNode? -> rootNode.add(newChild) })
        this.root = Tree(rootNode)
        root!!.putClientProperty("JTree.lineStyle", "Horizontal")
        root!!.isRootVisible = false
        root!!.cellRenderer = FindingTreeRenderer()
        root!!.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(me: MouseEvent) {
                doMouseClicked()
            }
        })
    }

    private fun addOrUpdateTreeNode(finding: Result, fileNodes: MutableList<FileTreeNode>) {
        val match = fileNodes.stream().filter { r: FileTreeNode -> r.target == finding.target }.findFirst()
        if (match.isPresent) {
            match.get().update(finding)
            return
        }

        val f = FileTreeNode(finding)
        fileNodes.add(f)
    }


    fun doMouseClicked() {
        val lastSelectedNode = root!!.lastSelectedPathComponent ?: return
        if (lastSelectedNode is LocationTreeNode) {
            val node = lastSelectedNode
            val findingLocation = node.location
            findingsHelper.setMisconfiguration(node.misconfiguration, findingLocation.Filename)
            openFileLocation(findingLocation)
        } else if (lastSelectedNode is VulnerabilityTreeNode) {
            val node = lastSelectedNode
            val findingLocation = node.location
            findingsHelper.setVulnerability(node.vulnerability, findingLocation.Filename)
            openFileLocation(findingLocation)
        } else if (lastSelectedNode is SecretTreeNode) {
            val node = lastSelectedNode
            findingsHelper.setSecret(node.secret, node.location.Filename)
            openFileLocation(node.location)
        }
    }

    private fun openFileLocation(findingLocation: Location?) {
        if (findingLocation == null) {
            return
        }
        val file = VirtualFileManager.getInstance()
            .refreshAndFindFileByNioPath(Paths.get(project.basePath, findingLocation.Filename))
        if (file == null || !file.exists()) {
            TrivyNotificationGroup.notifyInformation(
                project,
                String.format("File %s cannot be opened", findingLocation.Filename)
            )
            return
        }
        val ofd = OpenFileDescriptor(project, file, findingLocation.StartLine - 1, 0)

        val editor = FileEditorManager.getInstance(project).openTextEditor(ofd, true) ?: return
        editor.selectionModel
            .setBlockSelection(
                LogicalPosition(findingLocation.StartLine - 1, 0),
                LogicalPosition(findingLocation.EndLine - 1, 1000)
            )
    }

    fun redraw() {
        this.removeAll()
        if (this.root != null) {
            updateView()
        }

        configureToolbar()
        this.validate()
        this.repaint()
    }

    private fun updateView() {


        val splitPane = JSplitPane(0)
        splitPane.border = null
        splitPane.dividerSize = 2
        splitPane.isContinuousLayout = true

        val tv =  JBScrollPane(this.root)
        tv.border = null
        val findings = JBScrollPane(this.findingsHelper)
        findings.border = null
        splitPane.add(tv)
        splitPane.add(findings)

        this.add(splitPane)
    }
}
