package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.AppNotification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications

    fun loadNotifications() {
        db.collection("notifications")
            .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _notifications.value =
                        snapshot.documents.mapNotNull {
                            it.toObject(AppNotification::class.java)
                        }
                }
            }
    }
}