package com.aquasecurity.plugins.trivy.settings;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(
        name = "com.aquasecurity.plugins.trivy.settings.TrivySettingState",
        storages = @Storage("trivy.xml")
)
public class TrivySettingState implements PersistentStateComponent<TrivySettingState> {

    public String TrivyPath = "trivy";

    public static TrivySettingState getInstance() {
        return ApplicationManager.getApplication().getService(TrivySettingState.class);
    }

    @Nullable
    @Override
    public TrivySettingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TrivySettingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}