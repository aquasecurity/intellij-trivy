package com.aquasecurity.plugins.trivy.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class CheckForTrivyAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {

    // check the path to see if trivy is installed
    val project = e.project ?: return
    run()
  }

  companion object {
    fun run() {
      val settings = com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance
      if (settings.trivyPath.isEmpty()) {
        com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance.trivyInstalled = false
        return
      }

      val trivyPath = settings.trivyPath.trim()
      // check if trivyPath is resolvable on the System PATH
      try {
        val process = Runtime.getRuntime().exec(arrayOf(trivyPath, "--version"))
        val exitCode = process.waitFor()
        if (exitCode == 0) {
          com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance.trivyInstalled = true
        } else {
          com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance.trivyInstalled = false
        }
        return
      } catch (e: Exception) {
        // save in the context that trivy is not configured
        com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance.trivyInstalled = false
        // set the binary path to empty
        com.aquasecurity.plugins.trivy.settings.TrivySettingState.instance.trivyPath = ""
        return
      }
    }
  }
}
