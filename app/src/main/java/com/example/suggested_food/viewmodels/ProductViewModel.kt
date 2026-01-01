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

    private val _productDetail = MutableStateFlow<ProductModel?>(null)
    val productDetail: StateFlow<ProductModel?> = _productDetail

    private val _detailLoading = MutableStateFlow(false)
    val detailLoading: StateFlow<Boolean> = _detailLoading

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

    fun fetchProductById(productId: String) {
        _detailLoading.value = true
        firestore.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { doc ->
                _productDetail.value =
                    doc.toObject(ProductModel::class.java)?.copy(id = doc.id)
                _detailLoading.value = false
            }
            .addOnFailureListener {
                _detailLoading.value = false
            }
    }

    fun getProductsByCategory(categoryId: String): List<ProductModel> {
        return _products.value.filter { it.categoryId == categoryId }
    }

    fun getAvailableProducts(): List<ProductModel> {
        return _products.value.filter { it.stock > 0 }
    }

    fun getSaleProducts(): List<ProductModel> {
        return _products.value.filter { it.onSale }
    }

    fun searchProducts(query: String): List<ProductModel> {
        return _products.value.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
}
