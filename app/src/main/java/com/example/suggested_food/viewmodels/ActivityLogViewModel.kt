package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ActivityLog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ActivityLogViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _logs = MutableStateFlow<List<ActivityLog>>(emptyList())
    val logs: StateFlow<List<ActivityLog>> = _logs

    private val _users = MutableStateFlow<Map<String, String>>(emptyMap())
    val users: StateFlow<Map<String, String>> = _users

    fun loadUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->

                val map = snapshot.documents.associate { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    doc.id to name
                }

                _users.value = map
            }
    }

    fun loadLogs() {
        db.collection("activity_logs")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                _logs.value = snapshot?.documents?.mapNotNull {
                    it.toObject(ActivityLog::class.java)?.copy(id = it.id)
                } ?: emptyList()
            }
    }
}