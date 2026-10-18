package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ChatMessage
import com.example.data.ai.GeminiAiService
import com.example.data.ai.GeminiLiveVoiceManager
import com.example.data.ai.GeminiModel
import com.example.data.ai.LiveVoiceState
import com.example.data.model.BuyerInquiry
import com.example.data.model.CropCategory
import com.example.data.model.CropListing
import com.example.data.model.FarmingMethod
import com.example.data.model.MandiComparison
import com.example.data.model.StockStatus
import com.example.data.model.VehicleOption
import com.example.data.repository.KrishiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppTab {
    DASHBOARD,
    MY_CROPS,
    ADD_CROP,
    INQUIRIES,
    LOGISTICS,
    AI_CHAT
}

data class AddCropFormState(
    val selectedCategory: CropCategory = CropCategory.GRAINS,
    val cropNameKannada: String = "ರಾಗಿ",
    val cropNameEnglish: String = "Finger Millet / Ragi",
    val variety: String = "MR-1 / Indaf-9 (ತಳಿ)",
    val farmingMethod: FarmingMethod = FarmingMethod.ORGANIC,
    val photos: List<String> = listOf(
        KrishiRepository.SACKS_PHOTO_URL,
        KrishiRepository.HANDS_PHOTO_URL
    ),
    val quantity: String = "45",
    val quantityUnit: String = "ಕ್ವಿಂಟಾಲ್ (Quintals)",
    val price: String = "3400",
    val moq: String = "1 Quintal / 50 kg",
    val harvestDate: String = "12 Nov 2024 (ಕಳೆದ ವಾರ ಕೊಯ್ಲು ಮುಗಿದಿದೆ)",
    val stockStatus: StockStatus = StockStatus.IN_STOCK,
    val farmLocation: String = "ಶಿವಳ್ಳಿ ಗ್ರಾಮ, ಮಂಡ್ಯ ತಾಲ್ಲೂಕು (Shivalli Village, Mandya)",
    val phoneCallsEnabled: Boolean = true,
    val whatsAppEnabled: Boolean = true,
    val farmPickupEnabled: Boolean = true,
    val homeDeliveryEnabled: Boolean = false,
    val isPublishing: Boolean = false,
    val isDraftSaved: Boolean = false
)

