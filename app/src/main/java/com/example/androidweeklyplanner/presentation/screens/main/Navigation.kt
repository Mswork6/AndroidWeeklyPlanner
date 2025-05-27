package com.example.androidweeklyplanner.presentation.screens.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID

fun NavGraphBuilder.installMainScreen(
    onNavigateToTaskAddScreen: () -> Unit,
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    onNavigateToAllTasks: () -> Unit,
) = composable("main") {
    MainScreen(
        onNavigateToTaskAddScreen = onNavigateToTaskAddScreen,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
        onNavigateToAllTasks = onNavigateToAllTasks
    )
}