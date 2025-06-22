package com.example.taskplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskplanner.R
import com.example.taskplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme

@Composable
internal fun TaskScreenInputField(
    nameText: String,
    taskNameError: String?,
    editState: Boolean,
    onTaskTitleValueChange: (String) -> Unit,
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
        maxLines = 2,
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
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenInputField(
            nameText = "",
            taskNameError = null,
            editState = false,
            onTaskTitleValueChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}