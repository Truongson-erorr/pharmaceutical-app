package com.example.suggested_food.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _user = mutableStateOf<UserModel?>(null)
    val user: State<UserModel?> = _user

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val auth = FirebaseAuth.getInstance()

    private val _message = mutableStateOf<String?>(null)
    val message: State<String?> = _message

    fun clearMessage() {
        _message.value = null
    }

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

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {

        if (currentPassword.isBlank()) {
            _message.value = "Vui lòng nhập mật khẩu hiện tại"
            return
        }

        if (newPassword.length < 6) {
            _message.value = "Mật khẩu mới phải có ít nhất 6 ký tự"
            return
        }

        if (newPassword != confirmPassword) {
            _message.value = "Xác nhận mật khẩu không khớp"
            return
        }

        val user = auth.currentUser

        if (user == null) {
            _message.value = "Không tìm thấy tài khoản"
            return
        }

        val email = user.email

        if (email.isNullOrBlank()) {
            _message.value = "Không tìm thấy email"
            return
        }

        _loading.value = true

        val credential =
            EmailAuthProvider.getCredential(
                email,
                currentPassword
            )

        user.reauthenticate(credential)
            .addOnSuccessListener {

                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        _loading.value = false
                        _message.value = "Đổi mật khẩu thành công"
                    }
                    .addOnFailureListener {
                        _loading.value = false
                        _message.value =
                            it.message ?: "Đổi mật khẩu thất bại"
                    }
            }
            .addOnFailureListener {
                _loading.value = false
                _message.value = "Mật khẩu hiện tại không đúng"
            }
    }
}