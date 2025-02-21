package de.schwarz.it.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.schwarz.it.data.database.ArticleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val articleDao: ArticleDao,
    overviewNavigator: OverviewNavigator,
) : ViewModel() {
    val viewState: StateFlow<OverviewViewState> = articleDao.getAll().map { dbArticles ->
        OverviewViewState(
            articles = dbArticles.map { dbArticle ->
                OverviewArticle(name = dbArticle.name, id = dbArticle.code, count = dbArticle.count)
            },
            onAddArticle = overviewNavigator::addArticle,
            articleProvider = { articleId ->
                dbArticles.find { it.code == articleId }?.let {
                    DetailArticle(
                        name = it.name,
                        id = it.code,
                        count = it.count,
                        categories = it.categories,
                        brands = it.brands,
                        changeCount = { count ->
                            when {
                                count !in 0..999 -> DetailArticle.InputError.OutOfRange
                                else -> {
                                    changeCount(it.code, count)
                                    null
                                }
                            }
                        }
                    )
                } ?: DetailArticle.Empty
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), OverviewViewState(emptyList(), { DetailArticle.Empty }, {}))

    private fun changeCount(id: Long, count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            articleDao.updateCount(id, count)
        }
    }
}