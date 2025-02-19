package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBSplitter
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class TrivySettingsComponent {
    val panel: JPanel
    private val TrivyPath = TextFieldWithBrowseButton()
    private val CriticalSeverity = JBCheckBox("Critical")
    private val HighSeverity = JBCheckBox("High")
    private val MediumSeverity = JBCheckBox("Medium")
    private val LowSeverity = JBCheckBox("Low")
    private val UnknownSeverity = JBCheckBox("Unknown")
    private val OfflineScan = JBCheckBox("Offline scan")
    private val ScanForSecrets = JBCheckBox("Enable secret scanning")
    private val ScanForMisconfigurations = JBCheckBox("Enable misconfiguration scanning")
    private val ScanForVulnerabilities = JBCheckBox("Enable vulnerability scanning")
    private val IgnoreUnfixed = JBCheckBox("Only show issues with fixes")


    init {
        TrivyPath.addBrowseFolderListener(
            "Trivy binary path", "Set the explicit path to Trivy",
            ProjectManager.getInstance().defaultProject, FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        panel = FormBuilder.createFormBuilder()
            .addComponent(TitledSeparator("Path to Trivy"))
            .addLabeledComponent(JBLabel(), TrivyPath, 1, true)
            .addComponent(JBSplitter())
            .addComponent(TitledSeparator("Scanners"))
            .addLabeledComponent(JBLabel(), ScanForVulnerabilities, 1, false)
            .addLabeledComponent(JBLabel(), ScanForMisconfigurations, 1, false)
            .addLabeledComponent(JBLabel(), ScanForSecrets, 1, false)
            .addComponent(TitledSeparator("Reported Severity Levels"))
            .addLabeledComponent(JBLabel(), CriticalSeverity, 1, false)
            .addLabeledComponent(JBLabel(), HighSeverity, 1, false)
            .addLabeledComponent(JBLabel(), MediumSeverity, 1, false)
            .addLabeledComponent(JBLabel(), LowSeverity, 1, false)
            .addLabeledComponent(JBLabel(), UnknownSeverity, 1, false)
            .addComponent(TitledSeparator("Other Settings"))
            .addLabeledComponent(JBLabel(), OfflineScan, 1, false)
            .addLabeledComponent(JBLabel(), IgnoreUnfixed, 1, false)

            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = TrivyPath

    fun getTrivyPath(): String {
        return TrivyPath.text
    }

    val criticalSeverityRequired: Boolean
        get() = CriticalSeverity.isSelected

    val highSeverityRequired: Boolean
        get() = HighSeverity.isSelected

    val mediumSeverityRequired: Boolean
        get() = MediumSeverity.isSelected


    val lowSeverityRequired: Boolean
        get() = LowSeverity.isSelected

    val unknownSeverityRequired: Boolean
        get() = UnknownSeverity.isSelected

    val showOnlyFixed: Boolean
        get() = IgnoreUnfixed.isSelected

    val offlineScanRequired: Boolean
        get() = OfflineScan.isSelected

    val scanForSecrets: Boolean
        get() =  ScanForSecrets.isSelected

    val scanForMisconfiguration: Boolean
        get() = ScanForMisconfigurations.isSelected

val scanForVulnerabilities: Boolean
    get() = ScanForVulnerabilities.isSelected

    fun setTrivyPath(newText: String) {
        TrivyPath.text = newText
    }

    fun setCriticalSeverity(required: Boolean) {
        CriticalSeverity.isSelected = required
    }

    fun setHighSeverity(required: Boolean) {
        HighSeverity.isSelected = required
    }

    fun setMediumSeverity(required: Boolean) {
        MediumSeverity.isSelected = required
    }

    fun setLowSeverity(required: Boolean) {
        LowSeverity.isSelected = required
    }

    fun setUnknownSeverity(required: Boolean) {
        UnknownSeverity.isSelected = required
    }

    fun setOfflineScan(required: Boolean) {
        OfflineScan.isSelected = required
    }

    fun setIgnoreUnfixed(required: Boolean) {
        IgnoreUnfixed.isSelected = required
    }

    fun setSecretScanning(required: Boolean) {
        ScanForSecrets.isSelected = required
    }

    fun setMisconfigurationScanning(required: Boolean) {
        ScanForMisconfigurations.isSelected = required
    }

    fun setVulnerabilityScanning(required: Boolean) {
        ScanForVulnerabilities.isSelected = required
    }
}