package de.schwarz.it.articleviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import de.schwarz.it.articleviewer.ui.theme.ArticleViewerTheme
import de.schwarz.it.core.navigation.NavigationService
import de.schwarz.it.core.navigation.NavigationTarget
import de.schwarz.it.core.navigation.NavigationTarget.Route
import de.schwarz.it.data.database.ArticleDao
import de.schwarz.it.data.database.entities.Article
import de.schwarz.it.data.datafetcher.ArticleDataFetcher
import de.schwarz.it.feature.addArticle.AddArticleScreen
import de.schwarz.it.feature.overview.OverviewScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main entry point to the app. Holds navigation, opens and closes the database,
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var articleDao: ArticleDao

    @Inject
    lateinit var dataFetcher: ArticleDataFetcher

    @Inject
    lateinit var navigationService: NavigationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openAndFillDatabase()
        enableEdgeToEdge()
        setContent {
            ArticleViewerTheme {
                var target by remember {
                    mutableStateOf<NavigationTarget>(
                        value = Route(Route.RouteTargets.Overview),
                        policy = neverEqualPolicy(),
                    )
                }
                var showDialog by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(Unit) {
                    navigationService.currentNavigation.collect {
                        target = it
                        showDialog = target == NavigationTarget.Dialog
                    }
                }
                var currentDestination by rememberSaveable(target) {
                    mutableStateOf((target as? Route)?.target.toBottomBarItem())
                }
                NavigationSuiteScaffold(
                    navigationSuiteItems = {
                        BottomBarItem.entries.forEach { item ->
                            item(
                                icon = {
                                    Icon(item.icon, contentDescription = stringResource(item.titleId))
                                },
                                label = {
                                    Text(stringResource(item.titleId))
                                },
                                onClick = {
                                    currentDestination = item
                                },
                                selected = item == currentDestination,
                            )
                        }
                    },
                ) {
                    (target as? Route)?.let { route ->
                        NavigationTargetSink(route)
                    }

                    if (showDialog) {
                        AddArticleScreen(onDismiss = {
                            showDialog = false
                        })
                    }
                }
            }
        }
    }

    @Composable
    private fun NavigationTargetSink(target: Route) {
        when (val route = target.target) {
            is Route.RouteTargets.Detail -> OverviewScreen(modifier = Modifier.safeContentPadding(), deeplinkId = route.id)
            Route.RouteTargets.Overview -> OverviewScreen(modifier = Modifier.safeContentPadding())
            Route.RouteTargets.Settings -> Unit
            else -> Unit
        }
    }

    private fun openAndFillDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (!articleDao.hasArticles()) {
                val articles = dataFetcher.loadArticles()
                articleDao.addAll(
                    articles.map { article ->
                        Article(
                            code = article.code,
                            name = article.name,
                            packageQuantity = article.quantity,
                            brands = article.brands,
                            categories = article.categories,
                            count = 0,
                        )
                    },
                )
            }
        }
    }

    private fun Route.RouteTargets?.toBottomBarItem() = when (this) {
        Route.RouteTargets.Overview -> BottomBarItem.Overview
        is Route.RouteTargets.Detail -> BottomBarItem.Overview
        Route.RouteTargets.Settings -> BottomBarItem.Settings
        null -> BottomBarItem.Overview
    }

    private enum class BottomBarItem(
        @StringRes val titleId: Int,
        val icon: ImageVector,
    ) {
        Overview(R.string.nav_item_overview, Icons.Default.Home),
        Settings(R.string.nav_item_settings, Icons.Default.Settings),
    }
}
