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
                text = "Xin chào 👋\nMình là AI gợi ý các loại thuốc không kê đơn dựa trên triệu chứng bạn cung cấp.\nThông tin chỉ mang tính tham khảo, bạn nên hỏi thêm dược sĩ hoặc bác sĩ khi cần.",
                isUser = false
            )
        )
    )

    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _addToCartEvent = MutableSharedFlow<ProductModel>()
    val addToCartEvent = _addToCartEvent.asSharedFlow()

    private val _pendingSelectProducts =
        MutableStateFlow<List<ProductModel>>(emptyList())

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

        if (handleSelectByNumber(userMsg)) return

        val healthKeywords = listOf(
            "đau", "sốt", "ho", "viêm", "nhức", "buồn nôn",
            "chóng mặt", "đau bụng", "tiêu chảy", "cảm", "dị ứng", "mắc ỉa"
        )

        if (healthKeywords.none { userMsg.contains(it, ignoreCase = true) }) {
            addBotMessage(
                "💊 Mình chỉ tư vấn khi bạn mô tả triệu chứng nhé.\n" +
                        "Ví dụ: ho, sốt, đau đầu, đau bụng…"
            )
            return
        }

        callGemini(userMsg, productNames, allProducts)
    }

    private fun handleSelectByNumber(userMsg: String): Boolean {
        val list = _pendingSelectProducts.value
        if (list.isEmpty()) return false

        val index = userMsg.trim().toIntOrNull()
        if (index == null || index !in 1..list.size) {
            addBotMessage("❗ Bạn vui lòng nhập số từ 1 đến ${list.size}")
            return true
        }

        val product = list[index - 1]
        addProductToCart(product)

        _pendingSelectProducts.value = emptyList()
        return true
    }

    private fun addProductToCart(product: ProductModel) {
        viewModelScope.launch {
            _addToCartEvent.emit(product)
            addBotMessage("Mình đã thêm **${product.name}** vào giỏ hàng cho bạn!")
        }
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
        "Bạn là trợ lý AI hỗ trợ tra cứu thông tin và gợi ý sản phẩm chăm sóc sức khỏe không kê đơn."
        
        Danh sách thuốc hiện có:
        $productListText
        
        YÊU CẦU BẮT BUỘC:
        - CHỈ chọn thuốc trong danh sách trên
        - KHÔNG bịa thuốc
        - KHÔNG đánh số
        - Format trả lời:
        
        Các thuốc phù hợp:
        - TenThuoc1: mô tả chi tiết công dụng
        - TenThuoc2: mô tả chi tiết công dụng
        - TenThuoc3: mô tả chi tiết công dụng
        
        Cuối cùng hỏi:
        "Bạn muốn thêm thuốc số mấy vào giỏ hàng, mình có thể giúp bạn?"
        
        Triệu chứng:
        "$userMsg"
        """.trimIndent()

        viewModelScope.launch {
            try {
                val response = geminiClient.generateContent(prompt)
                val botReply =
                    response.text ?: "Xin lỗi, mình chưa tìm được thuốc phù hợp."

                extractProductsFromGemini(botReply, allProducts)

            } catch (e: Exception) {
                handleGeminiError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun extractProductsFromGemini(
        botText: String,
        allProducts: List<ProductModel>
    ) {
        val suggestedNames = botText.lines()
            .mapNotNull { line ->
                if (line.trim().startsWith("- ")) {
                    line.substringAfter("- ")
                        .substringBefore(":")
                        .trim()
                } else null
            }
            .distinct()

        val matchedProducts = allProducts.filter { product ->
            suggestedNames.any { it.equals(product.name, ignoreCase = true) }
        }

        if (matchedProducts.isEmpty()) {
            addBotMessage("😥 Mình chưa tìm được thuốc phù hợp trong hệ thống.")
            return
        }

        _pendingSelectProducts.value = matchedProducts

        val numberedText = matchedProducts.mapIndexed { index, product ->
            """
        ${index + 1}. ${product.name}
        👉 ${product.description}
        """.trimIndent()
        }.joinToString("\n\n")

        addBotMessage(
            "Cảm ơn bạn đã mô tả triệu chứng, sau khi xem xét, mình đề xuất các thuốc sau:\n\n$numberedText\n\n" +
                    "👉 Bạn muốn thêm thuốc số mấy vào giỏ hàng?"
        )
    }

    private fun handleGeminiError(e: Exception) {
        val message =
            if (e.message?.contains("Quota", true) == true)
                "⚠️ Hệ thống đang bận, bạn thử lại sau nhé ⏳"
            else
                "⚠️ Có lỗi xảy ra, vui lòng thử lại sau"

        addBotMessage(message)
    }

    private fun addUserMessage(text: String) {
        _messages.value += ChatMessage(text, true)
    }

    private fun addBotMessage(text: String) {
        _messages.value += ChatMessage(text, false)
    }
}
