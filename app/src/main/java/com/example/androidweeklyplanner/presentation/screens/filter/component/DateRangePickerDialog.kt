package com.example.androidweeklyplanner.presentation.screens.filter.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.convertToMillis
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    initialStart: LocalDate?,
    initialEnd: LocalDate?,
    onConfirm: (start: Long?, end: Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val startDate = convertToMillis(initialStart)
    val endDate = convertToMillis(initialEnd)

    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate,
        initialSelectedEndDateMillis = endDate
    )

    DatePickerDialog(
        properties = DialogProperties(usePlatformDefaultWidth = true),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        pickerState.selectedStartDateMillis,
                        pickerState.selectedEndDateMillis
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                )
            ) { Text(stringResource(R.string.description_confirm)) }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                )
            ) {
                Text(stringResource(R.string.description_cancel))
            }
        },
    ) {
        DateRangePicker(
            state = pickerState,
            showModeToggle = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),
            title = {
                Text(
                    text = stringResource(R.string.description_choose_date_range),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 16.dp
                        )
                )
            },
            headline = { CustomDateRangeHeadline(state = pickerState) },
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = MaterialTheme.colorScheme.tertiary,
                todayContentColor = MaterialTheme.colorScheme.onPrimary,
                selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
                selectedDayContentColor = MaterialTheme.colorScheme.onTertiary,
                dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedContainerColor = DatePickerDefaults.colors().containerColor,
                    focusedContainerColor = DatePickerDefaults.colors().containerColor,
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    errorContainerColor = DatePickerDefaults.colors().containerColor,
                    focusedPrefixColor = MaterialTheme.colorScheme.tertiary,
                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.tertiary,
                        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                )

            )
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            TextButton(
                onClick = { pickerState.setSelection(startDateMillis = null, endDateMillis = null) } ,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                )
            ) { Text(stringResource(R.string.description_reset_dates)) }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDateRangeHeadline(
    state: DateRangePickerState,
    modifier: Modifier = Modifier,
) {
    val defaultFormatter: DatePickerFormatter = remember {
        DatePickerDefaults.dateFormatter()
    }

    val locale = CalendarLocale.getDefault()

    val startText = state.selectedStartDateMillis
        ?.let { millis -> defaultFormatter.formatDate(millis, locale) }
        ?: stringResource(R.string.description_start_date)
    val endText = state.selectedEndDateMillis
        ?.let { millis -> defaultFormatter.formatDate(millis, locale) }
        ?: stringResource(R.string.description_end_date)

    Text(
        text = "$startText — $endText",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 12.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRangePickerDialogPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        DateRangePickerDialog(
            initialStart = null,
            initialEnd = null,
            onConfirm = { _, _ -> },
            onDismiss = { }
        )
    }
}