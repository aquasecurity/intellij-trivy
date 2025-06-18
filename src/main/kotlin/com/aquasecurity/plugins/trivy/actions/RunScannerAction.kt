package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.ui.TrivyWindow
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.wm.ToolWindowManager
import java.io.File
import java.util.UUID.randomUUID
import javax.swing.SwingUtilities

object TrivyRunState {
  var isRunning = false
}

/** RunScannerAction executes Trivy then calls update results */
class RunScannerAction : AnAction() {
  private var project: Project? = null

  override fun update(e: AnActionEvent) {
    super.update(e)

    e.presentation.isEnabled =
        com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance.trivyInstalled
  }

  override fun getActionUpdateThread(): ActionUpdateThread {
    return super.getActionUpdateThread()
  }

  override fun actionPerformed(e: AnActionEvent) {
    this.project = e.project

    if (project == null) {
      return
    }
    runTrivy(this.project!!)
  }

  private fun runTrivy(project: Project) {
    if (TrivyRunState.isRunning) {
      return
    }

    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Trivy Explorer")
    val content = toolWindow?.contentManager?.getContent(0)
    val trivyWindow = content?.component as? TrivyWindow ?: return

    try {
      trivyWindow.showRunning()
      TrivyRunState.isRunning = true
      val resultFile: File
      val pluginTempDir = File(PathManager.getSystemPath(), "Trivy")
      val id = randomUUID().toString()
      resultFile =
          FileUtil.createTempFile(pluginTempDir, String.format("trivy-%s", id), ".json", true)
      val runner =
          TrivyBackgroundRunTask(project, resultFile) { _, _ ->
            ResultProcessor.updateResults(project, resultFile, trivyWindow)
            TrivyRunState.isRunning = false
          }
      if (SwingUtilities.isEventDispatchThread()) {
        ProgressManager.getInstance().run(runner)
      } else {
        ApplicationManager.getApplication().invokeLater(runner)
      }
    } catch (ex: Exception) {
      TrivyNotificationGroup.notifyError(project, ex.localizedMessage)
      TrivyRunState.isRunning = false
    }
  }
}
