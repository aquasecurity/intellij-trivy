package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.settings.TrivyProjectSettingState
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

const val DEFAULT_CSPM_URL = "https://cloud.aquasec.com"
const val DEFAULT_AQUA_API_URL = "https://api.aquasec.com"

internal class TrivyBackgroundRunTask(
    private val project: Project,
    private val resultFile: File,
    private val callback: BiConsumer<Project, File>
) : Backgroundable(project, "Running Trivy", false), Runnable {
  override fun run(indicator: ProgressIndicator) {
    this.run()
  }

  override fun run() {
    val settings = TrivySettingState.instance
    val projectSettings = TrivyProjectSettingState.getInstance(project)

    val commandParts: MutableList<String?> = ArrayList()
    commandParts.add(settings.trivyPath)
    commandParts.add("fs")

    commandParts.add("--list-all-pkgs")
    commandParts.add(String.format("--scanners=%s", requiredChecks(settings)))
    commandParts.add(String.format("--severity=%s", requiredSeverities(settings)))

    if (settings.offlineScan) {
      commandParts.add("--offline-scan")
    }

    if (settings.ignoreUnfixed) {
      commandParts.add("--ignore-unfixed")
    }

    if (projectSettings.useConfig && projectSettings.configPath != "") {
      commandParts.add(String.format("--config=%s", projectSettings.configPath))
    }

    if (projectSettings.useIgnore && projectSettings.ignorePath != "") {
      commandParts.add(String.format("--ignorefile=%s", projectSettings.ignorePath))
    }

    commandParts.add("--format=json")
    if (!projectSettings.useAquaPlatform) {
      commandParts.add(String.format("--output=%s", resultFile.absolutePath))
    }
    commandParts.add(project.basePath)

    val commandLine = GeneralCommandLine(commandParts)
    commandLine.setWorkDirectory(project.basePath)

    if (projectSettings.useAquaPlatform) {
      configureCommandLineEnv(commandLine, projectSettings, resultFile)
    }

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
          handler, ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER, 100000000)
      TrivyNotificationGroup.notifyInformation(project, "Trivy run completed, updating results")
      SwingUtilities.invokeLater { callback.accept(this.project, this.resultFile) }
    } catch (e: ExecutionException) {
      TrivyNotificationGroup.notifyError(project, e.localizedMessage)
    }
  }

  private fun configureCommandLineEnv(
      commandLine: GeneralCommandLine,
      projectSettings: TrivyProjectSettingState,
      resultFile: File
  ) {
    if (TrivySettingState.instance.aquaApiURL != "") {
      commandLine.environment["AQUA_URL"] = TrivySettingState.instance.aquaApiURL
    }
    if (TrivySettingState.instance.cspmServerURL != "") {
      commandLine.environment["CSPM_URL"] = TrivySettingState.instance.cspmServerURL
    }
    commandLine.environment["AQUA_KEY"] = TrivySettingState.instance.apiKey
    commandLine.environment["AQUA_SECRET"] = TrivySettingState.instance.apiSecret
    commandLine.environment["TRIVY_RUN_AS_PLUGIN"] = "aqua"
    commandLine.environment["AQUA_ASSURANCE_EXPORT"] = resultFile.absolutePath

    if (!projectSettings.uploadResults) {
      commandLine.environment["TRIVY_SKIP_REPOSITORY_UPLOAD"] = "true"
      commandLine.environment["TRIVY_SKIP_RESULT_UPLOAD"] = "true"
    }
  }

  private fun requiredSeverities(settings: TrivySettingState): String {
    val requiredSeverities: MutableList<String> = ArrayList()

    if (settings.criticalSeverity) {
      requiredSeverities.add("CRITICAL")
    }
    if (settings.highSeverity) {
      requiredSeverities.add("HIGH")
    }
    if (settings.mediumSeverity) {
      requiredSeverities.add("MEDIUM")
    }
    if (settings.lowSeverity) {
      requiredSeverities.add("LOW")
    }
    if (settings.unknownSeverity) {
      requiredSeverities.add("UNKNOWN")
    }

    return requiredSeverities.joinToString(separator = ",")
  }

  private fun requiredChecks(settings: TrivySettingState): String {
    var requiredChecks: MutableList<String> = ArrayList()
    if (settings.scanForMisconfigurations) {
      requiredChecks.add("misconfig")
    }
    if (settings.scanForVulnerabilities) {
      requiredChecks.add("vuln")
    }
    if (TrivySettingState.instance.scanForSecrets) {
      requiredChecks.add("secret")
    }

    return requiredChecks.joinToString(separator = ",")
  }
}
