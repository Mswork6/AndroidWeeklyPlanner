package com.example.courseworkandroidweeklyplanner.presentation.screens.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID

fun NavGraphBuilder.installMainScreen(
    onNavigateToTaskAddScreen: () -> Unit,
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
) = composable("main") {
    MainScreen(
        onNavigateToTaskAddScreen = onNavigateToTaskAddScreen,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
    )
}