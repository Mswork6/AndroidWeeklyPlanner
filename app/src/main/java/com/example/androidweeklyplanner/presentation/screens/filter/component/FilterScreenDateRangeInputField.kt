package com.example.androidweeklyplanner.presentation.screens.filter.component

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
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.dateToString
import java.time.LocalDate

@Composable
internal fun FilterScreenDateRangeInputField(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier.clickable(
        onClick = onClick),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    val startText = startDate
        ?.let { dateToString(it) } ?: stringResource(R.string.description_not_defined)

    val endText = endDate
        ?.let { dateToString(it) } ?: stringResource(R.string.description_not_defined)


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.description_date_range),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(R.string.description_start_date_extended, startText),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(R.string.description_end_date_extended, endText),
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
        FilterScreenDateRangeInputField(
            startDate = null,
            endDate = null,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}