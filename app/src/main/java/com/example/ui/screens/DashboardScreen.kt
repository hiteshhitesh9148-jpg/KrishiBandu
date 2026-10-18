package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuyerInquiry
import com.example.data.model.CropListing
import com.example.data.model.MandiComparison
import com.example.data.model.VehicleOption
import com.example.ui.components.ApmcComparisonCard
import com.example.ui.components.CropCard
import com.example.ui.components.HelplineCard
import com.example.ui.components.InquiriesSection
import com.example.ui.components.TransportSection
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryContainer
import com.example.ui.theme.AgriPrimaryFixedDim
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSecondaryFixed
import com.example.ui.theme.AgriTertiaryFixed

@Composable
fun DashboardScreen(
    crops: List<CropListing>,
    inquiries: List<BuyerInquiry>,
    mandiComparisons: List<MandiComparison>,
    vehicleOptions: List<VehicleOption>,
    isSharedPool: Boolean,
    selectedVehicleId: String,
    onAddCropClick: () -> Unit,
    onVoiceAssistClick: () -> Unit,
    onAiChatClick: () -> Unit = {},
    onEditPrice: (CropListing) -> Unit,
    onViewInquiries: (CropListing) -> Unit,
    onRelist: (CropListing) -> Unit,
    onToggleSharedPool: (Boolean) -> Unit,
    onSelectVehicle: (String) -> Unit,
    onCallDriver: () -> Unit,
    onBookTransport: () -> Unit,
    onWhatsAppInquiry: (BuyerInquiry) -> Unit,
    onCallInquiry: (BuyerInquiry) -> Unit,
    onSendQuoteInquiry: (BuyerInquiry) -> Unit,
    onHelplineClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Farmer Ambient Greeting Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("farmer_greeting_banner"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    AgriPrimary,
                                    AgriPrimaryContainer,
                                    AgriSecondary
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Verification Pill & Audio Trigger
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Verified Farmer Badge
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(AgriSecondaryFixed.copy(alpha = 0.22f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AgriSecondaryFixed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ಹಸಿರು ಪ್ರಮಾಣಿತ • Verified Farmer",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriSecondaryFixed
                                )
                            }

                            // Voice Guide Audio Button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .clickable { onVoiceAssistClick() }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                    .testTag("voice_guide_button"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Voice Guide",
                                    tint = AgriTertiaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ಧ್ವನಿ ಸಹಾಯ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Greeting
                        Text(
                            text = "ನಮಸ್ಕಾರ, ಬಸವರಾಜ್ ಅವರೇ!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PinDrop,
                                contentDescription = null,
                                tint = AgriPrimaryFixedDim,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "ಶಿವಳ್ಳಿ ಗ್ರಾಮ, ಮಂಡ್ಯ ಜಿಲ್ಲೆ (Shivalli, Mandya)",
                                fontSize = 12.sp,
                                color = AgriPrimaryFixedDim
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 3 Live Metric Strips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Metric 1: Active Crops
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "ಬೆಳೆಗಳು (Active)",
                                    fontSize = 10.sp,
                                    color = AgriPrimaryFixedDim
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${crops.size} ",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "ಬೆಳೆ",
                                        fontSize = 11.sp,
                                        color = AgriSecondaryFixed
                                    )
                                }
                            }

                            // Metric 2: Buyer Inquiries
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "ವಿಚಾರಣೆ (Buyers)",
                                    fontSize = 10.sp,
                                    color = AgriPrimaryFixedDim
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "18 ",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "+5 ಹೊಸದು",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriTertiaryFixed
                                    )
                                }
                            }

                            // Metric 3: Total Stock Value
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.12f))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "ದಾಸ್ತಾನು ಮೌಲ್ಯ",
                                    fontSize = 10.sp,
                                    color = AgriPrimaryFixedDim
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "₹42.5k",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Big Prominent Action: List Crop Button
        item {
            Button(
                onClick = onAddCropClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .testTag("list_new_crop_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AgriSecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = AgriPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "+ ಹೊಸ ಬೆಳೆ ಸೇರಿಸಿ",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "List New Crop for Direct Consumer Sale",
                                fontSize = 11.sp,
                                color = AgriPrimaryFixedDim
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Go",
                        tint = AgriSecondaryFixed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Krishi Mitra AI Card (Gemini 3.8 Live & Chatbot)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("krishi_mitra_ai_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgriSecondaryContainer.copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(AgriPrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "ಕೃಷಿ ಮಿತ್ರ AI ಸಲಹೆಗಾರ",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(AgriPrimary)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Gemini AI",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                Text(
                                    text = "ಮಂಡ್ಯ ಮಂಡಿ ದರ, ರೋಗ ತಡೆಗಟ್ಟುವಿಕೆ ಮತ್ತು ಲೈವ್ ಧ್ವನಿ ಸಂವಾದ",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Live Voice Button (gemini-3.8-live)
                        Button(
                            onClick = onVoiceAssistClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("dashboard_live_voice_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ಧ್ವನಿ ಸಂವಾದ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Chatbot Button (gemini-3.8-flash / 3.1-flash-lite / 4-argon)
                        Button(
                            onClick = onAiChatClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("dashboard_ai_chat_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AgriSecondary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ಚಾಟ್ ಮಾಡಿ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3. Section Title: Crop Listings
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(AgriPrimary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ನನ್ನ ಲಭ್ಯ ಬೆಳೆಗಳು",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${crops.size} Listings)",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .clickable { /* Filter or View All */ }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ಎಲ್ಲವೂ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "All",
                        tint = AgriSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // 4. Crop Cards
        items(crops, key = { it.id }) { crop ->
            CropCard(
                crop = crop,
                onEditPrice = { onEditPrice(crop) },
                onViewInquiries = { onViewInquiries(crop) },
                onRelist = { onRelist(crop) }
            )
        }

        // 5. APMC Mandi Rate Comparison Bulletin
        item {
            ApmcComparisonCard(comparisons = mandiComparisons)
        }

        // 6. Farm Transport & Logistics Facility
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

        // 7. Recent Consumer Inquiries
        item {
            InquiriesSection(
                inquiries = inquiries,
                onWhatsAppClick = onWhatsAppInquiry,
                onCallClick = onCallInquiry,
                onSendQuoteClick = onSendQuoteInquiry
            )
        }

        // 8. Kisan Helpline Support Card
        item {
            HelplineCard(onCallClick = onHelplineClick)
        }

        // Bottom space buffer
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
