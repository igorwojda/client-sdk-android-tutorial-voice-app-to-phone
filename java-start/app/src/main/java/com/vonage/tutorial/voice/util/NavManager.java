package com.vonage.tutorial.voice.util;

import androidx.annotation.IdRes;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

public final class NavManager {

    private static NavManager INSTANCE;
    NavController navController;

    public static NavManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NavManager();
        }

        return INSTANCE;
    }

    public void init(NavController navController) {
        this.navController = navController;
    }

    public void navigate(NavDirections navDirections) {
        navController.navigate(navDirections);
    }

    public void popBackStack(@IdRes int destinationId, Boolean inclusive) {
        navController.popBackStack(destinationId, inclusive);
    }

    public void popBackStack() {
        navController.popBackStack();
    }
}