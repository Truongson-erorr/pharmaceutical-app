package com.example.suggested_food.viewmodels

import androidx.lifecycle.ViewModel
import com.example.suggested_food.models.ExportReceipt
import com.example.suggested_food.models.Patient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PatientViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val patientRef = db.collection("patients")

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    init {
        loadPatients()
    }

    private val _patientReceipts =
        MutableStateFlow<List<ExportReceipt>>(emptyList())

    val patientReceipts: StateFlow<List<ExportReceipt>>
            = _patientReceipts

    private fun loadPatients() {
        patientRef.addSnapshotListener { snapshot, _ ->

            val list = snapshot?.documents?.mapNotNull {
                it.toObject(Patient::class.java)
            } ?: emptyList()

            _patients.value = list
        }
    }

    fun upsertPatient(
        name: String,
        phone: String,
        totalPrice: Long
    ) {
        val docRef = patientRef.document(phone)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(docRef)

            if (snapshot.exists()) {

                val patient =
                    snapshot.toObject(Patient::class.java)!!

                transaction.update(
                    docRef,
                    mapOf(
                        "name" to name,
                        "totalOrders" to patient.totalOrders + 1,
                        "totalSpent" to patient.totalSpent + totalPrice,
                        "lastVisit" to System.currentTimeMillis(),
                        "updatedAt" to System.currentTimeMillis()
                    )
                )

            } else {

                val newPatient = Patient(
                    id = phone,
                    name = name,
                    phone = phone,
                    totalOrders = 1,
                    totalSpent = totalPrice,
                    lastVisit = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis()
                )

                transaction.set(docRef, newPatient)
            }
        }
    }

    fun loadPatientReceipts(phone: String) {

        db.collection("export_receipts")
            .whereEqualTo("customerPhone", phone)
            .addSnapshotListener { snapshot, _ ->

                _patientReceipts.value =
                    snapshot?.documents?.mapNotNull {
                        it.toObject(ExportReceipt::class.java)
                    } ?: emptyList()
            }
    }
}