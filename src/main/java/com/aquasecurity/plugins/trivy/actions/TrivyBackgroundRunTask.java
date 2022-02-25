package com.aquasecurity.plugins.trivy.actions;

import com.aquasecurity.plugins.trivy.settings.TrivySettingState;
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

class TrivyBackgroundRunTask extends Task.Backgroundable implements Runnable {

    private final Project project;
    private final File resultFile;
    private final BiConsumer<Project, File> callback;

    public TrivyBackgroundRunTask(Project project, File resultFile, BiConsumer<Project, File> updateResults) {
        super(project, "Running Trivy", false);
        this.project = project;
        this.resultFile = resultFile;
        this.callback = updateResults;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        this.run();
    }

    @Override
    public void run() {
        List<String> commandParts = new ArrayList<>();
        commandParts.add(TrivySettingState.getInstance().TrivyPath);
        commandParts.add("fs");
        commandParts.add("--security-checks=config,vuln");
        commandParts.add("--format=json");
        commandParts.add(String.format("--output=%s", resultFile.getAbsolutePath()));
        commandParts.add(this.project.getBasePath());

        GeneralCommandLine commandLine = new GeneralCommandLine(commandParts);

        Process process;
        try {
            process = commandLine.createProcess();
        } catch (ExecutionException e) {
            TrivyNotificationGroup.notifyError(project, e.getLocalizedMessage());
            return;
        }

        TrivyNotificationGroup.notifyInformation(project, commandLine.getCommandLineString());
        OSProcessHandler handler = new OSProcessHandler(process, commandLine.getCommandLineString());

        try {
            ScriptRunnerUtil.getProcessOutput(handler,
                    ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER,
                    100000000);
            TrivyNotificationGroup.notifyInformation(project, "Trivy run completed, updating results");
            SwingUtilities.invokeLater(() -> {
                callback.accept(this.project, this.resultFile);
            });
        } catch (ExecutionException e) {
            TrivyNotificationGroup.notifyError(project, e.getLocalizedMessage());
        }

    }
}
