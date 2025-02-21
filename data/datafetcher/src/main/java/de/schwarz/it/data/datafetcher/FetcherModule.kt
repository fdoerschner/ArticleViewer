package de.schwarz.it.data.datafetcher

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
class FetcherModule {
    @Provides
    fun provideDataFetcher(
        @ApplicationContext context: Context,
    ): ArticleDataFetcher = StaticLoader(context)
}