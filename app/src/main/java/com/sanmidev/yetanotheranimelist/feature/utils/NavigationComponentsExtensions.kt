package com.sanmidev.yetanotheranimelist.feature.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections


    /***
     * Extension method used to safely navigate between fragment.
     * @param navDirections is the directon to go to
     * @param currentFragmentId is the id of the current fragment in the backstack
     * @author oluwasanmi Aderibigbe
     */

    fun NavController.navigateSafely(
        navDirections: NavDirections,
        currentFragmentId: Int
    ) {
        if (this.currentDestination?.id == currentFragmentId) {
            this.navigate(navDirections)
        }
    }
