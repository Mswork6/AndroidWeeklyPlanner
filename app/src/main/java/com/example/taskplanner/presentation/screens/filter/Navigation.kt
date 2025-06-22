package com.example.taskplanner.presentation.screens.filter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val FILTER_ROUTE = "filter"

fun NavGraphBuilder.installFilterScreen(
    onNavigateToListScreen: (NavBackStackEntry) -> Unit
) = composable(FILTER_ROUTE) { backStackEntry: NavBackStackEntry ->
    FilterScreen(
        onNavigateToListScreen = { onNavigateToListScreen(backStackEntry)},
        modifier = Modifier.fillMaxSize()
    )
}

fun NavController.navigateToFilterScreen() = this.navigate(FILTER_ROUTE)