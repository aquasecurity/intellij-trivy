package com.aquasecurity.plugins.trivy.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.HintHint;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class TrivySettingsComponent {

    private final JPanel settingsPanel;
    private final TextFieldWithBrowseButton TrivyPath = new TextFieldWithBrowseButton();
    private final JBCheckBox CriticalSeverity = new JBCheckBox("Critical");
    private final JBCheckBox HighSeverity = new JBCheckBox("High");
    private final JBCheckBox MediumSeverity = new JBCheckBox("Medium");
    private final JBCheckBox LowSeverity = new JBCheckBox("Low");
    private final JBCheckBox UnknownSeverity = new JBCheckBox("Unknown");
    private final JBCheckBox OfflineScan = new JBCheckBox("Offline scan");
    private final JBCheckBox SecretScanning = new JBCheckBox("Enable secret scanning");
    private final JBCheckBox IgnoreUnfixed = new JBCheckBox("Only show issues with fixes");


    public TrivySettingsComponent() {

        TrivyPath.addBrowseFolderListener("Trivy binary path", "Set the explicit path to Trivy",
                ProjectManager.getInstance().getDefaultProject(), FileChooserDescriptorFactory.createSingleFileDescriptor());

        settingsPanel = FormBuilder.createFormBuilder()
                .addComponent(new TitledSeparator("Path to Trivy"))
                .addLabeledComponent(new JBLabel(), TrivyPath, 1, true)
                .addComponent(new JBSplitter())
                .addComponent(new TitledSeparator("Reported Severity Levels"))
                .addLabeledComponent(new JBLabel(), CriticalSeverity, 1, false)
                .addLabeledComponent(new JBLabel(), HighSeverity, 1, false)
                .addLabeledComponent(new JBLabel(), MediumSeverity, 1, false)
                .addLabeledComponent(new JBLabel(), LowSeverity, 1, false)
                .addLabeledComponent(new JBLabel(), UnknownSeverity, 1, false)
                .addComponent(new TitledSeparator("Other Settings"))
                .addLabeledComponent(new JBLabel(), OfflineScan, 1, false)
                .addLabeledComponent(new JBLabel(), IgnoreUnfixed, 1, false)
                .addLabeledComponent(new JBLabel(), SecretScanning, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return settingsPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return TrivyPath;
    }

    @NotNull
    public String getTrivyPath() {
        return TrivyPath.getText();
    }

    @NotNull
    public boolean getCriticalSeverityRequired() {
        return CriticalSeverity.isSelected();
    }

    @NotNull
    public boolean getHighSeverityRequired() {
        return HighSeverity.isSelected();
    }

    @NotNull
    public boolean getMediumSeverityRequired() {
        return MediumSeverity.isSelected();
    }


    @NotNull
    public boolean getLowSeverityRequired() {
        return LowSeverity.isSelected();
    }

    @NotNull
    public boolean getUnknownSeverityRequired() {
        return UnknownSeverity.isSelected();
    }

    @NotNull
    public boolean getShowOnlyFixed() {
        return IgnoreUnfixed.isSelected();
    }

    @NotNull
    public boolean getOfflineScanRequired() {
        return OfflineScan.isSelected();
    }

    @NotNull
    public boolean getSecretScanning() {
        return SecretScanning.isSelected();
    }


    public void setTrivyPath(@NotNull String newText) {
        TrivyPath.setText(newText);
    }

    public void setCriticalSeverity(@NotNull boolean required) { CriticalSeverity.setSelected(required);    }

    public void setHighSeverity(@NotNull boolean required) { HighSeverity.setSelected(required);    }

    public void setMediumSeverity(@NotNull boolean required) { MediumSeverity.setSelected(required);    }

    public void setLowSeverity(@NotNull boolean required) { LowSeverity.setSelected(required);    }

    public void setUnknownSeverity(@NotNull boolean required) { UnknownSeverity.setSelected(required);    }

    public void setOfflineScan(@NotNull boolean required) { OfflineScan.setSelected(required);    }

    public void setIgnoreUnfixed(@NotNull boolean required) { IgnoreUnfixed.setSelected(required);    }

    public void setSecretScanning(@NotNull boolean required) {SecretScanning.setSelected(required);}

}