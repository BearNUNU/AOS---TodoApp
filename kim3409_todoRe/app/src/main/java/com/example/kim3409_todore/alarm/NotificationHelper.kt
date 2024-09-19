package com.example.kim3409_todore.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.kim3409_todore.R
import com.example.kim3409_todore.TodoDetailActivity
import com.example.kim3409_todore.data.todo.TodoEntity

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private val channelId = "todo_channel"
    private val channelNm = "todo_nm"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    private fun createChannel() {
        val channel = NotificationChannel(channelId, channelNm, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = Color.GRAY
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getChannelNotification(todo: TodoEntity): NotificationCompat.Builder {
        val intent = Intent(applicationContext, TodoDetailActivity::class.java).apply {
            putExtra("id", todo.id)
            putExtra("isEdit", false)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            todo.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(todo.title)
            .setContentText(todo.description)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)// 클릭시 알림 사라짐
    }
}