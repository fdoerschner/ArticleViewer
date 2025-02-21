package de.schwarz.it.data.datafetcher

data class Article(
    val code : Long,
    val name : String,
    val quantity : String,
    val brands: List<String>,
    val categories: List<String>,
)
