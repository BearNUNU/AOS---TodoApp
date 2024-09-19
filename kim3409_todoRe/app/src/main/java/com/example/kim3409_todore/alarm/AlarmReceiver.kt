package com.example.kim3409_todore.alarm


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val serviceIntent = Intent(it, AlarmService::class.java)
            it.startService(serviceIntent)
        }
    }
}