package com.example.suggested_food.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.suggested_food.models.ProductData
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
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
import java.io.InputStreamReader
import kotlin.math.exp

data class MedicineResult(
    val name: String,
    val score: Float,
    val composition: String,
    val uses: String,
    val sideEffects: String,
    val imageUrl: String,
    val manufacturer: String,
    val excellent: String,
    val average: String,
    val poor: String
)

class SuggestViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _result = MutableStateFlow<List<MedicineResult>>(emptyList())
    val result: StateFlow<List<MedicineResult>> = _result

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var module: Module? = null
    private lateinit var vocab: Map<String, Int>
    private lateinit var labels: List<String>

    private val medicineList: List<ProductData> by lazy {
        loadMedicineCsv()
    }

    private fun loadMedicineCsv(): List<ProductData> {

        val list = mutableListOf<ProductData>()

        val inputStream = getApplication<Application>()
            .assets.open("models_ai/Medicine_Detail_with_Predictions.csv")

        val parser = CSVParserBuilder().withSeparator(',').build()

        val reader = CSVReaderBuilder(InputStreamReader(inputStream))
            .withCSVParser(parser)
            .build()

        val rows = reader.readAll()

        rows.drop(1).forEachIndexed { index, cols ->

            if (cols.size >= 9) {
                list.add(
                    ProductData(
                        id = index,
                        name = cols[0].trim(),
                        composition = cols[1].trim(),
                        uses = cols[2].trim(),
                        sideEffects = cols[3].trim(),
                        imageUrl = cols[4].trim(),
                        manufacturer = cols[5].trim(),
                        excellentReview = cols[6].trim(),
                        averageReview = cols[7].trim(),
                        poorReview = cols[8].trim()
                    )
                )
            }
        }

        return list
    }

    fun suggest(symptom: String) {

        if (symptom.isBlank()) return

        _loading.value = true

        viewModelScope.launch(Dispatchers.Default) {

            loadAI()

            val translated = translateSymptom(symptom)

            val vector = textToVector(translated)

            val tensor = Tensor.fromBlob(
                vector,
                longArrayOf(1, vector.size.toLong())
            )

            val output = module!!
                .forward(IValue.from(tensor))
                .toTensor()

            val scores = softmax(output.dataAsFloatArray)

            val results = scores
                .withIndex()
                .mapNotNull { (index, score) ->

                    val medicine =
                        labels.getOrNull(index) ?: return@mapNotNull null

                    val detail = medicineList.find {
                        it.name.equals(medicine, ignoreCase = true)
                    } ?: return@mapNotNull null

                    MedicineResult(
                        name = medicine,
                        score = score,
                        composition = detail.composition,
                        uses = detail.uses,
                        sideEffects = detail.sideEffects,
                        imageUrl = detail.imageUrl,
                        manufacturer = detail.manufacturer,
                        excellent = detail.excellentReview,
                        average = detail.averageReview,
                        poor = detail.poorReview
                    )
                }
                .sortedByDescending { it.score }
                .take(5)

            _result.value = results
            _loading.value = false
        }
    }

    private val symptomMap = mapOf(
        "sổ mũi" to "runny nose",
        "nghẹt mũi" to "nasal congestion",
        "hắt hơi" to "sneezing",
        "đau họng" to "sore throat",
        "sốt" to "fever",
        "ho" to "cough",
        "ho khan" to "dry cough",
        "ho có đờm" to "productive cough",
        "đau đầu" to "headache",
        "chóng mặt" to "dizziness",
        "mệt mỏi" to "fatigue",
        "đau bụng" to "abdominal pain",
        "buồn nôn" to "nausea",
        "nôn mửa" to "vomiting",
        "tiêu chảy" to "diarrhea",
        "táo bón" to "constipation",
        "đau lưng" to "back pain",
        "đau cơ" to "muscle pain",
        "đau khớp" to "joint pain",
        "khó thở" to "shortness of breath",
        "đau ngực" to "chest pain",
        "ớn lạnh" to "chills",
        "đổ mồ hôi" to "sweating",
        "ngứa da" to "itching",
        "khô miệng" to "dry mouth",
        "chán ăn" to "loss of appetite",
        "giảm vị giác" to "loss of taste",
        "mất khứu giác" to "loss of smell",
        "đau tai" to "ear pain",
        "ù tai" to "tinnitus",
        "đỏ mắt" to "red eyes",
        "chảy nước mắt" to "watery eyes",
        "tim đập nhanh" to "rapid heartbeat"
    )

    private fun translateSymptom(text: String): String {

        var result = text.lowercase()

        symptomMap.forEach { (vi, en) ->
            result = result.replace(vi, en)
        }

        return result
    }

    private fun textToVector(text: String): FloatArray {

        val vector = FloatArray(vocab.size)

        text.split(" ").forEach {
            vocab[it]?.let { index ->
                vector[index] += 1f
            }
        }

        return vector
    }

    private fun softmax(logits: FloatArray): FloatArray {

        val max = logits.maxOrNull() ?: 0f
        val expVals = logits.map { exp((it - max).toDouble()).toFloat() }
        val sum = expVals.sum()

        return expVals.map { it / sum }.toFloatArray()
    }

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

    private fun assetFilePath(
        context: Application,
        assetName: String
    ): String {

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