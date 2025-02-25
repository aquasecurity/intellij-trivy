package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings.
 */
class TrivySettingsConfigurable(private val project: Project) : Configurable {
    private var trivySettingsComponent: TrivySettingsComponent? = null

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "Trivy: Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return trivySettingsComponent!!.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        trivySettingsComponent = TrivySettingsComponent()
        return trivySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = TrivySettingState.instance
        var projectSettings = TrivyProjectSettingState.getInstance(project)

        if (trivySettingsComponent == null) {
            return false
        }

        val modified =
            (trivySettingsComponent!!.getTrivyPath() != settings.trivyPath ||
                    !trivySettingsComponent!!.criticalSeverityRequired == settings.criticalSeverity ||
                    !trivySettingsComponent!!.highSeverityRequired == settings.highSeverity ||
                    !trivySettingsComponent!!.mediumSeverityRequired == settings.mediumSeverity ||
                    !trivySettingsComponent!!.lowSeverityRequired == settings.lowSeverity ||
                    !trivySettingsComponent!!.unknownSeverityRequired == settings.unknownSeverity ||
                    !trivySettingsComponent!!.offlineScanRequired == settings.offlineScan ||
                    !trivySettingsComponent!!.showOnlyFixed == settings.ignoreUnfixed ||
                    !trivySettingsComponent!!.scanForSecrets == settings.scanForSecrets ||
                    !trivySettingsComponent!!.scanForMisconfiguration == settings.scanForMisconfigurations ||
                    !trivySettingsComponent!!.scanForVulnerabilities == settings.scanForVulnerabilities ||
                    trivySettingsComponent!!.getConfigPath() != projectSettings.configPath ||
                    !trivySettingsComponent!!.useConfig == projectSettings.useConfig ||
                    trivySettingsComponent!!.getIgnorePath() != projectSettings.ignorePath ||
                    !trivySettingsComponent!!.useIgnore == projectSettings.useIgnore)

        return modified
    }

    override fun apply() {
        val settings = TrivySettingState.instance
        val projectSettings = TrivyProjectSettingState.getInstance(project)

        settings.trivyPath = trivySettingsComponent!!.getTrivyPath()
        settings.criticalSeverity = trivySettingsComponent!!.criticalSeverityRequired
        settings.highSeverity = trivySettingsComponent!!.highSeverityRequired
        settings.mediumSeverity = trivySettingsComponent!!.mediumSeverityRequired
        settings.lowSeverity = trivySettingsComponent!!.lowSeverityRequired
        settings.unknownSeverity = trivySettingsComponent!!.unknownSeverityRequired
        settings.offlineScan = trivySettingsComponent!!.offlineScanRequired
        settings.ignoreUnfixed = trivySettingsComponent!!.showOnlyFixed
        settings.scanForSecrets = trivySettingsComponent!!.scanForSecrets
        settings.scanForMisconfigurations = trivySettingsComponent!!.scanForMisconfiguration
        settings.scanForVulnerabilities = trivySettingsComponent!!.scanForVulnerabilities
        projectSettings.configPath = trivySettingsComponent!!.getConfigPath()
        projectSettings.useConfig = trivySettingsComponent!!.useConfig
        projectSettings.ignorePath = trivySettingsComponent!!.getIgnorePath()
        projectSettings.useIgnore = trivySettingsComponent!!.useIgnore
    }

    override fun reset() {
        val settings = TrivySettingState.instance
        trivySettingsComponent!!.setTrivyPath(settings.trivyPath)
        trivySettingsComponent!!.setCriticalSeverity(settings.criticalSeverity)
        trivySettingsComponent!!.setHighSeverity(settings.highSeverity)
        trivySettingsComponent!!.setMediumSeverity(settings.mediumSeverity)
        trivySettingsComponent!!.setLowSeverity(settings.lowSeverity)
        trivySettingsComponent!!.setUnknownSeverity(settings.unknownSeverity)
        trivySettingsComponent!!.setOfflineScan(settings.offlineScan)
        trivySettingsComponent!!.setIgnoreUnfixed(settings.ignoreUnfixed)
        trivySettingsComponent!!.setSecretScanning(settings.scanForSecrets)
        trivySettingsComponent!!.setMisconfigurationScanning(settings.scanForMisconfigurations)
        trivySettingsComponent!!.setVulnerabilityScanning(settings.scanForVulnerabilities)
        trivySettingsComponent!!.setConfigFilePath(TrivyProjectSettingState.getInstance(project).configPath)
        trivySettingsComponent!!.setUseConfig(TrivyProjectSettingState.getInstance(project).useConfig)
        trivySettingsComponent!!.setIgnoreFilePath(TrivyProjectSettingState.getInstance(project).ignorePath)
        trivySettingsComponent!!.setUseIgnore(TrivyProjectSettingState.getInstance(project).useIgnore)
    }

    override fun disposeUIResources() {
        trivySettingsComponent = null
    }
}