package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ExportReceipt
import com.example.suggested_food.models.ImportReceipt
import com.example.suggested_food.models.StatisticalUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StatisticalViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _uiState =
        MutableStateFlow(StatisticalUiState())

    val uiState: StateFlow<StatisticalUiState>
            = _uiState.asStateFlow()

    init {
        loadAllImports()
        loadAllExports()
    }

    private fun loadAllImports() {

        db.collection("import_receipts")
            .addSnapshotListener { snapshot, _ ->

                val imports =
                    snapshot?.documents?.mapNotNull {
                        it.toObject(ImportReceipt::class.java)
                    } ?: emptyList()

                _uiState.update {
                    it.copy(importList = imports)
                }

                calculateSummary()
            }
    }

    private fun loadAllExports() {

        db.collection("export_receipts")
            .addSnapshotListener { snapshot, _ ->

                val exports =
                    snapshot?.documents?.mapNotNull {
                        it.toObject(ExportReceipt::class.java)
                    } ?: emptyList()

                _uiState.update {
                    it.copy(exportList = exports)
                }

                calculateSummary()
            }
    }

    private fun calculateSummary() {

        val imports = _uiState.value.importList
        val exports = _uiState.value.exportList

        val totalImportAmount =
            imports.sumOf { it.totalPrice }

        val totalExportAmount =
            exports.sumOf { it.totalPrice }

        val totalImportCount =
            imports.size

        val totalExportCount =
            exports.size

        val topImported5 = imports
            .groupBy { it.productName }
            .mapValues { entry ->
                entry.value.sumOf { it.quantity }
            }
            .toList()
            .sortedByDescending { it.second }
            .take(5)

        val topExported5 = exports
            .groupBy { it.productName }
            .mapValues { entry ->
                entry.value.sumOf { it.quantity }
            }
            .toList()
            .sortedByDescending { it.second }
            .take(5)

        val monthlyImports =
            imports
                .groupBy {
                    val cal = java.util.Calendar.getInstance()
                    cal.timeInMillis = it.date
                    "${cal.get(java.util.Calendar.MONTH) + 1}/${cal.get(java.util.Calendar.YEAR)}"
                }
                .mapValues { entry ->
                    entry.value.sumOf { receipt ->
                        receipt.totalPrice
                    }
                }

        val monthlyExports =
            exports
                .groupBy {
                    val cal = java.util.Calendar.getInstance()
                    cal.timeInMillis = it.date
                    "${cal.get(java.util.Calendar.MONTH) + 1}/${cal.get(java.util.Calendar.YEAR)}"
                }
                .mapValues { entry ->
                    entry.value.sumOf { receipt ->
                        receipt.totalPrice
                    }
                }

        _uiState.update {
            it.copy(
                totalImportAmount = totalImportAmount,
                totalExportAmount = totalExportAmount,
                totalProfit =
                totalExportAmount - totalImportAmount,

                totalImportCount = totalImportCount,
                totalExportCount = totalExportCount,

                topImported5 = topImported5,
                topExported5 = topExported5,

                monthlyImportAmounts = monthlyImports,
                monthlyExportAmounts = monthlyExports
            )
        }
    }
}