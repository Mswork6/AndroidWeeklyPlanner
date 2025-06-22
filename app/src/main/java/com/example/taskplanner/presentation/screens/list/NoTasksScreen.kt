package com.example.taskplanner.presentation.screens.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.taskplanner.presentation.core.theme.extendedColors

@Composable
fun NoTasksScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    if (title != null) {
        Text(
            text = title,
            modifier = Modifier
                .padding(horizontal = 40.dp),
            color = MaterialTheme.extendedColors.gray,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoTasksScreenPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        NoTasksScreen(
            title = "Loading your data",
            modifier = Modifier.fillMaxSize()
        )
    }
}