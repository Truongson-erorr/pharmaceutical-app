package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DrugLookupViewModel : ViewModel() {

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val geminiClient = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun searchDrug(drugName: String) {
        if (drugName.isBlank() || _isLoading.value) return

        _isLoading.value = true
        _result.value = null

        val prompt = """
            Bạn là trợ lý AI tra cứu thông tin thuốc.
         
            Hãy trả lời chi tiết về thuốc: "$drugName"
            
            Bao gồm:
            - Công dụng
            - Liều dùng tham khảo
            - Chống chỉ định
            - Tác dụng phụ
            - Lưu ý an toàn
            
            Không kê đơn.
            Thông tin chỉ mang tính tham khảo.
        """.trimIndent()

        viewModelScope.launch {
            try {
                val response = geminiClient.generateContent(prompt)
                _result.value =
                    response.text ?: "Không tìm thấy thông tin phù hợp."
            } catch (e: Exception) {
                _result.value = "⚠️ Có lỗi xảy ra. Vui lòng thử lại sau."
            } finally {
                _isLoading.value = false
            }
        }
    }
}