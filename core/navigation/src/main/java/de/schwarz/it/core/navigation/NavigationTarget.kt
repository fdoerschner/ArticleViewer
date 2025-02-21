package de.schwarz.it.core.navigation

/**
 * Navigation Target differentiating between the different possible routes of the app.
 */
sealed interface NavigationTarget {
    /**
     * Open a dialog. Normally should be a generic data class holding necessary date to display the wanted dialog.
     */
    data object Dialog : NavigationTarget

    /**
     * Helper target to close the current [Dialog] from the outside.
     */
    data object CloseDialog : NavigationTarget

    /**
     * Base-Route to a top level target. Holds a [target] in itself to differentiate between the different top level objects.
     */
    data class Route(val target: RouteTargets) : NavigationTarget {
        /**
         * Possible top level routes
         */
        sealed interface RouteTargets {
            /**
             * Showing the overview with nothing preselected.
             */
            data object Overview : RouteTargets

            /**
             * Showing the settings.
             */
            data object Settings : RouteTargets

            /**
             * Showing the Detail with the given [id]
             */
            data class Detail(val id: Long) : RouteTargets
        }
    }
}