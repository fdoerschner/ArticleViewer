package de.schwarz.it.feature.overview

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.schwarz.it.feature.overview.DetailArticle.InputError

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()

    BackHandler(navigator.canNavigateBack()) { navigator.navigateBack() }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                OverviewListScreen(
                    articles = viewState.articles,
                    onAddClick = viewState.onAddArticle,
                    onItemClick = { id ->
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    CountingScreen(
                        article = viewState.articleProvider(it),
                        canNavigateBack = navigator.canNavigateBack(),
                        onBack = {
                            navigator.navigateBack()
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun OverviewListScreen(
    articles: List<OverviewArticle>,
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(articles) { article ->
                OverviewItem(article, Modifier.clickable {
                    onItemClick(article.id)
                })
            }
        }
    }
}

@Composable
private fun OverviewItem(article: OverviewArticle, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(article.name, style = MaterialTheme.typography.bodyLarge)
                Text(article.id.toString(), style = MaterialTheme.typography.bodyMedium)
            }

            val color = if (article.count > 0) Color.Green else Color.Red
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountingScreen(article: DetailArticle, canNavigateBack: Boolean, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(
                            onClick = onBack
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                        }
                    }
                },
                title = {
                    Text(article.name)
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CountingElementHeadline(stringResource(R.string.article_brand_headline))
                Text(text = article.brands.joinToString(","))
            }
            item {
                CountingElementHeadline(stringResource(R.string.article_categories_headline))
                Text(text = article.categories.joinToString(","))
            }
            item {
                CountingElementHeadline(stringResource(R.string.article_count_headline))
                CountingInput(article.count, article.changeCount)
            }
        }
    }
}

@Composable
private fun CountingElementHeadline(headline: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(bottom = 4.dp),
        text = headline,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun CountingInput(count: Int, changeCount: (Int) -> InputError?, modifier: Modifier = Modifier) {
    var error by remember {
        mutableStateOf<InputError?>(null)
    }
    val onChange by rememberUpdatedState(changeCount)
    Row(modifier) {
        IconButton(onClick = {
            error = onChange(count + 1)
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "")
        }
        OutlinedTextField(
            value = count.toString(),
            onValueChange = { newText ->
                error = onChange(newText.toInt())
            },
            supportingText = {
                if (error != null) {
                    Text(stringResource(R.string.article_count_input_out_of_range))
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            isError = error != null,

            )
        IconButton(onClick = {
            error = onChange(count - 1)
        }) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OverviewItemPreview() {
    OverviewItem(OverviewArticle("Klosergarten Delikatess Gurkenfäßchen", 123456789, count = 0))
}