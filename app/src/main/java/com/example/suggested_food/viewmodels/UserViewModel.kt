package com.example.suggested_food.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {

    var user by mutableStateOf<UserModel?>(null)
        private set

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun loadUser() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snap, _ ->
                user = snap?.toObject(UserModel::class.java)
            }
    }

    fun updateAddress(newAddress: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .update("address", newAddress)
    }
}
