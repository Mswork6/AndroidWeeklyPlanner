package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.component.SortDialogWindow
import com.example.courseworkandroidweeklyplanner.presentation.screens.shared.DatePickerModal
import com.example.courseworkandroidweeklyplanner.presentation.convertToLocalDate
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val state by viewModel.state.collectAsState()
    SearchScreenContent(
        state = state,
        onAction = viewModel::handle,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    state: SearchScreenState,
    onAction: (SearchScreenAction) -> Unit,
    modifier: Modifier
) = when (state) {
    is SearchScreenState.Initial -> {
        CircularProgressIndicator()
    }
    is SearchScreenState.Default -> {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { onAction(SearchScreenAction.SetCalendarVisibility(true)) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.action_search)
                )
            }
            IconButton(onClick = { onAction(SearchScreenAction.SetSorterVisibility(true)) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_alt_24),
                    contentDescription = stringResource(R.string.action_filter)
                )
            }
            if (state.isCalendarVisible) {
                DatePickerModal(
                    onDateSelected = { dateMillis ->
                        dateMillis?.let {
                            onAction(SearchScreenAction.SetDate(convertToLocalDate(dateMillis)))
                        }
                        onAction(SearchScreenAction.SetCalendarVisibility(false))
                    },
                    onDismiss = {
                        onAction(SearchScreenAction.SetCalendarVisibility(false))
                    }
                )
            }
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent1Preview() {
    val state = SearchScreenState.Default(
        sort = SortType.STANDARD,
        isCalendarVisible = false,
        isSorterVisible = false,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent2Preview() {
    val state = SearchScreenState.Default(
        sort = SortType.STANDARD,
        isCalendarVisible = true,
        isSorterVisible = false,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenContent3Preview() {
    val state = SearchScreenState.Default(
        sort = SortType.STANDARD,
        isCalendarVisible = false,
        isSorterVisible = true,
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        SearchScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}