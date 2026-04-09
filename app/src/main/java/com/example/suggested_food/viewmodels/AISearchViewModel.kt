package com.example.suggested_food.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.BuildConfig
import com.example.suggested_food.models.ProductModel
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AISearchViewModel(
    application: Application,
    private val productViewModel: com.example.suggested_food.viewmodels.ProductViewModel
) : AndroidViewModel(application) {

    private val TAG = "AISearchDebug"

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val geminiClient = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun searchDrug(userQuestion: String) {
        if (userQuestion.isBlank() || _isLoading.value) return

        Log.d(TAG, "QUESTION: $userQuestion")
        _isLoading.value = true
        _result.value = null

        val firstWord = userQuestion.trim().split(" ").firstOrNull()?.lowercase() ?: ""
        val matchedProducts: List<ProductModel> = productViewModel.getProductsByPrefix(firstWord)

        if (matchedProducts.isEmpty()) {
            _result.value = "Xin lỗi, thuốc này chưa có trong hệ thống."
            _isLoading.value = false
            return
        }

        val drugName = matchedProducts.first().name
        Log.d(TAG, "MATCHED DRUG: $drugName")
        val contextText = userQuestion.removePrefix(firstWord).trim()

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
            Người dùng hỏi: "$userQuestion"

            Thuốc có trong hệ thống với tên: "$drugName".
            Ngữ cảnh câu hỏi: "$contextText"

            Hãy trả lời chi tiết về thuốc này dựa trên dữ liệu y tế đáng tin cậy.
        """.trimIndent()

        viewModelScope.launch {
            try {
                val response = geminiClient.generateContent(prompt)
                val aiText = response.text ?: "Không tìm thấy thông tin phù hợp."
                _result.value = aiText
                Log.d(TAG, "AI RESPONSE: $aiText")
            } catch (e: Exception) {
                Log.e(TAG, "Gemini error: ${e.message}")
                _result.value = "⚠️ Có lỗi xảy ra. Vui lòng thử lại sau."
            } finally {
                _isLoading.value = false
            }
        }
    }
}