package com.aquasecurity.plugins.trivy.settings

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
            trivySettingsComponent!!.getCspmServerURL != settings.cspmServerURL ||
            trivySettingsComponent!!.getAquaApiURL != settings.aquaApiURL ||
            trivySettingsComponent!!.getUseAquaPlatform != projectSettings.useAquaPlatform ||
            trivySettingsComponent!!.getUploadResultsToPlatform != projectSettings.uploadResults)

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
    settings.cspmServerURL = trivySettingsComponent!!.getCspmServerURL
    settings.aquaApiURL = trivySettingsComponent!!.getAquaApiURL

    projectSettings.configPath = trivySettingsComponent!!.getConfigPath()
    projectSettings.useConfig = trivySettingsComponent!!.getUseConfig
    projectSettings.ignorePath = trivySettingsComponent!!.getIgnorePath()
    projectSettings.useIgnore = trivySettingsComponent!!.getUseIgnore
    projectSettings.useAquaPlatform = trivySettingsComponent!!.getUseAquaPlatform
    projectSettings.uploadResults = trivySettingsComponent!!.getUploadResultsToPlatform
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
      trivySettingsComponent?.setCspmServerURL(settings.cspmServerURL)
      trivySettingsComponent?.setAquaApiURL(settings.aquaApiURL)
      trivySettingsComponent?.setUseAquaPlatform(
        TrivyProjectSettingState.getInstance(project).useAquaPlatform)
      trivySettingsComponent?.setUploadResultsToPlatform(
        TrivyProjectSettingState.getInstance(project).uploadResults)
  }

  override fun disposeUIResources() {
    trivySettingsComponent = null
  }
}
