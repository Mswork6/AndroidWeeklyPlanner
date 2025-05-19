package com.example.courseworkandroidweeklyplanner.presentation.screens.list.sorting

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.SearchScreenAction
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.SearchScreenState
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.component.SortDialogWindow
import com.example.courseworkandroidweeklyplanner.presentation.screens.shared.ActionTab

@Composable
fun SearchListScreen(
    viewModel: SearchListScreenViewModel = hiltViewModel(),
    onNavigateToWeekTasks: () -> Unit,
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsState()
    SearchListScreenContent(
        state = state,
        onAction = viewModel::handle,
        modifier = modifier,
        onNavigateToWeekTasks = onNavigateToWeekTasks
    )
}

@Composable
private fun SearchListScreenContent(
    state: SearchScreenState,
    onAction: (SearchScreenAction) -> Unit,
    modifier: Modifier,
    onNavigateToWeekTasks: () -> Unit
) = when (state) {
    is SearchScreenState.Initial -> {
        CircularProgressIndicator()
    }

    is SearchScreenState.Default -> {
        Row(
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
                onClick = { onAction(SearchScreenAction.SetSorterVisibility(true)) },
                modifier = Modifier.weight(1f)
            )
            if (state.isSorterVisible) {
                SortDialogWindow(
                    selectedOption = state.sort,
                    onOptionSelected = { option ->
                        onAction(SearchScreenAction.SetSort(option))
                        onAction(SearchScreenAction.SetSorterVisibility(false))
                    },
                    onDismissRequest = {
                        onAction(SearchScreenAction.SetSorterVisibility(false))
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }
    }
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
    val state = SearchScreenState.Default(
        sort = SortType.STANDARD,
        isSorterVisible = false,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchListScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            onNavigateToWeekTasks = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent2Preview() {
    val state = SearchScreenState.Default(
        sort = SortType.STANDARD,
        isSorterVisible = false,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchListScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            onNavigateToWeekTasks = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent3Preview() {
    val state = SearchScreenState.Default(
        sort = SortType.STANDARD,
        isSorterVisible = true,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchListScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            onNavigateToWeekTasks = {}
        )
    }
}