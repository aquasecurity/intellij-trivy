package com.aquasecurity.plugins.trivy.settings

import com.aquasecurity.plugins.trivy.actions.CheckForTrivyAction
import com.aquasecurity.plugins.trivy.actions.tasks.TrivyDownloadBinaryTask
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBSplitter
import com.intellij.ui.TitledSeparator
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.*
import com.intellij.util.ui.FormBuilder
import java.awt.event.ItemEvent
import javax.swing.DefaultListModel
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

/** Supports creating and managing a [JPanel] for the Settings Dialog. */
class TrivySettingsComponent {
  var panel: JPanel = JPanel()
  val project = com.intellij.openapi.project.ProjectManager.getInstance().openProjects.firstOrNull()
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
  private val skipDirsModel = DefaultListModel<String>()
  private val skipDirList = JBList(skipDirsModel)

  // Aqua Platform support
  private val useAquaPlatform = JBCheckBox("Use Aqua Platform")
  private val apiKey = JBPasswordField()
  private val apiSecret = JBPasswordField()
  // Add the `Dev` entry to the array when testing locally and connecting to Dev
  private val region = ComboBox(arrayOf("US", "EU", "Singapore", "Sydney", "Custom"))
  private val customAquaUrlLabel = JBLabel("Custom Aqua URL")
  private val customAuthUrlLabel = JBLabel("Custom Auth URL")
  private val customAquaUrl = JBTextField()
  private val customAuthUrl = JBTextField()

  private val enableDotNetProject = JBCheckBox("Enable .NET Project Support")
  private val enableGradle = JBCheckBox("Enable Gradle Support")
  private val enablePackageJson = JBCheckBox("Enable Package.json Support")
  private val enableSASTScanning = JBCheckBox("Enable SAST Scanning")

  init {
    val fcd = FileChooserDescriptor(true, true, true, true, false, false)

    trivyPath.addBrowseFolderListener(TextBrowseFolderListener(fcd))
    trivyConfigPath.addBrowseFolderListener(TextBrowseFolderListener(fcd))
    trivyIgnorePath.addBrowseFolderListener(TextBrowseFolderListener(fcd))

    for (dir in TrivyProjectSettingState.getInstance(project!!).skipDirList) {
      skipDirsModel.addElement(dir)
    }

    region.addItemListener(
        fun(_: ItemEvent) {
          val showCustom = region.selectedItem == "Custom"
          customAquaUrlLabel.isVisible = showCustom
          customAquaUrl.isVisible = showCustom
          customAuthUrlLabel.isVisible = showCustom
          customAuthUrl.isVisible = showCustom

          panel.revalidate()
          panel.repaint()
        })

    trivyPath.text = TrivySettingState.instance.trivyPath
    if (project != null) {
      trivyConfigPath.text = TrivyProjectSettingState.getInstance(project).configPath
      trivyIgnorePath.text = TrivyProjectSettingState.getInstance(project).ignorePath
    }
    updatePanel()
  }

