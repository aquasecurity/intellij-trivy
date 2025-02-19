package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.settings.TrivySettingState
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import java.io.File
import java.util.function.BiConsumer
import javax.swing.SwingUtilities

internal class TrivyBackgroundRunTask(
    private val project: Project,
    private val resultFile: File,
    private val callback: BiConsumer<Project, File>
) :
    Backgroundable(project, "Running Trivy", false), Runnable {
    override fun run(indicator: ProgressIndicator) {
        this.run()
    }

    override fun run() {
        val severities = requiredSeverities

        val commandParts: MutableList<String?> = ArrayList()
        commandParts.add(TrivySettingState.instance.TrivyPath)
        commandParts.add("fs")

        var requiredChecks = "misconfig,vuln"
        if (TrivySettingState.instance.SecretScanning) {
            requiredChecks = String.format("%s,secret", requiredChecks)
        }

        commandParts.add(String.format("--scanners=%s", requiredChecks))
        commandParts.add(String.format("--severity=%s", severities))

        if (TrivySettingState.instance.OfflineScan) {
            commandParts.add("--offline-scan")
        }

        if (TrivySettingState.instance.IgnoreUnfixed) {
            commandParts.add("--ignore-unfixed")
        }

        commandParts.add("--format=json")
        commandParts.add(String.format("--output=%s", resultFile.absolutePath))
        commandParts.add(project.basePath)

        val commandLine = GeneralCommandLine(commandParts)

        val process: Process
        try {
            process = commandLine.createProcess()
        } catch (e: ExecutionException) {
            TrivyNotificationGroup.notifyError(project, e.localizedMessage)
            return
        }
        val handler = OSProcessHandler(process, commandLine.commandLineString)

        try {
            ScriptRunnerUtil.getProcessOutput(
                handler,
                ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER,
                100000000
            )
            TrivyNotificationGroup.notifyInformation(project, "Trivy run completed, updating results")
            SwingUtilities.invokeLater {
                callback.accept(this.project, this.resultFile)
            }
        } catch (e: ExecutionException) {
            TrivyNotificationGroup.notifyError(project, e.localizedMessage)
        }
    }

    private val requiredSeverities: String
        get() {
            val requiredSeverities: MutableList<String> = ArrayList()

            if (TrivySettingState.instance.CriticalSeverity) {
                requiredSeverities.add("CRITICAL")
            }
            if (TrivySettingState.instance.HighSeverity) {
                requiredSeverities.add("HIGH")
            }
            if (TrivySettingState.instance.MediumSeverity) {
                requiredSeverities.add("MEDIUM")
            }
            if (TrivySettingState.instance.LowSeverity) {
                requiredSeverities.add("LOW")
            }
            if (TrivySettingState.instance.UnknownSeverity) {
                requiredSeverities.add("UNKNOWN")
            }

            return java.lang.String.join(",", requiredSeverities)
        }
}