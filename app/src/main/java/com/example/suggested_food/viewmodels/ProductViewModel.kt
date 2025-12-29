package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        _loading.value = true
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.toObject(ProductModel::class.java)?.copy(id = doc.id)
                }
                _products.value = list
                _loading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }

    // Lấy sản phẩm theo category
    fun getProductsByCategory(categoryId: String): List<ProductModel> {
        return _products.value.filter { it.categoryId == categoryId }
    }

    // Lọc sản phẩm còn hàng
    fun getAvailableProducts(): List<ProductModel> {
        return _products.value.filter { it.stock > 0 }
    }

    // Lấy sản phẩm đang sale
    fun getSaleProducts(): List<ProductModel> {
        return _products.value.filter { it.onSale }
    }

    // Lấy sản phẩm theo tên (search)
    fun searchProducts(query: String): List<ProductModel> {
        return _products.value.filter { it.name.contains(query, ignoreCase = true) }
    }
}
