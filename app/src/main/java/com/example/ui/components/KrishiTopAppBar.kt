package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.repository.KrishiRepository
import com.example.ui.theme.AgriOutlineVariant
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryFixed
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSurfaceContainerHigh
import com.example.ui.theme.AgriTertiaryContainer

@Composable
fun KrishiTopAppBar(
    isKannadaPrimary: Boolean,
    notificationCount: Int = 3,
    onLanguageToggle: () -> Unit,
    onAiChatClick: () -> Unit = {},
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("krishi_top_app_bar"),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo Emblem
            AsyncImage(
                model = KrishiRepository.KRISHI_LOGO_URL,
                contentDescription = "KrishiBandhu Emblem",
                modifier = Modifier
                    .height(34.dp)
                    .width(34.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Title & Mandya Location
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "KrishiBandhu",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AgriPrimary,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = " • ",
                        color = AgriOutlineVariant,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "ರೈತ ಮಿತ್ರ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = AgriSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Mandya (ಮಂಡ್ಯ)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Language Toggle Switch [KN | EN]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AgriSurfaceContainerHigh)
                    .clickable { onLanguageToggle() }
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .testTag("language_toggle_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isKannadaPrimary) AgriPrimary else Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "KN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isKannadaPrimary) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (!isKannadaPrimary) AgriPrimary else Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "EN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isKannadaPrimary) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Notifications Bell with Badge
            Box {
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("notifications_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
                if (notificationCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(AgriTertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = notificationCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // AI Chatbot Button
            IconButton(
                onClick = onAiChatClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AgriSecondaryContainer.copy(alpha = 0.5f))
                    .testTag("top_bar_ai_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Krishi Mitra AI",
                    tint = AgriPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Farmer Profile Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onProfileClick() }
                    .testTag("farmer_profile_avatar"),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = KrishiRepository.FARMER_AVATAR_URL,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(AgriPrimaryFixed),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
