package com.aquasecurity.plugins.trivy.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager

object TrivyScanOutputManager {
  fun getOrCreateToolWindow(project: Project): ToolWindow? {
    val toolWindowManager = ToolWindowManager.getInstance(project)
    val toolWindow = toolWindowManager.getToolWindow("Trivy Scan Output")

    return toolWindow
  }

  fun showToolWindow(project: Project) {
    val toolWindow = getOrCreateToolWindow(project)
    if (toolWindow != null) {
      // Ensure the tool window is available and show it
      toolWindow.setAvailable(true, null)
      toolWindow.show(null)
    }
  }
}
