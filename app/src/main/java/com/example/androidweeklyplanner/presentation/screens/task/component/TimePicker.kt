package com.example.androidweeklyplanner.presentation.screens.task.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.core.LightBlue20
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialogExample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                selectorColor = MaterialTheme.colorScheme.tertiary,
                clockDialSelectedContentColor = Color.White,
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.tertiary,
                timeSelectorUnselectedContainerColor = TimePickerDefaults.colors().containerColor,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onTertiary,
                clockDialColor = LightBlue20
            )
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.description_cancel),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.description_confirm),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        text = { content() }
    )
}
