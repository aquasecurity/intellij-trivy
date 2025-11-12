package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.ui.TrivyScanOutputManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ShowScanOutputAction : AnAction() {
  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project ?: return
    TrivyScanOutputManager.showToolWindow(project)
  }

  override fun update(e: AnActionEvent) {
    super.update(e)
    e.presentation.text = "Show Scan Output"
    e.presentation.description = "Show the Trivy scan output"
  }
}
