package com.example.suggested_food.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.io.File
import java.io.FileOutputStream
import kotlin.math.exp

data class MedicineResult(
    val name: String,
    val score: Float
)

class SuggestViewModel(
    application: Application
) : AndroidViewModel(application) {

    // =========================
    // STATE
    // =========================
    private val _result = MutableStateFlow<List<MedicineResult>>(emptyList())
    val result: StateFlow<List<MedicineResult>> = _result

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // =========================
    // MODEL
    // =========================
    private var module: Module? = null
    private lateinit var vocab: Map<String, Int>
    private lateinit var labels: List<String>

    // =========================
    // VIET → ENG MAP
    // =========================
    private val symptomMap = mapOf(
        "sổ mũi" to "runny nose",
        "nghẹt mũi" to "nasal congestion",
        "đau đầu" to "headache",
        "đau bụng" to "abdominal pain",
        "buồn nôn" to "nausea",
        "tiêu chảy" to "diarrhea",
        "ho" to "cough",
        "sốt" to "fever",
        "chóng mặt" to "dizziness",
        "mệt mỏi" to "fatigue",
        "khó thở" to "shortness of breath",
        "đau họng" to "sore throat",
        "đau" to "pain"
    )

    // =========================
    // STRONG FILTER (QUAN TRỌNG NHẤT)
    // =========================
    private val symptomKeywords = mapOf(
        "runny nose" to listOf("nasal", "spray", "rhinitis", "antihistamine"),
        "nasal congestion" to listOf("spray", "decongestant"),
        "fever" to listOf("paracetamol", "pcm", "tablet", "antipyretic"),
        "headache" to listOf("pain", "analgesic", "migraine", "tablet"),
        "cough" to listOf("syrup", "cough", "expectorant"),
        "diarrhea" to listOf("oral", "rehydration", "electrolyte"),
        "abdominal pain" to listOf("antispasmodic", "tablet"),
        "sore throat" to listOf("lozenge", "spray")
    )

    // =========================
    // MAIN FUNCTION
    // =========================
    fun suggest(symptom: String) {

        if (symptom.isBlank()) return

        _loading.value = true

        viewModelScope.launch(Dispatchers.Default) {

            loadAI()

            // 1. translate
            val translated = translateSymptom(symptom)

            // 2. vector
            val vector = textToVector(translated)

            val tensor = Tensor.fromBlob(
                vector,
                longArrayOf(1, vector.size.toLong())
            )

            // 3. predict
            val output = module!!
                .forward(IValue.from(tensor))
                .toTensor()

            val scores = softmax(output.dataAsFloatArray)

            // =========================
            // FILTER + RANKING
            // =========================
            val results = scores
                .withIndex()
                .mapNotNull { (index, score) ->

                    val medicine = labels.getOrNull(index) ?: return@mapNotNull null
                    val med = medicine.lowercase()

                    // kiểm tra liên quan triệu chứng
                    val isRelevant = symptomKeywords.any { (symptomKey, keywords) ->
                        translated.contains(symptomKey) &&
                                keywords.any { med.contains(it) }
                    }

                    if (!isRelevant) return@mapNotNull null

                    MedicineResult(
                        name = medicine,
                        score = score
                    )
                }
                .sortedByDescending { it.score }
                .take(10)

            _result.value = results
            _loading.value = false
        }
    }

    // =========================
    // TRANSLATE VIET → ENG
    // =========================
    private fun translateSymptom(text: String): String {

        var result = text.lowercase()

        symptomMap
            .toList()
            .sortedByDescending { it.first.length }
            .forEach { (vi, en) ->
                result = result.replace(vi, en)
            }

        return result
    }

    // =========================
    // TEXT → VECTOR
    // =========================
    private fun textToVector(text: String): FloatArray {

        val vector = FloatArray(vocab.size)

        text.lowercase()
            .split(" ")
            .forEach { word ->
                vocab[word]?.let {
                    vector[it] += 1f
                }
            }

        return vector
    }

    // =========================
    // SOFTMAX
    // =========================
    private fun softmax(logits: FloatArray): FloatArray {

        val max = logits.maxOrNull() ?: 0f
        val expVals = logits.map { exp((it - max).toDouble()).toFloat() }
        val sum = expVals.sum()

        return expVals.map { it / sum }.toFloatArray()
    }

    // =========================
    // LOAD MODEL
    // =========================
    private fun loadAI() {

        if (module != null) return

        val context = getApplication<Application>()

        module = Module.load(
            assetFilePath(context, "models_ai/medicine_model_android.pt")
        )

        vocab = loadVocab(context)
        labels = loadLabels(context)
    }

    private fun loadVocab(context: Application): Map<String, Int> {

        val json = context.assets
            .open("models_ai/vectorizer_vocab.json")
            .bufferedReader()
            .readText()

        val obj = JSONObject(json)

        return obj.keys().asSequence()
            .associateWith { obj.getInt(it) }
    }

    private fun loadLabels(context: Application): List<String> {

        val json = context.assets
            .open("models_ai/labels.json")
            .bufferedReader()
            .readText()

        val arr = JSONArray(json)

        return List(arr.length()) { arr.getString(it) }
    }

    private fun assetFilePath(context: Application, assetName: String): String {

        val file = File(context.filesDir, assetName)
        if (file.exists()) return file.absolutePath

        file.parentFile?.mkdirs()

        context.assets.open(assetName).use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }
}