package de.schwarz.it.articleviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
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
                        policy = neverEqualPolicy()
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
                    val route = when ((target as? Route)?.target) {
                        Route.RouteTargets.Overview -> BottomBarItem.Overview
                        Route.RouteTargets.Settings -> BottomBarItem.Settings
                        null -> BottomBarItem.Overview
                    }
                    mutableStateOf(route)
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
                                selected = item == currentDestination
                            )
                        }
                    }
                ) {
                    when (currentDestination) {
                        BottomBarItem.Overview -> {
                            OverviewScreen(Modifier.safeContentPadding())
                        }

                        BottomBarItem.Settings -> Unit
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

    private fun openAndFillDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (!articleDao.hasArticles()) {
                val articles = dataFetcher.loadArticles()
                articleDao.addAll(articles.map { article ->
                    Article(
                        code = article.code,
                        name = article.name,
                        packageQuantity = article.quantity,
                        brands = article.brands,
                        categories = article.categories,
                        count = 0
                    )
                })
            }
        }
    }
}
