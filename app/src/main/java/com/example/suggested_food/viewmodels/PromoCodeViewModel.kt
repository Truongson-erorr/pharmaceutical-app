package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.PromoCode
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PromoCodeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val promoRef = db.collection("promo_codes")
    private val _promoCodes = MutableStateFlow<List<PromoCode>>(emptyList())
    val promoCodes: StateFlow<List<PromoCode>> = _promoCodes

    init {
        loadPromoCodes()
    }

    private fun loadPromoCodes() {
        promoRef.addSnapshotListener { snapshot, _ ->
            val list = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(PromoCode::class.java)?.copy(id = doc.id)
            } ?: emptyList()

            _promoCodes.value = list
        }
    }

    fun addPromoCode(promo: PromoCode) {
        val doc = promoRef.document()

        doc.set(
            promo.copy(
                id = doc.id,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    fun updatePromoCode(promo: PromoCode) {
        promoRef.document(promo.id).set(
            promo.copy(
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    fun deletePromoCode(id: String) {
        promoRef.document(id).delete()
    }

    fun toggleActive(promo: PromoCode) {
        val newState = !promo.isActive

        promoRef.document(promo.id).update(
            mapOf(
                "isActive" to newState,
                "updatedAt" to System.currentTimeMillis()
            )
        )
    }
}