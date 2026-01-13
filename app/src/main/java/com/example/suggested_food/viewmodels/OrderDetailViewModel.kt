package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.OrderModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderDetailViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _order = MutableStateFlow<OrderModel?>(null)
    val order: StateFlow<OrderModel?> = _order

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun loadOrder(orderId: String) {
        firestore.collection("orders")
            .document(orderId)
            .get()
            .addOnSuccessListener { snapshot ->
                _order.value = snapshot.toObject(OrderModel::class.java)
                _loading.value = false
            }
            .addOnFailureListener {
                _loading.value = false
            }
    }
}
