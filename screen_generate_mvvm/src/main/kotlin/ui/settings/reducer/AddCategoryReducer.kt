package ui.settings.reducer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import model.Category
import model.CategoryScreenElements
import ui.settings.SettingsEffect
import ui.settings.SettingsState
import javax.inject.Inject

interface AddCategoryReducer {
    operator fun invoke()
}

class AddCategoryReducerImpl @Inject constructor(
    private val state: MutableStateFlow<SettingsState>,
    effect: MutableSharedFlow<SettingsEffect>,
    scope: CoroutineScope,
    private val selectScreenElementReducer: SelectScreenElementReducer
) : BaseReducer(state, effect, scope), AddCategoryReducer {

    override fun invoke() {
        val newId = state.value.categories.size
        val newCategories = state.value.categories.toMutableList()
            .apply { add(CategoryScreenElements(Category.getDefault(newId), emptyList())) }
        pushState {
            copy(
                isModified = true,
                selectedElementIndex = null,
                categories = newCategories,
                selectedCategoryIndex = newCategories.size - 1
            )
        }
        selectScreenElementReducer(-1)
    }
}
