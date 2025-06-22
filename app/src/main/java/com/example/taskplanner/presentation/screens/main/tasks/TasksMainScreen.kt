package com.example.taskplanner.presentation.screens.main.tasks

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
import com.example.taskplanner.domain.model.Day
import com.example.taskplanner.presentation.screens.main.component.DayCard
import com.example.taskplanner.presentation.screens.main.tasks.component.TaskDialogWindow
import java.util.UUID

@Composable
fun TasksMainScreen(
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksMainScreenViewModel = hiltViewModel(),
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
    state: TasksMainScreenState,
    onAction: (TasksMainScreenAction) -> Unit,
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    modifier: Modifier,
) = when (state) {
    is TasksMainScreenState.Initial -> {
        CircularProgressIndicator()
    }

    is TasksMainScreenState.Default -> {
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(items = state.days, key = Day::date) { day ->
                val needAnimation = state.playingDates.contains(day.date)

                DayCard(
                    day = day,
                    needAnimation = needAnimation,
                    onTaskItemClick = { onAction(TasksMainScreenAction.TaskDialogAction.Open(it)) },
                    onStopEncouragingAnimation = { date ->
                        onAction(
                            TasksMainScreenAction.StopEncouragingAnimation(date))
                    },
                    dayItemModifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    taskItemModifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        when (state.dialogState) {
            is TasksMainScreenState.TaskScreenDialogState.None -> Unit
            is TasksMainScreenState.TaskScreenDialogState.Opened -> {
                val task = state.dialogState.task
                TaskDialogWindow(
                    task = task,
                    onDismissRequest = { onAction(TasksMainScreenAction.TaskDialogAction.Close) },
                    onCompleteTask = { onAction(TasksMainScreenAction.ToggleTaskStatus(it)) },
                    onOpenTask = { onNavigateToTaskOpenScreen(task.id) },
                    onEditTask = { onNavigateToTaskEditScreen(task.id) },
                    onDeleteTask = { onAction(TasksMainScreenAction.DeleteTask(task)) }
                )
            }
        }
    }
}