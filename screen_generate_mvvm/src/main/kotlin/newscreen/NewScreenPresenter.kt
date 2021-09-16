package newscreen


class NewScreenPresenter(private val view: NewScreenView,
                         private val fileCreator: FileCreator,
                         private val sourceRootRepository: SourceRootRepository,
                         private val packageExtractor: PackageExtractor,
                         private val writeActionDispatcher: WriteActionDispatcher) {

    fun onOkClick(packageName: String, screenName: String) {
        writeActionDispatcher.dispatch {
            sourceRootRepository.findFirstModuleSourceRoot()?.let { fileCreator.createScreenFiles(it, packageName, screenName) }
        }
        view.close()
    }

    fun onLoadView() {
        view.showPackage(packageExtractor.extractFromCurrentPath())
    }
}