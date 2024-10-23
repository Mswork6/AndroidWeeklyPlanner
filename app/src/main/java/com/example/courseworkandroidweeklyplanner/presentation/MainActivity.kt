package com.example.courseworkandroidweeklyplanner.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.installMainScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.installAddScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.installEditScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.installViewScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.navigateToAddScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.navigateToEditScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.navigateToViewScreen
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourseWorkAndroidWeeklyPlannerTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "main") {
                    installMainScreen(
                        onNavigateToTaskAddScreen = navController::navigateToAddScreen,
                        onNavigateToTaskEditScreen = navController::navigateToEditScreen,
                        onNavigateToTaskOpenScreen = navController::navigateToViewScreen
                    )
                    installAddScreen(navController::atomicBack)
                    installEditScreen(navController::atomicBack)
                    installViewScreen(navController::atomicBack)
                }
            }
        }
    }
}



