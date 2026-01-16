package com.example.suggested_food.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ChatMessageDoctor
import com.google.firebase.firestore.FirebaseFirestore

class ChatDoctorViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _messages = mutableStateListOf<ChatMessageDoctor>()
    val messages: List<ChatMessageDoctor> = _messages

    fun listenMessages(chatId: String) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    _messages.clear()
                    _messages.addAll(it.toObjects(ChatMessageDoctor::class.java))
                }
            }
    }

    fun sendMessage(chatId: String, message: ChatMessageDoctor) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
    }
}
