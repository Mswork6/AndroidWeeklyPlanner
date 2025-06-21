package com.example.androidweeklyplanner.presentation.screens.list.sorting.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun DateRangeInputField(
    startDate: LocalDate?,
    onStartDateChange: (LocalDate?) -> Unit,
    endDate: LocalDate?,
    onEndDateChange: (LocalDate?) -> Unit,
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.MAX,
    modifier: Modifier = Modifier
) {
    // Формат dd.MM.yyyy
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    var startText by remember { mutableStateOf(startDate?.format(formatter) ?: "") }
    var endText   by remember { mutableStateOf(endDate?.format(formatter)   ?: "") }

    var startError by remember { mutableStateOf(false) }
    var endError   by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Временной диапазон",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "С", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))

            OutlinedTextField(
                value = startText,
                onValueChange = { new ->
                    startText = new
                    val parsed = runCatching { LocalDate.parse(new, formatter) }.getOrNull()
                    startError = parsed == null || parsed < minDate
                    if (!startError) onStartDateChange(parsed)
                },
                isError = startError,
                placeholder = { Text("дд.MM.гггг") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(120.dp)
                    .height(IntrinsicSize.Min)
            )
            if (startError) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Некорректная дата",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(text = "По", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))

            OutlinedTextField(
                value = endText,
                onValueChange = { new ->
                    endText = new
                    val parsed = runCatching { LocalDate.parse(new, formatter) }.getOrNull()
                    endError = parsed == null || parsed > maxDate
                    if (!endError) onEndDateChange(parsed)
                },
                isError = endError,
                placeholder = { Text("дд.MM.гггг") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(120.dp)
                    .height(IntrinsicSize.Min)
            )
            if (endError) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Некорректная дата",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRangeInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        DateRangeInputField(
            startDate = null,
            endDate = LocalDate.of(2025, 6, 10),
            onStartDateChange = { },
            onEndDateChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}