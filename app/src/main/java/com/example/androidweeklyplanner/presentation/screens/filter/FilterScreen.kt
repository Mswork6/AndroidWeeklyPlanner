package com.example.androidweeklyplanner.presentation.screens.filter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterDifficultyDialogWindow
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterScreenDifficultiesInputField
import com.example.androidweeklyplanner.presentation.screens.shared.TopBar

@Composable
fun FilterScreen(
    viewModel: FilterScreenViewModel = hiltViewModel(),
    onNavigateToListScreen: () -> Unit,
    modifier: Modifier,
) {
    val state by viewModel.state.collectAsState()
    FilterScreenContent(
        state = state,
        onAction = viewModel::handle,
        modifier = modifier,
        onNavigateToListScreen = onNavigateToListScreen
    )
}

@Composable
fun FilterScreenContent(
    state: FilterScreenState,
    onAction: (FilterScreenActions) -> Unit,
    modifier: Modifier,
    onNavigateToListScreen: () -> Unit,
) {
    when (state) {
        is FilterScreenState.Initial -> {
            CircularProgressIndicator()
        }

        is FilterScreenState.Default -> FilterScreenBaseContent(
            state = state,
            onAction = onAction,
            modifier = modifier,
            onNavigateToListScreen = onNavigateToListScreen
        )
    }
}


@Composable
fun FilterScreenBaseContent(
    state: FilterScreenState.Default,
    onAction: (FilterScreenActions) -> Unit,
    modifier: Modifier,
    onNavigateToListScreen: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                actionName = stringResource(R.string.description_apply),
                navigateBackAction = onNavigateToListScreen,
                confirmAction = { onNavigateToListScreen() }
            )
        }
    ) { padding: PaddingValues ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            FilterScreenDifficultiesInputField(
                difficulties = state.selectedDifficulties,
                onClick = { onAction(FilterScreenActions.SetDifficultyPickerVisibility(true)) },
                modifier = Modifier.fillMaxWidth()
            )

        }

        if (state.isDifficultyFilterDialogOpened) {
            FilterDifficultyDialogWindow(
                selectedOptions = state.selectedDifficulties,
                onOptionsSelected = { options ->
                    onAction(
                        FilterScreenActions.SetDifficultyFilter(
                            options
                        )
                    )
                },
                onDismissRequest = {
                    onAction(
                        FilterScreenActions.SetDifficultyPickerVisibility(false)
                    )
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterScreenContent1Preview() {
    val state = FilterScreenState.Default(
        startDate = null,
        endDate = null,
        selectedPriorities = emptySet(),
        selectedDifficulties = emptySet(),
        selectedCategories = emptySet(),
        sortPriorityOrder = SortType.STANDARD,
        sortDifficultyOrder = SortType.STANDARD,
        isDatePickerOpened = false,
        isPriorityFilterDialogOpened = false,
        isDifficultyFilterDialogOpened = false,
        isCategoryFilterDialogOpened = false,
        isSortDialogOpened = false
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        FilterScreenContent(
            state = state,
            onAction = {},
            onNavigateToListScreen = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}