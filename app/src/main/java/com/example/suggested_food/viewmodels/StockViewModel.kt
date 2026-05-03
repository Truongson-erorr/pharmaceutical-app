package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StockViewModel : ViewModel() {

    companion object {
        const val LOW_STOCK_THRESHOLD = 10
    }

    fun totalStock(list: List<ProductModel>): Int {
        return list.sumOf { it.stock }
    }

    fun lowStock(list: List<ProductModel>): List<ProductModel> {
        return list.filter { it.stock <= LOW_STOCK_THRESHOLD && it.stock > 0 }
    }

    fun outOfStock(list: List<ProductModel>): List<ProductModel> {
        return list.filter { it.stock == 0 }
    }
}