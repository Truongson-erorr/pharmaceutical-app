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

    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _selectedReceipt =
        MutableStateFlow<ImportReceipt?>(null)
    val selectedReceipt: StateFlow<ImportReceipt?> = _selectedReceipt

    private val _importList =
        MutableStateFlow<List<ImportReceipt>>(emptyList())
    val importList: StateFlow<List<ImportReceipt>> = _importList

    private val _users = MutableStateFlow<Map<String, String>>(emptyMap())
    val users: StateFlow<Map<String, String>> = _users

    fun loadUsers() {
        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { snapshot ->

                val map = snapshot.documents.associate { doc ->
                    doc.id to (doc.getString("name") ?: "Unknown")
                }

                _users.value = map
            }
    }

    fun loadAllImports() {
        db.collection("import_receipts")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _importList.value =
                        snapshot.documents.mapNotNull {
                            it.toObject(ImportReceipt::class.java)
                        }
                }
            }
    }

    fun saveImportReceipt(receipt: ImportReceipt) {

        _loading.value = true
        _errorMessage.value = null
        _saveState.value = null

        val productRef =
            db.collection("products").document(receipt.productId)

        val importRef =
            db.collection("import_receipts").document(receipt.id)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(productRef)

            val currentStock =
                snapshot.getLong("stock")?.toInt() ?: 0

            val newStock = currentStock + receipt.quantity

            transaction.update(productRef, "stock", newStock)
            transaction.set(importRef, receipt)

            newStock
        }
            .addOnSuccessListener { newStock ->

                _loading.value = false
                _saveState.value = true

                val notifRef =
                    db.collection("notifications").document()

                val notification = mapOf(
                    "id" to notifRef.id,
                    "title" to "Nhập kho thành công",
                    "message" to "Đã nhập ${receipt.quantity} ${receipt.productName}",
                    "time" to System.currentTimeMillis(),
                    "type" to "IMPORT"
                )

                notifRef.set(notification)

                val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                val userId = user?.uid ?: "unknown"
                val userName = user?.displayName ?: "Unknown"

                db.collection("activity_logs")
                    .add(
                        mapOf(
                            "type" to "IMPORT",
                            "title" to "Nhập kho",
                            "message" to "Nhập ${receipt.quantity} ${receipt.productName}",
                            "productId" to receipt.productId,
                            "productName" to receipt.productName,
                            "quantity" to receipt.quantity,
                            "userId" to userId,
                            "userName" to userName,
                            "timestamp" to System.currentTimeMillis()
                        )
                    )
            }
            .addOnFailureListener {
                _loading.value = false
                _errorMessage.value = it.message ?: "Có lỗi xảy ra"
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
                _errorMessage.value = it.message
            }
    }


    fun clearState() {
        _saveState.value = null
        _errorMessage.value = null
    }
}