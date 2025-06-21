package com.example.androidweeklyplanner.presentation.screens.main.sorting

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.screens.main.sorting.component.SortDialogWindow
import com.example.androidweeklyplanner.presentation.screens.shared.ActionTab
import com.example.androidweeklyplanner.presentation.screens.shared.LoadingScreen

@Composable
fun MainActionsScreen(
    viewModel: MainScreenActionsViewModel = hiltViewModel(),
    onNavigateToAllTasks: () -> Unit,
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsState()
    SearchScreenContent(
        state = state,
        onAction = viewModel::handle,
        modifier = modifier,
        onNavigateToAllTasks = onNavigateToAllTasks
    )
}

@Composable
private fun SearchScreenContent(
    state: MainScreenActionsState,
    onAction: (MainScreenAction) -> Unit,
    modifier: Modifier,
    onNavigateToAllTasks: () -> Unit
) = when (state) {
    is MainScreenActionsState.Initial -> {
        LoadingScreen(
            modifier = modifier.fillMaxSize(),
            title = stringResource(R.string.description_loading_data)
        )
    }

    is MainScreenActionsState.Default -> {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            ActionTab(
                icon = ImageVector.vectorResource(R.drawable.baseline_format_list_bulleted_24),
                contentDescription = stringResource(R.string.action_all_tasks),
                label = stringResource(R.string.action_all_tasks),
                onClick = onNavigateToAllTasks,
                modifier = Modifier.weight(1f)
            )
            SearchScreenDivider()
            ActionTab(
                icon = ImageVector.vectorResource(R.drawable.baseline_filter_alt_24),
                contentDescription = stringResource(R.string.action_sort),
                label = stringResource(R.string.action_sort),
                onClick = { onAction(MainScreenAction.SetSorterVisibility(true)) },
                modifier = Modifier.weight(1f)
            )
            if (state.isSorterVisible) {
                SortDialogWindow(
                    selectedConfig = state.sortConfig,
                    onOptionSelected = { newConfig ->
                        onAction(MainScreenAction.SetSort(newConfig))
                        onAction(MainScreenAction.SetSorterVisibility(false))
                    },
                    onDismissRequest = {
                        onAction(MainScreenAction.SetSorterVisibility(false))
                    },
                    modifier = Modifier.fillMaxWidth()
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
    val state = MainScreenActionsState.Default(
        sortConfig = SortConfig(),
        isSorterVisible = false,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            onNavigateToAllTasks = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent2Preview() {
    val state = MainScreenActionsState.Default(
        sortConfig = SortConfig(),
        isSorterVisible = false,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            onNavigateToAllTasks = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent3Preview() {
    val state = MainScreenActionsState.Default(
        sortConfig = SortConfig(),
        isSorterVisible = true,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            onNavigateToAllTasks = {}
        )
    }
}