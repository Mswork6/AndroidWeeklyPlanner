package com.example.courseworkandroidweeklyplanner.presentation.screens.task

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import java.util.UUID

fun NavController.navigateToEditScreen(taskId: UUID) = this.navigate("edit/$taskId")

fun NavGraphBuilder.installEditScreen(
    onNavigateBack: (NavBackStackEntry) -> Unit
) = composable(
    route = "edit/{taskId}",
    arguments = listOf(
        navArgument("taskId") {
            nullable = false
            type = NavType.StringType
        }
    )
) { backStackEntry: NavBackStackEntry ->
    val taskId: UUID = backStackEntry.arguments
        ?.getString("taskId")
        ?.let(String::toUuidOrNull)
        ?: error("taskId is not specified!")
    val mode = TaskScreenViewModel.Mode.Edit(taskId)
    val viewModel = hiltViewModel<TaskScreenViewModel, TaskScreenViewModel.Factory>(
        creationCallback = { factory: TaskScreenViewModel.Factory ->
            factory.create(mode)
        }
    )
    TaskScreen(
        viewModel = viewModel,
        onNavigateBack = { onNavigateBack(backStackEntry) },
        modifier = Modifier.fillMaxSize()
    )
}

fun NavController.navigateToViewScreen(taskId: UUID) = this.navigate("view/$taskId")

fun NavGraphBuilder.installViewScreen(
    onNavigateBack: (NavBackStackEntry) -> Unit
) = composable(
    route = "view/{taskId}",
    arguments = listOf(
        navArgument("taskId") {
            nullable = false
            type = NavType.StringType
        }
    ),
    deepLinks = listOf(
        navDeepLink { uriPattern = "todo://view/{taskId}" }
    )
) { backStackEntry: NavBackStackEntry ->
    val taskId: UUID = backStackEntry.arguments
        ?.getString("taskId")
        ?.let(String::toUuidOrNull)
        ?: error("taskId is not specified!")
    val mode = TaskScreenViewModel.Mode.View(taskId)
    val viewModel = hiltViewModel<TaskScreenViewModel, TaskScreenViewModel.Factory>(
        creationCallback = { factory: TaskScreenViewModel.Factory ->
            factory.create(mode)
        }
    )
    TaskScreen(
        viewModel = viewModel,
        onNavigateBack = { onNavigateBack(backStackEntry) },
        modifier = Modifier.fillMaxSize()
    )
}

fun NavController.navigateToAddScreen() = this.navigate("add")

fun NavGraphBuilder.installAddScreen(
    onNavigateBack: (NavBackStackEntry) -> Unit
) = composable(route = "add") { backStackEntry: NavBackStackEntry ->
    val mode = TaskScreenViewModel.Mode.Add
    val viewModel = hiltViewModel<TaskScreenViewModel, TaskScreenViewModel.Factory>(
        creationCallback = { factory: TaskScreenViewModel.Factory ->
            factory.create(mode)
        }
    )
    TaskScreen(
        viewModel = viewModel,
        onNavigateBack = { onNavigateBack(backStackEntry) },
        modifier = Modifier.fillMaxSize()
    )
}

private fun String.toUuidOrNull(): UUID? = try {
    UUID.fromString(this)
} catch (exception: IllegalArgumentException) {
    null
}