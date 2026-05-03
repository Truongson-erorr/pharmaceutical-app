package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InventoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

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

    fun addProduct(product: ProductModel, onDone: () -> Unit = {}) {
        firestore.collection("products")
            .add(product)
            .addOnSuccessListener { onDone() }
    }

    fun updateProduct(product: ProductModel, onDone: () -> Unit = {}) {
        firestore.collection("products")
            .document(product.id)
            .set(product)
            .addOnSuccessListener { onDone() }
    }

    fun deleteProduct(productId: String, onDone: () -> Unit = {}) {
        firestore.collection("products")
            .document(productId)
            .delete()
            .addOnSuccessListener { onDone() }
    }

    fun search(query: String): List<ProductModel> {
        return _products.value.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
}