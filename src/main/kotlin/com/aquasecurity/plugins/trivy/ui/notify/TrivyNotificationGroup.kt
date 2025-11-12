package com.aquasecurity.plugins.trivy.ui.notify

import com.aquasecurity.plugins.trivy.ui.TrivyScanOutputManager
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object TrivyNotificationGroup {
  // Resolve the NotificationGroup on-demand to avoid accessing services during class initialization
  private fun getNotificationGroup(): NotificationGroup? = NotificationGroup.findRegisteredGroup("Trivy Notifications")

  fun notifyError(project: Project?, content: String, withShowOutputAction: Boolean = false) {
    notify(project, content, NotificationType.ERROR, withShowOutputAction)
  }

  fun notifyInformation(project: Project?, content: String, withShowOutputAction: Boolean = false) {
    notify(project, content, NotificationType.INFORMATION, withShowOutputAction)
  }

  private fun notify(project: Project?, content: String, notificationType: NotificationType, withShowOutputAction: Boolean) {
    val group = getNotificationGroup() ?: return
    val notification = group.createNotification(content, notificationType)

    if (withShowOutputAction && project != null) {
      // Add a simple action that shows the Trivy Scan Output tool window when clicked
      notification.addAction(
        NotificationAction.createSimple("Show Scan Output") { TrivyScanOutputManager.showToolWindow(project) }
      )
    }

    notification.notify(project)
  }
}
