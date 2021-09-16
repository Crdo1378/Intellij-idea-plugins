import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ShowNotification(private val message: String ?=null) : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        if (message == null){
            e.project?.showNotification("Hello")
        }
        else{
            e.project?.showNotification(message)
        }
    }

    private fun Project.showNotification(message: String) {
        NotificationGroup("someId", NotificationDisplayType.BALLOON)
            .createNotification(
                "My plugin shay",
                message, NotificationType.WARNING,
                null
            )
            .notify(this)
    }
}