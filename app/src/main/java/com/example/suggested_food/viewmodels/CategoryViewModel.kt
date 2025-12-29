package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        _loading.value = true
        firestore.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.toObject(CategoryModel::class.java)?.copy(id = doc.id)
                }
                _categories.value = list
                _loading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }
}
