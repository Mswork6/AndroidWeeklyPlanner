package com.example.androidweeklyplanner.presentation.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(
        space = 16.dp,
        alignment = Alignment.CenterVertically
    ),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    CircularProgressIndicator(
        modifier = Modifier.size(96.dp)
    )
    if (title != null) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ErrorScreenPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        LoadingScreen(
            title = "Loading your data",
            modifier = Modifier.fillMaxSize()
        )
    }
}