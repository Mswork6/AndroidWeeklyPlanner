package com.example.androidweeklyplanner.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.androidweeklyplanner.presentation.screens.main.sorting.MainActionsScreen
import com.example.androidweeklyplanner.presentation.screens.main.tasks.TasksMainScreen
import com.example.androidweeklyplanner.presentation.screens.main.week.WeekScreen
import java.util.UUID

@Composable
fun MainScreen(
    onNavigateToTaskAddScreen: () -> Unit,
    onNavigateToTaskEditScreen: (taskId: UUID) -> Unit,
    onNavigateToTaskOpenScreen: (taskId: UUID) -> Unit,
    onNavigateToAllTasks: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            WeekScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
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
            MainActionsScreen(
                modifier = Modifier.fillMaxWidth(),
                onNavigateToAllTasks = onNavigateToAllTasks
            )
            TasksMainScreen(
                onNavigateToTaskEditScreen = onNavigateToTaskEditScreen,
                onNavigateToTaskOpenScreen = onNavigateToTaskOpenScreen,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
