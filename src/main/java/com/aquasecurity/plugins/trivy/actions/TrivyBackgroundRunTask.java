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
        String severities = getRequiredSeverities();

        List<String> commandParts = new ArrayList<>();
        commandParts.add(TrivySettingState.getInstance().TrivyPath);
        commandParts.add("fs");

        String requiredChecks = "config,vuln";
        if (TrivySettingState.getInstance().SecretScanning) {
            requiredChecks = String.format("%s,secret", requiredChecks);
        }

        commandParts.add(String.format("--security-checks=%s", requiredChecks));
        commandParts.add(String.format("--severity=%s", severities));

        if (TrivySettingState.getInstance().OfflineScan) {
            commandParts.add("--offline-scan");
        }

        if (TrivySettingState.getInstance().IgnoreUnfixed) {
            commandParts.add("--ignore-unfixed");
        }

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

    private String getRequiredSeverities() {
        List<String> requiredSeverities = new ArrayList<>();

        if (TrivySettingState.getInstance().CriticalSeverity) {
            requiredSeverities.add("CRITICAL");
        }
        if (TrivySettingState.getInstance().HighSeverity) {
            requiredSeverities.add("HIGH");
        }
        if (TrivySettingState.getInstance().MediumSeverity) {
            requiredSeverities.add("MEDIUM");
        }
        if (TrivySettingState.getInstance().LowSeverity) {
            requiredSeverities.add("LOW");
        }
        if (TrivySettingState.getInstance().UnknownSeverity) {
            requiredSeverities.add("UNKNOWN");
        }

        return String.join(",", requiredSeverities);
    }
}
