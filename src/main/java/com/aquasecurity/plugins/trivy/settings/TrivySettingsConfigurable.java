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
        boolean modified = !TrivySettingsComponent.getTrivyPath().equals(settings.TrivyPath) ||
                !TrivySettingsComponent.getCriticalSeverityRequired() == settings.CriticalSeverity ||
                !TrivySettingsComponent.getHighSeverityRequired() == settings.HighSeverity ||
                !TrivySettingsComponent.getMediumSeverityRequired() == settings.MediumSeverity||
                !TrivySettingsComponent.getLowSeverityRequired() == settings.LowSeverity         ||
                !TrivySettingsComponent.getUnknownSeverityRequired() == settings.UnknownSeverity ||
                !TrivySettingsComponent.getOfflineScanRequired() == settings.OfflineScan ||
                !TrivySettingsComponent.getShowOnlyFixed() == settings.IgnoreUnfixed
                ;
        return modified;
    }

    @Override
    public void apply() {
        TrivySettingState settings = TrivySettingState.getInstance();
        settings.TrivyPath = TrivySettingsComponent.getTrivyPath();
        settings.CriticalSeverity = TrivySettingsComponent.getCriticalSeverityRequired();
        settings.HighSeverity = TrivySettingsComponent.getHighSeverityRequired();
        settings.MediumSeverity = TrivySettingsComponent.getMediumSeverityRequired();
        settings.LowSeverity = TrivySettingsComponent.getLowSeverityRequired();
        settings.UnknownSeverity = TrivySettingsComponent.getUnknownSeverityRequired();
        settings.OfflineScan = TrivySettingsComponent.getOfflineScanRequired();
        settings.IgnoreUnfixed = TrivySettingsComponent.getShowOnlyFixed();

    }

    @Override
    public void reset() {
        TrivySettingState settings = TrivySettingState.getInstance();
        TrivySettingsComponent.setTrivyPath(settings.TrivyPath);
        TrivySettingsComponent.setCriticalSeverity(settings.CriticalSeverity);
        TrivySettingsComponent.setHighSeverity(settings.HighSeverity);
        TrivySettingsComponent.setMediumSeverity(settings.MediumSeverity);
        TrivySettingsComponent.setLowSeverity(settings.LowSeverity);
        TrivySettingsComponent.setUnknownSeverity(settings.UnknownSeverity);
        TrivySettingsComponent.setOfflineScan(settings.OfflineScan);
        TrivySettingsComponent.setIgnoreUnfixed(settings.IgnoreUnfixed);

    }

    @Override
    public void disposeUIResources() {
        TrivySettingsComponent = null;
    }

}
