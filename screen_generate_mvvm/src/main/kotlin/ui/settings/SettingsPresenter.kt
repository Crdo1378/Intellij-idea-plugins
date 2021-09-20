package ui.settings

import data.repository.SettingsRepository
import model.ScreenElement
import model.Settings

const val UNNAMED_ELEMENT = "UnnamedElement"
const val TEMPLATE = "package %packageName%\n\nclass %name%%screenElement%"
const val SAMPLE_SCREEN_NAME = "Sample"
const val SAMPLE_PACKAGE_NAME = "com.sample"

class SettingsPresenter(private val view: SettingsView,
                        private val settingsRepository: SettingsRepository) {

    val screenElements = mutableListOf<ScreenElement>()
    var currentSelectedScreenElement: ScreenElement? = null
    var isModified = false
    lateinit var initialScreenElements: List<ScreenElement>

    fun onLoadView() {
        initialScreenElements = settingsRepository.loadSettings().screenElements
        copyScreenElementsFromInitialSettings()
        view.setUpListeners()
        view.showScreenElements(screenElements)
    }

    private fun copyScreenElementsFromInitialSettings() {
        screenElements.clear()
        initialScreenElements.mapTo(screenElements) { it.copy() }
    }

    fun onAddClick() {
        val newScreenElement = ScreenElement(UNNAMED_ELEMENT, TEMPLATE)
        screenElements.add(newScreenElement)
        view.addScreenElement(newScreenElement)
        view.selectScreenElement(screenElements.size - 1)
        isModified = true
    }

    fun onDeleteClick(index: Int) {
        screenElements.removeAt(index)
        view.removeScreenElement(index)
        isModified = true
    }

    fun onScreenElementSelect(index: Int) {
        if (index in 0 until screenElements.size) {
            val selectedElement = screenElements[index]
            currentSelectedScreenElement = selectedElement
            view.removeTextChangeListeners()
            view.showName(selectedElement.name)
            view.showTemplate(selectedElement.template)
            view.showSampleCode(selectedElement.body(SAMPLE_SCREEN_NAME, SAMPLE_PACKAGE_NAME))
            view.addTextChangeListeners()
        } else {
            currentSelectedScreenElement = null
            view.removeTextChangeListeners()
            view.showName("")
            view.showTemplate("")
            view.showSampleCode("")
        }
    }

    fun onNameChange(name: String) {
        currentSelectedScreenElement?.let {
            it.name = name
            view.updateScreenElement(screenElements.indexOf(it), it)
            view.showSampleCode(it.body(SAMPLE_SCREEN_NAME, SAMPLE_PACKAGE_NAME))
            isModified = true
        }
    }

    fun onApplySettings() {
        initialScreenElements = screenElements.toList()
        copyScreenElementsFromInitialSettings()
        settingsRepository.update(Settings(initialScreenElements, "", ""))
        isModified = false
    }

    fun onResetSettings() {
        copyScreenElementsFromInitialSettings()
        view.clearScreenElements()
        view.showScreenElements(screenElements)
        isModified = false
    }

    fun onMoveDownClick(index: Int) = onMoveClick(index, index + 1)

    fun onMoveUpClick(index: Int) = onMoveClick(index, index - 1)

    private fun onMoveClick(index: Int, destinationIndex: Int) {
        screenElements.swap(index, destinationIndex)
        view.updateScreenElement(index, screenElements[index])
        view.updateScreenElement(destinationIndex, screenElements[destinationIndex])
        view.selectScreenElement(destinationIndex)
        isModified = true
    }

    fun onTemplateChange(text: String) {
        currentSelectedScreenElement?.let {
            it.template = text
            view.showSampleCode(it.body(SAMPLE_SCREEN_NAME, SAMPLE_PACKAGE_NAME))
            isModified = true
        }
    }

    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}
