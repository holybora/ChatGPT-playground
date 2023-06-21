package com.lvs.chatgpt.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.lvs.chatgpt.ui.CGPTDestinations.CHATS_ROUTE
import com.lvs.chatgpt.ui.CGPTDestinations.SETTINGS_ROUTE

object CGPTDestinations {
    const val CHATS_ROUTE = "chats"
    const val SETTINGS_ROUTE = "settings"
}

class CGPTNavigationActions(navController: NavController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(CHATS_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(SETTINGS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}