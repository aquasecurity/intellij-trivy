package com.aquasecurity.plugins.trivy.ui.notify;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class TrivyNotificationGroup {
    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroup.findRegisteredGroup("Trivy Notifications");


    public static void notifyError(@Nullable Project project, String content) {
        notify(project, content, NotificationType.ERROR);
    }

    public static void notifyWarning(@Nullable Project project, String content) {
        notify(project, content, NotificationType.WARNING);
    }

    public static void notifyInformation(@Nullable Project project, String content) {
        notify(project, content, NotificationType.INFORMATION);
    }

    private static void notify(@Nullable Project project, String content, NotificationType notificationType) {
        NOTIFICATION_GROUP.createNotification(content, notificationType).notify(project);
    }
}
