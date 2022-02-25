package com.aquasecurity.plugins.trivy.settings;


import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class TrivySettingsConfigurable implements Configurable {

    private TrivySettingsComponent TrivySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Trivy: Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return TrivySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        TrivySettingsComponent = new TrivySettingsComponent();
        return TrivySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        TrivySettingState settings = TrivySettingState.getInstance();
        boolean modified = !TrivySettingsComponent.getTrivyPath().equals(settings.TrivyPath);
        return modified;
    }

    @Override
    public void apply() {
        TrivySettingState settings = TrivySettingState.getInstance();
        settings.TrivyPath = TrivySettingsComponent.getTrivyPath();
    }

    @Override
    public void reset() {
        TrivySettingState settings = TrivySettingState.getInstance();
        TrivySettingsComponent.setTrivyPath(settings.TrivyPath);
    }

    @Override
    public void disposeUIResources() {
        TrivySettingsComponent = null;
    }

}
