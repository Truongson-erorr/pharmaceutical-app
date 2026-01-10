package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedIn

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    init {
        auth.currentUser?.uid?.let { uid ->
            loadUserName(uid)
        }
    }

    fun register(email: String, password: String, name: String) {
        _loading.value = true
        _error.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener

                val user = UserModel(
                    uid = uid,
                    email = email,
                    name = name
                )

                firestore.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        _isLoggedIn.value = true
                        _userName.value = name
                        _loading.value = false
                    }
                    .addOnFailureListener {
                        _error.value = it.message
                        _loading.value = false
                    }
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }

    fun login(email: String, password: String) {
        _loading.value = true
        _error.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                _isLoggedIn.value = true
                result.user?.uid?.let { loadUserName(it) }
                _loading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _userName.value = null
    }

    fun getCurrentUser() = auth.currentUser

    private fun loadUserName(uid: String) {
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                _userName.value = doc.getString("name")
            }
            .addOnFailureListener {
                _userName.value = null
            }
    }
}
