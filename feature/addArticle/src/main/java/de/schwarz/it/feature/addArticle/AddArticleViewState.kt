package de.schwarz.it.feature.addArticle

import de.schwarz.it.core.common.UiString

data class AddArticleViewState(
    val inputs: List<InputField>,
    val onSave: () -> Unit,
    val onClose: () -> Unit,
)

data class InputField(
    val value: String,
    val label: UiString,
    val onChange: (String) -> Unit,
    val isMandatory: Boolean = false,
    val inputType: InputType = InputType.Text,
    val inputError: InputError? = null,
)

enum class InputType {
    Number, Text
}

sealed interface InputError {
    data object CodeAlreadyTaken: InputError

    data object MandatoryNotFilled: InputError

    data object InputOutOfBounds: InputError
}