package com.aquasecurity.plugins.trivy.settings

import com.aquasecurity.plugins.trivy.actions.CheckForTrivyAction
import com.intellij.openapi.actionSystem.ActionUiKind
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/** Provides controller functionality for application settings. */
class TrivySettingsConfigurable(private val project: Project) : Configurable {
  private var trivySettingsComponent: TrivySettingsComponent? = null

  // A default constructor with no arguments is required because this implementation
  // is registered as an applicationConfigurable EP
  override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
    return "Trivy: Settings"
  }

  override fun getPreferredFocusedComponent(): JComponent? {
      return trivySettingsComponent?.preferredFocusedComponent
  }

  override fun createComponent(): JComponent? {
    trivySettingsComponent = TrivySettingsComponent()
      return trivySettingsComponent?.panel
  }

  override fun isModified(): Boolean {
    val settings = TrivySettingState.instance
    var projectSettings = TrivyProjectSettingState.getInstance(project)

    if (trivySettingsComponent == null) {
      return false
    }

    val modified =
        (trivySettingsComponent!!.getTrivyPath() != settings.trivyPath ||
            !trivySettingsComponent!!.getCriticalSeverityRequired == settings.criticalSeverity ||
            !trivySettingsComponent!!.getHighSeverityRequired == settings.highSeverity ||
            !trivySettingsComponent!!.getMediumSeverityRequired == settings.mediumSeverity ||
            !trivySettingsComponent!!.getLowSeverityRequired == settings.lowSeverity ||
            !trivySettingsComponent!!.getUnknownSeverityRequired == settings.unknownSeverity ||
            !trivySettingsComponent!!.getOfflineScanRequired == settings.offlineScan ||
            !trivySettingsComponent!!.getShowOnlyFixed == settings.ignoreUnfixed ||
            !trivySettingsComponent!!.getScanForSecrets == settings.scanForSecrets ||
            !trivySettingsComponent!!.getScanForMisconfigurations ==
                settings.scanForMisconfigurations ||
            !trivySettingsComponent!!.getScanForVulnerabilities ==
                settings.scanForVulnerabilities ||
            trivySettingsComponent!!.getConfigPath() != projectSettings.configPath ||
            !trivySettingsComponent!!.getUseConfig == projectSettings.useConfig ||
            trivySettingsComponent!!.getIgnorePath() != projectSettings.ignorePath ||
            !trivySettingsComponent!!.getUseIgnore == projectSettings.useIgnore ||
            trivySettingsComponent!!.getApiKey != settings.apiKey ||
            trivySettingsComponent!!.getApiSecret != settings.apiSecret ||
            trivySettingsComponent!!.getRegion != settings.region ||
            trivySettingsComponent!!.getUseAquaPlatform != projectSettings.useAquaPlatform )

    return modified
  }

  override fun apply() {
    val settings = TrivySettingState.instance
    val projectSettings = TrivyProjectSettingState.getInstance(project)
      if (trivySettingsComponent == null) {
          return
      }

    settings.trivyPath = trivySettingsComponent!!.getTrivyPath()
    settings.criticalSeverity = trivySettingsComponent!!.getCriticalSeverityRequired
    settings.highSeverity = trivySettingsComponent!!.getHighSeverityRequired
    settings.mediumSeverity = trivySettingsComponent!!.getMediumSeverityRequired
    settings.lowSeverity = trivySettingsComponent!!.getLowSeverityRequired
    settings.unknownSeverity = trivySettingsComponent!!.getUnknownSeverityRequired
    settings.offlineScan = trivySettingsComponent!!.getOfflineScanRequired
    settings.ignoreUnfixed = trivySettingsComponent!!.getShowOnlyFixed
    settings.scanForSecrets = trivySettingsComponent!!.getScanForSecrets
    settings.scanForMisconfigurations = trivySettingsComponent!!.getScanForMisconfigurations
    settings.scanForVulnerabilities = trivySettingsComponent!!.getScanForVulnerabilities
    settings.apiKey = trivySettingsComponent!!.getApiKey
    settings.apiSecret = trivySettingsComponent!!.getApiSecret
    settings.region = trivySettingsComponent!!.getRegion

    projectSettings.configPath = trivySettingsComponent!!.getConfigPath()
    projectSettings.useConfig = trivySettingsComponent!!.getUseConfig
    projectSettings.ignorePath = trivySettingsComponent!!.getIgnorePath()
    projectSettings.useIgnore = trivySettingsComponent!!.getUseIgnore

    if (trivySettingsComponent!!.getUseAquaPlatform)  {
      // Only set useAquaPlatform to true if both apiKey and apiSecret are set
        projectSettings.useAquaPlatform =  (trivySettingsComponent!!.getApiKey != "" && trivySettingsComponent!!.getApiSecret != "")
    } else {
      projectSettings.useAquaPlatform = false
    }

    val dataContext = DataContext { key ->
      if (PlatformDataKeys.PROJECT.`name` == key) project else null
    }
    val event = AnActionEvent.createEvent(dataContext, null, "", ActionUiKind.NONE, null)
    CheckForTrivyAction().actionPerformed(event)

    // update the toolbar based on the new settings
//    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Trivy Explorer")
//    val content = toolWindow?.contentManager?.getContent(0)
//    val trivyWindow = content?.component as? TrivyWindow ?: return
//    trivyWindow.redraw()
  }

  override fun reset() {
    val settings = TrivySettingState.instance
      trivySettingsComponent?.setTrivyPath(settings.trivyPath)
      trivySettingsComponent?.setCriticalSeverity(settings.criticalSeverity)
      trivySettingsComponent?.setHighSeverity(settings.highSeverity)
      trivySettingsComponent?.setMediumSeverity(settings.mediumSeverity)
      trivySettingsComponent?.setLowSeverity(settings.lowSeverity)
      trivySettingsComponent?.setUnknownSeverity(settings.unknownSeverity)
      trivySettingsComponent?.setOfflineScan(settings.offlineScan)
      trivySettingsComponent?.setIgnoreUnfixed(settings.ignoreUnfixed)
      trivySettingsComponent?.setSecretScanning(settings.scanForSecrets)
      trivySettingsComponent?.setMisconfigurationScanning(settings.scanForMisconfigurations)
      trivySettingsComponent?.setVulnerabilityScanning(settings.scanForVulnerabilities)
      trivySettingsComponent?.setConfigFilePath(
        TrivyProjectSettingState.getInstance(project).configPath)
      trivySettingsComponent?.setUseConfig(TrivyProjectSettingState.getInstance(project).useConfig)
      trivySettingsComponent?.setIgnoreFilePath(
        TrivyProjectSettingState.getInstance(project).ignorePath)
      trivySettingsComponent?.setUseIgnore(TrivyProjectSettingState.getInstance(project).useIgnore)
      trivySettingsComponent?.setApiKey(settings.apiKey)
      trivySettingsComponent?.setApiSecret(settings.apiSecret)
        trivySettingsComponent?.setRegion(settings.region)
      trivySettingsComponent?.setUseAquaPlatform(
        TrivyProjectSettingState.getInstance(project).useAquaPlatform)

  }

  override fun disposeUIResources() {
    trivySettingsComponent = null
  }
}
