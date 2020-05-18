package com.vonage.tutorial.voice.util

import androidx.navigation.NavController
import androidx.navigation.NavDirections

object NavManager {
    private lateinit var navController: NavController

    fun init(navController: NavController) {
        this.navController = navController
    }

    fun navigate(navDirections: NavDirections) {
        navController.navigate(navDirections)
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}
