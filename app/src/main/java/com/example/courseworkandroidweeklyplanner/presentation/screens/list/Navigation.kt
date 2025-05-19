package com.example.courseworkandroidweeklyplanner.presentation.screens.list

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.courseworkandroidweeklyplanner.presentation.screens.list.ListScreen
import java.util.UUID

private const val LIST_ROUTE = "list"

fun NavGraphBuilder.installListScreen(
    onNavigateToTaskAddScreen: () -> Unit,
    onNavigateToTaskEditScreen: (UUID) -> Unit,
    onNavigateToTaskOpenScreen: (UUID) -> Unit,
    onNavigateToWeekTasks: (NavBackStackEntry) -> Unit
) = composable(LIST_ROUTE) { backStackEntry: NavBackStackEntry ->
    ListScreen(
        onNavigateToTaskAddScreen = onNavigateToTaskAddScreen,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
        onNavigateToWeekTasks = { onNavigateToWeekTasks(backStackEntry) }
    )
}

fun NavController.navigateToListScreen() = this.navigate("list")