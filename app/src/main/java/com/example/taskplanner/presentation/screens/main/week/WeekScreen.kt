package com.example.taskplanner.presentation.screens.main.week

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskplanner.R
import com.example.taskplanner.domain.model.Week
import com.example.taskplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.taskplanner.presentation.dateToString
import com.example.taskplanner.presentation.screens.shared.DatePickerModal
import java.time.LocalDate

@Composable
fun WeekScreen(
    modifier: Modifier = Modifier,
    viewModel: WeekScreenViewModel = hiltViewModel(),
) {
    val state: WeekScreenState by viewModel.state.collectAsState()
    WeekScreenContent(
        state = state,
        onAction = viewModel::handle,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeekScreenContent(
    state: WeekScreenState,
    onAction: (WeekScreenAction) -> Unit,
    modifier: Modifier,
) = when (state) {
    is WeekScreenState.Initial -> Unit
    is WeekScreenState.Default -> {
        Row(
            modifier = modifier
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onAction(WeekScreenAction.SelectPreviousWeek) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_previous_week)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable(
                        onClick = { onAction(WeekScreenAction.SetCalendarVisibility(true)) }
                    )
                    .fillMaxHeight()
            ) {
                Text(text = dateToString(state.week))
            }
            IconButton(onClick = { onAction(WeekScreenAction.SelectNextWeek) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.action_next_week)
                )
            }

            if (state.isCalendarVisible) {
                DatePickerModal(
                    onDateSelected = { dateMillis ->
                        dateMillis?.let {
                            onAction(WeekScreenAction.SetDate(dateMillis))
                        }
                    },
                    onDismiss = {
                        onAction(WeekScreenAction.SetCalendarVisibility(false))
                    }
                )
            }




        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeekScreenContentInitialPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        WeekScreenContent(
            state = WeekScreenState.Initial,
            onAction = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeekScreenContentDefaultPreview() {
    val state = WeekScreenState.Default(
        Week.of(date = LocalDate.of(2024, 10, 7)),
        isCalendarVisible = false
    )
    CourseWorkAndroidWeeklyPlannerTheme {
        WeekScreenContent(
            state = state,
            onAction = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
