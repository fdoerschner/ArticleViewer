package de.schwarz.it.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.schwarz.it.data.database.entities.Article

/**
 * Article database
 */
@Database(
    entities = [
        Article::class,
    ],
    version = 1,
)
@TypeConverters(ListConverter::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val articleDao: ArticleDao
}