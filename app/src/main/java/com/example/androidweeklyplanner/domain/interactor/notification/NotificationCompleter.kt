package com.example.androidweeklyplanner.domain.interactor.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.androidweeklyplanner.domain.getTaskId
import com.example.androidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.androidweeklyplanner.domain.usecase.ToggleTaskStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationCompleter : BroadcastReceiver() {
    @Inject lateinit var taskInteractor: TaskInteractor
    @Inject lateinit var toggleTaskStatus: ToggleTaskStatusUseCase
    @Inject lateinit var notificationInteractor: NotificationInteractor
    @Inject lateinit var scope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        scope.launch {
            val taskId = getTaskId(intent.extras)
            val task = taskInteractor.getTask(taskId)
            if (task != null && !task.isDone) {
                toggleTaskStatus(task)
                notificationInteractor.deleteNotification(taskId)
            }
        }

        try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.cancel(getTaskId(intent.extras).hashCode())
        } catch (_: Exception) {}
    }
}