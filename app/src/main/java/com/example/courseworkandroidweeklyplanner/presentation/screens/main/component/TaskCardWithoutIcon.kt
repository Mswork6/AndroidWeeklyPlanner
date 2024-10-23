package com.example.courseworkandroidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate
import java.util.UUID

@Composable
fun TaskCardWithoutIcon(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = Card(
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary
    ),
    elevation = CardDefaults.cardElevation(2.dp),
    modifier = modifier.clickable(onClick = onClick)
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.icon_checkbox_unchecked),
                contentDescription = stringResource(R.string.description_task_check_box)
            )
            Text(task.name)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskCardWithoutIconPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskCardWithoutIcon(
            onClick = { },
            task = Task(
                id = UUID.randomUUID(),
                name = "Отвезти бананы в грузию",
                description = "Нужно сесть в грузовик и привезти бананы",
                priority = Priority.BASIC,
                date = LocalDate.of(2024, 10, 13),
                time = null,
                isDone = false
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}