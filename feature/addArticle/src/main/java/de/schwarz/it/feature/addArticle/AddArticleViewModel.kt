package de.schwarz.it.feature.addArticle

import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.schwarz.it.core.common.UiString
import de.schwarz.it.data.database.ArticleDao
import de.schwarz.it.data.database.entities.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddArticleViewModel @Inject constructor(
    private val articleDao: ArticleDao,
    private val addArticleNavigator: AddArticleNavigator,
) : ViewModel() {
    private val _inputsMap = MutableStateFlow(mapOf<InputFieldType, String>())
    private val _inputErrors = MutableStateFlow(mapOf<InputFieldType, InputError?>())
    val viewState = combine(
        _inputsMap.onEach { currentInput ->
            _inputErrors.value = InputFieldType.entries.associateWith { type ->
                checkForInputError(type, currentInput[type].orEmpty())
            }
        },
        _inputErrors
    ) { currentInput, errors ->
        AddArticleViewState(
            inputs = InputFieldType.entries
                .sortedBy { it.order }
                .map { type ->
                    createInputField(type, currentInput[type].orEmpty(), errors[type])
                },
            onClose = ::close,
            onSave = {
                val code = currentInput[InputFieldType.Code] ?: run {
                    _inputErrors.update { it.plus(InputFieldType.Code to InputError.MandatoryNotFilled) }
                    return@AddArticleViewState
                }
                val name = currentInput[InputFieldType.Name] ?: run {
                    _inputErrors.update { it.plus(InputFieldType.Name to InputError.MandatoryNotFilled) }
                    return@AddArticleViewState
                }

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        articleDao.singleArticle(
                            Article(
                                code = code.toLong(),
                                name = name,
                                packageQuantity = currentInput[InputFieldType.PackageQuantity].orEmpty(),
                                brands = currentInput[InputFieldType.Brand].orEmpty().split(","),
                                categories = currentInput[InputFieldType.Categories].orEmpty().split(","),
                                count = currentInput[InputFieldType.Count]?.toInt() ?: 0
                            )
                        )
                        close()
                    } catch (e: SQLiteConstraintException) {
                        _inputErrors.update { it.plus(InputFieldType.Code to InputError.CodeAlreadyTaken) }
                    }
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AddArticleViewState(emptyList(), onSave = {}, onClose = {}))

    private fun createInputField(type: InputFieldType, value: String, inputError: InputError?): InputField {
        val label = UiString.ResourceString(type.textId)
        return InputField(
            value = value,
            label = label,
            onChange = { newContent ->
                val checkedInput = when (type) {
                    InputFieldType.Code -> newContent.filter { it.isDigit() }.take(7)
                    InputFieldType.Count -> newContent.filter { it.isDigit() }
                    else -> newContent
                }
                _inputsMap.update {
                    it.plus(type to checkedInput)
                }
            },
            isMandatory = type in listOf(InputFieldType.Code, InputFieldType.Name),
            inputType = when (type) {
                InputFieldType.Code, InputFieldType.Count -> InputType.Number
                else -> InputType.Text
            },
            inputError = inputError,
        )
    }

    private fun checkForInputError(type: InputFieldType, value: String): InputError? {
        val count = value.trim().count()
        return when (type) {
            InputFieldType.Code -> when (count) {
                0 -> InputError.MandatoryNotFilled
                in 1..6 -> InputError.InputOutOfBounds
                else -> null
            }

            InputFieldType.Name -> when (count) {
                0 -> InputError.MandatoryNotFilled
                in 1..3 -> InputError.InputOutOfBounds
                else -> null
            }

            else -> null
        }
    }

    private fun close() {
        _inputsMap.update { emptyMap() }
        addArticleNavigator.openOverview()
    }

    enum class InputFieldType(val order: Int, @StringRes val textId: Int) {
        Code(0, R.string.add_article_code_input_label),
        Name(1, R.string.add_article_name_input_label),
        Brand(2, R.string.add_article_brand_input_label),
        PackageQuantity(3, R.string.add_article_package_quantity_label),
        Categories(4, R.string.add_article_categories_input_label),
        Count(5, R.string.add_article_count_input_label)
    }
}