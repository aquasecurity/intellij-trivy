package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.settings.CredentialCheck
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
    commandParts.add(String.format("--output=%s", resultFile.absolutePath))
    commandParts.add(project.basePath)

    val commandLine = GeneralCommandLine(commandParts)
    commandLine.setWorkDirectory(project.basePath)

    if (projectSettings.useAquaPlatform) {
      super.setTitle("Running Aqua Platform Scan")
      configureCommandLineEnv(commandLine, projectSettings, resultFile)

      // verify the credentials firts
      if (!CredentialCheck.isValidCredentials(
          commandLine.environment.get("AQUA_KEY") ?: "",
          commandLine.environment.get("AQUA_SECRET") ?: "",
          commandLine.environment.get("AQUA_URL") ?: "",
          commandLine.environment.get("CSPM_URL") ?: "")) {
        TrivyNotificationGroup.notifyError(
            project, "Invalid Aqua Platform credentials. Please check your settings.")
        return
      }
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

  private fun getEnvUrls(region: String): Pair<String, String> {

    return when (region) {
      "Dev" ->
          Pair(
              "https://stage.api.cloudsploit.com", "https://api.dev.supply-chain.cloud.aquasec.com")

      "EU" ->
          Pair("https://eu.api.cloudsploit.com", "https://api.eu.supply-chain.cloud.aquasec.com")

      "Singapore" ->
          Pair(
              "https://ap-1.api.cloudsploit.com", "https://api.ap-1.supply-chain.cloud.aquasec.com")

      "Sydney" ->
          Pair(
              "https://ap-2.api.cloudsploit.com", "https://api.ap-2.supply-chain.cloud.aquasec.com")
      else -> Pair("https://api.cloudsploit.com", "https://api.supply-chain.cloud.aquasec.com")
    }
  }

  private fun configureCommandLineEnv(
      commandLine: GeneralCommandLine,
      projectSettings: TrivyProjectSettingState,
      resultFile: File
  ) {

    if (TrivySettingState.instance.region == "Custom") {
      if (TrivySettingState.instance.customAquaUrl.isNotEmpty()) {
        commandLine.environment["AQUA_URL"] = TrivySettingState.instance.customAquaUrl
      }
      if (TrivySettingState.instance.customAuthUrl.isNotEmpty()) {
        commandLine.environment["CSPM_URL"] = TrivySettingState.instance.customAuthUrl
      }
    } else {
      val urls = getEnvUrls(TrivySettingState.instance.region)
      val cspmServerURL = urls.first
      val aquaApiURL = urls.second

      if (aquaApiURL != "") {
        commandLine.environment["AQUA_URL"] = aquaApiURL
      }
      if (cspmServerURL != "") {
        commandLine.environment["CSPM_URL"] = cspmServerURL
      }
    }

    commandLine.environment["AQUA_KEY"] = TrivySettingState.instance.apiKey
    commandLine.environment["AQUA_SECRET"] = TrivySettingState.instance.apiSecret
    commandLine.environment["TRIVY_RUN_AS_PLUGIN"] = "aqua"
    commandLine.environment["AQUA_ASSURANCE_EXPORT"] =
        resultFile.absolutePath.replace(".json", "_assurance.json")
    commandLine.environment["TRIVY_SKIP_REPOSITORY_UPLOAD"] = "true"
    commandLine.environment["TRIVY_SKIP_RESULT_UPLOAD"] = "true"

    if (projectSettings.enableDotNetProject) {
      commandLine.environment["DOTNET_PROJ"] = "1"
    }
    if (projectSettings.enableGradle) {
      commandLine.environment["GRADLE"] = "1"
    }
    if (projectSettings.enablePackageJson) {
      commandLine.environment["PACKAGE_JSON"] = "1"
    }
    if (projectSettings.enableSASTScanning) {
      commandLine.environment["SAST"] = "1"
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
