package com.example.suggested_food.viewmodels

import android.util.Log
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
                    "Xin chào 👋\nTôi có thể giúp gì cho bạn hôm nay?",
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

    fun sendMessage(userMsg: String) {
            if (userMsg.isBlank() || _isLoading.value) return

            _messages.value += ChatMessage(userMsg, true)
            _isLoading.value = true

            viewModelScope.launch {
                try {
                    Log.d("ChatViewModel", "User: $userMsg")

                    val response = geminiClient.generateContent(userMsg)
                    val botReply =
                        response.text ?: "Xin lỗi, tôi chưa có câu trả lời phù hợp."

                    Log.d("ChatViewModel", "Bot: $botReply")

                    _messages.value += ChatMessage(botReply, false)

                } catch (e: Exception) {
                    Log.e("GeminiError", "Message: ${e.message}")
                    Log.e("GeminiError", "Cause: ${e.cause}")
                    Log.e("GeminiError", "Stacktrace:", e)

                    _messages.value += ChatMessage(
                        "⚠️ Lỗi hệ thống:\n${e.message}",
                        false
                    )
                } finally {
                    _isLoading.value = false
                }
            }
        }
        init {
            Log.d("GeminiInit", "API_KEY length = ${BuildConfig.GEMINI_API_KEY.length}")
            Log.d("GeminiInit", "Using model = gemini-2.5-flash")
        }
}