class KrishiViewModel(
    private val repository: KrishiRepository = KrishiRepository(),
    private val aiService: GeminiAiService = GeminiAiService()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _isKannadaPrimary = MutableStateFlow(true)
    val isKannadaPrimary: StateFlow<Boolean> = _isKannadaPrimary.asStateFlow()

    val crops: StateFlow<List<CropListing>> = repository.crops
    val inquiries: StateFlow<List<BuyerInquiry>> = repository.inquiries
    val mandiComparisons: List<MandiComparison> = repository.mandiComparisons
    val vehicleOptions: List<VehicleOption> = repository.vehicleOptions

    private val _formState = MutableStateFlow(AddCropFormState())
    val formState: StateFlow<AddCropFormState> = _formState.asStateFlow()

    // Dialog & bottom sheet states
    private val _editingCrop = MutableStateFlow<CropListing?>(null)
    val editingCrop: StateFlow<CropListing?> = _editingCrop.asStateFlow()

    private val _replyingInquiry = MutableStateFlow<BuyerInquiry?>(null)
    val replyingInquiry: StateFlow<BuyerInquiry?> = _replyingInquiry.asStateFlow()

    private val _showVoiceDialog = MutableStateFlow(false)
    val showVoiceDialog: StateFlow<Boolean> = _showVoiceDialog.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    private val _showProfileSheet = MutableStateFlow(false)
    val showProfileSheet: StateFlow<Boolean> = _showProfileSheet.asStateFlow()

    // Gemini Chatbot State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "ನಮಸ್ಕಾರ ಬಸವರಾಜ್ ಅವರೇ! ನಾನು ನಿಮ್ಮ ಕೃಷಿ ಮಿತ್ರ (Krishi Mitra AI). ಮಂಡ್ಯ APMC ಮಾರುಕಟ್ಟೆ ದರಗಳು, ಸಾವಯವ ಕೃಷಿ ರಕ್ಷಣೆ, ಬೆಳೆ ಯೋಜನೆ ಅಥವಾ ಸಾರಿಗೆ ಬಗ್ಗೆ ಯಾವುದೇ ಮಾಹಿತಿ ಬೇಕಾದರೂ ಕೇಳಿ.",
                isUser = false,
                modelUsed = GeminiModel.FLASH
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _selectedModel = MutableStateFlow(GeminiModel.FLASH)
    val selectedModel: StateFlow<GeminiModel> = _selectedModel.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Live Voice State
    private val _showLiveVoiceModal = MutableStateFlow(false)
    val showLiveVoiceModal: StateFlow<Boolean> = _showLiveVoiceModal.asStateFlow()

    private var liveVoiceManager: GeminiLiveVoiceManager? = null
    private val _voiceState = MutableStateFlow(LiveVoiceState.IDLE)
    val voiceState: StateFlow<LiveVoiceState> = _voiceState.asStateFlow()

    private val _liveTranscribedText = MutableStateFlow("")
    val liveTranscribedText: StateFlow<String> = _liveTranscribedText.asStateFlow()

    private val _liveAiResponseText = MutableStateFlow("")
    val liveAiResponseText: StateFlow<String> = _liveAiResponseText.asStateFlow()

    private val _audioAmplitude = MutableStateFlow(0f)
    val audioAmplitude: StateFlow<Float> = _audioAmplitude.asStateFlow()

    // Logistics state
    private val _isSharedPool = MutableStateFlow(true)
    val isSharedPool: StateFlow<Boolean> = _isSharedPool.asStateFlow()

    private val _selectedVehicleId = MutableStateFlow("v1")
    val selectedVehicleId: StateFlow<String> = _selectedVehicleId.asStateFlow()

    private val _transportBookedMessage = MutableSharedFlow<String>()
    val transportBookedMessage: SharedFlow<String> = _transportBookedMessage.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun toggleLanguage() {
        _isKannadaPrimary.value = !_isKannadaPrimary.value
    }

    // Chatbot Methods
    fun selectAiModel(model: GeminiModel) {
        _selectedModel.value = model
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(
            text = text,
            isUser = true,
            modelUsed = _selectedModel.value
        )
        _chatMessages.update { it + userMessage }

        viewModelScope.launch {
            _isAiThinking.value = true
            val responseText = aiService.generateChatResponse(
                model = _selectedModel.value,
                history = _chatMessages.value,
                userMessage = text
            )

            val botMessage = ChatMessage(
                text = responseText,
                isUser = false,
                modelUsed = _selectedModel.value
            )
            _chatMessages.update { it + botMessage }
            _isAiThinking.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "ನಮಸ್ಕಾರ ಬಸವರಾಜ್ ಅವರೇ! ನಾನು ನಿಮ್ಮ ಕೃಷಿ ಮಿತ್ರ. ಹೊಸ ಸಂಭಾಷಣೆ ಆರಂಭವಾಗಿದೆ. ಏನು ಮಾಹಿತಿ ಬೇಕು ಹೇಳಿ?",
                isUser = false,
                modelUsed = _selectedModel.value
            )
        )
    }

    // Live Voice Methods
    fun initLiveVoice(context: Context) {
        if (liveVoiceManager == null) {
            val manager = GeminiLiveVoiceManager(context, viewModelScope, aiService)
            liveVoiceManager = manager

            viewModelScope.launch {
                manager.voiceState.collect { _voiceState.value = it }
            }
            viewModelScope.launch {
                manager.transcribedText.collect { _liveTranscribedText.value = it }
            }
            viewModelScope.launch {
                manager.aiResponseText.collect { _liveAiResponseText.value = it }
            }
            viewModelScope.launch {
                manager.audioAmplitude.collect { _audioAmplitude.value = it }
            }
        }
    }

    fun openLiveVoice(context: Context? = null) {
        if (context != null) {
            initLiveVoice(context)
        }
        _showLiveVoiceModal.value = true
        liveVoiceManager?.startListening()
    }

    fun dismissLiveVoice() {
        _showLiveVoiceModal.value = false
        liveVoiceManager?.stopSpeaking()
    }

    fun startListeningLive() {
        liveVoiceManager?.startListening()
    }

    fun stopSpeakingLive() {
        liveVoiceManager?.stopSpeaking()
    }

    fun sendLiveQuickPrompt(prompt: String) {
        liveVoiceManager?.processLiveQuery(prompt)
    }

    fun openVoiceAssistant() {
        _showLiveVoiceModal.value = true
    }

    fun dismissVoiceAssistant() {
        _showLiveVoiceModal.value = false
        liveVoiceManager?.stopSpeaking()
    }

    fun openNotifications() {
        _showNotificationsSheet.value = true
    }

    fun dismissNotifications() {
        _showNotificationsSheet.value = false
    }

    fun openProfile() {
        _showProfileSheet.value = true
    }

    fun dismissProfile() {
        _showProfileSheet.value = false
    }

    fun startEditingPrice(crop: CropListing) {
        _editingCrop.value = crop
    }

    fun dismissEditingPrice() {
        _editingCrop.value = null
    }

    fun savePrice(cropId: String, newPrice: Int) {
        repository.updatePrice(cropId, newPrice)
        _editingCrop.value = null
        viewModelScope.launch {
            _toastMessage.emit("ದರ ಯಶಸ್ವಿಯಾಗಿ ನವೀಕರಿಸಲಾಗಿದೆ (Price Updated: ₹$newPrice)")
        }
    }

    fun relist(cropId: String) {
        repository.relistCrop(cropId)
        viewModelScope.launch {
            _toastMessage.emit("ಬೆಳೆ ಮರು-ನೋಂದಣಿ ಯಶಸ್ವಿಯಾಗಿದೆ! (Crop Relisted)")
        }
    }

    fun startReplying(inquiry: BuyerInquiry) {
        _replyingInquiry.value = inquiry
    }

    fun dismissReplying() {
        _replyingInquiry.value = null
    }

    fun sendPriceQuote(inquiryId: String, quotePrice: String) {
        _replyingInquiry.value = null
        viewModelScope.launch {
            _toastMessage.emit("ದರ ಉಲ್ಲೇಖವನ್ನು ಖರೀದಿದಾರರಿಗೆ ಕಳುಹಿಸಲಾಗಿದೆ! (Quote Sent: ₹$quotePrice)")
        }
    }

    // Transport / Logistics
    fun toggleSharedPool(enabled: Boolean) {
        _isSharedPool.value = enabled
    }

    fun selectVehicle(vehicleId: String) {
        _selectedVehicleId.value = vehicleId
    }

    fun bookTransport() {
        val vehicle = vehicleOptions.find { it.id == _selectedVehicleId.value }
        val vehicleName = vehicle?.nameKannada ?: "ವಾಹನ"
        viewModelScope.launch {
            _transportBookedMessage.emit("$vehicleName ಯಶಸ್ವಿಯಾಗಿ ಬುಕ್ ಮಾಡಲಾಗಿದೆ! ಚಾಲಕ ರಾಮಕೃಷ್ಣ 5 ನಿಮಿಷದಲ್ಲಿ ತಲುಪಲಿದ್ದಾರೆ.")
        }
    }

    // Add Crop Form Methods
    fun updateCategory(category: CropCategory) {
        _formState.update { it.copy(selectedCategory = category) }
    }

    fun updateCropName(kannada: String, english: String) {
        _formState.update { it.copy(cropNameKannada = kannada, cropNameEnglish = english) }
    }

    fun updateVariety(variety: String) {
        _formState.update { it.copy(variety = variety) }
    }

    fun updateFarmingMethod(method: FarmingMethod) {
        _formState.update { it.copy(farmingMethod = method) }
    }

    fun removePhoto(index: Int) {
        _formState.update {
            val list = it.photos.toMutableList()
            if (index in list.indices) {
                list.removeAt(index)
            }
            it.copy(photos = list)
        }
    }

    fun addPhoto(url: String) {
        _formState.update {
            it.copy(photos = it.photos + url)
        }
    }

    fun updateQuantity(quantity: String) {
        _formState.update { it.copy(quantity = quantity) }
    }

    fun updateQuantityUnit(unit: String) {
        _formState.update { it.copy(quantityUnit = unit) }
    }

    fun updatePriceValue(price: String) {
        _formState.update { it.copy(price = price) }
    }

    fun updateMoq(moq: String) {
        _formState.update { it.copy(moq = moq) }
    }

    fun updateHarvestDate(date: String) {
        _formState.update { it.copy(harvestDate = date) }
    }

    fun updateStockStatus(status: StockStatus) {
        _formState.update { it.copy(stockStatus = status) }
    }

    fun togglePhoneCalls(enabled: Boolean) {
        _formState.update { it.copy(phoneCallsEnabled = enabled) }
    }

    fun toggleWhatsApp(enabled: Boolean) {
        _formState.update { it.copy(whatsAppEnabled = enabled) }
    }

    fun toggleFarmPickup(enabled: Boolean) {
        _formState.update { it.copy(farmPickupEnabled = enabled) }
    }

    fun toggleHomeDelivery(enabled: Boolean) {
        _formState.update { it.copy(homeDeliveryEnabled = enabled) }
    }

    fun saveDraft() {
        _formState.update { it.copy(isDraftSaved = true) }
        viewModelScope.launch {
            _toastMessage.emit("ಡ್ರಾಫ್ಟ್ ಉಳಿಸಲಾಗಿದೆ! (Draft Saved)")
            delay(1800)
            _formState.update { it.copy(isDraftSaved = false) }
        }
    }

    fun publishCrop(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _formState.update { it.copy(isPublishing = true) }
            delay(1200)

            val state = _formState.value
            val priceInt = state.price.toIntOrNull() ?: 3400
            val qtyInt = state.quantity.toIntOrNull() ?: 45
            val photo = state.photos.firstOrNull() ?: KrishiRepository.RAGI_IMG_URL

            val newListing = CropListing(
                id = UUID.randomUUID().toString(),
                nameKannada = state.cropNameKannada,
                nameEnglish = state.cropNameEnglish,
                variety = state.variety,
                price = priceInt,
                unitKannada = state.quantityUnit.split(" ").firstOrNull() ?: "ಕ್ವಿಂಟಾಲ್",
                unitEnglish = "Unit",
                quantity = qtyInt,
                quantityUnit = state.quantityUnit,
                inquiriesCount = 0,
                stockStatus = state.stockStatus,
                imageUrl = photo,
                farmingMethod = state.farmingMethod,
                mandiPrice = (priceInt * 0.9).toInt(),
                mandiAdvantageText = "ಮಂಡಿ ದರಕ್ಕಿಂತ ನೇರ ಲಾಭ ನಿರೀಕ್ಷಿಸಲಾಗಿದೆ",
                harvestDate = state.harvestDate
            )

            repository.addCrop(newListing)
            _formState.update { it.copy(isPublishing = false) }
            _toastMessage.emit("ಬೆಳೆ ಯಶಸ್ವಿಯಾಗಿ ಪ್ರಕಟಿಸಲಾಗಿದೆ! (Published Successfully)")
            _currentTab.value = AppTab.DASHBOARD
            onSuccess()
        }
    }

    override fun onCleared() {
        super.onCleared()
        liveVoiceManager?.release()
    }
}
