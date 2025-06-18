package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.actions.tasks.TrivyDownloadBinaryTask
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import javax.swing.SwingUtilities

class InstallTrivyAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    ProgressManager.getInstance().run(TrivyDownloadBinaryTask(e.project!!, false))
  }

  companion object {
    fun runDownload(project: Project, initial: Boolean = false) {
      val runner = TrivyDownloadBinaryTask(project, initial)
      if (SwingUtilities.isEventDispatchThread()) {
        ProgressManager.getInstance().run(runner)
      } else {
        ApplicationManager.getApplication().invokeLater(runner)
      }
    }
  }

  override fun getActionUpdateThread(): ActionUpdateThread {
    return super.getActionUpdateThread()
  }
}
