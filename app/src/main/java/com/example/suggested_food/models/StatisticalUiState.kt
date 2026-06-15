package com.example.suggested_food.models

data class StatisticalUiState(
    val importList: List<ImportReceipt> = emptyList(),
    val exportList: List<ExportReceipt> = emptyList(),

    val totalImportAmount: Int = 0,
    val totalExportAmount: Int = 0,
    val totalProfit: Int = 0,
    val totalImportCount: Int = 0,
    val totalExportCount: Int = 0,

    val monthlyImportAmounts: Map<String, Int> = emptyMap(),
    val monthlyExportAmounts: Map<String, Int> = emptyMap(),

    val topImported5: List<Pair<String, Int>> = emptyList(),
    val topExported5: List<Pair<String, Int>> = emptyList(),

    val allImported: List<Pair<String, Int>> = emptyList(),
    val allExported: List<Pair<String, Int>> = emptyList()
)