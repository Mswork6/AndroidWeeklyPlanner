package com.example.taskplanner.presentation.screens.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID

private const val LIST_ROUTE = "list"

fun NavGraphBuilder.installListScreen(
    onNavigateToTaskAddScreen: () -> Unit,
    onNavigateToTaskEditScreen: (UUID) -> Unit,
    onNavigateToTaskOpenScreen: (UUID) -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onNavigateToWeekTasks: (NavBackStackEntry) -> Unit
) = composable(LIST_ROUTE) { backStackEntry: NavBackStackEntry ->
    ListScreen(
        onNavigateToTaskAddScreen = onNavigateToTaskAddScreen,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
        onNavigateToFilterScreen = onNavigateToFilterScreen,
        onNavigateToWeekTasks = { onNavigateToWeekTasks(backStackEntry) },
        modifier =  Modifier.fillMaxSize()
    )
}

fun NavController.navigateToListScreen() = this.navigate(LIST_ROUTE)