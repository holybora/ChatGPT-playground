package com.lvs.chatgpt.ui.main

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope


/**
 * Stores information about navigation events to be used with JankStats
 */

@Composable
fun rememberCGPTAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): CGPTAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        windowSizeClass,
        navController,
        coroutineScope,
    ) {
        CGPTAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            windowSizeClass = windowSizeClass
        )
    }
}

@Stable
class CGPTAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

}


@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(key1 = navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("Navigation", destination.route.toString())
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}