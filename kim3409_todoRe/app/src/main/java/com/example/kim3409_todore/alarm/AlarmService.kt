package com.example.kim3409_todore.alarm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import com.example.kim3409_todore.R
import com.example.kim3409_todore.data.todo.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.logging.Handler

class AlarmService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val application = applicationContext as Application
        val todoRepository = TodoRepository(application)

        CoroutineScope(Dispatchers.IO).launch {
            val todos = todoRepository.getAllTodos()

            val calendarCurrent = Calendar.getInstance()

            todos.forEach { todo ->
                todo.alarmDate?.let { alarmDate ->
                    val calendarAlarm = Calendar.getInstance().apply { time = alarmDate }

                    if (calendarAlarm.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR) &&
                        calendarAlarm.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH) &&
                        calendarAlarm.get(Calendar.DAY_OF_MONTH) == calendarCurrent.get(Calendar.DAY_OF_MONTH) &&
                        calendarAlarm.get(Calendar.HOUR_OF_DAY) == calendarCurrent.get(Calendar.HOUR_OF_DAY) &&
                        calendarAlarm.get(Calendar.MINUTE) == calendarCurrent.get(Calendar.MINUTE)) {

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
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}



//https://medium.com/wantedjobs/android-workmanager-%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EB%A7%A4%EC%9D%BC-%EC%95%8C%EB%A6%BC-%ED%91%9C%EC%8B%9C%ED%95%98%EA%B8%B0-d29b3a1a3c7b