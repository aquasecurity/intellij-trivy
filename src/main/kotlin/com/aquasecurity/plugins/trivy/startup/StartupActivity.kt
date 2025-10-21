package com.aquasecurity.plugins.trivy.startup

import com.aquasecurity.plugins.trivy.actions.CheckForTrivyAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class StartupActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    ApplicationManager.getApplication().invokeLater { CheckForTrivyAction.run() }
  }
}
