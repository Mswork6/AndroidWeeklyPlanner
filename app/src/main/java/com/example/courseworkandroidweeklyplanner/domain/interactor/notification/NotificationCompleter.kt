package com.example.courseworkandroidweeklyplanner.domain.interactor.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.courseworkandroidweeklyplanner.domain.getTaskId
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.domain.usecase.ToggleTaskStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationCompleter : BroadcastReceiver() {
    @Inject lateinit var taskInteractor: TaskInteractor
    @Inject lateinit var toggleTaskStatus: ToggleTaskStatusUseCase
    @Inject lateinit var scope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        scope.launch {
            val taskId = getTaskId(intent.extras)
            val task = taskInteractor.getTask(taskId)
            if (task != null && !task.isDone) {
                toggleTaskStatus(task)
            }
        }

        try {
            val notifId = getTaskId(intent.extras).hashCode()
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            nm.cancel(notifId)
        } catch(e:Exception){ Log.w("NotificationCompleter", e) }
    }
}