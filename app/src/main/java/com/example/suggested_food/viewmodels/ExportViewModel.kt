package com.example.suggested_food.viewmodel

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ExportReceipt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExportViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun saveExportReceipt(receipt: ExportReceipt) {
        _loading.value = true

        val productRef =
            db.collection("products").document(receipt.productId)

        val exportRef =
            db.collection("export_receipts").document(receipt.id)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(productRef)

            val currentStock =
                snapshot.getLong("stock")?.toInt() ?: 0

            if (receipt.quantity > currentStock) {
                throw Exception("Không đủ tồn kho")
            }

            val newStock = currentStock - receipt.quantity

            transaction.update(productRef, "stock", newStock)
            transaction.set(exportRef, receipt)

        }.addOnSuccessListener {
            _loading.value = false
            _saveState.value = true
        }.addOnFailureListener {
            _loading.value = false
            _saveState.value = false
        }
    }
}