package com.example.androidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Day
import com.example.androidweeklyplanner.domain.model.DayType
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Composable
fun DayCard(
    day: Day,
    needAnimation: Boolean,
    onTaskItemClick: (Task) -> Unit,
    onStopEncouragingAnimation: (LocalDate) -> Unit,
    dayItemModifier: Modifier = Modifier,
    taskItemModifier: Modifier = Modifier
) = Column {
    var isExpanded: Boolean by remember {
        mutableStateOf(false)
    }
    val hasTasks = day.tasks.isNotEmpty()

    DayItem(
        day = day,
        isExpanded = isExpanded,
        enabled = hasTasks,
        needAnimation = needAnimation,
        onStopEncouragingAnimation = { date ->
            onStopEncouragingAnimation(date)
        },
        onClick = { if (hasTasks) isExpanded = isExpanded.not() },
        modifier = dayItemModifier
    )
    AnimatedVisibility(isExpanded) {
        Column {
            for (task in day.tasks) {
                TaskItem(
                    task = task,
                    onClick = { onTaskItemClick(task) },
                    modifier = taskItemModifier
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DayCardPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        val list: List<Task> = listOf(
            Task(
                id = UUID.randomUUID(),
                name = "Сделать Джаву",
                description = "Сдать до 25 числа",
                date = LocalDate.of(2024, 10, 25),
                priority = Priority.HIGH,
                difficulty = Difficulty.MEDIUM,
                category = Category.WORK,
                time = LocalTime.of(17,33),
                notificationTimeOffset = null,
                isDone = false
            ),

            Task(
                id = UUID.randomUUID(),
                name = "Сделать Джаву",
                description = "Сдать до 25 числа",
                date = LocalDate.of(2024, 10, 25),
                priority = Priority.HIGH,
                difficulty = Difficulty.MEDIUM,
                category = Category.WORK,
                time = LocalTime.of(17,33),
                notificationTimeOffset = null,
                isDone = false
            )
        )
        val day = Day(
            id = 0,
            type = DayType.MONDAY,
            date = LocalDate.of(2024, 10, 14),
            tasks = list
        )
        DayCard(
            day = day,
            needAnimation = false,
            onTaskItemClick = { },
            onStopEncouragingAnimation = { },
            dayItemModifier = Modifier,
            taskItemModifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
        )

    }
}