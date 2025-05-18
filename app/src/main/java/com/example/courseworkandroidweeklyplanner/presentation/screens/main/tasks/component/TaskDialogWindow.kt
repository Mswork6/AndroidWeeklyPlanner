package com.example.courseworkandroidweeklyplanner.presentation.screens.main.tasks.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Category
import com.example.courseworkandroidweeklyplanner.domain.model.Difficulty
import com.example.courseworkandroidweeklyplanner.domain.model.NotificationTime
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Composable
fun TaskDialogWindow(
    task: Task,
    onDismissRequest: () -> Unit,
    onCompleteTask: (task: Task) -> Unit,
    onOpenTask: () -> Unit,
    onEditTask: () -> Unit,
    onDeleteTask: (id: UUID) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                TextButton(
                    onClick = {
                        onCompleteTask(task)
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = when (task.isDone) {
                            false -> stringResource(R.string.action_complete_task)
                            true -> stringResource(R.string.action_cancel_completion_of_task)
                        },
                        modifier = Modifier
                            .weight(3f)
                    )
                    Icon(
                        imageVector = when (task.isDone) {
                            false -> ImageVector.vectorResource(R.drawable.icon_taskdialogwindow_complete)
                            true -> ImageVector.vectorResource(R.drawable.icon_taskdialogwindow_undo)
                        },
                        contentDescription = stringResource(R.string.action_complete_task),
                        modifier = Modifier
                            .weight(1f),
                    )
                }

                TextButton(
                    onClick = {
                        onOpenTask()
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = stringResource(R.string.action_open_task),
                        modifier = Modifier
                            .weight(3f)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_taskdialogwindow_open),
                        contentDescription = stringResource(R.string.action_open_task),
                        modifier = Modifier
                            .weight(1f),
                    )

                }

                TextButton(
                    onClick = {
                        onEditTask()
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = stringResource(R.string.action_edit_task),
                        modifier = Modifier
                            .weight(3f)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_taskdialogwindow_edit),
                        contentDescription = stringResource(R.string.action_complete_task),
                        modifier = Modifier
                            .weight(1f),
                    )

                }

                TextButton(
                    onClick = {
                        onDeleteTask(task.id)
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = stringResource(R.string.action_delete_task),
                        modifier = Modifier
                            .weight(3f)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_taskdialogwindow_delete),
                        contentDescription = stringResource(R.string.action_delete_task),
                        modifier = Modifier
                            .weight(1f),
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DayCardPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        val task = Task(
            id = UUID.randomUUID(),
            name = "Сходить в зал",
            description = "",
            date = LocalDate.of(2024, 10, 22),
            priority = Priority.LOW,
            difficulty = Difficulty.EASY,
            category = Category.WORK,
            time = LocalTime.of(17,33),
            notificationTimeOffset = null,
            isDone = false
        )

        TaskDialogWindow(
            task = task,
            onDismissRequest = { },
            onCompleteTask = { },
            onOpenTask = { },
            onEditTask = { },
            onDeleteTask = { },
            modifier = Modifier
                .width(250.dp),
        )
    }
}

