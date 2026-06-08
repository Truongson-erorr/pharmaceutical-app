package com.example.suggested_food.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.UserModel
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _user = mutableStateOf<UserModel?>(null)
    val user: State<UserModel?> = _user

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    fun loadCurrentUser(uid: String) {
        _loading.value = true

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                _user.value = doc.toObject(UserModel::class.java)
                _loading.value = false
            }
            .addOnFailureListener {
                _loading.value = false
            }
    }

    fun updateUser(user: UserModel) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .set(user)
    }
}