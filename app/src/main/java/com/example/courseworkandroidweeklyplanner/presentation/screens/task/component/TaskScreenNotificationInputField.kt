package com.example.courseworkandroidweeklyplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.presentation.timeToString
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalTime

@Composable
internal fun TaskScreenNotificationInputField(
    selectedTime: LocalTime?,
    isChecked: Boolean,
    editState: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = Modifier.fillMaxWidth().clickable(
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
            text = stringResource(R.string.description_notification),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = if (isChecked && selectedTime != null) {
                timeToString(selectedTime)
            } else {
                stringResource(R.string.description_not_defined)
            },
            style = MaterialTheme.typography.labelSmall
        )
    }
    Switch(
        enabled = editState,
        colors = SwitchDefaults.colors(
            checkedTrackColor = Color.White,
            uncheckedTrackColor = Color.White,
            checkedBorderColor = Color.Black,
            uncheckedBorderColor = Color.Black,
            checkedThumbColor = MaterialTheme.colorScheme.tertiary,
            uncheckedThumbColor = Color.Black,
        ),
        checked = isChecked,
        onCheckedChange = null
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenNotificationInputFieldPreview() {
    var isChecked by remember { mutableStateOf(false) }
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenNotificationInputField(
            selectedTime = null,
            isChecked = isChecked,
            editState = false,
            onClick = { isChecked = isChecked.not() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}