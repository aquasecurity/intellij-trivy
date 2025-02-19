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
            (trivySettingsComponent!!.getTrivyPath() != settings.TrivyPath ||
                    !trivySettingsComponent!!.criticalSeverityRequired == settings.CriticalSeverity ||
                    !trivySettingsComponent!!.highSeverityRequired == settings.HighSeverity ||
                    !trivySettingsComponent!!.mediumSeverityRequired == settings.MediumSeverity ||
                    !trivySettingsComponent!!.lowSeverityRequired == settings.LowSeverity ||
                    !trivySettingsComponent!!.unknownSeverityRequired == settings.UnknownSeverity ||
                    !trivySettingsComponent!!.offlineScanRequired == settings.OfflineScan ||
                    !trivySettingsComponent!!.showOnlyFixed == settings.IgnoreUnfixed ||
                    !trivySettingsComponent!!.getSecretScanning() == settings.SecretScanning)

        return modified
    }

    override fun apply() {
        val settings = TrivySettingState.instance
        settings.TrivyPath = trivySettingsComponent!!.getTrivyPath()
        settings.CriticalSeverity = trivySettingsComponent!!.criticalSeverityRequired
        settings.HighSeverity = trivySettingsComponent!!.highSeverityRequired
        settings.MediumSeverity = trivySettingsComponent!!.mediumSeverityRequired
        settings.LowSeverity = trivySettingsComponent!!.lowSeverityRequired
        settings.UnknownSeverity = trivySettingsComponent!!.unknownSeverityRequired
        settings.OfflineScan = trivySettingsComponent!!.offlineScanRequired
        settings.IgnoreUnfixed = trivySettingsComponent!!.showOnlyFixed
        settings.SecretScanning = trivySettingsComponent!!.getSecretScanning()

    }

    override fun reset() {
        val settings = TrivySettingState.instance
        trivySettingsComponent!!.setTrivyPath(settings.TrivyPath)
        trivySettingsComponent!!.setCriticalSeverity(settings.CriticalSeverity)
        trivySettingsComponent!!.setHighSeverity(settings.HighSeverity)
        trivySettingsComponent!!.setMediumSeverity(settings.MediumSeverity)
        trivySettingsComponent!!.setLowSeverity(settings.LowSeverity)
        trivySettingsComponent!!.setUnknownSeverity(settings.UnknownSeverity)
        trivySettingsComponent!!.setOfflineScan(settings.OfflineScan)
        trivySettingsComponent!!.setIgnoreUnfixed(settings.IgnoreUnfixed)
        trivySettingsComponent!!.setSecretScanning(settings.SecretScanning)
    }

    override fun disposeUIResources() {
        trivySettingsComponent = null
    }
}