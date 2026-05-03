package com.example.suggested_food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.models.ImportReceipt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImportViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun saveImportReceipt(receipt: ImportReceipt) {

        _loading.value = true

        val productRef =
            db.collection("products")
                .document(receipt.productId)

        val importRef =
            db.collection("import_receipts")
                .document(receipt.id)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(productRef)

            val currentStock =
                snapshot.getLong("stock")?.toInt() ?: 0

            val newStock = currentStock + receipt.quantity

            transaction.update(productRef, "stock", newStock)

            transaction.set(importRef, receipt)

        }.addOnSuccessListener {
            _loading.value = false
            _saveState.value = true
        }.addOnFailureListener {
            _loading.value = false
            _saveState.value = false
        }
    }
}