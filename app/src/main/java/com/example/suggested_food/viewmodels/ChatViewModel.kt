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

    /* -------------------- STATE -------------------- */

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

    private val _suggestedProducts = MutableStateFlow<List<ProductModel>>(emptyList())
    val suggestedProducts: StateFlow<List<ProductModel>> = _suggestedProducts

    private val _addToCartEvent = MutableSharedFlow<ProductModel>()
    val addToCartEvent = _addToCartEvent.asSharedFlow()

    private val geminiClient = GenerativeModel(
        modelName = "gemini-flash-latest",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun sendMessage(
        userMsg: String,
        productNames: List<String>,
        allProducts: List<ProductModel>
    ) {
        if (userMsg.isBlank() || _isLoading.value) return

        addUserMessage(userMsg)

        if (handleAddToCartIntent(userMsg)) return

        if (userMsg.length < 5) {
            addBotMessage(
                "👋 Bạn có thể mô tả triệu chứng rõ hơn một chút không?\n" +
                        "Ví dụ: đau đầu, ho, sốt, buồn nôn…"
            )
            return
        }

        val healthKeywords = listOf(
            "đau", "sốt", "ho", "viêm", "nhức", "buồn nôn",
            "chóng mặt", "đau bụng", "tiêu chảy", "cảm", "dị ứng"
        )

        if (healthKeywords.none { userMsg.contains(it, ignoreCase = true) }) {
            addBotMessage(
                "💊 Mình chuyên tư vấn thuốc nhé.\n" +
                        "Bạn hãy mô tả triệu chứng sức khỏe để mình hỗ trợ chính xác hơn."
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
            - Trả về đúng format:
            
            Những thuốc phù hợp với triệu chứng của bạn là:
            - TenThuoc1
            - TenThuoc2
            
            Sau đó giải thích ngắn gọn.
            Cuối cùng hỏi:
            "Bạn có muốn mình thêm thuốc nào vào giỏ hàng không?"
            
            Triệu chứng:
            "$userMsg"
        """.trimIndent()

        viewModelScope.launch {
            try {
                val response = geminiClient.generateContent(prompt)
                val botReply =
                    response.text ?: "Xin lỗi, mình chưa tìm được thuốc phù hợp."

                addBotMessage(botReply)
                extractSuggestedProducts(botReply, allProducts)

            } catch (e: Exception) {
                handleGeminiError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun extractSuggestedProducts(
        botText: String,
        allProducts: List<ProductModel>
    ) {
        val drugNames = botText.lines()
            .filter { it.startsWith("- ") }
            .map { it.removePrefix("- ").trim() }

        _suggestedProducts.value = allProducts.filter { product ->
            drugNames.any { it.equals(product.name, ignoreCase = true) }
        }
    }

    private fun handleAddToCartIntent(userMsg: String): Boolean {
        val lower = userMsg.lowercase()

        val matchedProduct = _suggestedProducts.value.firstOrNull {
            lower.contains(it.name.lowercase())
        }

        matchedProduct?.let {
            viewModelScope.launch {
                _addToCartEvent.emit(it)
                addBotMessage("✅ Mình đã thêm **${it.name}** vào giỏ hàng cho bạn rồi 🛒")
            }
            return true
        }

        return false
    }

    private fun handleGeminiError(e: Exception) {
        val message =
            when {
                e.message?.contains("Quota exceeded", true) == true ->
                    "⚠️ Hệ thống đang bận, bạn chờ vài giây rồi thử lại nhé ⏳"

                else ->
                    "⚠️ Có lỗi xảy ra, vui lòng thử lại sau"
            }

        addBotMessage(message)
    }

    private fun addUserMessage(text: String) {
        _messages.value += ChatMessage(text, true)
    }

    private fun addBotMessage(text: String) {
        _messages.value += ChatMessage(text, false)
    }
}
