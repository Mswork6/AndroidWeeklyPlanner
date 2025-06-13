package com.example.androidweeklyplanner.presentation.screens.main.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.presentation.timeToString

@Composable
fun TaskItemTest1(
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
    contentModifier = Modifier
        .padding(
            top = 16.dp,
            bottom = 16.dp,
            start = 8.dp,
            end = 8.dp
        )
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
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = task.category.emoji)
    Spacer(modifier = Modifier.width(4.dp))
    Text(
        text = task.name,
        modifier = Modifier
            .weight(1f),
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = timeToString(task.time),
        style = MaterialTheme.typography.bodyMedium
    )

//    if (task.time != null) {
//        Icon(
//            imageVector = Icons.Default.Notifications,
//            contentDescription = stringResource(R.string.description_time),
//            modifier = Modifier.weight(0.5f)
//        )
//    } OUTDATED (May use later)

    Spacer(modifier = Modifier.width(8.dp))
    Row {
        when (task.difficulty) {
            Difficulty.HARD -> BatteryIcon(color = colorResource(R.color.red))
            Difficulty.MEDIUM -> BatteryIcon(
                bodyCoefficient = 0.5f,
                color = colorResource(R.color.orange)
            )

            Difficulty.EASY -> BatteryIcon(
                bodyCoefficient = 0.25f,
                color = colorResource(R.color.green)
            )
        }
    }

}

