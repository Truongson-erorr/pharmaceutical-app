package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    init {
        auth.currentUser?.uid?.let { uid ->
            loadUserData(uid)
        }
    }

    fun register(email: String, password: String, name: String, role: String) {

        _loading.value = true
        _error.value = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->

                val uid = result.user?.uid ?: return@addOnSuccessListener

                val user = UserModel(
                    uid = uid,
                    email = email,
                    name = name,
                    role = role
                )

                firestore.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {

                        _userName.value = name
                        _userRole.value = role
                        _isLoggedIn.value = true
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
                val uid = result.user?.uid ?: return@addOnSuccessListener
                loadUserData(uid)
                _isLoggedIn.value = true
                _loading.value = false
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }

    private fun loadUserData(uid: String) {

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                _userName.value = doc.getString("name")
                _userRole.value = doc.getString("role")
            }
            .addOnFailureListener {
                _userName.value = null
                _userRole.value = null
            }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _userName.value = null
        _userRole.value = null
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        _loading.value = true
        _error.value = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->

                val firebaseUser = result.user ?: return@addOnSuccessListener
                val uid = firebaseUser.uid

                val userRef = firestore.collection("users").document(uid)

                userRef.get().addOnSuccessListener { doc ->

                    if (!doc.exists()) {

                        val user = UserModel(
                            uid = uid,
                            email = firebaseUser.email ?: "",
                            name = firebaseUser.displayName ?: "Google User",
                            role = "user"
                        )

                        userRef.set(user)
                    }

                    loadUserData(uid)
                    _isLoggedIn.value = true
                    _loading.value = false
                }
            }
            .addOnFailureListener {
                _error.value = it.message
                _loading.value = false
            }
    }

    fun getCurrentUser() = auth.currentUser
}