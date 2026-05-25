package com.example.suggested_food.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.suggested_food.models.ReminderEntity
import com.example.suggested_food.receivers.ReminderReceiver

object ReminderScheduler {

    fun scheduleReminder(
        context: Context,
        reminder: ReminderEntity
    ) {

        try {

            val intent =
                Intent(
                    context,
                    ReminderReceiver::class.java
                ).apply {

                    putExtra(
                        "title",
                        reminder.title
                    )

                    putExtra(
                        "desc",
                        reminder.description ?: ""
                    )
                }

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    reminder.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                reminder.triggerTime,
                pendingIntent
            )

            Log.d(
                "SCHEDULE",
                "Reminder scheduled"
            )

        } catch (e: Exception) {

            Log.e(
                "SCHEDULE",
                "Schedule failed",
                e
            )
        }
    }
}