package com.example.androidweeklyplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.dateToString
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate

@Composable
internal fun TaskScreenDateInputField(
    selectedDate: LocalDate?,
    editState: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier.clickable(
        onClick = onClick,
        enabled = editState),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.description_date),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = selectedDate?.let { dateToString(it) }
                ?: stringResource(R.string.description_not_defined),
            style = MaterialTheme.typography.labelSmall
        )
    }
    Icon(
        imageVector = Icons.Default.DateRange,
        contentDescription = null
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenDateInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenDateInputField(
            selectedDate = LocalDate.of(2024, 10, 18),
            editState = true,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}