package com.example.suggested_food.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.suggested_food.notifications.NotificationHelper

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        Log.d(
            "RECEIVER",
            "TRIGGERED"
        )

        val title =
            intent.getStringExtra("title") ?: ""

        val desc =
            intent.getStringExtra("desc") ?: ""

        NotificationHelper.showNotification(
            context,
            title,
            desc
        )
    }
}