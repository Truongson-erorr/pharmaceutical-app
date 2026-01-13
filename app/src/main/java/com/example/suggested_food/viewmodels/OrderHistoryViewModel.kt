package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.OrderModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderHistoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _orders = MutableStateFlow<List<OrderModel>>(emptyList())
    val orders: StateFlow<List<OrderModel>> = _orders

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun loadOrders(userId: String) {
        _loading.value = true

        firestore.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)
                        ?.copy(id = doc.id)
                }
                _orders.value = list
                _loading.value = false
            }
            .addOnFailureListener {
                _loading.value = false
            }
    }
}
