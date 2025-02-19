package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import java.io.File
import java.io.IOException
import java.util.UUID.randomUUID
import javax.swing.SwingUtilities


/**
 * RunScannerAction executes Trivy then calls update results
 */
class RunScannerAction : AnAction() {
    private var project: Project? = null

    override fun update(e: AnActionEvent) {
        super.update(e)
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

    companion object {

        fun runTrivy(project: Project) {
            val resultFile: File
            try {
                val pluginTempDir = File(PathManager.getSystemPath(), "Trivy")
                val id = randomUUID().toString()
                resultFile = FileUtil.createTempFile(pluginTempDir, String.format("trivy-%s",id ), ".json", true)
            } catch (ex: IOException) {
                TrivyNotificationGroup.notifyError(project, ex.localizedMessage)
                return
            }

            val runner = TrivyBackgroundRunTask(
                project,
                resultFile
            ) { _, _ ->
                ResultProcessor.updateResults(
                    project, resultFile
                )
            }
            if (SwingUtilities.isEventDispatchThread()) {
                ProgressManager.getInstance().run(runner)
            } else {
                ApplicationManager.getApplication().invokeLater(runner)
            }
        }
    }
}