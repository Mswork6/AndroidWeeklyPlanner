package com.example.courseworkandroidweeklyplanner.presentation.screens.task

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.presentation.screens.shared.DatePickerModal
import com.example.courseworkandroidweeklyplanner.presentation.screens.shared.ErrorScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.DialWithDialogExample
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.PriorityDialogWindow
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.TaskScreenDateInputField
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.TaskScreenInputField
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.TaskScreenNotificationInputField
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.TaskScreenPriorityInputField
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.component.TaskScreenTopBar
import com.example.courseworkandroidweeklyplanner.presentation.PastOrPresentSelectableDates
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskScreen(
    viewModel: TaskScreenViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier
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
    modifier: Modifier
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
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TaskScreenTopBar(
                actionName = when (state) {
                    is TaskScreenState.Add -> stringResource(id = R.string.action_add_task)
                    is TaskScreenState.Edit -> stringResource(id = R.string.action_edit_task)
                    is TaskScreenState.View -> null
                },
                navigateBackAction = onNavigateBack,
                confirmAction = { onAction(TaskScreenAction.Save) }
            )
        }
    ) { padding: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp, alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            TaskScreenInputField(
                nameText = state.name,
                taskNameError = state.errorMessage?.let { stringResource(id = it) },
                descriptionText = state.description,
                editState = state.editable,
                onTaskTitleValueChange = { onAction(TaskScreenAction.SetName(it)) },
                onTaskDescriptionValueChange = { onAction(TaskScreenAction.SetDescription(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            TaskScreenPriorityInputField(
                editState = state.editable,
                priority = state.priority,
                onClick = { onAction(TaskScreenAction.SetPriorityPickerVisibility(true)) },
                modifier = Modifier.fillMaxWidth()
            )
            TaskAddScreenDivider()
            TaskScreenDateInputField(
                selectedDate = state.date,
                editState = state.editable,
                onClick = { onAction(TaskScreenAction.SetDatePickerVisibility(true)) },
                modifier = Modifier.fillMaxWidth()
            )
            TaskAddScreenDivider()
            TaskScreenNotificationInputField(
                selectedTime = state.time,
                isChecked = state.time != null,
                editState = state.editable,
                onClick = {
                    if (state.time == null) {
                        onAction(TaskScreenAction.SetTimePickerVisibility(true))
                    } else {
                        onAction(TaskScreenAction.SetTime(null, null))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.isDatePickerOpened) {
            DatePickerModal(
                selectableDates = PastOrPresentSelectableDates,
                onDateSelected = { dateInMillis ->
                    if (dateInMillis != null) {
                        onAction(TaskScreenAction.SetDate(dateInMillis))
                    }
                    onAction(TaskScreenAction.SetDatePickerVisibility(false))
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
    }
}

@Composable
private fun TaskAddScreenDivider(
    modifier: Modifier = Modifier
) = HorizontalDivider(
    modifier = modifier
        .fillMaxWidth()
        .background(color = Color.Gray)
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskScreenContent1Preview() {
    val state = TaskScreenState.View(
        name = "Name",
        description = "description",
        date = LocalDate.now(),
        priority = Priority.HIGH,
        time = LocalTime.now(),
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
        time = LocalTime.now(),
        isDatePickerOpened = false,
        isPriorityPickerOpened = false,
        isTimePickerOpened = false,
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
        time = LocalTime.now(),
        isDatePickerOpened = false,
        isPriorityPickerOpened = false,
        isTimePickerOpened = false,
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