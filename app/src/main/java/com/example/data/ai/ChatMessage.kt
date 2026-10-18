package com.example.data.ai

import java.util.UUID

enum class GeminiModel(
    val modelId: String,
    val displayNameKannada: String,
    val displayNameEnglish: String,
    val description: String,
    val badge: String
) {
    FLASH(
        modelId = "gemini-3.8-flash",
        displayNameKannada = "ಜನರಲ್ (ಸಾಮಾನ್ಯ)",
        displayNameEnglish = "gemini-3.8-flash",
        description = "ದೈನಂದಿನ ಕೃಷಿ ಸಲಹೆ ಮತ್ತು ಮಂಡಿ ಮಾಹಿತಿ",
        badge = "General"
    ),
    FLASH_LITE(
        modelId = "gemini-3.1-flash-lite",
        displayNameKannada = "ವೇಗದ ಸಲಹೆ (Fast)",
        displayNameEnglish = "gemini-3.1-flash-lite",
        description = "ತ್ವರಿತ ದರ ಮತ್ತು ಲೆಕ್ಕಾಚಾರ",
        badge = "Fast"
    ),
    ARGON(
        modelId = "gemini-4-argon",
        displayNameKannada = "ಆಳವಾದ ವಿಶ್ಲೇಷಣೆ (Complex)",
        displayNameEnglish = "gemini-4-argon",
        description = "ಸಂಕೀರ್ಣ ಮಣ್ಣು, ಬೆಳೆ ರಕ್ಷಣೆ ಮತ್ತು ಆರ್ಥಿಕ ಯೋಜನೆ",
        badge = "Complex"
    ),
    LIVE(
        modelId = "gemini-3.8-live",
        displayNameKannada = "ಲೈವ್ ಧ್ವನಿ ಸಂವಾದ",
        displayNameEnglish = "gemini-3.8-live",
        description = "ನೈಜ-ಸಮಯದ ನೇರ ಸಂವಾದ ಲೈವ್ API",
        badge = "Live Voice"
    )
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "ಈಗ (Now)",
    val modelUsed: GeminiModel = GeminiModel.FLASH,
    val isStreaming: Boolean = false,
    val audioUrl: String? = null
)
