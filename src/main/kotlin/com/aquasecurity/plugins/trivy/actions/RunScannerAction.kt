package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import java.io.File
import java.io.IOException
import javax.swing.SwingUtilities


/**
 * RunScannerAction executes Trivy then calls update results
 */
class RunScannerAction : AnAction() {
    private var project: Project? = null

    override fun update(e: AnActionEvent) {
        super.update(e)
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
                resultFile = File.createTempFile("Trivy", ".json")
            } catch (ex: IOException) {
                TrivyNotificationGroup.notifyError(project, ex.localizedMessage)
                return
            }

            val runner = TrivyBackgroundRunTask(
                project, resultFile
            ) { project: Project?, resultFile: File? ->
                ResultProcessor.updateResults(
                    project!!, resultFile
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