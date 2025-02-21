package de.schwarz.it.core.navigation

sealed interface NavigationTarget {
    data object Dialog: NavigationTarget

    data object CloseDialog: NavigationTarget

    data class Route(val target: RouteTargets) : NavigationTarget {
        sealed interface RouteTargets {
            data object Overview: RouteTargets

            data object Settings: RouteTargets

            data class Detail(val id: Long): RouteTargets
        }
    }
}