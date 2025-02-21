package de.schwarz.it.core.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiString {
    data class RawString(val text: String) : UiString

    data class ResourceString(@StringRes val textId: Int) : UiString

    @Composable
    fun asString() = when (this) {
        is RawString -> text
        is ResourceString -> stringResource(textId)
    }
}