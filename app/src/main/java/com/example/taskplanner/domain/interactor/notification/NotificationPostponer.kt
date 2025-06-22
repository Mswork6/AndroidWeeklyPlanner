package com.example.taskplanner.domain.interactor.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.taskplanner.domain.getTaskId
import com.example.taskplanner.domain.interactor.saver.TaskInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class NotificationPostponer : BroadcastReceiver() {
    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var notificationInteractor: NotificationInteractor

    @Inject
    lateinit var scope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        try {
            scope.launch {
                val taskId = getTaskId(intent.extras)
                val task = taskInteractor.getTask(taskId) ?: return@launch
                notificationInteractor.deleteNotification(taskId)
                val originalOffset = task.notificationTimeOffset ?: 0L
                var newOffset = originalOffset + 15L
                val taskDateTime = LocalDateTime.of(task.date, task.time)
                var nextNotify = taskDateTime.plusMinutes(newOffset)
                while (!nextNotify.isAfter(LocalDateTime.now())) {
                    newOffset += 15L
                    nextNotify = taskDateTime.plusMinutes(newOffset)
                }
                taskInteractor.updateTask(task.copy(notificationTimeOffset = newOffset))

            }
            try {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.cancel(getTaskId(intent.extras).hashCode())
            } catch (_: Exception) {}

        } catch (exception: Exception) {
            Log.d(this::class.java.toString(), exception.stackTraceToString())
        }
    }
}