package com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.sorting.component.SortDialogWindow

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    modifier: Modifier,
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
    modifier: Modifier,
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
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClick = { }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_format_list_bulleted_24),
                    contentDescription = stringResource(R.string.action_search),
                    modifier = Modifier.padding(12.dp)
                )
                Text(text = stringResource(R.string.action_all_tasks))
            }
            SearchScreenDivider()
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClick = { onAction(SearchScreenAction.SetSorterVisibility(true)) }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_alt_24),
                    contentDescription = stringResource(R.string.action_sort),
                    modifier = Modifier.padding(12.dp)
                )
                Text(text = stringResource(R.string.action_sort))
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