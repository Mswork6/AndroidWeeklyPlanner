package com.example.androidweeklyplanner.presentation.screens.filter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.description
import com.example.androidweeklyplanner.presentation.screens.filter.component.DateRangePickerDialog
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterCategoryDialogWindow
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterDifficultyDialogWindow
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterPriorityDialogWindow
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterScreenCategoryInputField
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterScreenDateRangeInputField
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterScreenDifficultyInputField
import com.example.androidweeklyplanner.presentation.screens.filter.component.FilterScreenPriorityInputField
import com.example.androidweeklyplanner.presentation.screens.shared.ScreenHorizontalDivider
import com.example.androidweeklyplanner.presentation.screens.shared.SortingChipGroup
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
                confirmAction = { 
                    onAction(FilterScreenActions.Save)
                    onNavigateToListScreen()
                }
            )
        }
    ) { padding: PaddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp, alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(
                    text = stringResource(R.string.description_filters),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                FilterScreenDateRangeInputField(
                    startDate = state.startDate,
                    endDate = state.endDate,
                    onClick = { onAction(FilterScreenActions.SetDatePickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()

                )
            }
            item { ScreenHorizontalDivider() }
            item {
                FilterScreenPriorityInputField(
                    priorities = state.selectedPriorities,
                    onClick = { onAction(FilterScreenActions.SetPriorityPickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                FilterScreenDifficultyInputField(
                    difficulties = state.selectedDifficulties,
                    onClick = { onAction(FilterScreenActions.SetDifficultyPickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                FilterScreenCategoryInputField(
                    categories = state.selectedCategories,
                    onClick = { onAction(FilterScreenActions.SetCategoryPickerVisibility(true)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                Text(
                    text = stringResource(R.string.description_sorting),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item { ScreenHorizontalDivider() }
            item {
                SortingChipGroup(
                    title = stringResource(R.string.description_task_priority)
                            + ": " + stringResource(state.sortPriorityOrder.description),
                    height = 28.dp,
                    textStyle = MaterialTheme.typography.titleSmall,
                    selectedOrder = state.sortPriorityOrder,
                    onOrderChange = { newOrder ->
                        onAction(FilterScreenActions.SetPriorityOrder(newOrder))
                    }
                )
            }
            item {
                SortingChipGroup(
                    title = stringResource(R.string.description_task_difficulty)
                            + ": " + stringResource(state.sortDifficultyOrder.description),
                    height = 28.dp,
                    textStyle = MaterialTheme.typography.titleSmall,
                    selectedOrder = state.sortDifficultyOrder,
                    onOrderChange = { newOrder ->
                        onAction(FilterScreenActions.SetDifficultyOrder(newOrder))
                    }
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {onAction(FilterScreenActions.ResetValues)},
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(stringResource(R.string.description_reset_values))
                    }
                }
            }
        }

        if (state.isDifficultyFilterDialogOpened) {
            FilterDifficultyDialogWindow(
                selectedOptions = state.selectedDifficulties,
                onOptionsSelected = { options ->
                    onAction(FilterScreenActions.SetDifficultyFilter(options))
                },
                onDismissRequest = {
                    onAction(FilterScreenActions.SetDifficultyPickerVisibility(false))
                }
            )
        }

        if (state.isPriorityFilterDialogOpened) {
            FilterPriorityDialogWindow(
                selectedOptions = state.selectedPriorities,
                onOptionSelected = { options ->
                    onAction(FilterScreenActions.SetPriorityFilter(options))
                },
                onDismissRequest = {
                    onAction(FilterScreenActions.SetPriorityPickerVisibility(false))
                })
        }

        if (state.isCategoryFilterDialogOpened) {
            FilterCategoryDialogWindow(
                selectedOptions = state.selectedCategories,
                onOptionsSelected = { options ->
                    onAction(FilterScreenActions.SetCategoryFilter(options))
                },
                onDismissRequest = {
                    onAction(FilterScreenActions.SetCategoryPickerVisibility(false))
                })
        }

        if (state.isDatePickerOpened) {
            DateRangePickerDialog(
                initialStart = state.startDate,
                initialEnd = state.endDate,
                onConfirm = { start, end ->
                    onAction(FilterScreenActions.SetDateFilter(start, end))
                    onAction(FilterScreenActions.SetDatePickerVisibility(false))
                },
                onDismiss = { onAction(FilterScreenActions.SetDatePickerVisibility(false)) }
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