package de.schwarz.it.feature.addArticle

/**
 * Article navigator to handle outgoing navigation events.
 */
fun interface AddArticleNavigator {
    /**
     * Open the overview and/or details, if an [id] is provided.
     */
    fun openOverview(id: Long?)
}