package com.example.courseworkandroidweeklyplanner.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.courseworkandroidweeklyplanner.domain.TASK_ID_KEY
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

        // читаем экстру ровно один раз
        val incomingTaskId: UUID? = intent
            .getStringExtra(TASK_ID_KEY)
            ?.toUuidOrNull()

        setContent {
            CourseWorkAndroidWeeklyPlannerTheme {
                val controller = rememberNavController().also { navController = it }

                // для BackHandler ниже
                val backStack by controller.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route

                // стартовый экран — либо view, либо main
                val startRoute = incomingTaskId
                    ?.let { "view/$it" }
                    ?: "main"

                NavHost(
                    navController = controller,
                    startDestination = startRoute
                ) {
                    installMainScreen(
                        onNavigateToTaskAddScreen  = controller::navigateToAddScreen,
                        onNavigateToTaskEditScreen = controller::navigateToEditScreen,
                        onNavigateToTaskOpenScreen = controller::navigateToViewScreen
                    )
                    installAddScreen(controller::atomicBack)
                    installEditScreen(controller::atomicBack)
                    installViewScreen { _ ->
                        // UI-кнопка “назад” внутри TaskScreen
                        // Попытаться popBackStack: если вернёт false → cold-launch → навигируем на main
                        val didPop = controller.popBackStack()
                        if (!didPop) {
                            controller.navigate("main")
                        }
                    }
                }

                // перехват hardware BACK только если мы именно на view-экране
                if (currentRoute == "view/{taskId}") {
                    BackHandler {
                        val didPop = controller.popBackStack()
                        if (!didPop) {
                            controller.navigate("main")
                        }
                    }
                }

                // горячий старт (Activity уже есть в памяти) — после рендера обрабатываем Intent
                LaunchedEffect(Unit) {
                    if (incomingTaskId == null) {
                        handleIntent(intent)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        intent.getStringExtra(TASK_ID_KEY)
            ?.toUuidOrNull()
            ?.let { id ->
                navController.navigateToViewScreen(id)
            }
    }

    private fun String.toUuidOrNull(): UUID? =
        try {
            UUID.fromString(this)
        } catch (e: IllegalArgumentException) {
            null
        }
}