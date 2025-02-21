package de.schwarz.it.core.navigation

import kotlinx.coroutines.flow.Flow

/**
 * Service to abstract the possible [NavigationTarget] from every feature-module that needs to.
 */
interface NavigationService {
    val currentNavigation: Flow<NavigationTarget>

    /**
     * Navigate to the given [target]
     */
    fun navigate(target: NavigationTarget)
}