package com.example.androidweeklyplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme

@Composable
internal fun TaskScreenNotificationTimeInputField(
    notificationTime: String?,
    editState: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(
            onClick = onClick,
            enabled = editState
        ),
    verticalAlignment = Alignment.CenterVertically,
) {
    Column(
        modifier = modifier.weight(3f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.description_notification_time),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = notificationTime ?: stringResource(R.string.description_not_defined),
            style = MaterialTheme.typography.labelSmall
        )
    }
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.baseline_notifications_24),
        contentDescription = null
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenNotificationInputFieldPreview() {
    var isChecked by remember { mutableStateOf(false) }
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenNotificationTimeInputField(
            notificationTime = null,
            editState = false,
            onClick = { isChecked = isChecked.not() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}