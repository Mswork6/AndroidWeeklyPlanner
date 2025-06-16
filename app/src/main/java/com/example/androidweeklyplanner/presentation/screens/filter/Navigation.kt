package com.example.androidweeklyplanner.presentation.screens.filter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.androidweeklyplanner.presentation.screens.list.ListScreen
import java.util.UUID

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