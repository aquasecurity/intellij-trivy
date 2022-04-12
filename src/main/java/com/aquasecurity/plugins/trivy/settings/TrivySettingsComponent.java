package com.aquasecurity.plugins.trivy.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class TrivySettingsComponent {

    private final JPanel settingsPanel;
    private final TextFieldWithBrowseButton TrivyPath = new TextFieldWithBrowseButton();

    public TrivySettingsComponent() {

        TrivyPath.addBrowseFolderListener("Trivy binary path", "Set the explicit path to Trivy",
                ProjectManager.getInstance().getDefaultProject(), FileChooserDescriptorFactory.createSingleFileDescriptor());

        settingsPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Specific Trivy path: "), TrivyPath, 1, true)
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

    public void setTrivyPath(@NotNull String newText) {
        TrivyPath.setText(newText);
    }


}