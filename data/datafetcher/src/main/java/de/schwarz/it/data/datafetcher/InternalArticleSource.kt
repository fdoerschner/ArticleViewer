package de.schwarz.it.data.datafetcher

import org.jetbrains.kotlinx.dataframe.annotations.ColumnName

internal data class InternalArticleSource(
    @ColumnName("code") val code: Long,
    @ColumnName("product_name_de") val name: String,
    @ColumnName("quantity") val quantity: String?,
    @ColumnName("brands") val brands: String?,
    @ColumnName("categories") val categories: String?,
)
