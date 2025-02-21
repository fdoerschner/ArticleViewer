package de.schwarz.it.feature.overview

/**
 * Navigator interface for every needed action from the overview and that should be handled by the implementor.
 */
fun interface OverviewNavigator {
    /**
     * The overview needs to add an article.
     */
    fun addArticle()
}