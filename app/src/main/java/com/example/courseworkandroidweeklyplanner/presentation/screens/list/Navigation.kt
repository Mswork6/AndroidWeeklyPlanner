package com.example.courseworkandroidweeklyplanner.presentation.screens.list

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
) = composable(LIST_ROUTE) {
    ListScreen(
        onNavigateToTaskAddScreen = onNavigateToTaskAddScreen,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen
    )
}

fun NavController.navigateToListScreen() = this.navigate("list")