package com.example.suggested_food.viewmodel

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ImportReceipt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImportViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _selectedReceipt =
        MutableStateFlow<ImportReceipt?>(null)

    val selectedReceipt: StateFlow<ImportReceipt?> =
        _selectedReceipt

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

    fun loadImportReceipt(receiptId: String) {

        _loading.value = true

        db.collection("import_receipts")
            .document(receiptId)
            .get()
            .addOnSuccessListener { snapshot ->

                _selectedReceipt.value =
                    snapshot.toObject(ImportReceipt::class.java)

                _loading.value = false
            }
            .addOnFailureListener {
                _loading.value = false
            }
    }
}