package de.schwarz.it.core.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Base string interface for resolving texts in the ui, disregarding their different nature of resource or raw text.
 */
sealed interface UiString {
    /**
     * Container holding a raw string. Mostly used if a text from an api call needs to be sent to the ui.
     */
    data class RawString(val text: String) : UiString

    /**
     * Container holding a string resource id, so the ui can resolve the text if necessary.
     */
    data class ResourceString(@StringRes val textId: Int) : UiString

    /**
     * resolve the container to the actual text.
     */
    @Composable
    fun asString() = when (this) {
        is RawString -> text
        is ResourceString -> stringResource(textId)
    }
}