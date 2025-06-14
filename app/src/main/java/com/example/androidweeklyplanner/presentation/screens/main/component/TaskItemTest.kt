package com.example.androidweeklyplanner.presentation.screens.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.presentation.timeToString

@Composable
fun TaskItemTest(
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
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
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

            Text(
                text = task.name,
                modifier = Modifier
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
//    if (task.time != null) {
//        Icon(
//            imageVector = Icons.Default.Notifications,
//            contentDescription = stringResource(R.string.description_time),
//            modifier = Modifier.weight(0.5f)
//        )
//    } OUTDATED (May use later)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(
                text = task.category.emoji,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_access_time_16),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = timeToString(task.time),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
            //Spacer(modifier = Modifier.width(8.dp))

        }
    }
}