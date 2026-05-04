package com.example.suggested_food.viewmodel

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.StockHistoryItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StockHistoryViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _histories =
        MutableStateFlow<List<StockHistoryItem>>(emptyList())
    val histories: StateFlow<List<StockHistoryItem>> = _histories

    init {
        loadHistory()
    }

    private fun loadHistory() {

        val result = mutableListOf<StockHistoryItem>()

        db.collection("import_receipts")
            .get()
            .addOnSuccessListener { importDocs ->

                importDocs.forEach { doc ->

                    result.add(
                        StockHistoryItem(
                            id = doc.id,
                            type = "IMPORT",
                            productName = doc.getString("productName") ?: "",
                            quantity =
                            (doc.get("quantity") as? Number)?.toInt() ?: 0,
                            totalPrice =
                            (doc.get("totalPrice") as? Number)?.toInt() ?: 0,
                            date = doc.getLong("date") ?: 0L
                        )
                    )
                }

                loadExport(result)
            }
            .addOnFailureListener {
                _histories.value = emptyList()
            }
    }

    private fun loadExport(
        result: MutableList<StockHistoryItem>
    ) {

        db.collection("export_receipts")
            .get()
            .addOnSuccessListener { exportDocs ->

                exportDocs.forEach { doc ->

                    result.add(
                        StockHistoryItem(
                            id = doc.id,
                            type = "EXPORT",
                            productName = doc.getString("productName") ?: "",
                            quantity =
                            (doc.get("quantity") as? Number)?.toInt() ?: 0,
                            totalPrice =
                            (doc.get("totalPrice") as? Number)?.toInt() ?: 0,
                            date = doc.getLong("date") ?: 0L
                        )
                    )
                }

                _histories.value =
                    result.sortedByDescending { it.date }
            }
            .addOnFailureListener {
                _histories.value =
                    result.sortedByDescending { it.date }
            }
    }
}