package ui.newscreen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import data.file.CurrentPath

class NewScreenAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val currentPath = e.getData(VIRTUAL_FILE)?.let {
            CurrentPath(
                it.path,
                it.isDirectory
            )
        }
        NewScreenDialog(e.project!!, currentPath).show()
    }
}