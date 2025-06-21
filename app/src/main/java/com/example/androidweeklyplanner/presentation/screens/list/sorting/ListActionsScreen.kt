package com.example.androidweeklyplanner.presentation.screens.list.sorting

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.screens.shared.ActionTab

@Composable
fun ListActionsScreen(
    onNavigateToWeekTasks: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    modifier: Modifier,
) {
    SearchListScreenContent(
        modifier = modifier,
        onNavigateToFilterScreen = onNavigateToFilterScreen,
        onNavigateToWeekTasks = onNavigateToWeekTasks
    )
}

@Composable
private fun SearchListScreenContent(
    modifier: Modifier,
    onNavigateToFilterScreen: () -> Unit,
    onNavigateToWeekTasks: () -> Unit,
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min),
) {
    ActionTab(
        icon = ImageVector.vectorResource(R.drawable.baseline_calendar_month_24),
        contentDescription = stringResource(R.string.action_weekly_tasks),
        label = stringResource(R.string.action_weekly_tasks),
        onClick = onNavigateToWeekTasks,
        modifier = Modifier.weight(1f)
    )
    SearchScreenDivider()
    ActionTab(
        icon = ImageVector.vectorResource(R.drawable.baseline_filter_alt_24),
        contentDescription = stringResource(R.string.action_sort_filter),
        label = stringResource(R.string.action_sort_filter),
        onClick = onNavigateToFilterScreen,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun SearchScreenDivider(
    modifier: Modifier = Modifier,
) = VerticalDivider(
    modifier = modifier
        .background(color = Color.Gray)
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent1Preview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchListScreenContent(
            modifier = Modifier.fillMaxWidth(),
            onNavigateToWeekTasks = {},
            onNavigateToFilterScreen = {}
        )
    }
}
