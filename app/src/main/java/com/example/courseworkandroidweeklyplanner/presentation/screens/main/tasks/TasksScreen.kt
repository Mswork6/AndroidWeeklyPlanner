package com.example.courseworkandroidweeklyplanner.presentation.screens.main.tasks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.courseworkandroidweeklyplanner.domain.model.Day
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.component.DayCard
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.tasks.component.TaskDialogWindow
import java.util.UUID

@Composable
fun TasksScreen(
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    TasksScreenContent(
        state = state,
        onAction = viewModel::handle,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
        modifier = modifier
    )
}

@Composable
private fun TasksScreenContent(
    state: TasksScreenState,
    onAction: (TasksScreenAction) -> Unit,
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    modifier: Modifier
) = when (state) {
    is TasksScreenState.Initial -> {
        CircularProgressIndicator()
    }
    is TasksScreenState.Default -> {
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(items = state.days, key = Day::id) { day ->
                DayCard(
                    day = day,
                    onTaskItemClick = { onAction(TasksScreenAction.TaskDialogAction.Open(it)) },
                    dayItemModifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    taskItemModifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        when (state.dialogState) {
            is TasksScreenState.TaskScreenDialogState.None -> Unit
            is TasksScreenState.TaskScreenDialogState.Opened -> {
                val task = state.dialogState.task
                TaskDialogWindow(
                    task = task,
                    onDismissRequest = { onAction(TasksScreenAction.TaskDialogAction.Close) },
                    onCompleteTask = { onAction(TasksScreenAction.ToggleTaskStatus(it)) },
                    onOpenTask = { onNavigateToTaskOpenScreen(task.id) },
                    onEditTask = { onNavigateToTaskEditScreen(task.id) },
                    onDeleteTask = { onAction(TasksScreenAction.DeleteTask(task)) }
                )
            }
        }
    }
}