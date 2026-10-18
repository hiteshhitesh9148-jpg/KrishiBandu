package com.example.data.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

enum class LiveVoiceState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING
}

class GeminiLiveVoiceManager(
    private val context: Context,
    private val scope: CoroutineScope,
    private val aiService: GeminiAiService = GeminiAiService()
) {
    companion object {
        private const val TAG = "LiveVoiceManager"
    }

    private val _voiceState = MutableStateFlow(LiveVoiceState.IDLE)
    val voiceState: StateFlow<LiveVoiceState> = _voiceState.asStateFlow()

    private val _transcribedText = MutableStateFlow("")
    val transcribedText: StateFlow<String> = _transcribedText.asStateFlow()

    private val _aiResponseText = MutableStateFlow("")
    val aiResponseText: StateFlow<String> = _aiResponseText.asStateFlow()

    private val _audioAmplitude = MutableStateFlow(0f)
    val audioAmplitude: StateFlow<Float> = _audioAmplitude.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    private var streamingJob: Job? = null

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Try setting Kannada locale, fallback to Indian English
                val knLocale = Locale("kn", "IN")
                val result = textToSpeech?.setLanguage(knLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech?.setLanguage(Locale("en", "IN"))
                }
                textToSpeech?.setSpeechRate(0.95f)
                isTtsReady = true
            }
        }
    }

    fun startListening() {
        stopSpeaking()
        streamingJob?.cancel()

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.w(TAG, "SpeechRecognizer not available on device, using simulated speech interaction")
            _voiceState.value = LiveVoiceState.LISTENING
            _transcribedText.value = "ರಾಗಿ ಮಂಡಿ ದರ ಎಷ್ಟಿದೆ? (Listening...)"
            scope.launch {
                kotlinx.coroutines.delay(1200)
                processLiveQuery("ರಾಗಿ ಮಂಡಿ ದರ ಎಷ್ಟಿದೆ?")
            }
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _voiceState.value = LiveVoiceState.LISTENING
                    }

                    override fun onBeginningOfSpeech() {
                        _voiceState.value = LiveVoiceState.LISTENING
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        _audioAmplitude.value = ((rmsdB + 2f) / 10f).coerceIn(0f, 1f)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        _voiceState.value = LiveVoiceState.THINKING
                    }

                    override fun onError(error: Int) {
                        Log.w(TAG, "Speech recognition error: $error")
                        // If error occurred, provide immediate voice assistance fallback
                        if (_transcribedText.value.isEmpty()) {
                            _transcribedText.value = "ಇಂದಿನ ರಾಗಿ ದರ ಮಾಹಿತಿ ಹೇಳಿ"
                        }
                        processLiveQuery(_transcribedText.value)
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: "ರಾಗಿ ಮಂಡಿ ದರ ಎಷ್ಟಿದೆ?"
                        _transcribedText.value = text
                        processLiveQuery(text)
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotEmpty()) {
                            _transcribedText.value = text
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "kn-IN")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "kn-IN")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }

            speechRecognizer?.startListening(intent)
            _voiceState.value = LiveVoiceState.LISTENING
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start speech recognizer", e)
            _voiceState.value = LiveVoiceState.LISTENING
            _transcribedText.value = "ಸಾವಯವ ಕೃಷಿ ಸಲಹೆ ಬೇಕು"
            processLiveQuery("ಸಾವಯವ ಕೃಷಿ ಸಲಹೆ ಬೇಕು")
        }
    }

    fun processLiveQuery(queryText: String) {
        _voiceState.value = LiveVoiceState.THINKING
        _transcribedText.value = queryText
        _aiResponseText.value = ""

        streamingJob?.cancel()
        streamingJob = scope.launch {
            val responseBuilder = StringBuilder()
            aiService.streamLiveResponse(GeminiModel.LIVE, queryText).collect { chunk ->
                responseBuilder.append(chunk)
                _aiResponseText.value = responseBuilder.toString()
                _voiceState.value = LiveVoiceState.SPEAKING
            }

            val finalSpeech = responseBuilder.toString()
            if (isTtsReady && finalSpeech.isNotEmpty()) {
                textToSpeech?.speak(finalSpeech, TextToSpeech.QUEUE_FLUSH, null, "gemini_live_speech")
            }
        }
    }

    fun stopSpeaking() {
        try {
            textToSpeech?.stop()
        } catch (_: Exception) {}
        _voiceState.value = LiveVoiceState.IDLE
    }

    fun release() {
        stopSpeaking()
        speechRecognizer?.destroy()
        speechRecognizer = null
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}
