package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.BuildConfig
import com.example.suggested_food.models.ChatMessage
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                text = "Xin chào 👋\nTôi có thể giúp gì cho bạn hôm nay?",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val geminiClient = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun sendMessage(
        userMsg: String,
        productNames: List<String>
    ) {
        if (userMsg.isBlank() || _isLoading.value) return

        _messages.value += ChatMessage(userMsg, true)
        _isLoading.value = true

        val productListText =
            if (productNames.isNotEmpty())
                productNames.joinToString(", ")
            else
                "Không có thuốc nào trong hệ thống"

        val prompt = """
        Bạn là trợ lý tư vấn dược.
        
        Danh sách thuốc hiện có:
        $productListText
        
        YÊU CẦU BẮT BUỘC:
        - CHỈ chọn thuốc trong danh sách trên
        - KHÔNG bịa thuốc mới
        - Nếu không có thuốc phù hợp, nói rõ
        - Trả về theo format:
        
        Những thuốc phù hợp với triệu chứng của bạn là:
        - TenThuoc1
        - TenThuoc2
        
        Sau đó giải thích ngắn gọn.
        Kèm cảnh báo hỏi dược sĩ/bác sĩ.
        
        Triệu chứng người dùng:
        "$userMsg"
        """.trimIndent()

        viewModelScope.launch {
            try {
                val response = geminiClient.generateContent(prompt)
                val botReply =
                    response.text ?: "Xin lỗi, tôi chưa tìm được thuốc phù hợp."

                _messages.value += ChatMessage(botReply, false)

            } catch (e: Exception) {
                _messages.value += ChatMessage(
                    "⚠️ Lỗi hệ thống: ${e.message}",
                    false
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
