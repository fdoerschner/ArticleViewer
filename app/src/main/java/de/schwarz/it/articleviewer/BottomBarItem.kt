package de.schwarz.it.articleviewer

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomBarItem(
    @StringRes val titleId: Int,
    val icon: ImageVector,
) {
    Overview(R.string.nav_item_overview, Icons.Default.Home),
    Settings(R.string.nav_item_settings, Icons.Default.Settings)
}