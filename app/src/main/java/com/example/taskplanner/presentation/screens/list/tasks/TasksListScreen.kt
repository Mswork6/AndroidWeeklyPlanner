package com.example.taskplanner.presentation.screens.list.tasks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskplanner.R
import com.example.taskplanner.domain.model.Task
import com.example.taskplanner.presentation.screens.list.NoTasksScreen
import com.example.taskplanner.presentation.screens.list.component.ListScreenTaskItem
import com.example.taskplanner.presentation.screens.main.tasks.component.TaskDialogWindow
import java.util.UUID

@Composable
fun TasksListScreen(
    onNavigateToTaskEditScreen: (UUID) -> Unit,
    onNavigateToTaskOpenScreen: (UUID) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksListScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    TasksListScreenContent(
        state = state,
        onAction = viewModel::handle,
        onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
        onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
        modifier = modifier
    )
}

@Composable
private fun TasksListScreenContent(
    state: TasksListScreenState,
    onAction: (TasksListScreenAction) -> Unit,
    onNavigateToTaskEditScreen: (UUID) -> Unit,
    onNavigateToTaskOpenScreen: (UUID) -> Unit,
    modifier: Modifier
) = when (state) {
    is TasksListScreenState.Initial -> {
        CircularProgressIndicator()
    }
    is TasksListScreenState.Default -> {
        when (state.tasks.isEmpty()) {
            true -> NoTasksScreen(
                modifier = Modifier.fillMaxSize(),
                title = stringResource(R.string.description_no_tasks)
            )

            false -> {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(
                        items = state.tasks,
                        key = Task::id
                    ) { task ->
                        ListScreenTaskItem(
                            task = task,
                            onClick = { onAction(TasksListScreenAction.TaskDialogAction.Open(task)) },
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }

                when (val dialog = state.dialogState) {
                    is TasksListScreenState.TaskDialogState.None -> Unit
                    is TasksListScreenState.TaskDialogState.Opened -> {
                        val task = dialog.task
                        TaskDialogWindow(
                            task = task,
                            onDismissRequest = { onAction(TasksListScreenAction.TaskDialogAction.Close) },
                            onCompleteTask = { onAction(TasksListScreenAction.ToggleTaskStatus(it)) },
                            onOpenTask = { onNavigateToTaskOpenScreen(task.id) },
                            onEditTask = { onNavigateToTaskEditScreen(task.id) },
                            onDeleteTask = { onAction(TasksListScreenAction.DeleteTask(task)) }
                        )
                    }
                }
            }

        }
    }
}