package com.example.suggested_food.viewmodel

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ReminderEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReminderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val reminderRef = db.collection("reminders")

    private val _reminders = MutableStateFlow<List<ReminderEntity>>(emptyList())
    val reminders: StateFlow<List<ReminderEntity>> = _reminders

    init {
        listenReminders()
    }

    private fun listenReminders() {
        reminderRef.addSnapshotListener { snapshot, _ ->
            val list = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(ReminderEntity::class.java)?.copy(id = doc.id)
            } ?: emptyList()

            _reminders.value = list
        }
    }

    fun addReminder(reminder: ReminderEntity) {
        val doc = reminderRef.document()
        val data = reminder.copy(id = doc.id)
        doc.set(data)
    }

    fun deleteReminder(id: String) {
        reminderRef.document(id).delete()
    }

    fun markDone(id: String) {
        reminderRef.document(id)
            .update("isDone", true)
    }

    fun toggleEnabled(id: String) {
        val ref = reminderRef.document(id)

        ref.get().addOnSuccessListener {
            val current = it.getBoolean("isEnabled") ?: true
            ref.update("isEnabled", !current)
        }
    }

    fun getByType(type: String): List<ReminderEntity> {
        return _reminders.value.filter { it.actionType == type }
    }

    fun getActive(): List<ReminderEntity> {
        return _reminders.value.filter { it.isEnabled && !it.isDone }
    }
}