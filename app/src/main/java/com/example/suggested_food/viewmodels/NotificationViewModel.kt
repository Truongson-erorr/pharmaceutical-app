package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.models.AppNotification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications
    private val _notifCount = MutableStateFlow(0)
    val notifCount: StateFlow<Int> = _notifCount

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadNotifications() {

        _isLoading.value = true

        db.collection("notifications")
            .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {

                    val list = snapshot.documents.mapNotNull {
                        it.toObject(AppNotification::class.java)
                    }

                    viewModelScope.launch {
                        delay(1500)
                        _notifications.value = list
                        _notifCount.value = list.size
                        _isLoading.value = false
                    }
                }
            }
    }
}