package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.LiveVoiceState
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryContainer
import com.example.ui.theme.AgriPrimaryFixed
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSecondaryFixed
import com.example.ui.theme.AgriSurfaceContainer
import com.example.ui.theme.AgriSurfaceContainerHigh
import com.example.ui.theme.AgriSurfaceContainerLow
import com.example.ui.theme.AgriTertiaryContainer
import com.example.ui.theme.AgriTertiaryFixed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveVoiceConversationModal(
    voiceState: LiveVoiceState,
    transcribedText: String,
    aiResponseText: String,
    audioAmplitude: Float,
    onStartListening: () -> Unit,
    onStopSpeaking: () -> Unit,
    onQuickPrompt: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.testTag("live_voice_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Title, Live API Badge & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AgriPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ಧ್ವನಿ ಸಂವಾದ (Live Voice)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(AgriSecondary)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "gemini-3.8-live (Live API)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriSecondary
                            )
                        }
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // State Indicator & Animated Sound Visualizer
            val statusText = when (voiceState) {
                LiveVoiceState.LISTENING -> "ಕೇಳಿಸಿಕೊಳ್ಳುತ್ತಿದೆ... (Listening)"
                LiveVoiceState.THINKING -> "ವಿಶ್ಲೇಷಿಸುತ್ತಿದೆ... (Analyzing with Live API)"
                LiveVoiceState.SPEAKING -> "ಉತ್ತರಿಸುತ್ತಿದೆ... (Speaking)"
                LiveVoiceState.IDLE -> "ಮಾತನಾಡಲು ಮೈಕ್ ಒತ್ತಿ (Tap to Talk)"
            }

            Text(
                text = statusText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = when (voiceState) {
                    LiveVoiceState.LISTENING -> AgriSecondary
                    LiveVoiceState.THINKING -> AgriTertiaryContainer
                    LiveVoiceState.SPEAKING -> AgriPrimary
                    LiveVoiceState.IDLE -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Pulsing Audio Visualizer Ring
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (voiceState == LiveVoiceState.LISTENING || voiceState == LiveVoiceState.SPEAKING) 1.25f else 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                // Outer Pulse Ring
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            when (voiceState) {
                                LiveVoiceState.LISTENING -> AgriSecondaryContainer.copy(alpha = 0.45f)
                                LiveVoiceState.SPEAKING -> AgriPrimaryFixed.copy(alpha = 0.45f)
                                LiveVoiceState.THINKING -> AgriTertiaryFixed.copy(alpha = 0.45f)
                                LiveVoiceState.IDLE -> AgriSurfaceContainerHigh.copy(alpha = 0.45f)
                            }
                        )
                )

                // Middle Ring
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    AgriPrimary,
                                    AgriSecondary
                                )
                            )
                        )
                        .clickable {
                            if (voiceState == LiveVoiceState.SPEAKING) {
                                onStopSpeaking()
                            } else {
                                onStartListening()
                            }
                        }
                        .testTag("live_mic_action_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (voiceState == LiveVoiceState.SPEAKING) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = Color.White,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Transcription & AI Response Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_transcription_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgriSurfaceContainerLow),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Farmer Speech
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AgriSecondary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ನೀವು ಹೇಳಿದ್ದು (You said):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = transcribedText.ifEmpty { "..." },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    // Gemini 3.8 Live Response
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = AgriPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ಕೃಷಿ ಮಿತ್ರ (Gemini 3.8 Live):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriPrimary
                        )
                    }
                    Text(
                        text = aiResponseText.ifEmpty { "ಲೈವ್ ಉತ್ತರ ಕೇಳಲು ಧ್ವನಿ ನೀಡಿ..." },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Spoken Prompt Pills
            Text(
                text = "ತ್ವರಿತ ಪ್ರಶ್ನೆಗಳು (Quick Voice Prompts):",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val prompts = listOf(
                    "ಇಂದಿನ ರಾಗಿ ದರ",
                    "ಸಾವಯವ ಜೀವಾಮೃತ",
                    "ಬೆಂಗಳೂರು ಸಾರಿಗೆ"
                )
                prompts.forEach { prompt ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AgriSurfaceContainer)
                            .clickable { onQuickPrompt(prompt) }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = prompt,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = AgriPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
