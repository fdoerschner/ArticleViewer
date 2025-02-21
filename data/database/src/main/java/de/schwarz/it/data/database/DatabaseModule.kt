package de.schwarz.it.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class DatabaseModule {
    @Provides
    @Singleton
    internal fun provideDatabase(
        @ApplicationContext context: Context,
    ): ArticleDatabase = Room.databaseBuilder(context, ArticleDatabase::class.java, "articles").build()

    @Provides
    fun provideArticleDao(database: ArticleDatabase) = database.articleDao
}