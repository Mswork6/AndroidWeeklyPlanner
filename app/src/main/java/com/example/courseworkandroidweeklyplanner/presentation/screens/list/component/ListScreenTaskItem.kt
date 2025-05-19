package com.example.courseworkandroidweeklyplanner.presentation.screens.list.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Category
import com.example.courseworkandroidweeklyplanner.domain.model.Difficulty
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.presentation.color
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.courseworkandroidweeklyplanner.presentation.dateTimeToString
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.component.ItemCard
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Composable
fun ListScreenTaskItem(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = ItemCard(
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
    ),
    onClick = onClick,
    modifier = modifier,
) {

    val stripeColor = task.difficulty.color
    val stripeWidth = 12.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    )
    {
        // Used to write category color stripe
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(stripeWidth)
                .align(Alignment.CenterEnd)
                .background(colorResource(stripeColor))
        )
        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    bottom = 12.dp,
                    start = 8.dp,
                    end = stripeWidth + 4.dp
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (task.isDone)
                        ImageVector.vectorResource(R.drawable.icon_checkbox_done)
                    else
                        ImageVector.vectorResource(R.drawable.baseline_check_box_outline_blank_24),
                    contentDescription = null,
                    tint = if (task.isDone) Color.Unspecified else when (task.priority) {
                        Priority.LOW -> colorResource(R.color.gray)
                        Priority.BASIC -> colorResource(R.color.black)
                        Priority.HIGH -> colorResource(R.color.red)
                    }
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = task.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_access_time_16),
                    contentDescription = null
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = dateTimeToString(task.date, task.time),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (task.notificationTimeOffset != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_notifications_16),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(1.dp))
                    Text(
                        text = dateTimeToString(
                            task.date,
                            task.time,
                            task.notificationTimeOffset
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (task.category != Category.NONE) {
                        Text(
                            text = task.category.emoji,
                            modifier = Modifier
                                .width(24.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.width(2.dp))
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskCardWithIconPreview1() {
    CourseWorkAndroidWeeklyPlannerTheme {
        ListScreenTaskItem(
            onClick = { },
            task = Task(
                id = UUID.randomUUID(),
                name = "Отвезти бананы в грузию",
                description = "Нужно сесть в грузовик и привезти бананы",
                priority = Priority.HIGH,
                difficulty = Difficulty.EASY,
                category = Category.VACATION,
                date = LocalDate.of(2024, 10, 13),
                time = LocalTime.of(17, 33),
                notificationTimeOffset = -1000,
                isDone = true
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        )
    }
}