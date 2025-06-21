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
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.description

@Composable
internal fun TaskScreenDifficultyInputField(
    editState: Boolean,
    difficulty: Difficulty,
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
            text = stringResource(R.string.description_difficulty),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = when (difficulty) {
                Difficulty.HARD -> stringResource(Difficulty.HARD.description)
                Difficulty.MEDIUM -> stringResource(Difficulty.MEDIUM.description)
                Difficulty.EASY -> stringResource(Difficulty.EASY.description)
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
        TaskScreenDifficultyInputField(
            editState = true,
            difficulty = Difficulty.HARD,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}