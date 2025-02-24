package com.example.courseworkandroidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Difficulty
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = ItemCard(
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
    ),
    onClick = onClick,
    modifier = modifier

) {
    Icon(
        imageVector = when (task.isDone) {
            true -> ImageVector.vectorResource(R.drawable.icon_checkbox_done)
            false -> ImageVector.vectorResource(R.drawable.baseline_check_box_outline_blank_24)
        },
        contentDescription = stringResource(R.string.description_task_check_box),
        tint = if (task.isDone) {
            Color.Unspecified
        } else {
            when (task.priority) {
                Priority.LOW -> colorResource(R.color.gray)
                Priority.BASIC -> colorResource(R.color.black)
                Priority.HIGH -> colorResource(R.color.red)
            }
        },

        )
    Spacer(modifier = Modifier.width(16.dp))
    Text(
        text = task.name,
        modifier = Modifier
            .weight(5f)
    )
    if (task.time != null) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = stringResource(R.string.description_notification),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskCardWithIconPreview1() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskItem(
            onClick = { },
            task = Task(
                id = UUID.randomUUID(),
                name = "Отвезти бананы в грузию",
                description = "Нужно сесть в грузовик и привезти бананы",
                priority = Priority.HIGH,
                difficulty = Difficulty.MEDIUM,
                date = LocalDate.of(2024, 10, 13),
                time = null,
                isDone = true
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskCardWithIconPreview2() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskItem(
            onClick = { },
            task = Task(
                id = UUID.randomUUID(),
                name = "Отвезти бананы в грузию",
                description = "Нужно сесть в грузовик и привезти бананы",
                priority = Priority.HIGH,
                difficulty = Difficulty.MEDIUM,
                date = LocalDate.of(2024, 10, 13),
                time = LocalTime.of(23, 30),
                isDone = true
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

