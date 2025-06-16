package com.example.androidweeklyplanner.presentation.screens.list.sorting

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.screens.filter.FilterScreenActions
import com.example.androidweeklyplanner.presentation.screens.filter.FilterScreenState
import com.example.androidweeklyplanner.presentation.screens.filter.FilterScreenViewModel
import com.example.androidweeklyplanner.presentation.screens.shared.ActionTab

@Composable
fun ListActionsScreen(
    viewModel: FilterScreenViewModel = hiltViewModel(),
    onNavigateToWeekTasks: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsState()
    SearchListScreenContent(
        state = state,
        onAction = viewModel::handle,
        modifier = modifier,
        onNavigateToFilterScreen = onNavigateToFilterScreen,
        onNavigateToWeekTasks = onNavigateToWeekTasks
    )
}

@Composable
private fun SearchListScreenContent(
    state: FilterScreenState,
    onAction: (FilterScreenActions) -> Unit,
    modifier: Modifier,
    onNavigateToFilterScreen: () -> Unit,
    onNavigateToWeekTasks: () -> Unit
) = when (state) {
    is FilterScreenState.Initial -> {
        CircularProgressIndicator()
    }

    is FilterScreenState.Default -> {
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
                onClick = onNavigateToFilterScreen,
                modifier = Modifier.weight(1f)
            )
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

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun SearchScreenContent1Preview() {
//    val state = ListScreenActionsState.Default(
//        sort = SortType.STANDARD,
//        isSorterVisible = false,
//    )
//    CourseWorkAndroidWeeklyPlannerTheme {
//        SearchListScreenContent(
//            state = state,
//            onAction = {},
//            modifier = Modifier.fillMaxWidth(),
//            onNavigateToWeekTasks = {}
//        )
//    }
//}
//
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun SearchScreenContent2Preview() {
//    val state = ListScreenActionsState.Default(
//        sort = SortType.STANDARD,
//        isSorterVisible = false,
//    )
//    CourseWorkAndroidWeeklyPlannerTheme {
//        SearchListScreenContent(
//            state = state,
//            onAction = {},
//            modifier = Modifier.fillMaxWidth(),
//            onNavigateToWeekTasks = {}
//        )
//    }
//}
//
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun SearchScreenContent3Preview() {
//    val state = ListScreenActionsState.Default(
//        sort = SortType.STANDARD,
//        isSorterVisible = true,
//    )
//    CourseWorkAndroidWeeklyPlannerTheme {
//        SearchListScreenContent(
//            state = state,
//            onAction = {},
//            modifier = Modifier.fillMaxWidth(),
//            onNavigateToWeekTasks = {}
//        )
//    }
//}