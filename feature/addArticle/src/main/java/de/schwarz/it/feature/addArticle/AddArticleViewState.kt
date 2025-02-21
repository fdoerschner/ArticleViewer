package de.schwarz.it.feature.addArticle

import de.schwarz.it.core.common.UiString

/**
 * Add article view state holding the list of input fields to create an article and callbacks to handle events.
 */
data class AddArticleViewState(
    val inputs: List<InputField>,
    val onSave: () -> Unit,
    val onClose: () -> Unit,
)

/**
 * Ui representation of an input field.
 */
data class InputField(
    val value: String,
    val label: UiString,
    val onChange: (String) -> Unit,
    val isMandatory: Boolean = false,
    val inputType: InputType = InputType.Text,
    val inputError: InputError? = null,
)

/**
 * abstraction of the different possible input types, triggering and holding what kind of keyboard should be shown.
 */
enum class InputType {
    Number, Text
}

/**
 * Different kind of input errors that should be handled in the ui.
 */
sealed interface InputError {
    /**
     * The Code input already exists, a new article-code as to be created.
     */
    data object CodeAlreadyTaken : InputError

    /**
     * The field associated with this error is mandatory and must be filled.
     */
    data object MandatoryNotFilled : InputError

    /**
     * The field with this error has a number of characters are not correct.
     */
    data object InputOutOfBounds : InputError
}