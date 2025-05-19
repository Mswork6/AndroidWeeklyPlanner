package com.example.courseworkandroidweeklyplanner.presentation.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.presentation.screens.list.sorting.SearchListScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.list.tasks.TasksListScreen
import java.util.UUID

@Composable
fun ListScreen(
    onNavigateToTaskAddScreen: () -> Unit,
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    onNavigateToWeekTasks: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToTaskAddScreen,
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
    ) { padding: PaddingValues ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(
                space = 4.dp,
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.Start
        ) {
            SearchListScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                onNavigateToWeekTasks = onNavigateToWeekTasks
            )
            TasksListScreen(
                onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
                onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}