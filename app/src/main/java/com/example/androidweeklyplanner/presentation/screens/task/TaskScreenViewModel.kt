package com.example.androidweeklyplanner.presentation.screens.task

import androidx.lifecycle.viewModelScope
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationEventBus
import com.example.androidweeklyplanner.domain.interactor.builder.NameReport
import com.example.androidweeklyplanner.domain.interactor.builder.TaskBuilderInteractor
import com.example.androidweeklyplanner.domain.interactor.builder.TaskBuilderReport
import com.example.androidweeklyplanner.domain.interactor.builder.TaskBuilderState
import com.example.androidweeklyplanner.domain.interactor.builder.TaskLimitReport
import com.example.androidweeklyplanner.domain.model.NotificationTime
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.presentation.core.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel(assistedFactory = TaskScreenViewModel.Factory::class)
class TaskScreenViewModel @AssistedInject constructor(
    @Assisted private val mode: Mode,
    interactorFactory: TaskBuilderInteractor.Factory,
    private val notificationEventBus: NotificationEventBus,
) : BaseViewModel<TaskScreenState, TaskScreenAction>() {
    private val _state: MutableStateFlow<TaskScreenState> =
        MutableStateFlow(TaskScreenState.Initial)
    override val state: StateFlow<TaskScreenState> = _state.asStateFlow()

    private val taskBuilderInteractor: TaskBuilderInteractor = interactorFactory.create(
        taskId = mode.taskId,
        scope = viewModelScope
    )

    init {
        viewModelScope.launch {
            taskBuilderInteractor.state.collect { taskState ->
                when (taskState) {
                    is TaskBuilderState.Initial -> Unit
                    is TaskBuilderState.Invalid -> _state.emit(TaskScreenState.Error)
                    is TaskBuilderState.Default -> _state.update { screenState ->
                        when (mode) {
                            is Mode.Add -> when (screenState) {
                                is TaskScreenState.Add -> merge(screenState, taskState.task)
                                is TaskScreenState.Initial,
                                is TaskScreenState.Edit,
                                is TaskScreenState.View,
                                -> initAddState(taskState.task)

                                is TaskScreenState.Error,
                                is TaskScreenState.Success,
                                -> screenState
                            }

                            is Mode.Edit -> when (screenState) {
                                is TaskScreenState.Edit -> merge(screenState, taskState.task)
                                is TaskScreenState.Initial,
                                is TaskScreenState.Add,
                                is TaskScreenState.View,
                                -> initEditState(taskState.task)

                                is TaskScreenState.Error,
                                is TaskScreenState.Success,
                                -> screenState
                            }

                            is Mode.View -> when (screenState) {
                                is TaskScreenState.View -> merge(screenState, taskState.task)
                                is TaskScreenState.Initial,
                                is TaskScreenState.Edit,
                                is TaskScreenState.Add,
                                -> initViewState(taskState.task)

                                is TaskScreenState.Error,
                                is TaskScreenState.Success,
                                -> screenState
                            }
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            notificationEventBus.events.collect {
                _state.update { screenState ->
                    when (screenState) {
                        is TaskScreenState.Add -> screenState.copy(
                            isPriorityPickerOpened = false,
                            isDifficultyPickerOpened = false,
                            isCategoryPickerOpened = false,
                            isDatePickerOpened = false,
                            isTimePickerOpened = false,
                            isNotificationTimePickerOpened = false,
                            isTaskLimitWindowOpened = false
                        )

                        is TaskScreenState.Edit -> screenState.copy(
                            isPriorityPickerOpened = false,
                            isDifficultyPickerOpened = false,
                            isCategoryPickerOpened = false,
                            isDatePickerOpened = false,
                            isTimePickerOpened = false,
                            isNotificationTimePickerOpened = false,
                            isTaskLimitWindowOpened = false
                        )

                        else -> screenState
                    }
                }
            }
        }
    }

    override suspend fun execute(action: TaskScreenAction) = when (action) {
        is TaskScreenAction.SetDate -> taskBuilderInteractor.setDate(action.dateInMillis)
        is TaskScreenAction.SetDatePickerVisibility -> setDeadlinePickerVisibility(action.opened)
        is TaskScreenAction.SetDescription -> taskBuilderInteractor.setDescription(action.description)
        is TaskScreenAction.SetName -> taskBuilderInteractor.setName(action.name)
        is TaskScreenAction.SetTime -> taskBuilderInteractor.setTime(action.hour, action.minute)
        is TaskScreenAction.SetTimePickerVisibility -> setTimePickerVisibility(action.opened)
        is TaskScreenAction.SetNotificationTime -> { setNotificationTime(action.notificationTime) }
        is TaskScreenAction.SetNotificationTimePickerVisibility -> setNotificationTimePickerVisibility(
            action.opened
        )

        is TaskScreenAction.SetPriority -> taskBuilderInteractor.setPriority(action.priority)
        is TaskScreenAction.SetPriorityPickerVisibility -> setPriorityPickerVisibility(action.opened)
        is TaskScreenAction.SetDifficulty -> taskBuilderInteractor.setDifficulty(action.difficulty)
        is TaskScreenAction.SetDifficultyPickerVisibility -> setDifficultyPickerVisibility(action.opened)
        is TaskScreenAction.SetCategory -> taskBuilderInteractor.setCategory(action.category)
        is TaskScreenAction.SetCategoryPickerVisibility -> setCategoryPickerVisibility(action.opened)
        is TaskScreenAction.SetTaskLimitWindowVisibility -> setTaskLimitScreenVisibility(action.opened)
        is TaskScreenAction.ValidateAndReact -> validateAndReact()
        is TaskScreenAction.Save -> save()
    }

    private suspend fun validateAndReact() {
        val report: TaskBuilderReport = taskBuilderInteractor.validate()
        val errorMessage: Int? = when (report) {
            is TaskBuilderReport.Default -> when (report.nameReport) {
                NameReport.Empty -> R.string.error_empty_title
                NameReport.TooLong -> R.string.error_long_title
                NameReport.UselessWhitespaces -> R.string.error_whitespaces
                NameReport.Valid -> null
            }

            TaskBuilderReport.NotInitialized -> null
        }
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(errorMessage = errorMessage)
                is TaskScreenState.Edit -> it.copy(errorMessage = errorMessage)
                is TaskScreenState.Initial,
                is TaskScreenState.Error,
                is TaskScreenState.Success,
                is TaskScreenState.View,
                -> it
            }
        }

        if (report is TaskBuilderReport.Default
            && report.nameReport is NameReport.Valid
            && report.taskLimitReport is TaskLimitReport.Exceeded
        ) {
            setTaskLimitScreenVisibility(true)
        }

        if (report is TaskBuilderReport.Default
            && report.nameReport is NameReport.Valid
            && report.taskLimitReport is TaskLimitReport.Valid
        ) {
            save()
        }
    }

    private suspend fun save() {
        taskBuilderInteractor.save()
        _state.emit(TaskScreenState.Success)
    }

    private fun setDeadlinePickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isDatePickerOpened = opened)
                is TaskScreenState.Edit -> it.copy(isDatePickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setTimePickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isTimePickerOpened = opened)
                is TaskScreenState.Edit -> it.copy(isTimePickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setNotificationTimePickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isNotificationTimePickerOpened = opened)
                is TaskScreenState.Edit -> it.copy(isNotificationTimePickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setPriorityPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isPriorityPickerOpened = opened)
                is TaskScreenState.Edit -> it.copy(isPriorityPickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setDifficultyPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isDifficultyPickerOpened = opened)
                is TaskScreenState.Edit -> it.copy(isDifficultyPickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setCategoryPickerVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isCategoryPickerOpened = opened)
                is TaskScreenState.Edit -> it.copy(isCategoryPickerOpened = opened)
                else -> it
            }
        }
    }

    private fun setTaskLimitScreenVisibility(opened: Boolean) {
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(isTaskLimitWindowOpened = opened)
                is TaskScreenState.Edit -> it.copy(isTaskLimitWindowOpened = opened)
                else -> it
            }
        }
    }

    private fun setNotificationTime(notificationTime: NotificationTime) {
        taskBuilderInteractor.setNotificationTime(notificationTime)

        // setting notification time offset
        _state.update {
            when (it) {
                is TaskScreenState.Add -> it.copy(notificationTimeOffsetEnum = notificationTime)
                is TaskScreenState.Edit -> it.copy(notificationTimeOffsetEnum = notificationTime)
                else -> it
            }
        }
    }

