package com.example.androidweeklyplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.presentation.color
import com.example.androidweeklyplanner.presentation.description
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme

@Composable
internal fun TaskScreenPriorityInputField(
    editState: Boolean,
    priority: Priority,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier.clickable(
        onClick = onClick,
        enabled = editState
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.description_priority),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(priority.description),
            color = priority.color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenPriorityInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenPriorityInputField(
            editState = true,
            priority = Priority.HIGH,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}