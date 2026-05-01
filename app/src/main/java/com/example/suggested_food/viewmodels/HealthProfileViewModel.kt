package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.HealthProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HealthProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _profile = MutableStateFlow(HealthProfile())
    val profile = _profile.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun loadProfile(userId: String) {
        _loading.value = true

        db.collection("health_profiles")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->

                val data = doc.toObject(HealthProfile::class.java)

                if (data != null) {
                    _profile.value = data
                }

                _loading.value = false
            }
            .addOnFailureListener {
                _loading.value = false
            }
    }

    fun saveProfile(profile: HealthProfile) {
        db.collection("health_profiles")
            .document(profile.userId)
            .set(profile, SetOptions.merge())
    }
}