package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.HealthProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HealthProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _profile =
        MutableStateFlow(HealthProfile())
    val profile = _profile.asStateFlow()

    fun loadProfile(userId: String) {
        db.collection("health_profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                doc.toObject(HealthProfile::class.java)
                    ?.let {
                        _profile.value = it
                    }
            }
    }

    fun saveProfile(profile: HealthProfile) {
        db.collection("health_profiles")
            .document(profile.userId)
            .set(profile)
    }

    fun updateProfile(profile: HealthProfile) {
        _profile.value = profile
    }
}