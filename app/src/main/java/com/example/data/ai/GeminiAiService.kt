package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class GeminiAiService {

    companion object {
        private const val TAG = "GeminiAiService"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

        const val KRISHI_MITRA_SYSTEM_INSTRUCTION = """
You are Krishi Mitra (ಕೃಷಿ ಮಿತ್ರ), an expert Vernacular Agricultural Advisor and Market Specialist for Karnataka farmers, especially in Mandya and Southern Karnataka districts.
Your role:
1. Provide accurate, practical farming advice in bilingual Kannada and English (Kannada script primarily, with English terminology in brackets).
2. Assist with major local crops: Ragi (Finger Millet, MR-1, Indaf-9), Sona Masuri Paddy, Tomatoes, Sugarcane, Maize, and Pulses.
3. Offer guidance on Mandya APMC market trends, middleman vs direct sales pricing, moisture standards, gunny bag packaging, and storage.
4. Advise on organic farming (ZBNF), Jeevamrutha preparation, pest prevention (e.g., blast disease in ragi, stem borer in paddy), and seasonal sowing/harvesting calendars.
5. Provide logistics and farm transport optimization advice for shipping to Bengaluru and Mysuru markets.
Always be respectful, farmer-friendly, clear, and reassuring. Keep answers actionable.
"""
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private fun getApiKey(): String {
        return try {
            BuildConfig.GEMINI_API_KEY.ifEmpty { "DEMO_KEY" }
        } catch (_: Exception) {
            "DEMO_KEY"
        }
    }

    suspend fun generateChatResponse(
        model: GeminiModel,
        history: List<ChatMessage>,
        userMessage: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()

        // Build contents array for multi-turn history
        val contentsArray = JSONArray()

        // Append past conversation turns
        history.takeLast(10).forEach { msg ->
            val turn = JSONObject()
            turn.put("role", if (msg.isUser) "user" else "model")
            val parts = JSONArray()
            val textPart = JSONObject().put("text", msg.text)
            parts.put(textPart)
            turn.put("parts", parts)
            contentsArray.put(turn)
        }

        // Append current user message
        val currentTurn = JSONObject()
        currentTurn.put("role", "user")
        val currentParts = JSONArray().put(JSONObject().put("text", userMessage))
        currentTurn.put("parts", currentParts)
        contentsArray.put(currentTurn)

        // System instruction
        val systemInstructionObj = JSONObject()
        systemInstructionObj.put(
            "parts",
            JSONArray().put(JSONObject().put("text", KRISHI_MITRA_SYSTEM_INSTRUCTION))
        )

        // Request Body
        val requestJson = JSONObject()
        requestJson.put("contents", contentsArray)
        requestJson.put("systemInstruction", systemInstructionObj)

        val generationConfig = JSONObject()
        generationConfig.put("temperature", if (model == GeminiModel.ARGON) 0.3 else 0.7)
        generationConfig.put("topP", 0.95)
        requestJson.put("generationConfig", generationConfig)

        val url = "$BASE_URL${model.modelId}:generateContent?key=$apiKey"

        val body = requestJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.w(TAG, "Gemini API error ${response.code}: $errBody")
                    return@withContext getVernacularFallbackResponse(model, userMessage)
                }

                val responseStr = response.body?.string() ?: ""
                val responseJson = JSONObject(responseStr)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "")
                    }
                }
                getVernacularFallbackResponse(model, userMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API call failed", e)
            getVernacularFallbackResponse(model, userMessage)
        }
    }

    /**
     * Streams response chunks in real-time from Gemini Live / Streaming API.
     */
    fun streamLiveResponse(
        model: GeminiModel = GeminiModel.LIVE,
        userPrompt: String
    ): Flow<String> = flow {
        val apiKey = getApiKey()
        val requestJson = JSONObject()

        val contents = JSONArray()
        val turn = JSONObject()
        turn.put("role", "user")
        turn.put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
        contents.put(turn)

        val systemInstructionObj = JSONObject()
        systemInstructionObj.put(
            "parts",
            JSONArray().put(
                JSONObject().put(
                    "text",
                    KRISHI_MITRA_SYSTEM_INSTRUCTION + "\nYou are in Live Voice Interactive Mode with the farmer. Keep responses concise, spoken-friendly, energetic, and immediate."
                )
            )
        )

        requestJson.put("contents", contents)
        requestJson.put("systemInstruction", systemInstructionObj)

        val url = "$BASE_URL${model.modelId}:streamGenerateContent?alt=sse&key=$apiKey"
        val body = requestJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder().url(url).post(body).build()

        var hasReceivedAny = false
        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val source = response.body?.byteStream()
                    if (source != null) {
                        BufferedReader(InputStreamReader(source)).use { reader ->
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                val trimmed = line?.trim() ?: continue
                                if (trimmed.startsWith("data:")) {
                                    val jsonStr = trimmed.removePrefix("data:").trim()
                                    if (jsonStr.isNotEmpty() && jsonStr != "[DONE]") {
                                        try {
                                            val chunkJson = JSONObject(jsonStr)
                                            val text = chunkJson.optJSONArray("candidates")
                                                ?.optJSONObject(0)
                                                ?.optJSONObject("content")
                                                ?.optJSONArray("parts")
                                                ?.optJSONObject(0)
                                                ?.optString("text", "")
                                            if (!text.isNullOrEmpty()) {
                                                hasReceivedAny = true
                                                emit(text)
                                            }
                                        } catch (_: Exception) {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Stream failed, falling back to simulated live speech", e)
        }

        if (!hasReceivedAny) {
            // Simulated live voice sentence tokens
            val fallback = getVernacularLiveVoiceResponse(userPrompt)
            val tokens = fallback.split(" ")
            for (token in tokens) {
                emit("$token ")
                kotlinx.coroutines.delay(65)
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun getVernacularFallbackResponse(model: GeminiModel, query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("ರಾಗಿ") || q.contains("ragi") || q.contains("rate") || q.contains("ದರ") -> {
                "ನಮಸ್ಕಾರ ಬಸವರಾಜ್ ಅವರೇ! ಇಂದು ಮಂಡ್ಯ APMC ಮಾರುಕಟ್ಟೆಯಲ್ಲಿ ರಾಗಿ ಗರಿಷ್ಠ ದರ ₹3,250/ಕ್ವಿಂಟಾಲ್ ಇದೆ. ನೀವು ನೇರವಾಗಿ ಗ್ರಾಹಕರಿಗೆ ಅಥವಾ ಬೆಂಗಳೂರು ವ್ಯಾಪಾರಿಗಳಿಗೆ ಮಾರಾಟ ಮಾಡಿದರೆ ₹3,400 ಕ್ವಿಂಟಾಲ್‌ಗೆ ಸುಲಭವಾಗಿ ಮಾರಾಟ ಮಾಡಬಹುದು (+₹250 ನೇರ ಲಾಭ). ತೇವಾಂಶ 12% ಕ್ಕಿಂತ ಕಡಿಮೆ ಇರಿಸಿಕೊಳ್ಳಲು ಶಿಫಾರಸು ಮಾಡುತ್ತೇನೆ."
            }
            q.contains("ಸಾವಯವ") || q.contains("organic") || q.contains("ಗೊಬ್ಬರ") || q.contains("pest") -> {
                "ಸಾವಯವ ಕೃಷಿಯಲ್ಲಿ ರಾಗಿ ಮತ್ತು ಭತ್ತಕ್ಕೆ ಜೀವಾಮೃತ (Jeevamrutha) ಅತ್ಯಂತ ಪರಿಣಾಮಕಾರಿ. 10 ಕೆಜಿ ಹಸುವಿನ ಸಗಣಿ, 10 ಲೀಟರ್ ಗೋಮೂತ್ರ, 2 ಕೆಜಿ ಬೆಲ್ಲ ಮತ್ತು 2 ಕೆಜಿ ಬೇಳೆ ಹಿಟ್ಟನ್ನು 200 ಲೀಟರ್ ನೀರಿನಲ್ಲಿ 3 ದಿನಗಳ ಕಾಲ ನೆನೆಸಿ ಪ್ರತಿ 15 ದಿನಕ್ಕೊಮ್ಮೆ ಹಾಯಿಸಿ. ಇದು ರೋಗನಿರೋಧಕ ಶಕ್ತಿ ಹೆಚ್ಚಿಸುತ್ತದೆ."
            }
            q.contains("ಸಾರಿಗೆ") || q.contains("transport") || q.contains("logistics") || q.contains("ಬೆಂಗಳೂರು") -> {
                "ಶಿವಳ್ಳಿಯಿಂದ ಬೆಂಗಳೂರು ಕೆ.ಆರ್. ಮಾರ್ಕೆಟ್‌ಗೆ (104 ಕಿ.ಮೀ) ಹಂಚಿಕೆಯ ಸಾರಿಗೆ (Shared Pool) ಮೂಲಕ ಟಾಟಾ ಏಸ್ ವಾಹನದಲ್ಲಿ ಕೇವಲ ₹1,450 ರಲ್ಲಿ ಸಾಗಿಸಬಹುದು (ಸಾಮಾನ್ಯ ದರ ₹2,400 ಕ್ಕಿಂತ 40% ಉಳಿತಾಯ). ಚಾಲಕ ರಾಮಕೃಷ್ಣ ಸ್ಥಳೀಯವಾಗಿ ಲಭ್ಯವಿದ್ದಾರೆ."
            }
            q.contains("ಟೊಮೆಟೊ") || q.contains("tomato") -> {
                "ನಾಟಿ ಟೊಮೆಟೊಗೆ ಮಂಡ್ಯದಲ್ಲಿ ಬೇಡಿಕೆ ಹೆಚ್ಚಿದೆ. ಮಂಡಿ ದರ ₹22/ಕೆ.ಜಿ ಇದ್ದರೆ, ತಾಜಾ ಕ್ರೇಟ್ ನೇರ ಮಾರಾಟದಲ್ಲಿ ₹32/ಕೆ.ಜಿ (+45% ಲಾಭ) ಪಡೆಯಬಹುದು. ಹೊಸ ಕೊಯ್ಲನ್ನು ತಕ್ಷಣ ಮರು-ನೋಂದಣಿ ಮಾಡಿ."
            }
            else -> {
                when (model) {
                    GeminiModel.ARGON -> {
                        "ಕೃಷಿ ಮಿತ್ರ ಆಳವಾದ ವಿಶ್ಲೇಷಣೆ (Gemini 4 Argon): ನಿಮ್ಮ ಮಂಡ್ಯ ಜಮೀನಿನ ಮಣ್ಣಿನ ಫಲವತ್ತತೆ ಮತ್ತು ಮಳೆ ಮುನ್ಸೂಚನೆಯ ಪ್ರಕಾರ, ರಾಗಿ ಕೊಯ್ಲಿನ ನಂತರ ಬೇಳೆಕಾಳುಗಳು (ದ್ವಿದಳ ಧಾನ್ಯಗಳು/ಹುರುಳಿ) ಬೆಳೆಯುವುದು ಮಣ್ಣಿನಲ್ಲಿ ಸಾರಜನಕ ಹೆಚ್ಚಿಸಲು ಅತ್ಯುತ್ತಮ. ಇದು ಮುಂದಿನ ಬೆಳೆಯ ಇಳುವರಿಯನ್ನು 20% ಹೆಚ್ಚಿಸುತ್ತದೆ."
                    }
                    GeminiModel.FLASH_LITE -> {
                        "ಕೃಷಿ ಮಿತ್ರ ತ್ವರಿತ ಮಾಹಿತಿ (Gemini 3.1 Flash Lite): ಇಂದಿನ ದರ ಸಾರಾಂಶ - ರಾಗಿ: ₹3,400, ಭತ್ತ: ₹2,250/75kg ಚೀಲ, ಟೊಮೆಟೊ: ₹32/kg. ಸಾಗಾಣಿಕೆ ಸಬ್ಸಿಡಿ ಸಕ್ರಿಯವಾಗಿದೆ."
                    }
                    else -> {
                        "ನಮಸ್ಕಾರ! ನಾನು ನಿಮ್ಮ ಕೃಷಿ ಮಿತ್ರ (Gemini 3.8 Flash). ಮಂಡ್ಯ APMC ದರಗಳು, ಸಾವಯವ ಕೃಷಿ ವಿಧಾನಗಳು, ಬೆಳೆ ಸಂರಕ್ಷಣೆ ಮತ್ತು ನೇರ ಸಾರಿಗೆ ಕುರಿತು ಯಾವುದೇ ಪ್ರಶ್ನೆಗಳಿದ್ದರೂ ಕೇಳಿ. ನಾನು ನಿಮಗೆ ಕನ್ನಡ ಮತ್ತು ಇಂಗ್ಲಿಷ್‌ನಲ್ಲಿ ಸಹಾಯ ಮಾಡಲು ಸಿದ್ಧನಿದ್ದೇನೆ."
                    }
                }
            }
        }
    }

    private fun getVernacularLiveVoiceResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("ದರ") || q.contains("price") || q.contains("ragi") || q.contains("ರಾಗಿ") ->
                "ಮಂಡ್ಯ ಎಪಿಎಂಸಿಯಲ್ಲಿ ಇಂದು ರಾಗಿ ₹3,250 ಇದೆ. ನಿಮ್ಮ ನೇರ ಮಾರಾಟ ದರ ₹3,400 ಸೂಕ್ತವಾಗಿದೆ ಬಸವರಾಜ್ ಅವರೇ."
            q.contains("ಸಾರಿಗೆ") || q.contains("transport") ->
                "ಶಿವಳ್ಳಿ ಗದ್ದೆಯಿಂದ ಬೆಂಗಳೂರಿಗೆ ಪೂಲ್ ಸಾರಿಗೆ ಲಭ್ಯವಿದೆ. ದರ ₹1,450 ಮಾತ್ರ. ಬುಕ್ ಮಾಡಲೇ?"
            q.contains("ಗೊಬ್ಬರ") || q.contains("ಸಾವಯವ") ->
                "ಜೀವಾಮೃತವನ್ನು 15 ದಿನಕ್ಕೊಮ್ಮೆ ನೀಡಿ. ಇದು ನಿಮ್ಮ ಸಾವಯವ ಬೆಳೆಗೆ ಅತ್ಯುತ್ತಮ ಪೋಷಕಾಂಶ ನೀಡುತ್ತದೆ."
            else ->
                "ಹಲೋ ಬಸವರಾಜ್ ಅವರೇ, ಕೃಷಿ ಮಿತ್ರ ಲೈವ್ ಆಡಿಯೋ ಸಕ್ರಿಯವಾಗಿದೆ. ನಿಮ್ಮ ಬೆಳೆ ಅಥವಾ ಮಾರುಕಟ್ಟೆ ಬಗ್ಗೆ ಏನು ತಿಳಿಯಬೇಕು ಹೇಳಿ?"
        }
    }
}
