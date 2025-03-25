package com.aquasecurity.plugins.trivy.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class TrivyWindowFactory : ToolWindowFactory {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val TrivyWindow = TrivyWindow(project)
    val contentFactory = ApplicationManager.getApplication().getService(ContentFactory::class.java)
    val content = contentFactory.createContent(TrivyWindow.content, "", false)
    toolWindow.contentManager.addContent(content)
  }
}
