package com.example.courseworkandroidweeklyplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme

internal data class TaskAddInputFieldState(
    val taskTitle: String,
    val taskDescription: String
)

@Composable
internal fun TaskScreenInputField(
    nameText: String,
    taskNameError: String?,
    descriptionText: String?,
    editState: Boolean,
    onTaskTitleValueChange: (String) -> Unit,
    onTaskDescriptionValueChange: (String) -> Unit,

    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    BasicTextField(
        value = nameText,
        readOnly = !editState,
        onValueChange = onTaskTitleValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            },
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.padding(8.dp)) {
                if (nameText.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.description_task_title),
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        }
    )

    if (taskNameError != null) {
        Text(
            text = taskNameError,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp)
        )
    }

    BasicTextField(
        value = descriptionText ?: "",
        readOnly = !editState,
        onValueChange = onTaskDescriptionValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 100.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.padding(8.dp)) {
                if (descriptionText?.isEmpty() != false) {
                    Text(
                        text = stringResource(id = R.string.description_task_description),
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenInputFieldPreview() {
    var state by remember {
        mutableStateOf(
            TaskAddInputFieldState(
                taskTitle = "",
                taskDescription = ""
            )
        )
    }
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenInputField(
            nameText = "",
            taskNameError = null,
            descriptionText = "",
            editState = false,
            onTaskTitleValueChange = { state = state.copy(taskTitle = it) },
            onTaskDescriptionValueChange = { state = state.copy(taskDescription = it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}