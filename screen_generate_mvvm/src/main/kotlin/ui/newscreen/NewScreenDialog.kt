package ui.newscreen

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import data.file.*
import data.repository.SettingsRepositoryImpl
import data.repository.SourceRootRepositoryImpl
import javax.swing.JComponent

class NewScreenDialog(project: Project, currentPath: CurrentPath?) : DialogWrapper(true), NewScreenView {

    private val panel = NewScreenJPanel()

    private val presenter: NewScreenPresenter

    init {
        val fileCreator = FileCreatorImpl(SettingsRepositoryImpl(project))
        val projectStructure = ProjectStructureImpl(project)
        val sourceRootRepository = SourceRootRepositoryImpl(projectStructure)
        val packageExtractor = PackageExtractorImpl(currentPath, sourceRootRepository)
        val writeActionDispatcher = WriteActionDispatcherImpl()
        presenter = NewScreenPresenter(this, fileCreator, sourceRootRepository, packageExtractor, writeActionDispatcher)
        init()
    }

    override fun doOKAction() = presenter.onOkClick(panel.packageTextField.text, panel.nameTextField.text)

    override fun createCenterPanel(): JComponent {
        presenter.onLoadView()
        return panel
    }

    override fun close() = close(DialogWrapper.OK_EXIT_CODE)

    override fun showPackage(packageName: String) {
        panel.packageTextField.text = packageName
    }
}