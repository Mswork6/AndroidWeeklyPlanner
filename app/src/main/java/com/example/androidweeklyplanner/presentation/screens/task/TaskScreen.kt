package com.example.androidweeklyplanner.presentation.screens.task

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.NotificationTime
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.presentation.PastOrPresentSelectableDates
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.notificationTimeString
import com.example.androidweeklyplanner.presentation.screens.shared.DatePickerModal
import com.example.androidweeklyplanner.presentation.screens.shared.ErrorScreen
import com.example.androidweeklyplanner.presentation.screens.shared.ScreenHorizontalDivider
import com.example.androidweeklyplanner.presentation.screens.shared.TopBar
import com.example.androidweeklyplanner.presentation.screens.task.component.CategoryDialogWindow
import com.example.androidweeklyplanner.presentation.screens.task.component.DialWithDialogExample
import com.example.androidweeklyplanner.presentation.screens.task.component.DifficultyDialogWindow
import com.example.androidweeklyplanner.presentation.screens.task.component.NotificationTimeDialogWindow
import com.example.androidweeklyplanner.presentation.screens.task.component.PriorityDialogWindow
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskLimitWindow
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenCategoryInputField
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenDateInputField
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenDifficultyInputField
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenInputField
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenNotificationTimeInputField
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenPriorityInputField
import com.example.androidweeklyplanner.presentation.screens.task.component.TaskScreenTimeInputField
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskScreen(
    viewModel: TaskScreenViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsState()
    TaskScreenContent(
        state = state,
        onAction = viewModel::handle,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
private fun TaskScreenContent(
    state: TaskScreenState,
    onAction: (TaskScreenAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
) = when (state) {
    is TaskScreenState.Initial -> CircularProgressIndicator(modifier = modifier)
    is TaskScreenState.Content -> TaskScreenBaseContent(
        state = state,
        onAction = onAction,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )

    is TaskScreenState.Error -> {
        ErrorScreen(
            title = "Something went wrong",
            modifier = Modifier.fillMaxSize()
        )
        LaunchedEffect(key1 = "navigate_back_job") {
            delay(2000)
            onNavigateBack()
        }
    }

    TaskScreenState.Success -> onNavigateBack()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenBaseContent(
    state: TaskScreenState.Content,
    onAction: (TaskScreenAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                actionName = when (state) {
                    is TaskScreenState.Add -> stringResource(id = R.string.action_add_task)
                    is TaskScreenState.Edit -> stringResource(id = R.string.action_edit_task)
                    is TaskScreenState.View -> null
                },
                navigateBackAction = onNavigateBack,
                confirmAction = { onAction(TaskScreenAction.ValidateAndReact) }
            )
        }
    ) { padding: PaddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp, alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                TaskScreenInputField(
                    nameText = state.name,
                    taskNameError = state.errorMessage?.let { stringResource(id = it) },
                    descriptionText = state.description,
                    editState = state.editable,
                    onTaskTitleValueChange = { onAction(TaskScreenAction.SetName(it)) },
                    onTaskDescriptionValueChange = { onAction(TaskScreenAction.SetDescription(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TaskScreenPriorityInputField(
                    editState = state.editable,
                    priority = state.priority,
                    onClick = { onAction(TaskScreenAction.SetPriorityPickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                TaskScreenDifficultyInputField(
                    editState = state.editable,
                    difficulty = state.difficulty,
                    onClick = { onAction(TaskScreenAction.SetDifficultyPickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                TaskScreenCategoryInputField(
                    editState = state.editable,
                    category = state.category,
                    onClick = { onAction(TaskScreenAction.SetCategoryPickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                TaskScreenDateInputField(
                    selectedDate = state.date,
                    editState = state.editable,
                    onClick = { onAction(TaskScreenAction.SetDatePickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                TaskScreenTimeInputField(
                    selectedTime = state.time,
                    editState = state.editable,
                    onClick = { onAction(TaskScreenAction.SetTimePickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                TaskScreenNotificationTimeInputField(
                    notificationTime =
                    if (state.notificationTimeOffset != null)
                        notificationTimeString(state.date, state.time, state.notificationTimeOffset)
                    else null,
                    editState = state.editable,
                    onClick = { onAction(TaskScreenAction.SetNotificationTimePickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (state.isDatePickerOpened) {
            DatePickerModal(
                selectableDates = PastOrPresentSelectableDates,
                onDateSelected = { dateInMillis ->
                    if (dateInMillis != null) {
                        onAction(TaskScreenAction.SetDate(dateInMillis))
                    }
                },
                onDismiss = {
                    onAction(TaskScreenAction.SetDatePickerVisibility(false))
                }
            )
        }

        if (state.isTimePickerOpened) {
            DialWithDialogExample(
                onDismiss = { onAction(TaskScreenAction.SetTimePickerVisibility(false)) },
                onConfirm = { timePickerState ->
                    onAction(TaskScreenAction.SetTime(timePickerState.hour, timePickerState.minute))
                    onAction(TaskScreenAction.SetTimePickerVisibility(false))
                }
            )
        }

        if (state.isNotificationTimePickerOpened) {
            NotificationTimeDialogWindow(
                selectedOption = state.notificationTimeOffsetEnum,
                onOptionSelected = { option ->
                    onAction(TaskScreenAction.SetNotificationTime(option))
                },
                onDismissRequest = {
                    onAction(TaskScreenAction.SetNotificationTimePickerVisibility(false))
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }

        if (state.isPriorityPickerOpened) {
            PriorityDialogWindow(
                selectedOption = state.priority,
                onOptionSelected = { option ->
                    onAction(TaskScreenAction.SetPriority(option))
                },
                onDismissRequest = {
                    onAction(TaskScreenAction.SetPriorityPickerVisibility(false))
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }

        if (state.isDifficultyPickerOpened) {
            DifficultyDialogWindow(
                selectedOption = state.difficulty,
                onOptionSelected = { option ->
                    onAction(TaskScreenAction.SetDifficulty(option))
                },
                onDismissRequest = {
                    onAction(TaskScreenAction.SetDifficultyPickerVisibility(false))
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }

        if (state.isCategoryPickerOpened) {
            CategoryDialogWindow(
                selectedOption = state.category,
                onOptionSelected = { option ->
                    onAction(TaskScreenAction.SetCategory(option))
                },
                onDismissRequest = {
                    onAction(TaskScreenAction.SetCategoryPickerVisibility(false))
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }

        if (state.isTaskLimitWindowOpened) {
            TaskLimitWindow(
                onOptionSelected = {
                    onAction(TaskScreenAction.Save)
                },
                onDismissRequest = {
                    onAction(TaskScreenAction.SetTaskLimitWindowVisibility(false))
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskScreenContent1Preview() {
    val state = TaskScreenState.View(
        name = "Name",
        description = "description",
        date = LocalDate.now(),
        priority = Priority.HIGH,
        difficulty = Difficulty.HARD,
        category = Category.WORK,
        time = LocalTime.now(),
        notificationTimeOffset = null
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenContent(
            state = state,
            onAction = {},
            onNavigateBack = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskScreenContent2Preview() {
    val state = TaskScreenState.Add(
        name = "Name",
        description = "description",
        date = LocalDate.now(),
        priority = Priority.HIGH,
        difficulty = Difficulty.HARD,
        category = Category.WORK,
        time = LocalTime.now(),
        notificationTimeOffsetEnum = NotificationTime.NONE,
        notificationTimeOffset = null,
        isDatePickerOpened = false,
        isPriorityPickerOpened = false,
        isDifficultyPickerOpened = false,
        isCategoryPickerOpened = false,
        isTimePickerOpened = false,
        isNotificationTimePickerOpened = false,
        isTaskLimitWindowOpened = false,
        errorMessage = null
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenContent(
            state = state,
            onAction = {},
            onNavigateBack = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskScreenContent3Preview() {
    val state = TaskScreenState.Edit(
        name = "Name",
        description = "description",
        date = LocalDate.now(),
        priority = Priority.HIGH,
        difficulty = Difficulty.HARD,
        category = Category.WORK,
        time = LocalTime.now(),
        notificationTimeOffsetEnum = NotificationTime.NONE,
        notificationTimeOffset = null,
        isDatePickerOpened = false,
        isPriorityPickerOpened = false,
        isDifficultyPickerOpened = false,
        isCategoryPickerOpened = false,
        isTimePickerOpened = false,
        isNotificationTimePickerOpened = false,
        isTaskLimitWindowOpened = false,
        errorMessage = null
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenContent(
            state = state,
            onAction = {},
            onNavigateBack = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}