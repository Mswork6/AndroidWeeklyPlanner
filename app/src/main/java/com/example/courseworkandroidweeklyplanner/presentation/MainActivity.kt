package com.example.courseworkandroidweeklyplanner.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.courseworkandroidweeklyplanner.presentation.screens.main.installMainScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.installAddScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.installEditScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.installViewScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.navigateToAddScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.navigateToEditScreen
import com.example.courseworkandroidweeklyplanner.presentation.screens.task.navigateToViewScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val incomingId: String? = intent.data?.lastPathSegment
        val isColdLaunch = incomingId != null
        val viewRoutePattern = "view/{taskId}"
        val startRoute = incomingId?.let { "view/$it" } ?: "main"

        setContent {
            CourseWorkAndroidWeeklyPlannerTheme {
                var coldPending by remember { mutableStateOf(isColdLaunch) }
                val controller = rememberNavController().also { navController = it }
                val backStack by controller.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route

                NavHost(
                    navController = controller,
                    startDestination = startRoute
                ) {
                    installMainScreen(
                        onNavigateToTaskAddScreen = controller::navigateToAddScreen,
                        onNavigateToTaskEditScreen = controller::navigateToEditScreen,
                        onNavigateToTaskOpenScreen = controller::navigateToViewScreen
                    )
                    installAddScreen(controller::atomicBack)
                    installEditScreen(controller::atomicBack)
                    installViewScreen { backStackEntry ->
                        if (coldPending && currentRoute == viewRoutePattern) {
                            controller.navigate("main") {
                                popUpTo(viewRoutePattern) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            controller.popBackStack()
                        }
                    }
                }

                if (coldPending && currentRoute == viewRoutePattern) {
                    BackHandler {
                        controller.navigate("main") {
                            popUpTo(viewRoutePattern) { inclusive = true }
                            launchSingleTop = true
                        }
                        coldPending = false
                    }
                }

                if (isColdLaunch && !coldPending && currentRoute == "main") {
                    BackHandler {
                        moveTaskToBack(true)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data
            ?.lastPathSegment
            ?.let { UUID.fromString(it) }
            ?.let { id -> navController.navigateToViewScreen(id) }
    }
}