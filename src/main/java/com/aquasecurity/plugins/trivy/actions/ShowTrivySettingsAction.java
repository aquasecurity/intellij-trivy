package com.aquasecurity.plugins.trivy.actions;

import com.aquasecurity.plugins.trivy.settings.TrivySettingsConfigurable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ShowTrivySettingsAction extends AnAction {


    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            return;
        }

        TrivySettingsConfigurable configurable = new TrivySettingsConfigurable();
        ShowSettingsUtil.getInstance().editConfigurable(project, configurable);
    }
}
