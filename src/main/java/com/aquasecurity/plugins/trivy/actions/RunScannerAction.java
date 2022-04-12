package com.aquasecurity.plugins.trivy.actions;


import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * RunScannerAction executes Trivy then calls update results
 */
public class RunScannerAction extends AnAction {

    private Project project;

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.project = e.getProject();

        if (project == null) {
            return;
        }

        File resultFile;
        try {
            resultFile = File.createTempFile("Trivy", ".json");
        } catch (IOException ex) {
            TrivyNotificationGroup.notifyError(project, ex.getLocalizedMessage());
            return;
        }

        TrivyBackgroundRunTask runner = new TrivyBackgroundRunTask(project, resultFile, ResultProcessor::updateResults);
        if (SwingUtilities.isEventDispatchThread()) {
            ProgressManager.getInstance().run(runner);
        } else {
            ApplicationManager.getApplication().invokeLater(runner);
        }
    }


}


