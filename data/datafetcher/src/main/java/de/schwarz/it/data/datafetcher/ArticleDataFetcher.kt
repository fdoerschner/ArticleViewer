package de.schwarz.it.data.datafetcher

fun interface ArticleDataFetcher {
    fun loadArticles(): List<Article>
}