  fun updatePanel() {
    if (project != null) {
      // Check if Trivy is installed and update the settings accordingly
      CheckForTrivyAction.run()
    }

    var builder =
        FormBuilder.createFormBuilder()
            .addComponent(TitledSeparator("Path to Trivy"))
            .addLabeledComponent(JBLabel("Trivy binary"), trivyPath, 1, false)

    if (!TrivySettingState.instance.trivyInstalled) {
      builder =
          builder.addLabeledComponent(
              JBLabel(),
              JButton("Download Trivy").apply {
                addActionListener {
                  if (project != null) {
                    com.intellij.openapi.progress.ProgressManager.getInstance()
                        .run(
                            TrivyDownloadBinaryTask(
                                project,
                                true,
                                callback = {
                                  trivyPath.text = TrivySettingState.instance.trivyPath
                                  // update the Settings UI after download
                                  CheckForTrivyAction.run()
                                }))
                  }
                }
              },
              1,
              false)
    } else {
      // if trivy is installed and the path is the plugin folder
      val pluginPath =
          PluginManagerCore.getPlugin(PluginId.getId("com.aquasecurity.plugins.intellij-Trivy"))
              ?.pluginPath
      if (pluginPath != null &&
          TrivySettingState.instance.trivyPath.startsWith(pluginPath.toString())) {
        builder =
            builder
                .addLabeledComponent(
                    JBLabel(),
                    JBLabel(
                        "Trivy is managed by the Trivy plugin, check for updates and install if available"),
                    1,
                    false)
                .addLabeledComponent(
                    JBLabel(),
                    JButton("Update Trivy").apply {
                      addActionListener {
                        if (project != null) {
                          com.intellij.openapi.progress.ProgressManager.getInstance()
                              .run(
                                  TrivyDownloadBinaryTask(
                                      project,
                                      false,
                                      callback = {
                                        trivyPath.text = TrivySettingState.instance.trivyPath
                                        // update the Settings UI after download
                                        CheckForTrivyAction.run()
                                      }))
                        }
                      }
                    },
                    1,
                    false)
      }
    }

    builder =
        builder
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
            .addComponent(TitledSeparator("Skip Directories"))
            .addLabeledComponent(
                JBLabel(), ToolbarDecorator.createDecorator(skipDirList).setAddAction {
                val dir = showAddDirectoryDialog()
                if (dir != null) skipDirsModel.addElement(dir)
            }.setRemoveAction { 
                if (skipDirList.selectedIndex >= 0) {
                    skipDirsModel.remove(skipDirList.selectedIndex)
                }
            }.createPanel(), 1, false)
            .addSeparator()
            .addLabeledComponent(JBLabel("Config file path"), trivyConfigPath, 1, false)
            .addLabeledComponent(JBLabel(), useConfigFile, 1, false)
            .addLabeledComponent(JBLabel("Ignore file path"), trivyIgnorePath, 1, false)
            .addLabeledComponent(JBLabel(), useIgnoreFile, 1, false)
            .addComponent(TitledSeparator("Aqua Platform"))
            .addLabeledComponent(JBLabel(), useAquaPlatform, 1, false)
            .addLabeledComponent(JBLabel("API Key"), apiKey, 1, false)
            .addLabeledComponent(JBLabel("API Secret"), apiSecret, 1, false)
            .addLabeledComponent(JBLabel("Region"), region, 1, false)
            .addLabeledComponent(customAquaUrlLabel, customAquaUrl, 1, false)
            .addLabeledComponent(customAuthUrlLabel, customAuthUrl, 1, false)
            .addLabeledComponent(JBLabel(), enableDotNetProject, 1, false)
            .addLabeledComponent(JBLabel(), enableGradle, 1, false)
            .addLabeledComponent(JBLabel(), enablePackageJson, 1, false)
            .addLabeledComponent(JBLabel(), enableSASTScanning, 1, false)
            .addComponentFillVertically(JPanel(), 0)

    panel = builder.panel
  }

  private fun showAddDirectoryDialog(): String? {
    val chooser = FileChooserDescriptor(false, true, false, false, false, false)
      .withTitle("Select Directory to Skip")
      .withDescription("Choose a directory within the project to skip during scanning")

    // Get the project base directory as VirtualFile
    val projectBaseDir = project?.let {
      com.intellij.openapi.vfs.LocalFileSystem.getInstance().findFileByPath(it.basePath ?: "")
    }

    // Set the root to limit selection to project directories
    if (projectBaseDir != null) {
      chooser.withRoots(projectBaseDir)
    }

    val file = FileChooser.chooseFile(chooser, project, projectBaseDir)
    return file?.path
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

  val getApiKey: String
    get() = String(apiKey.password)

  val getApiSecret: String
    get() = String(apiSecret.password)

  val getRegion: String
    get() = region.selectedItem as String

  val getCustomAquaUrl: String
    get() = customAquaUrl.text

  val getCustomAuthUrl: String
    get() = customAuthUrl.text

  val getEnableDotNetProject: Boolean
    get() = enableDotNetProject.isSelected

  val getEnableGradle: Boolean
    get() = enableGradle.isSelected

  val getEnablePackageJson: Boolean
    get() = enablePackageJson.isSelected

  val getEnableSASTScanning: Boolean
    get() = enableSASTScanning.isSelected

  fun getSkipDirs(): List<String> {
    return skipDirsModel.elements().toList()
  }

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

  fun setApiKey(newText: String) {
    apiKey.text = newText
  }

  fun setApiSecret(newText: String) {
    apiSecret.text = newText
  }

  fun setRegion(newText: String) {
    region.selectedItem = newText
    val showCustom = region.selectedItem == "Custom"
    customAquaUrlLabel.isVisible = showCustom
    customAquaUrl.isVisible = showCustom
    customAuthUrlLabel.isVisible = showCustom
    customAuthUrl.isVisible = showCustom
  }

  fun setCustomAquaUrl(newText: String) {
    customAquaUrl.text = newText
  }

  fun setCustomAuthUrl(newText: String) {
    customAuthUrl.text = newText
  }

  fun setEnableDotNetProject(required: Boolean) {
    enableDotNetProject.isSelected = required
  }

  fun setEnableGradle(required: Boolean) {
    enableGradle.isSelected = required
  }

  fun setEnablePackageJson(required: Boolean) {
    enablePackageJson.isSelected = required
  }

  fun setEnableSASTScanning(required: Boolean) {
    enableSASTScanning.isSelected = required
  }
  fun setSkipDirs(dirs: List<String>) {
    skipDirsModel.clear()
    dirs.forEach { skipDirsModel.addElement(it) }
  }
}
