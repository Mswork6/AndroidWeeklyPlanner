package com.example.androidweeklyplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.description

@Composable
internal fun TaskScreenCategoryInputField(
    editState: Boolean,
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier.clickable(
        onClick = onClick,
        enabled = editState
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.description_category),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = when (category) {
                Category.WORK -> stringResource(Category.WORK.description)
                Category.STUDY -> stringResource(Category.STUDY.description)
                Category.SPORT -> stringResource(Category.SPORT.description)
                Category.HOUSEHOLD_CHORES -> stringResource(Category.HOUSEHOLD_CHORES.description)
                Category.VACATION -> stringResource(Category.VACATION.description)
                Category.NONE -> stringResource(Category.NONE.description)
            },
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenDifficultyInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenCategoryInputField(
            editState = true,
            category = Category.NONE,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}