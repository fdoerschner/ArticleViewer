package de.schwarz.it.data.datafetcher

/**
 * Interface to fetch data from any source.
 */
fun interface ArticleDataFetcher {
    /**
     * Load and return a list of articles
     */
    fun loadArticles(): List<Article>
}