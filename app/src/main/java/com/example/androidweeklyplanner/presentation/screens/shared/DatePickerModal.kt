package com.example.androidweeklyplanner.presentation.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.PastOrPresentSelectableDates
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary,
                )
            ) {
                Text(stringResource(R.string.description_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(stringResource(R.string.description_cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = MaterialTheme.colorScheme.tertiary,
                todayContentColor = MaterialTheme.colorScheme.onPrimary,
                selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
                selectedYearContainerColor = MaterialTheme.colorScheme.tertiary,
                selectedDayContentColor = MaterialTheme.colorScheme.onTertiary,
                selectedYearContentColor = MaterialTheme.colorScheme.onTertiary,
                currentYearContentColor = MaterialTheme.colorScheme.onPrimary,
                yearContentColor = MaterialTheme.colorScheme.onPrimary,
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenDateInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        DatePickerModal(
            selectableDates = PastOrPresentSelectableDates,
            onDateSelected = { },
            onDismiss = { }
        )
    }
}