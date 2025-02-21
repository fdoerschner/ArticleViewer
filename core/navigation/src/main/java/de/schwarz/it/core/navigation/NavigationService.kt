package de.schwarz.it.core.navigation

import kotlinx.coroutines.flow.Flow

interface NavigationService {
    val currentNavigation: Flow<NavigationTarget>

    fun navigate(target: NavigationTarget)
}