package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.HyperlinkLabel
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
    private val trivyPath = TextFieldWithBrowseButton()
    private val trivyConfigPath = TextFieldWithBrowseButton()
    private val trivyIgnorePath = TextFieldWithBrowseButton()
    private val useConfigFile = JBCheckBox("Use config file")
    private val useIgnoreFile = JBCheckBox("Use ignore file")
    private val critical = JBCheckBox("Critical")
    private val high = JBCheckBox("High")
    private val medium = JBCheckBox("Medium")
    private val low = JBCheckBox("Low")
    private val unknown = JBCheckBox("Unknown")
    private val offlineScan = JBCheckBox("Offline scan")
    private val secretScanning = JBCheckBox("Enable secret scanning")
    private val misconfigurationScanning = JBCheckBox("Enable misconfiguration scanning")
    private val vulnScanning = JBCheckBox("Enable vulnerability scanning")
    private val ignoreUnfixed = JBCheckBox("Only show issues with fixes")
    private val downloadLink = HyperlinkLabel("Trivy must be installed, check here for instructions.")


    init {
        trivyPath.addBrowseFolderListener(
            ProjectManager.getInstance().defaultProject,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )
        trivyConfigPath.addBrowseFolderListener(
            ProjectManager.getInstance().defaultProject,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )
        trivyIgnorePath.addBrowseFolderListener(
            ProjectManager.getInstance().defaultProject,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        downloadLink.setHyperlinkTarget("https://trivy.dev/latest/getting-started/installation/")

        panel = FormBuilder.createFormBuilder()
            .addComponent(TitledSeparator("Path to Trivy"))
            .addComponent(downloadLink)
            .addLabeledComponent(JBLabel(), trivyPath, 1, true)
            .addComponent(JBSplitter())
            .addComponent(TitledSeparator("Scanners"))
            .addLabeledComponent(JBLabel(), vulnScanning, 1, false)
            .addLabeledComponent(JBLabel(), misconfigurationScanning, 1, false)
            .addLabeledComponent(JBLabel(), secretScanning, 1, false)
            .addComponent(TitledSeparator("Reported Severity Levels"))
            .addLabeledComponent(JBLabel(), critical, 1, false)
            .addLabeledComponent(JBLabel(), high, 1, false)
            .addLabeledComponent(JBLabel(), medium, 1, false)
            .addLabeledComponent(JBLabel(), low, 1, false)
            .addLabeledComponent(JBLabel(), unknown, 1, false)
            .addComponent(TitledSeparator("Other Settings"))
            .addLabeledComponent(JBLabel(), offlineScan, 1, false)
            .addLabeledComponent(JBLabel(), ignoreUnfixed, 1, false)
            .addLabeledComponent(JBLabel(), useConfigFile, 1, false)
            .addLabeledComponent(JBLabel(), trivyConfigPath, 1, true)
            .addLabeledComponent(JBLabel(), useIgnoreFile, 1, false)
            .addLabeledComponent(JBLabel("Ignore File"), trivyIgnorePath, 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = trivyPath

    fun getTrivyPath(): String {
        return trivyPath.text
    }

    fun getConfigPath(): String {
        return trivyConfigPath.text
    }

    fun getIgnorePath(): String {
        return trivyIgnorePath.text
    }

    val criticalSeverityRequired: Boolean
        get() = critical.isSelected

    val highSeverityRequired: Boolean
        get() = high.isSelected

    val mediumSeverityRequired: Boolean
        get() = medium.isSelected

    val lowSeverityRequired: Boolean
        get() = low.isSelected

    val unknownSeverityRequired: Boolean
        get() = unknown.isSelected

    val showOnlyFixed: Boolean
        get() = ignoreUnfixed.isSelected

    val offlineScanRequired: Boolean
        get() = offlineScan.isSelected

    val scanForSecrets: Boolean
        get() = secretScanning.isSelected

    val scanForMisconfiguration: Boolean
        get() = misconfigurationScanning.isSelected

    val scanForVulnerabilities: Boolean
        get() = vulnScanning.isSelected

    val useConfig: Boolean
        get() = useConfigFile.isSelected

    val useIgnore: Boolean
        get() = useIgnoreFile.isSelected

    fun setTrivyPath(newText: String) {
        trivyPath.text = newText
    }

    fun setCriticalSeverity(required: Boolean) {
        critical.isSelected = required
    }

    fun setHighSeverity(required: Boolean) {
        high.isSelected = required
    }

    fun setMediumSeverity(required: Boolean) {
        medium.isSelected = required
    }

    fun setLowSeverity(required: Boolean) {
        low.isSelected = required
    }

    fun setUnknownSeverity(required: Boolean) {
        unknown.isSelected = required
    }

    fun setOfflineScan(required: Boolean) {
        offlineScan.isSelected = required
    }

    fun setIgnoreUnfixed(required: Boolean) {
        ignoreUnfixed.isSelected = required
    }

    fun setSecretScanning(required: Boolean) {
        secretScanning.isSelected = required
    }

    fun setMisconfigurationScanning(required: Boolean) {
        misconfigurationScanning.isSelected = required
    }

    fun setVulnerabilityScanning(required: Boolean) {
        vulnScanning.isSelected = required
    }

    fun setConfigFilePath(newText: String) {
        trivyConfigPath.text = newText
    }

    fun setUseConfig(required: Boolean) {
        useConfigFile.isSelected = required
    }

    fun setIgnoreFilePath(newText: String) {
        trivyIgnorePath.text = newText
    }

    fun setUseIgnore(required: Boolean) {
        useIgnoreFile.isSelected = required
    }
}