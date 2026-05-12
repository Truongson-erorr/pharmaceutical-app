package com.example.suggested_food.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ExportReceipt
import com.example.suggested_food.models.Patient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExportViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val patientRef = db.collection("patients")

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
                snapshot?.documents?.let {
                    _exportList.value =
                        it.mapNotNull { doc ->
                            doc.toObject(ExportReceipt::class.java)
                        }
                }
            }
    }

    private fun upsertPatient(receipt: ExportReceipt) {

        val customerName = receipt.customer
        val phone = receipt.customerPhone

        if (customerName.isBlank() || phone.isBlank()) return

        val docRef = patientRef.document(phone)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(docRef)

            if (snapshot.exists()) {

                val patient = snapshot.toObject(Patient::class.java)!!

                transaction.update(
                    docRef,
                    mapOf(
                        "name" to customerName,
                        "totalOrders" to patient.totalOrders + 1,
                        "totalSpent" to patient.totalSpent + receipt.totalPrice,
                        "lastVisit" to System.currentTimeMillis(),
                        "updatedAt" to System.currentTimeMillis()
                    )
                )

            } else {

                val newPatient = Patient(
                    id = phone,
                    name = customerName,
                    phone = phone,
                    totalOrders = 1,
                    totalSpent = receipt.totalPrice.toLong(),
                    lastVisit = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis()
                )

                transaction.set(docRef, newPatient)
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

        val productRef = db.collection("products").document(receipt.productId)
        val exportRef = db.collection("export_receipts").document(receipt.id)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(productRef)

            val currentStock = snapshot.getLong("stock")?.toInt() ?: 0

            if (receipt.quantity > currentStock) {
                throw Exception("Không đủ tồn kho để xuất")
            }

            val newStock = currentStock - receipt.quantity

            transaction.update(productRef, "stock", newStock)
            transaction.set(exportRef, receipt)

            newStock
        }
            .addOnSuccessListener { newStock ->

                Log.d("EXPORT", "Transaction success, stock=$newStock")

                _loading.value = false
                _saveState.value = true

                upsertPatient(receipt)

                val exportNotifRef =
                    db.collection("notifications").document()

                val exportNotification = mapOf(
                    "id" to exportNotifRef.id,
                    "title" to "Xuất kho thành công",
                    "message" to "Đã xuất ${receipt.quantity} ${receipt.productName}",
                    "time" to System.currentTimeMillis(),
                    "type" to "EXPORT"
                )

                exportNotifRef.set(exportNotification)
                    .addOnSuccessListener {

                        Log.d("EXPORT", "EXPORT notification saved OK")

                        if (newStock < 10) {

                            Log.d("EXPORT", "WARNING condition TRUE")

                            val warningNotifRef =
                                db.collection("notifications").document()

                            val warningNotification = mapOf(
                                "id" to warningNotifRef.id,
                                "title" to "Cảnh báo tồn kho",
                                "message" to "${receipt.productName} còn $newStock sản phẩm",
                                "time" to System.currentTimeMillis(),
                                "type" to "WARNING"
                            )

                            warningNotifRef.set(warningNotification)
                                .addOnSuccessListener {
                                    Log.d("EXPORT", "WARNING saved OK")
                                }
                                .addOnFailureListener {
                                    Log.e("EXPORT", "WARNING FAIL: ${it.message}")
                                }
                        } else {
                            Log.d("EXPORT", "No WARNING needed")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("EXPORT", "EXPORT NOTIF FAIL: ${it.message}")
                    }
            }
            .addOnFailureListener { e ->

                _loading.value = false
                _saveState.value = false
                _errorMessage.value = e.message ?: "Có lỗi xảy ra"

                Log.e("EXPORT", "TRANSACTION FAIL: ${e.message}")

                val errorNotifRef =
                    db.collection("notifications").document()

                val errorNotification = mapOf<String, Any>(
                    "id" to errorNotifRef.id,
                    "title" to "Xuất kho thất bại",
                    "message" to "Thuốc: ${receipt.productName} - ${e.message ?: "Không đủ tồn kho"}",
                    "time" to System.currentTimeMillis(),
                    "type" to "WARNING"
                )

                errorNotifRef.set(errorNotification)
                    .addOnSuccessListener {
                        Log.d("EXPORT", "ERROR notification saved")
                    }
                    .addOnFailureListener {
                        Log.e("EXPORT", "ERROR notif FAIL: ${it.message}")
                    }
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