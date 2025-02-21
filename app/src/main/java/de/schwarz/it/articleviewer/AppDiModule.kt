package de.schwarz.it.articleviewer

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.schwarz.it.core.navigation.NavigationService
import de.schwarz.it.core.navigation.NavigationTarget
import de.schwarz.it.feature.addArticle.AddArticleNavigator
import de.schwarz.it.feature.overview.OverviewNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDiModule {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideRouteRepository() = object : NavigationService {
        private val _navigation = MutableSharedFlow<NavigationTarget>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        override val currentNavigation: Flow<NavigationTarget>
            get() = _navigation.onEach { _navigation.resetReplayCache() }

        override fun navigate(target: NavigationTarget) {
            _navigation.tryEmit(target)
        }
    }

    @Provides
    fun provideOverviewNavigator(navigationService: NavigationService) = OverviewNavigator {
        navigationService.navigate(NavigationTarget.Dialog)
    }

    @Provides
    fun provideAddArticleNavigator(navigationService: NavigationService) = AddArticleNavigator {
        navigationService.navigate(NavigationTarget.CloseDialog)
    }
}