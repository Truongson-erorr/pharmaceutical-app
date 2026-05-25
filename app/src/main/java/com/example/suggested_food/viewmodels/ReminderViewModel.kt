package com.example.suggested_food.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ReminderEntity
import com.example.suggested_food.notifications.ReminderScheduler
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReminderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val reminderRef =
        db.collection("reminder")

    private val _reminders =
        MutableStateFlow<List<ReminderEntity>>(emptyList())

    val reminders:
            StateFlow<List<ReminderEntity>> = _reminders

    init {
        listenReminders()
    }

    private fun listenReminders() {
        reminderRef.addSnapshotListener { snapshot, _ ->
            val list =
                snapshot?.documents?.map { doc ->

                    ReminderEntity(
                        id = doc.id,
                        title =
                        doc.getString("title") ?: "",
                        description =
                        doc.getString("description"),
                        triggerTime =
                        doc.getLong("triggerTime") ?: 0L,
                        repeatInterval =
                        (
                                doc.getLong(
                                    "repeatInterval"
                                ) ?: 0L
                                ).toInt(),

                        medicineId =
                        doc.getLong("medicineId"),
                        medicineName =
                        doc.getString("medicineName"),
                        actionType =
                        doc.getString("actionType")
                            ?: "CUSTOM",
                        isEnabled =
                        doc.getBoolean("isEnabled")
                            ?: true,
                        isDone =
                        doc.getBoolean("isDone")
                            ?: false,
                        createdAt =
                        doc.getLong("createdAt") ?: 0L
                    )

                } ?: emptyList()
            _reminders.value = list
        }
    }

    fun addReminder(
        context: Context,
        reminder: ReminderEntity
    ) {
        val doc = reminderRef.document()
        val data =
            reminder.copy(id = doc.id)
        doc.set(data)
            .addOnSuccessListener {
                ReminderScheduler.scheduleReminder(
                    context,
                    data
                )
                Log.d(
                    "REMINDER",
                    "SAVE OK ${doc.id}"
                )
                Log.d(
                    "SCHEDULE",
                    "Reminder Scheduled"
                )
            }
            .addOnFailureListener {
                Log.e(
                    "REMINDER",
                    "SAVE FAIL",
                    it
                )
            }
    }

    fun deleteReminder(id: String) {
        reminderRef.document(id).delete()
    }

    fun markDone(id: String) {
        reminderRef.document(id)
            .update("isDone", true)
            .addOnSuccessListener {
                Log.d(
                    "REMINDER",
                    "DONE UPDATED"
                )
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}