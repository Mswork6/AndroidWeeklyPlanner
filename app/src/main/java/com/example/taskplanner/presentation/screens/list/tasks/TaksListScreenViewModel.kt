package com.example.taskplanner.presentation.screens.list.tasks

import androidx.lifecycle.viewModelScope
import com.example.taskplanner.domain.interactor.notification.NotificationEventBus
import com.example.taskplanner.domain.interactor.saver.TaskInteractor
import com.example.taskplanner.domain.model.Task
import com.example.taskplanner.domain.usecase.GetTasksUseCase
import com.example.taskplanner.domain.usecase.ToggleTaskStatusUseCase
import com.example.taskplanner.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksListScreenViewModel @Inject constructor(
    private val getAllTasks: GetTasksUseCase,
    private val toggleTaskStatus: ToggleTaskStatusUseCase,
    private val taskInteractor: TaskInteractor,
    private val notificationEventBus: NotificationEventBus
) : BaseViewModel<TasksListScreenState, TasksListScreenAction>() {

    private val _state = MutableStateFlow<TasksListScreenState>(TasksListScreenState.Initial)
    override val state: StateFlow<TasksListScreenState> = _state.asStateFlow()

    private val actionDialogTask = MutableStateFlow<Task?>(null)

    init {
        viewModelScope.launch {
            getAllTasks()
                .combine(actionDialogTask) { tasks, selectedTask ->
                    TasksListScreenState.Default(
                        tasks = tasks,
                        dialogState = when (selectedTask){
                            null -> TasksListScreenState.TaskDialogState.None
                            else -> TasksListScreenState.TaskDialogState.Opened(selectedTask)
                        }
                    )
                }
                .collect { _state.value = it }
        }

        // Подписка на уведомление
        viewModelScope.launch {
            notificationEventBus.events.collect {
                actionDialogTask.value = null
            }
        }
    }

    override suspend fun execute(action: TasksListScreenAction) {
        when (action) {
            is TasksListScreenAction.TaskDialogAction.Open -> actionDialogTask.value = action.task
            is TasksListScreenAction.TaskDialogAction.Close -> actionDialogTask.value = null
            is TasksListScreenAction.ToggleTaskStatus -> toggleTaskStatus(action.task)
            is TasksListScreenAction.DeleteTask -> taskInteractor.deleteTask(action.task)
        }
    }
}