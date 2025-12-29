package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user!!.uid

                        val user = UserModel(
                            uid = uid,
                            email = email,
                            name = name
                        )

                        firestore.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnFailureListener {
                                _error.value = it.message
                            }
                    }
                    .addOnFailureListener {
                        _error.value = it.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        _loading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _loading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}
