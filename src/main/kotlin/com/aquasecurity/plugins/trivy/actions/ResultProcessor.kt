package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.model.Findings
import com.aquasecurity.plugins.trivy.ui.TrivyWindow
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import java.io.File
import java.io.IOException

/**
 * ResultProcessor takes the results finding and unmarshalls to object
 * Then updates the findings window
 */
object ResultProcessor {
    fun updateResults(project: Project, resultFile: File?) {
        if (!resultFile!!.exists()) {
            TrivyNotificationGroup.notifyError(
                project,
                "Failed to find the results file."
            )
            return
        }

        val findings: Findings
        try {
            val findingsMapper = ObjectMapper()
            findingsMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            findingsMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            findings = findingsMapper.readValue(resultFile, Findings::class.java)
        } catch (e: IOException) {
            TrivyNotificationGroup.notifyError(
                project,
                String.format("Failed to deserialize the results file. %s", e.localizedMessage)
            )
            return
        }

        // redraw the explorer with the updated content
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Trivy Findings")
        val content = toolWindow!!.contentManager.getContent(0)
        val TrivyWindow = content!!.component as TrivyWindow
        TrivyWindow.updateFindings(findings)
        TrivyWindow.redraw()
    }
}