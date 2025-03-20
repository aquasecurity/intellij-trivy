package com.aquasecurity.plugins.trivy.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class ShowTrivySettingsAction : AnAction() {
  override fun update(e: AnActionEvent) {
    super.update(e)
  }

  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project ?: return

    ShowSettingsUtil.getInstance().showSettingsDialog(project, "Trivy: Settings")
  }

  override fun getActionUpdateThread(): ActionUpdateThread {
    return super.getActionUpdateThread()
  }
}
