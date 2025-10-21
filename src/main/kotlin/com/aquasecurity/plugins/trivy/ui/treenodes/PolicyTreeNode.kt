package com.aquasecurity.plugins.trivy.ui.treenodes

import com.aquasecurity.plugins.trivy.model.commercial.ControlResult
import com.aquasecurity.plugins.trivy.model.commercial.PolicyResult
import com.aquasecurity.plugins.trivy.model.commercial.Result
import com.aquasecurity.plugins.trivy.model.report.Report
import com.intellij.icons.AllIcons
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

class PolicyTreeNode(private val policyTitle: String, result: PolicyResult?) :
    DefaultMutableTreeNode(), TrivyTreeNode {
  private val policyDescription = result?.reason

  fun removeAnsiCodes(input: String): String {
    val ansiRegex = "\u001B\\[[;\\d]*m".toRegex()
    return input.replace("\n", ": ").replace(ansiRegex, "")
  }

  fun addControlResult(
      controlResult: ControlResult,
      result: PolicyResult,
      report: Report,
      cr: Result
  ) {

    val prettyTitle = removeAnsiCodes(controlResult.type)

    var existing =
        children().toList().find { it is PolicyTreeNode && it.policyTitle == prettyTitle }
    if (existing == null) {
      existing = PolicyTreeNode(prettyTitle, result)
      add(existing)
    }

    var locationNodes: List<LocationTreeNode> = listOf()

    cr.policyResults?.forEach { pr ->
      if (pr.failed != null && pr.failed == true) {
        pr.controlResult?.forEach { ctl ->
          val r = report.findMatchingResult(cr.filename, ctl.matchedData)
          if (!r.isEmpty()) {
            r.forEach {
              val locNode = LocationTreeNode(cr.filename, "", it!!, true)
              locNode.setHelperObject(it)
              locationNodes += locNode
            }
          }
        }
      }
    }

    locationNodes.distinctBy { it -> it.title }.forEach { (existing as PolicyTreeNode).add(it) }
  }

  override val icon: Icon?
    get() = AllIcons.Actions.MenuPaste

  override val title: String?
    get() = policyTitle

  override val tooltip: String?
    get() = policyDescription
}
