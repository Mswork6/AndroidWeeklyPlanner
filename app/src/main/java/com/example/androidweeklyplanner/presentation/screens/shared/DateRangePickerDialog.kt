package com.example.androidweeklyplanner.presentation.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    initialStart: Long? = null,
    initialEnd: Long? = null,
    onConfirm: (start: Long?, end: Long?) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStart,
        initialSelectedEndDateMillis = initialEnd
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
                }
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier
            .height(500.dp)
    ) {
        DateRangePicker(
            state = pickerState,
            showModeToggle = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            title = {
                Text(
                    text = "Выберите временной диапазон",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 16.dp
                        )
                )
            },
            headline = {
                CustomDateRangeHeadline(state = pickerState)
            }
        )
        TextButton(
            onClick = { }
        ) { Text("Hello there") }
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

    // Вычисляем тексты для начала и конца
    val startText = state.selectedStartDateMillis
        ?.let { millis -> defaultFormatter.formatDate(millis, locale) } ?: "Начальная дата"
    val endText = state.selectedEndDateMillis
        ?.let { millis -> defaultFormatter.formatDate(millis, locale) } ?: "Конечная дата"

    Text(
        text = "$startText — $endText",
        style = MaterialTheme.typography.titleMedium, // уменьшенный шрифт
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 12.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenDateInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        DateRangePickerDialog(
            onConfirm = { _, _ -> },
            onReset = { },
            onDismiss = { }
        )
    }
}