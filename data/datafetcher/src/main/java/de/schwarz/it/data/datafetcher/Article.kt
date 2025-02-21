package de.schwarz.it.data.datafetcher

/**
 * Api representation of an article.
 */
data class Article(
    val code: Long,
    val name: String,
    val quantity: String,
    val brands: List<String>,
    val categories: List<String>,
)
