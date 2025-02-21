package de.schwarz.it.data.datafetcher

import android.content.Context
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.dropNulls
import org.jetbrains.kotlinx.dataframe.api.toList
import org.jetbrains.kotlinx.dataframe.io.readCSV

internal class StaticLoader(
    private val context: Context,
) : ArticleDataFetcher {
    private val codeColumn by column<Long>("code")
    private val nameColumn by column<String>("product_name_de")

    override fun loadArticles(): List<Article> {
        val inputStream = context.assets.open("demeter.csv")
        val inputs = DataFrame.readCSV(stream = inputStream, delimiter = '\t')
            .dropNulls(codeColumn, nameColumn)
            .cast<InternalArticleSource>()
            .toList()
        return inputs.map { csvArticle ->
            Article(
                code = csvArticle.code,
                name = csvArticle.name,
                quantity = csvArticle.quantity.orEmpty(),
                brands = csvArticle.brands?.split(",") ?: emptyList(),
                categories = csvArticle.categories?.split(",") ?: emptyList(),
            )
        }
            .distinctBy { it.code }
    }
}