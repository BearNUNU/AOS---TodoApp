package com.example.kim3409_todore.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmScheduler {
    companion object {
        fun scheduleAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val interval = 60000L

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                interval,
                pendingIntent
            )
        }
    }
}