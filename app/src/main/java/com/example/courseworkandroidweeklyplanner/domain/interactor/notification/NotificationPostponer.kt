package com.example.courseworkandroidweeklyplanner.domain.interactor.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.courseworkandroidweeklyplanner.domain.getTaskId
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.presentation.dateTimeToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationPostponer : BroadcastReceiver() {
    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var scope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        try {
            scope.launch {
                when (val task = taskInteractor.getTask(getTaskId(intent.extras))) {
                    null -> Unit
                    else -> {
                        notificationManager.cancel(task.id.hashCode())
                        val date = task.notificationTime?.plusMinutes(15)
                        Log.d("MSWORK6", date.toString())
                        taskInteractor.updateTask(task.copy(notificationTime = date))
                    }
                }
            }
        } catch (exception: Exception) {
            Log.d(this::class.java.toString(), exception.stackTraceToString())
        }
    }
}