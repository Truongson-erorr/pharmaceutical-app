package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.BuildConfig
import com.example.suggested_food.models.ChatMessage
import com.example.suggested_food.models.ProductModel
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                text = "Xin chào 👋\nHãy nhập triệu chứng để mình gợi ý thuốc phù hợp.",
                isUser = false
            )
        )
    )

    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _suggestedProducts =
        MutableStateFlow<List<ProductModel>>(emptyList())

    val suggestedProducts: StateFlow<List<ProductModel>> =
        _suggestedProducts

    private val geminiClient = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun sendMessage(
        userMsg: String,
        productNames: List<String>,
        allProducts: List<ProductModel>
    ) {
        if (userMsg.isBlank() || _isLoading.value) return

        addUserMessage(userMsg)

        val healthKeywords = listOf(
            "đau", "sốt", "ho", "viêm", "nhức",
            "buồn nôn", "chóng mặt", "đau bụng",
            "tiêu chảy", "cảm", "dị ứng"
        )

        if (healthKeywords.none { userMsg.contains(it, true) }) {
            addBotMessage(
                "💊 Hãy mô tả triệu chứng nhé.\nVí dụ: đau đầu, ho, sốt..."
            )
            return
        }

        callGemini(userMsg, productNames, allProducts)
    }

    private fun callGemini(
        userMsg: String,
        productNames: List<String>,
        allProducts: List<ProductModel>
    ) {
        _isLoading.value = true

        val productListText =
            productNames.joinToString(", ")

        val prompt = """
        Bạn là AI hỗ trợ gợi ý thuốc KHÔNG kê đơn.
        
        Danh sách thuốc:
        $productListText
        
        Yêu cầu:
        - Chỉ chọn thuốc trong danh sách
        - Không bịa thuốc
        - Gợi ý 5–10 thuốc phù hợp
        
        Format:
        - TenThuoc: mô tả ngắn
        
        Triệu chứng:
        $userMsg
        """.trimIndent()

        viewModelScope.launch {
            try {

                val response = geminiClient.generateContent(prompt)
                val botReply =
                    response.text ?: "Không tìm thấy thuốc phù hợp."

                addBotMessage(botReply)

                extractProducts(botReply, allProducts)

            } catch (e: Exception) {
                addBotMessage("⚠️ Hệ thống đang bận, thử lại sau.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun extractProducts(
        botText: String,
        allProducts: List<ProductModel>
    ) {

        val suggestedNames = botText.lines()
            .mapNotNull { line ->
                if (line.trim().startsWith("- "))
                    line.substringAfter("- ")
                        .substringBefore(":")
                        .trim()
                else null
            }

        val matchedProducts = allProducts.filter { product ->
            suggestedNames.any {
                it.equals(product.name, true)
            }
        }

        _suggestedProducts.value = matchedProducts
    }

    private fun addUserMessage(text: String) {
        _messages.value += ChatMessage(text, true)
    }

    private fun addBotMessage(text: String) {
        _messages.value += ChatMessage(text, false)
    }
}