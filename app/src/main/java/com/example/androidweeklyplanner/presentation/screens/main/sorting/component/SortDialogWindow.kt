package com.example.androidweeklyplanner.presentation.screens.main.sorting.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.description
import com.example.androidweeklyplanner.presentation.screens.shared.SortingChipGroup

@Composable
fun SortDialogWindow(
    selectedConfig: SortConfig,
    onOptionSelected: (SortConfig) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var tempConfig by remember { mutableStateOf(selectedConfig) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.description_choose_sorting_type),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                // Группа по приоритету
                SortingChipGroup(
                    title = stringResource(R.string.description_task_priority)
                            + ": " + stringResource(tempConfig.priorityOrder.description),
                    selectedOrder = tempConfig.priorityOrder,
                    onOrderChange = { newOrder ->
                        tempConfig = tempConfig.copy(priorityOrder = newOrder)
                    }
                )

                Spacer(Modifier.height(16.dp))

                // Группа по сложности
                SortingChipGroup(
                    title = stringResource(R.string.description_task_difficulty)
                            + ": " + stringResource(tempConfig.difficultyOrder.description),
                    selectedOrder = tempConfig.difficultyOrder,
                    onOrderChange = { newOrder ->
                        tempConfig = tempConfig.copy(difficultyOrder = newOrder)
                    }
                )

                Spacer(Modifier.height(24.dp))

                // Кнопки подтверждения и отмены
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = stringResource(R.string.description_cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            onOptionSelected(tempConfig)
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = stringResource(R.string.description_confirm))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RadioDialogWindowPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        SortDialogWindow(
            selectedConfig = SortConfig(),
            onOptionSelected = {},
            onDismissRequest = {}
        )
    }
}