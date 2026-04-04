package com.example.suggested_food.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.models.ProductData
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class DrugLookupViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "DrugLookupDebug"

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val medicineList: List<ProductData> by lazy { loadMedicineCsv() }

    private fun loadMedicineCsv(): List<ProductData> {
        val list = mutableListOf<ProductData>()
        try {
            val inputStream = getApplication<Application>().assets.open(
                "models_ai/Medicine_Detail_with_Predictions.csv"
            )

            val parser = CSVParserBuilder().withSeparator(',').build()
            val reader = CSVReaderBuilder(InputStreamReader(inputStream))
                .withCSVParser(parser)
                .build()

            val rows = reader.readAll()
            rows.drop(1).forEachIndexed { index, cols ->
                if (cols.size >= 9) {
                    list.add(
                        ProductData(
                            id = index,
                            name = cols[0].trim(),
                            composition = cols[1].trim(),
                            uses = cols[2].trim(),
                            sideEffects = cols[3].trim(),
                            imageUrl = cols[4].trim(),
                            manufacturer = cols[5].trim(),
                            excellentReview = cols[6].trim(),
                            averageReview = cols[7].trim(),
                            poorReview = cols[8].trim()
                        )
                    )
                } else {
                    Log.e(TAG, "❌ Wrong column count at line $index: ${cols.size}")
                }
            }
            Log.d(TAG, "✅ Loaded ${list.size} medicines from CSV")
            list.forEach { Log.d(TAG, "Medicine loaded: '${it.name}'") }

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading CSV: ${e.message}")
        }
        return list
    }

    fun searchDrug(query: String) {
        if (query.isBlank() || _isLoading.value) return

        _isLoading.value = true
        _result.value = null

        viewModelScope.launch(Dispatchers.Default) {
            val keyword = query.trim().lowercase()
            Log.d(TAG, "Searching for: '$keyword'")

            val matched = medicineList.filter { it.name.trim().lowercase().contains(keyword) }
            Log.d(TAG, "Found ${matched.size} matches")

            if (matched.isEmpty()) {
                _result.value = "Xin lỗi, thuốc này chưa có trong hệ thống."
            } else {
                val drug = matched.first()

                val response = buildString {
                    appendLine(drug.imageUrl) // đường dẫn hình ảnh
                    appendLine("## Tên thuốc")
                    appendLine(drug.name)
                    appendLine()
                    appendLine("## Thành phần")
                    appendLine(drug.composition)
                    appendLine()
                    appendLine("## Công dụng")
                    appendLine(drug.uses)
                    appendLine()
                    appendLine("## Tác dụng phụ")
                    appendLine(drug.sideEffects)
                    appendLine()
                    appendLine("## Nhà sản xuất")
                    appendLine(drug.manufacturer)
                    appendLine()
                    appendLine("## Đánh giá")
                    appendLine("Excellent: ${drug.excellentReview}, Average: ${drug.averageReview}, Poor: ${drug.poorReview}")
                }

                _result.value = response
                Log.d(TAG, "✅ Drug info loaded for: '${drug.name}'")
            }

            _isLoading.value = false
        }
    }
}