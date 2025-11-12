package com.aquasecurity.plugins.trivy.actions

import com.aquasecurity.plugins.trivy.settings.CredentialCheck
import com.aquasecurity.plugins.trivy.settings.Regions
import com.aquasecurity.plugins.trivy.settings.TrivyProjectSettingState
import com.aquasecurity.plugins.trivy.settings.TrivySettingState
import com.aquasecurity.plugins.trivy.ui.TrivyScanOutputManager
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.ui.content.ContentFactory
import java.io.File
import java.util.function.BiConsumer
import java.util.function.Consumer
import javax.swing.SwingUtilities

internal class TrivyBackgroundRunTask(
  private val project: Project,
  private val resultFile: File,
  private val callback: BiConsumer<Project, File>,
  private val failureCallback: Consumer<Project>,
) : Backgroundable(project, "Running Trivy", false), Runnable {
  companion object {
    private val LOG = Logger.getInstance(TrivyBackgroundRunTask::class.java)
  }
  override fun run(indicator: ProgressIndicator) {
    this.run()
  }

  override fun run() {
    val settings = TrivySettingState.instance
    val projectSettings = TrivyProjectSettingState.getInstance(project)

    // Check if result file already exists and is valid
    if (resultFile.exists() && resultFile.length() > 0) {
      LOG.info("Reusing existing Trivy results from ${resultFile.absolutePath}")
      TrivyNotificationGroup.notifyInformation(project, "Using existing Trivy scan results. Delete results to run a new scan.")
      SwingUtilities.invokeLater { callback.accept(this.project, this.resultFile) }
      return
    }

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

    // always set exit code to 0 to avoid interrupting the IDE
    // if there is a catastrophic error, the output file will be empty or missing
    commandParts.add("--exit-code=0")

    commandParts.add("--format=json")
    commandParts.add(String.format("--output=%s", resultFile.absolutePath))
    commandParts.add(project.basePath)

    val commandLine = GeneralCommandLine(commandParts)
    commandLine.setWorkDirectory(project.basePath)

    if (!projectSettings.skipDirList.isEmpty()) {
      // combine the skip directories into a single comma separated string
      val skipDirs = projectSettings.skipDirList.joinToString(separator = ",")
      commandLine.addParameter("--skip-dirs=$skipDirs")
    }

    if (projectSettings.useAquaPlatform) {
      super.setTitle("Running Aqua Platform Scan")
      configureCommandLineEnv(commandLine, projectSettings, resultFile)

      // verify the credentials firts
      if (
          !CredentialCheck.isValidCredentials(
              project,
              commandLine.environment.get("AQUA_KEY") ?: "",
              commandLine.environment.get("AQUA_SECRET") ?: "",
              commandLine.environment.get("AQUA_URL") ?: "",
              commandLine.environment.get("CSPM_URL") ?: "",
          )
      ) {
        TrivyNotificationGroup.notifyError(
            project,
            "Invalid Aqua Platform credentials. Please check your settings.",
        )
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


    // Create console and set it up on EDT
    SwingUtilities.invokeLater {
      LOG.info("Creating console view on EDT")
      val console = ConsoleViewImpl(project, true)

      val toolWindow = TrivyScanOutputManager.getOrCreateToolWindow(project)

      LOG.info("Tool window found: ${toolWindow != null}")

      if (toolWindow != null) {
        LOG.info("Clearing existing content and adding new console")
        toolWindow.contentManager.removeAllContents(true)
        val content = ContentFactory.getInstance().createContent(console.component, "Scan Output", false)
        toolWindow.contentManager.addContent(content)
        LOG.info("Attaching process to console")
        console.attachToProcess(handler)
        LOG.info("Console successfully attached to process")
      } else {
        LOG.warn("Trivy Scan Output tool window not found")
        TrivyNotificationGroup.notifyInformation(project, "Trivy scan started. Click the scan output button to view results")
      }
    }

    try {
      // Capture output (stdout+stderr) and wait for the process to finish so we can inspect exit code / panic
      val output = ScriptRunnerUtil.getProcessOutput(
          handler,
          ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER,
          100000000,
      )

      val exitCode = try {
        process.waitFor()
      } catch (e: InterruptedException) {
        LOG.warn("Interrupted while waiting for process", e)
        -1
      }

      val panicked = output.contains("panic:", ignoreCase = true) || output.contains("panic", ignoreCase = true)
      val resultFileValid = resultFile.exists() && resultFile.length() > 0
      val failed = exitCode != 0 || panicked || !resultFileValid

      if (failed) {
        LOG.warn("Trivy run failed or panicked. exitCode=$exitCode panicked=$panicked resultFileValid=$resultFileValid")
        TrivyNotificationGroup.notifyError(project, "Trivy run failed or panicked. See scan output for details.", true)
          SwingUtilities.invokeLater { failureCallback.accept(this.project) }

      } else {
        TrivyNotificationGroup.notifyInformation(project, "Trivy run completed, updating results")
        SwingUtilities.invokeLater { callback.accept(this.project, this.resultFile) }
      }
    } catch (e: ExecutionException) {
      TrivyNotificationGroup.notifyError(project, e.localizedMessage)
    }
  }

  private fun configureCommandLineEnv(
      commandLine: GeneralCommandLine,
      projectSettings: TrivyProjectSettingState,
      resultFile: File,
  ) {

    if (TrivySettingState.instance.region == "Custom") {
      if (TrivySettingState.instance.customAquaUrl.isNotEmpty()) {
        commandLine.environment["AQUA_URL"] = TrivySettingState.instance.customAquaUrl
      }
      if (TrivySettingState.instance.customAuthUrl.isNotEmpty()) {
        commandLine.environment["CSPM_URL"] = TrivySettingState.instance.customAuthUrl
      }
    } else {
      val urls = Regions.getEnvUrls(TrivySettingState.instance.region)
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
    commandLine.environment["TRIVY_IDE_IDENTIFIER"] = "intellij"

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
    if (TrivySettingState.instance.proxyAddressUrl.isNotEmpty()) {
      commandLine.environment["HTTP_PROXY"] = TrivySettingState.instance.proxyAddressUrl
      commandLine.environment["HTTPS_PROXY"] = TrivySettingState.instance.proxyAddressUrl
    }
    if (TrivySettingState.instance.caCertPath.isNotEmpty()) {
      commandLine.environment["SSL_CERT_FILE"] = TrivySettingState.instance.caCertPath
      commandLine.environment["CA_CERT"] = TrivySettingState.instance.caCertPath
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
    val requiredChecks: MutableList<String> = ArrayList()
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
