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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _selectedReceipt = MutableStateFlow<ExportReceipt?>(null)
    val selectedReceipt: StateFlow<ExportReceipt?> = _selectedReceipt

    private val _exportList = MutableStateFlow<List<ExportReceipt>>(emptyList())
    val exportList: StateFlow<List<ExportReceipt>> = _exportList

    fun clearState() {
        _saveState.value = null
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun loadAllExports() {
        db.collection("export_receipts")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _exportList.value =
                        snapshot.documents.mapNotNull {
                            it.toObject(ExportReceipt::class.java)
                        }
                }
            }
    }

    fun saveExportReceipt(receipt: ExportReceipt) {

        _loading.value = true
        _errorMessage.value = null
        _saveState.value = null

        if (receipt.quantity <= 0) {
            _loading.value = false
            _errorMessage.value = "Số lượng xuất phải lớn hơn 0"
            return
        }

        if (receipt.productId.isBlank()) {
            _loading.value = false
            _errorMessage.value = "Vui lòng chọn thuốc"
            return
        }

        val productRef =
            db.collection("products").document(receipt.productId)

        val exportRef =
            db.collection("export_receipts").document(receipt.id)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(productRef)

            val currentStock =
                snapshot.getLong("stock")?.toInt() ?: 0

            if (receipt.quantity > currentStock) {
                throw Exception("Không đủ tồn kho để xuất")
            }

            val newStock = currentStock - receipt.quantity

            transaction.update(productRef, "stock", newStock)
            transaction.set(exportRef, receipt)

        }.addOnSuccessListener {
            _loading.value = false
            _saveState.value = true
        }.addOnFailureListener { e ->
            _loading.value = false
            _saveState.value = false
            _errorMessage.value = e.message ?: "Có lỗi xảy ra"
        }
    }

    fun loadExportReceipt(receiptId: String) {

        _loading.value = true
        _errorMessage.value = null

        db.collection("export_receipts")
            .document(receiptId)
            .get()
            .addOnSuccessListener {

                _selectedReceipt.value =
                    it.toObject(ExportReceipt::class.java)

                _loading.value = false
            }
            .addOnFailureListener { e ->
                _loading.value = false
                _errorMessage.value = e.message
            }
    }
}