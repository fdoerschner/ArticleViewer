package de.schwarz.it.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("article")
data class Article(
    @PrimaryKey val code: Long,
    val name: String,
    val packageQuantity: String,
    val brands: List<String>,
    val categories: List<String>,
    val count: Int,
)
