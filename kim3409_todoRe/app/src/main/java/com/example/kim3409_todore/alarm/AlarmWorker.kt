package com.example.kim3409_todore.alarm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.kim3409_todore.R
import com.example.kim3409_todore.data.todo.TodoRepository
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class AlarmWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val application = applicationContext as Application
        val todoRepository = TodoRepository(application)
        val todos = todoRepository.getAllTodos()

        val current = System.currentTimeMillis()
        val currentDate = Date(current)
        Log.d("workmanager", "Current Date: $currentDate")
        Log.d("workmanager", "Todos: $todos")
        todos.forEach { todo ->
            todo.alarmDate?.let { alarmDate ->
                if (isSameDateAndTime(alarmDate, currentDate)) {
                    val notificationHelper = NotificationHelper(applicationContext)
                    val notification = notificationHelper.getChannelNotification(todo)
                        .setContentTitle(todo.title)
                        .setContentText(todo.description)
                        .setSmallIcon(R.drawable.ic_notification)
                        .build()
                    notificationHelper.getManager().notify(todo.id.toInt(), notification)
                }
            }
        }

        return Result.success()
    }

    private fun isSameDateAndTime(alarmDate: Date, currentTime: Date): Boolean {
        val calendarAlarm = Calendar.getInstance().apply { time = alarmDate }
        val calendarCurrent = Calendar.getInstance().apply { time = currentTime }

        return calendarAlarm.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR) &&
                calendarAlarm.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH) &&
                calendarAlarm.get(Calendar.DAY_OF_MONTH) == calendarCurrent.get(Calendar.DAY_OF_MONTH)
                &&
                calendarAlarm.get(Calendar.HOUR_OF_DAY) == calendarCurrent.get(Calendar.HOUR_OF_DAY) &&
                calendarAlarm.get(Calendar.MINUTE) == calendarCurrent.get(Calendar.MINUTE)
    }
}

fun schedulePeriodicWork(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<AlarmWorker>(10, TimeUnit.SECONDS)
        .build()
    Log.d("alar","schedulePeriodicWork 실행")
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "AlarmWorker",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )
}
