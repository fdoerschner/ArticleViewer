package de.schwarz.it.feature.overview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Basic viewstate of the overview, holding necessary data. [articles] should be displayed in the list, [articleProvider] will transform
 * the clicked [OverviewArticle] to a [DetailArticle] and open the pane.
 * If [onAddArticle] is triggered, a navigator should handle and resolve the request.
 */
@Parcelize
data class OverviewViewState(
    val articles: List<OverviewArticle>,
    val articleProvider: (Long) -> DetailArticle,
    val onAddArticle: () -> Unit,
) : Parcelable

/**
 * Overview article representing necessary data to display one list item on the overview.
 */
@Parcelize
data class OverviewArticle(
    val name: String,
    val id: Long,
    val count: Int,
) : Parcelable

/**
 * Detail article holding data necessary for displaying the detail/counting pane.
 */
@Parcelize
data class DetailArticle(
    val name: String,
    val id: Long,
    val count: Int,
    val categories: List<String>,
    val brands: List<String>,
    val changeCount: (Int) -> InputError?,
) : Parcelable {
    /**
     * Companion
     */
    companion object {
        val Empty = DetailArticle("", -1, 0, emptyList(), emptyList()) { null }
    }

    /**
     * basic interface bundling possible input error types.
     */
    sealed interface InputError {
        /**
         * The given input is not in the requested range of 0-999.
         */
        data object OutOfRange : InputError
    }
}