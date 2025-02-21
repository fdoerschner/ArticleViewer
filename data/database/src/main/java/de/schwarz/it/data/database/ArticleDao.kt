package de.schwarz.it.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.schwarz.it.data.database.entities.Article
import kotlinx.coroutines.flow.Flow

/**
 * Access to the article entity table.
 */
@Dao
abstract class ArticleDao {
    /**
     * Add a batch of articles to the database.
     */
    @Insert
    abstract fun addAll(articles: List<Article>)

    /**
     * Add a single article.
     */
    @Insert
    abstract fun singleArticle(article: Article)

    /**
     * Receives a flow with every database update to every article in the database.
     */
    @Query("SELECT * FROM article ORDER BY code ASC")
    abstract fun getAll(): Flow<List<Article>>

    /**
     * check if the database has any articles.
     */
    @Query("SELECT COUNT(code) > 0 FROM article")
    abstract fun hasArticles(): Boolean

    /**
     * Update the [count] of an article with the given [code]
     */
    @Query("UPDATE article SET count = :count WHERE code = :code")
    abstract fun updateCount(code: Long, count: Int)
}