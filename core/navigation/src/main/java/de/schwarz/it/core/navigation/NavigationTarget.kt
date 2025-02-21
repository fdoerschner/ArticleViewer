package de.schwarz.it.core.navigation

sealed interface NavigationTarget {
    data object Dialog: NavigationTarget

    data object CloseDialog: NavigationTarget

    data class Route(val target: RouteTargets) : NavigationTarget {
        enum class RouteTargets {
            Overview, Settings
        }
    }
}