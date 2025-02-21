package de.schwarz.it.articleviewer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Android application. Used and needed for dependency injection.
 */
@HiltAndroidApp
class ArticleViewerApplication : Application()