//    private fun setNotificationTimeOffset(notificationTime: NotificationTime) {
//        _state.update {
//            when (it) {
//                is TaskScreenState.Add -> it.copy(notificationTimeOffset = notificationTime)
//                is TaskScreenState.Edit -> it.copy(notificationTimeOffset = notificationTime)
//                else -> it
//            }
//        }
//    }

    private fun merge(state: TaskScreenState.View, task: Task): TaskScreenState.View {
        return with(task) {
            state.copy(
                name = name,
                description = description,
                date = date,
                priority = priority,
                difficulty = difficulty,
                category = category,
                time = time,
                notificationTimeOffset = notificationTimeOffset
            )
        }
    }

    private fun merge(state: TaskScreenState.Add, task: Task): TaskScreenState.Add {
        return with(task) {
            state.copy(
                name = name,
                description = description,
                date = date,
                priority = priority,
                difficulty = difficulty,
                category = category,
                time = time,
                notificationTimeOffset = notificationTimeOffset
            )
        }
    }

    private fun merge(state: TaskScreenState.Edit, task: Task): TaskScreenState.Edit {
        return with(task) {
            state.copy(
                name = name,
                description = description,
                date = date,
                priority = priority,
                difficulty = difficulty,
                category = category,
                time = time,
                notificationTimeOffset = notificationTimeOffset
            )
        }
    }

    private fun initAddState(task: Task): TaskScreenState.Add {
        return with(task) {
            TaskScreenState.Add(
                name = name,
                description = description,
                date = date,
                priority = priority,
                difficulty = difficulty,
                category = category,
                time = time,
                notificationTimeOffsetEnum = NotificationTime.NONE,
                notificationTimeOffset = notificationTimeOffset,
                isDatePickerOpened = false,
                isTimePickerOpened = false,
                isNotificationTimePickerOpened = false,
                isPriorityPickerOpened = false,
                isDifficultyPickerOpened = false,
                isCategoryPickerOpened = false,
                isTaskLimitWindowOpened = false,
                errorMessage = null
            )
        }
    }

    private fun initEditState(task: Task): TaskScreenState.Edit {
        return with(task) {
            TaskScreenState.Edit(
                name = name,
                description = description,
                date = date,
                priority = priority,
                difficulty = difficulty,
                category = category,
                time = time,
                notificationTimeOffsetEnum = null,
                notificationTimeOffset = notificationTimeOffset,
                isDatePickerOpened = false,
                isTimePickerOpened = false,
                isNotificationTimePickerOpened = false,
                isPriorityPickerOpened = false,
                isDifficultyPickerOpened = false,
                isCategoryPickerOpened = false,
                isTaskLimitWindowOpened = false,
                errorMessage = null
            )
        }
    }

    private fun initViewState(task: Task): TaskScreenState.View {
        return with(task) {
            TaskScreenState.View(
                name = name,
                description = description,
                date = date,
                priority = priority,
                difficulty = difficulty,
                category = category,
                time = time,
                notificationTimeOffset = notificationTimeOffset
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(mode: Mode): TaskScreenViewModel
    }

    sealed interface Mode {
        val taskId: UUID?
        val editable: Boolean

        data object Add : Mode {
            override val taskId: UUID?
                get() = null
            override val editable: Boolean
                get() = true
        }

        data class Edit(override val taskId: UUID) : Mode {
            override val editable: Boolean
                get() = true
        }

        data class View(override val taskId: UUID) : Mode {
            override val editable: Boolean
                get() = false
        }
    }
}