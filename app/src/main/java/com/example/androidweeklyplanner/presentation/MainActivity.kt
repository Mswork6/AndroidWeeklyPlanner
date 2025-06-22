package com.example.androidweeklyplanner.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationEventBus
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.screens.filter.installFilterScreen
import com.example.androidweeklyplanner.presentation.screens.filter.navigateToFilterScreen
import com.example.androidweeklyplanner.presentation.screens.list.installListScreen
import com.example.androidweeklyplanner.presentation.screens.list.navigateToListScreen
import com.example.androidweeklyplanner.presentation.screens.main.installMainScreen
import com.example.androidweeklyplanner.presentation.screens.task.installAddScreen
import com.example.androidweeklyplanner.presentation.screens.task.installEditScreen
import com.example.androidweeklyplanner.presentation.screens.task.installViewScreen
import com.example.androidweeklyplanner.presentation.screens.task.navigateToAddScreen
import com.example.androidweeklyplanner.presentation.screens.task.navigateToEditScreen
import com.example.androidweeklyplanner.presentation.screens.task.navigateToViewScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var eventBus: NotificationEventBus
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CourseWorkAndroidWeeklyPlannerTheme {
                navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main",
                    enterTransition = {
                        fadeIn(tween(
                            easing = FastOutSlowInEasing
                        ))
                    },
                    exitTransition = {
                        fadeOut(tween(
                            easing = FastOutSlowInEasing
                        ))
                    }
                ) {
                    installMainScreen(
                        onNavigateToTaskAddScreen  = navController::navigateToAddScreen,
                        onNavigateToTaskEditScreen = navController::navigateToEditScreen,
                        onNavigateToTaskOpenScreen = navController::navigateToViewScreen,
                        onNavigateToAllTasks =  navController::navigateToListScreen
                    )
                    installListScreen(
                        onNavigateToTaskAddScreen  = navController::navigateToAddScreen,
                        onNavigateToTaskEditScreen = navController::navigateToEditScreen,
                        onNavigateToTaskOpenScreen = navController::navigateToViewScreen,
                        onNavigateToFilterScreen = navController::navigateToFilterScreen,
                        onNavigateToWeekTasks = navController::atomicBack
                    )
                    installFilterScreen(
                        onNavigateToListScreen = navController::atomicBack
                    )
                    installAddScreen(navController::atomicBack)
                    installEditScreen(navController::atomicBack)
                    installViewScreen(navController::atomicBack)
                }

                //Запуск при "холодном старте"
                LaunchedEffect(navController, intent) {
                    navController.handleDeepLink(intent)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data
            ?.lastPathSegment
            ?.let { UUID.fromString(it) }
            ?.let { navController.navigateToViewScreen(it) }
        eventBus.notifyReceived()
    }
}