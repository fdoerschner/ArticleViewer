package de.schwarz.it.feature.overview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverviewViewState(
    val articles: List<OverviewArticle>,
    val articleProvider: (Long) -> DetailArticle,
    val onAddArticle: () -> Unit,
) : Parcelable

@Parcelize
data class OverviewArticle(
    val name: String,
    val id: Long,
    val count: Int,
) : Parcelable

@Parcelize
data class DetailArticle(
    val name: String,
    val id: Long,
    val count: Int,
    val categories: List<String>,
    val brands: List<String>,
    val changeCount: (Int) -> InputError?,
) : Parcelable {
    companion object {
        val Empty = DetailArticle("", -1, 0, emptyList(), emptyList()) { null }
    }

    sealed interface InputError {
        data object OutOfRange : InputError
    }
}