package com.aquasecurity.plugins.trivy.actions;

import com.aquasecurity.plugins.trivy.model.Findings;
import com.aquasecurity.plugins.trivy.ui.TrivyWindow;
import com.aquasecurity.plugins.trivy.ui.notify.TrivyNotificationGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;

import java.io.File;
import java.io.IOException;

/**
 * ResultProcessor takes the results finding and unmarshalls to object
 * Then updates the findings window
 */
public class ResultProcessor {

    public static void updateResults(Project project, File resultFile) {

        Findings findings;
        try {
            findings = new ObjectMapper().readValue(resultFile, Findings.class);
        } catch (IOException e) {
            TrivyNotificationGroup.notifyError(project, String.format("Failed to deserialize the results file. %s", e.getLocalizedMessage()));
            return;
        }

        // redraw the explorer with the updated content
        final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Trivy Findings");
        final Content content = toolWindow.getContentManager().getContent(0);
        final TrivyWindow TrivyWindow = (TrivyWindow) content.getComponent();
        TrivyWindow.updateFindings(findings);
        TrivyWindow.redraw();
    }

}
