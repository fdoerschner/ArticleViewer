package de.schwarz.it.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.schwarz.it.data.database.entities.Article
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ArticleDao {
    @Insert
    abstract fun addAll(articles: List<Article>)

    @Insert
    abstract fun singleArticle(article: Article)

    @Query("SELECT * FROM article")
    abstract fun getAll(): Flow<List<Article>>

    @Query("SELECT COUNT(code) > 0 FROM article")
    abstract fun hasArticles(): Boolean

    @Query("UPDATE article SET count = :count WHERE code = :code")
    abstract fun updateCount(code: Long, count: Int)
}