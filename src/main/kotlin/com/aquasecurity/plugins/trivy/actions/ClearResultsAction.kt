package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.ui.TrivyWindow
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

/**
 * ClearResultsAction removes the tree and findings
 */
class ClearResultsAction : AnAction() {
    private var project: Project? = null

    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        this.project = e.project

        if (project == null) {
            return
        }
        val toolWindow = ToolWindowManager.getInstance(project!!).getToolWindow("Trivy Findings")
        val content = toolWindow!!.contentManager.getContent(0)
        val TrivyWindow = content!!.component as TrivyWindow
        TrivyWindow.updateFindings(null)
        TrivyWindow.redraw()
    }
}