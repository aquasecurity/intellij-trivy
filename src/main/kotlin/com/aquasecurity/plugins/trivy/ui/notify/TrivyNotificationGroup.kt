package com.aquasecurity.plugins.trivy.ui.notify

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object TrivyNotificationGroup {
    private val NOTIFICATION_GROUP = NotificationGroup.findRegisteredGroup("Trivy Notifications")


    fun notifyError(project: Project?, content: String) {
        notify(project, content, NotificationType.ERROR)
    }

    fun notifyInformation(project: Project?, content: String) {
        notify(project, content, NotificationType.INFORMATION)
    }

    private fun notify(project: Project?, content: String, notificationType: NotificationType) {
        NOTIFICATION_GROUP!!.createNotification(content, notificationType).notify(project)
    }
}