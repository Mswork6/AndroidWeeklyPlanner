package com.example.androidweeklyplanner.presentation.screens.filter.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.description

@Composable
fun FilterCategoryDialogWindow(
    selectedOptions: Set<Category>,
    onOptionsSelected: (Set<Category>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var tempSelectedOptions by remember { mutableStateOf(selectedOptions) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(R.string.description_choose_task_category),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CheckboxGroupWithCategoryEnum(
                    selectedOptions = tempSelectedOptions,
                    onOptionToggled = { option, isChecked ->
                        tempSelectedOptions = if (isChecked) tempSelectedOptions + option
                        else tempSelectedOptions - option
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                            onOptionsSelected(tempSelectedOptions)
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

@Composable
fun CheckboxGroupWithCategoryEnum(
    selectedOptions: Set<Category>,
    onOptionToggled: (Category, Boolean) -> Unit,
) {
    Column {
        Category.entries.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        val newState = !selectedOptions.contains(option)
                        onOptionToggled(option, newState)
                    }

            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = { isChecked ->
                        onOptionToggled(option, isChecked)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                        checkmarkColor = CardDefaults.cardColors().containerColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = stringResource(option.description),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(3f)
                )
                Row(modifier = Modifier.weight(1f)) {
                    Text(option.emoji)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FilterCategoryDialogWindowPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        FilterCategoryDialogWindow(
            selectedOptions = setOf(Category.WORK, Category.SPORT),
            onOptionsSelected = {},
            onDismissRequest = {}
        )
    }
}