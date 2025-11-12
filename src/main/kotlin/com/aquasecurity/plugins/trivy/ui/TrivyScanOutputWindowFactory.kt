package com.aquasecurity.plugins.trivy.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout
import javax.swing.JLabel

class TrivyScanOutputWindowFactory : ToolWindowFactory {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val panel = JBPanel<JBPanel<*>>(BorderLayout())
    panel.add(JLabel(""), BorderLayout.CENTER)
    toolWindow.contentManager.addContent(
        toolWindow.contentManager.factory.createContent(panel, "", false)
    )

    // Keep the tool window registered but hidden by default. It will be made available
    // and shown on-demand via TrivyScanOutputManager.showToolWindow(project).
    toolWindow.setAvailable(false, null)
  }
}
