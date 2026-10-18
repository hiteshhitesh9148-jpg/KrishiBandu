package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.EditPriceDialog
import com.example.ui.components.HelplineCard
import com.example.ui.components.InquiriesSection
import com.example.ui.components.KrishiBottomNav
import com.example.ui.components.KrishiTopAppBar
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.components.PriceQuoteDialog
import com.example.ui.components.ProfileBottomSheet
import com.example.ui.components.TransportSection
import com.example.ui.components.VoiceAssistantDialog
import com.example.ui.screens.AddCropScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GeminiChatScreen
import com.example.ui.screens.LiveVoiceConversationModal
import com.example.ui.screens.MyCropsScreen
import com.example.ui.theme.KrishiBandhuTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.KrishiViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrishiBandhuTheme {
                KrishiApp()
            }
        }
    }
}

@Composable
fun KrishiApp(
    viewModel: KrishiViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val isKannadaPrimary by viewModel.isKannadaPrimary.collectAsStateWithLifecycle()
    val crops by viewModel.crops.collectAsStateWithLifecycle()
    val inquiries by viewModel.inquiries.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    val editingCrop by viewModel.editingCrop.collectAsStateWithLifecycle()
    val replyingInquiry by viewModel.replyingInquiry.collectAsStateWithLifecycle()
    val showVoiceDialog by viewModel.showVoiceDialog.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()
    val showProfileSheet by viewModel.showProfileSheet.collectAsStateWithLifecycle()

    // Gemini Chatbot & Live Voice States
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()

    val showLiveVoiceModal by viewModel.showLiveVoiceModal.collectAsStateWithLifecycle()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()
    val liveTranscribedText by viewModel.liveTranscribedText.collectAsStateWithLifecycle()
    val liveAiResponseText by viewModel.liveAiResponseText.collectAsStateWithLifecycle()
    val audioAmplitude by viewModel.audioAmplitude.collectAsStateWithLifecycle()

    val isSharedPool by viewModel.isSharedPool.collectAsStateWithLifecycle()
    val selectedVehicleId by viewModel.selectedVehicleId.collectAsStateWithLifecycle()

    // Back handling
    BackHandler(enabled = currentTab != AppTab.DASHBOARD) {
        viewModel.setTab(AppTab.DASHBOARD)
    }

    // Collect toast & booked messages
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.transportBookedMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Helper functions for calling and messaging
    val dialPhone = { number: String ->
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "ಕರೆ ಮಾಡಲಾಗುತ್ತಿದೆ: $number", Toast.LENGTH_SHORT).show()
        }
    }

    val openWhatsApp = { inquiryName: String ->
        Toast.makeText(context, "$inquiryName ಅವರೊಂದಿಗೆ WhatsApp ಚಾಟ್ ತೆರೆಯಲಾಗುತ್ತಿದೆ...", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentTab != AppTab.ADD_CROP) {
                KrishiTopAppBar(
                    isKannadaPrimary = isKannadaPrimary,
                    notificationCount = 3,
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onAiChatClick = { viewModel.setTab(AppTab.AI_CHAT) },
                    onNotificationsClick = { viewModel.openNotifications() },
                    onProfileClick = { viewModel.openProfile() }
                )
            }
        },
        bottomBar = {
            if (currentTab != AppTab.ADD_CROP) {
                KrishiBottomNav(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.setTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentTab,
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    AppTab.DASHBOARD -> {
                        DashboardScreen(
                            crops = crops,
                            inquiries = inquiries,
                            mandiComparisons = viewModel.mandiComparisons,
                            vehicleOptions = viewModel.vehicleOptions,
                            isSharedPool = isSharedPool,
                            selectedVehicleId = selectedVehicleId,
                            onAddCropClick = { viewModel.setTab(AppTab.ADD_CROP) },
                            onVoiceAssistClick = { viewModel.openLiveVoice(context) },
                            onAiChatClick = { viewModel.setTab(AppTab.AI_CHAT) },
                            onEditPrice = { viewModel.startEditingPrice(it) },
                            onViewInquiries = { viewModel.setTab(AppTab.INQUIRIES) },
                            onRelist = { viewModel.relist(it.id) },
                            onToggleSharedPool = { viewModel.toggleSharedPool(it) },
                            onSelectVehicle = { viewModel.selectVehicle(it) },
                            onCallDriver = { dialPhone("+919845012345") },
                            onBookTransport = { viewModel.bookTransport() },
                            onWhatsAppInquiry = { openWhatsApp(it.buyerNameEnglish) },
                            onCallInquiry = { dialPhone(it.phoneNumber) },
                            onSendQuoteInquiry = { viewModel.startReplying(it) },
                            onHelplineClick = { dialPhone("18004251555") }
                        )
                    }

                    AppTab.MY_CROPS -> {
                        MyCropsScreen(
                            crops = crops,
                            onAddCropClick = { viewModel.setTab(AppTab.ADD_CROP) },
                            onEditPrice = { viewModel.startEditingPrice(it) },
                            onViewInquiries = { viewModel.setTab(AppTab.INQUIRIES) },
                            onRelist = { viewModel.relist(it.id) }
                        )
                    }

                    AppTab.ADD_CROP -> {
                        AddCropScreen(
                            formState = formState,
                            onBack = { viewModel.setTab(AppTab.DASHBOARD) },
                            onCategoryChange = { viewModel.updateCategory(it) },
                            onCropNameChange = { kannada, english -> viewModel.updateCropName(kannada, english) },
                            onVarietyChange = { viewModel.updateVariety(it) },
                            onFarmingMethodChange = { viewModel.updateFarmingMethod(it) },
                            onRemovePhoto = { viewModel.removePhoto(it) },
                            onAddPhotoClick = {
                                Toast.makeText(context, "ಕ್ಯಾಮೆರಾ ತೆರೆಯಲಾಗಿದೆ (Camera Opened)", Toast.LENGTH_SHORT).show()
                            },
                            onQuantityChange = { viewModel.updateQuantity(it) },
                            onQuantityUnitChange = { viewModel.updateQuantityUnit(it) },
                            onPriceChange = { viewModel.updatePriceValue(it) },
                            onMoqChange = { viewModel.updateMoq(it) },
                            onHarvestDateChange = { viewModel.updateHarvestDate(it) },
                            onStockStatusChange = { viewModel.updateStockStatus(it) },
                            onTogglePhoneCalls = { viewModel.togglePhoneCalls(it) },
                            onToggleWhatsApp = { viewModel.toggleWhatsApp(it) },
                            onToggleFarmPickup = { viewModel.toggleFarmPickup(it) },
                            onToggleHomeDelivery = { viewModel.toggleHomeDelivery(it) },
                            onSaveDraft = { viewModel.saveDraft() },
                            onPublish = {
                                viewModel.publishCrop {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("ಬೆಳೆ ಯಶಸ್ವಿಯಾಗಿ ಪ್ರಕಟಿಸಲಾಗಿದೆ! (Crop Published)")
                                    }
                                }
                            }
                        )
                    }

                    AppTab.INQUIRIES -> {
                        InquiriesScreenView(
                            inquiries = inquiries,
                            onWhatsAppClick = { openWhatsApp(it.buyerNameEnglish) },
                            onCallClick = { dialPhone(it.phoneNumber) },
                            onSendQuoteClick = { viewModel.startReplying(it) }
                        )
                    }

                    AppTab.LOGISTICS -> {
                        LogisticsScreenView(
                            isSharedPool = isSharedPool,
                            selectedVehicleId = selectedVehicleId,
                            vehicleOptions = viewModel.vehicleOptions,
                            onToggleSharedPool = { viewModel.toggleSharedPool(it) },
                            onSelectVehicle = { viewModel.selectVehicle(it) },
                            onCallDriver = { dialPhone("+919845012345") },
                            onBookTransport = { viewModel.bookTransport() }
                        )
                    }

                    AppTab.AI_CHAT -> {
                        GeminiChatScreen(
                            messages = chatMessages,
                            selectedModel = selectedModel,
                            isLoading = isAiThinking,
                            onModelSelect = { viewModel.selectAiModel(it) },
                            onSendMessage = { viewModel.sendChatMessage(it) },
                            onOpenLiveVoice = { viewModel.openLiveVoice(context) },
                            onClearChat = { viewModel.clearChat() }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs
    editingCrop?.let { crop ->
        EditPriceDialog(
            crop = crop,
            onDismiss = { viewModel.dismissEditingPrice() },
            onSave = { newPrice -> viewModel.savePrice(crop.id, newPrice) }
        )
    }

    replyingInquiry?.let { inquiry ->
        PriceQuoteDialog(
            inquiry = inquiry,
            onDismiss = { viewModel.dismissReplying() },
            onSend = { price -> viewModel.sendPriceQuote(inquiry.id, price) }
        )
    }

    if (showVoiceDialog) {
        VoiceAssistantDialog(onDismiss = { viewModel.dismissVoiceAssistant() })
    }

    if (showNotificationsSheet) {
        NotificationsBottomSheet(onDismiss = { viewModel.dismissNotifications() })
    }

    if (showProfileSheet) {
        ProfileBottomSheet(onDismiss = { viewModel.dismissProfile() })
    }

    // Live Voice Modal (gemini-3.8-live)
    if (showLiveVoiceModal) {
        LiveVoiceConversationModal(
            voiceState = voiceState,
            transcribedText = liveTranscribedText,
            aiResponseText = liveAiResponseText,
            audioAmplitude = audioAmplitude,
            onStartListening = { viewModel.startListeningLive() },
            onStopSpeaking = { viewModel.stopSpeakingLive() },
            onQuickPrompt = { viewModel.sendLiveQuickPrompt(it) },
            onDismiss = { viewModel.dismissLiveVoice() }
        )
    }
}

@Composable
private fun InquiriesScreenView(
    inquiries: List<com.example.data.model.BuyerInquiry>,
    onWhatsAppClick: (com.example.data.model.BuyerInquiry) -> Unit,
    onCallClick: (com.example.data.model.BuyerInquiry) -> Unit,
    onSendQuoteClick: (com.example.data.model.BuyerInquiry) -> Unit
) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)
    ) {
        item {
            InquiriesSection(
                inquiries = inquiries,
                onWhatsAppClick = onWhatsAppClick,
                onCallClick = onCallClick,
                onSendQuoteClick = onSendQuoteClick
            )
        }
        item {
            HelplineCard(onCallClick = {})
        }
    }
}

@Composable
private fun LogisticsScreenView(
    isSharedPool: Boolean,
    selectedVehicleId: String,
    vehicleOptions: List<com.example.data.model.VehicleOption>,
    onToggleSharedPool: (Boolean) -> Unit,
    onSelectVehicle: (String) -> Unit,
    onCallDriver: () -> Unit,
    onBookTransport: () -> Unit
) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)
    ) {
        item {
            TransportSection(
                isSharedPool = isSharedPool,
                selectedVehicleId = selectedVehicleId,
                vehicleOptions = vehicleOptions,
                onToggleSharedPool = onToggleSharedPool,
                onSelectVehicle = onSelectVehicle,
                onCallDriver = onCallDriver,
                onBookTransport = onBookTransport
            )
        }
        item {
            HelplineCard(onCallClick = {})
        }
    }
}
