package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ActivityLog
import com.example.suggested_food.models.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InventoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    init {
        loadProducts()
    }

    fun loadProducts() {
        firestore.collection("products")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    _products.value = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)?.copy(id = doc.id)
                    }
                }
            }
    }

    private fun logActivity(log: ActivityLog) {
        db.collection("activity_logs")
            .add(log)
    }

    fun addProduct(
        product: ProductModel,
        userId: String,
        userName: String,
        onDone: () -> Unit = {}
    ) {
        firestore.collection("products")
            .add(product)
            .addOnSuccessListener { doc ->

                logActivity(
                    ActivityLog(
                        type = "PRODUCT_ADD",
                        title = "Thêm thuốc",
                        message = "Đã thêm thuốc: ${product.name}",
                        productId = doc.id,
                        productName = product.name,
                        userId = userId,
                        userName = userName
                    )
                )
                onDone()
            }
    }

    fun updateProduct(
        product: ProductModel,
        userId: String,
        userName: String,
        onDone: () -> Unit = {}
    ) {
        firestore.collection("products")
            .document(product.id)
            .set(product)
            .addOnSuccessListener {

                logActivity(
                    ActivityLog(
                        type = "PRODUCT_UPDATE",
                        title = "Cập nhật thuốc",
                        message = "Đã cập nhật thuốc: ${product.name}",
                        productId = product.id,
                        productName = product.name,
                        userId = userId,
                        userName = userName
                    )
                )
                onDone()
            }
    }

    fun deleteProduct(
        product: ProductModel,
        userId: String,
        userName: String,
        onDone: () -> Unit = {}
    ) {
        firestore.collection("products")
            .document(product.id)
            .delete()
            .addOnSuccessListener {

                logActivity(
                    ActivityLog(
                        type = "PRODUCT_DELETE",
                        title = "Xóa thuốc",
                        message = "Đã xóa thuốc: ${product.name}",
                        productId = product.id,
                        productName = product.name,
                        userId = userId,
                        userName = userName
                    )
                )
                onDone()
            }
    }

    fun search(query: String): List<ProductModel> {
        return _products.value.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
}