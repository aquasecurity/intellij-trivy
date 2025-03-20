package com.aquasecurity.plugins.trivy.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.HyperlinkLabel
import com.intellij.ui.JBSplitter
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/** Supports creating and managing a [JPanel] for the Settings Dialog. */
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

  // Aqua Platform support
  private val useAquaPlatform = JBCheckBox("Use Aqua Platform")
  private val updateResults = JBCheckBox("Upload results to Aqua Platform")
  private val apiKey = JBPasswordField()
  private val apiSecret = JBPasswordField()
  private val cspmServerURL = JBTextField()
  private val aquaApiURL = JBTextField()
  private val uploadResults = JBCheckBox("Upload results to Aqua Platform")

  init {
    val fcd = FileChooserDescriptor(true, true, true, true, false, false)

    trivyPath.addBrowseFolderListener(TextBrowseFolderListener(fcd))
    trivyConfigPath.addBrowseFolderListener(TextBrowseFolderListener(fcd))
    trivyIgnorePath.addBrowseFolderListener(TextBrowseFolderListener(fcd))
    downloadLink.setHyperlinkTarget("https://trivy.dev/latest/getting-started/installation/")

    panel =
        FormBuilder.createFormBuilder()
            .addComponent(TitledSeparator("Path to Trivy"))
            .addComponent(downloadLink)
            .addLabeledComponent(JBLabel("Trivy binary"), trivyPath, 1, false)
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
            .addSeparator()
            .addLabeledComponent(JBLabel("Config file path"), trivyConfigPath, 1, false)
            .addLabeledComponent(JBLabel(), useConfigFile, 1, false)
            .addLabeledComponent(JBLabel("Ignore file path"), trivyIgnorePath, 1, false)
            .addLabeledComponent(JBLabel(), useIgnoreFile, 1, false)
            .addComponent(TitledSeparator("Aqua Platform"))
            .addLabeledComponent(JBLabel(), useAquaPlatform, 1, false)
            .addLabeledComponent(JBLabel("API Key"), apiKey, 1, false)
            .addLabeledComponent(JBLabel("API Secret"), apiSecret, 1, false)
            .addLabeledComponent(JBLabel("Authentication Endpoint URL"), cspmServerURL, 1, false)
            .addLabeledComponent(JBLabel("Aqua API URL"), aquaApiURL, 1, false)
            .addLabeledComponent(JBLabel(), updateResults, 1, false)
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

  val getCriticalSeverityRequired: Boolean
    get() = critical.isSelected

  val getHighSeverityRequired: Boolean
    get() = high.isSelected

  val getMediumSeverityRequired: Boolean
    get() = medium.isSelected

  val getLowSeverityRequired: Boolean
    get() = low.isSelected

  val getUnknownSeverityRequired: Boolean
    get() = unknown.isSelected

  val getShowOnlyFixed: Boolean
    get() = ignoreUnfixed.isSelected

  val getOfflineScanRequired: Boolean
    get() = offlineScan.isSelected

  val getScanForSecrets: Boolean
    get() = secretScanning.isSelected

  val getScanForMisconfigurations: Boolean
    get() = misconfigurationScanning.isSelected

  val getScanForVulnerabilities: Boolean
    get() = vulnScanning.isSelected

  val getUseConfig: Boolean
    get() = useConfigFile.isSelected

  val getUseIgnore: Boolean
    get() = useIgnoreFile.isSelected

  val getUseAquaPlatform: Boolean
    get() = useAquaPlatform.isSelected

  val getUploadResultsToPlatform: Boolean
    get() = uploadResults.isSelected

  val getApiKey: String
    get() = String(apiKey.password)

  val getApiSecret: String
    get() = String(apiSecret.password)

  val getCspmServerURL: String
    get() = cspmServerURL.text

  val getAquaApiURL: String
    get() = aquaApiURL.text

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

  fun setUseAquaPlatform(required: Boolean) {
    useAquaPlatform.isSelected = required
  }

  fun setUploadResultsToPlatform(required: Boolean) {
    uploadResults.isSelected = required
  }

  fun setApiKey(newText: String) {
    apiKey.text = newText
  }

  fun setApiSecret(newText: String) {
    apiSecret.text = newText
  }

  fun setCspmServerURL(newText: String) {
    cspmServerURL.text = newText
  }

  fun setAquaApiURL(newText: String) {
    aquaApiURL.text = newText
  }
}
