package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings.
 */
class TrivySettingsConfigurable : Configurable {
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
                    !trivySettingsComponent!!.scanForVulnerabilities == settings.scanForVulnerabilities)

        return modified
    }

    override fun apply() {
        val settings = TrivySettingState.instance
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
    }

    override fun disposeUIResources() {
        trivySettingsComponent = null
    }
}