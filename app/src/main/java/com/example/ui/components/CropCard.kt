package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.CropListing
import com.example.data.model.StockStatus
import com.example.ui.theme.AgriError
import com.example.ui.theme.AgriErrorContainer
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSurfaceContainer
import com.example.ui.theme.AgriSurfaceContainerHigh
import com.example.ui.theme.AgriSurfaceContainerLow
import com.example.ui.theme.AgriSurfaceContainerLowest
import com.example.ui.theme.AgriTertiary
import com.example.ui.theme.AgriTertiaryContainer
import com.example.ui.theme.AgriTertiaryFixed

@Composable
fun CropCard(
    crop: CropListing,
    onEditPrice: () -> Unit,
    onViewInquiries: () -> Unit,
    onRelist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("crop_card_${crop.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AgriSurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Thumbnail + Titles + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = crop.imageUrl,
                    contentDescription = crop.nameEnglish,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AgriSurfaceContainer),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                text = "${crop.nameKannada} (${crop.nameEnglish})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 16.sp
                                ),
                                maxLines = 1
                            )
                            Text(
                                text = crop.variety,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1
                            )
                        }

                        // Stock Status Badge
                        StatusBadge(crop.stockStatus)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Price Tag
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₹${crop.price}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (crop.stockStatus == StockStatus.SOLD_OUT)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    AgriPrimary,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "/ ${crop.unitKannada} (${crop.unitEnglish})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // If not sold out: 2 Metric Badges
            if (crop.stockStatus != StockStatus.SOLD_OUT) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Metric 1: Quantity
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriSurfaceContainerLow)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Quantity",
                            tint = AgriSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "ದಾಸ್ತಾನು (Quantity)",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${crop.quantity} ${crop.quantityUnit}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Metric 2: Inquiries / Demand
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriSurfaceContainerLow)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubble,
                            contentDescription = "Inquiries",
                            tint = AgriTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "ಖರೀದಿದಾರರು (Inquiries)",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${crop.inquiriesCount} ವಿನಂತಿಗಳು",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriTertiaryContainer
                            )
                        }
                    }
                }

                // Profit Advantage Banner
                if (crop.mandiAdvantageText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriSecondaryContainer.copy(alpha = 0.45f))
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Profit",
                                tint = AgriSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = crop.mandiAdvantageText,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "+8% ಲಾಭ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Actions: Edit Price + View Inquiries
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onEditPrice,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("crop_edit_button_${crop.id}"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AgriSurfaceContainerHigh
                        )
                    ) {
                        Text(
                            text = "ದರ ಬದಲಿಸಿ (Edit)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = onViewInquiries,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("crop_inquiries_button_${crop.id}"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary)
                    ) {
                        Icon(
                            imageVector = if (crop.inquiriesCount > 8) Icons.Default.Call else Icons.Default.Forum,
                            contentDescription = "Inquiries",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (crop.inquiriesCount > 8) "ಗ್ರಾಹಕರ ಸಂಪರ್ಕ (${crop.inquiriesCount})" else "ವಿಚಾರಣೆ ನೋಡಿ (${crop.inquiriesCount})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            } else {
                // Sold Out: Relist prompt card
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AgriSurfaceContainerLow)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ಹೊಸ ಕೊಯ್ಲು ಸಿದ್ಧವಾಗಿದೆಯೇ?",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onRelist() }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ಮರು-ನೋಂದಣಿ (Relist)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Relist",
                            tint = AgriPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: StockStatus) {
    val (bgColor, textColor, dotColor) = when (status) {
        StockStatus.IN_STOCK -> Triple(
            AgriSecondaryContainer.copy(alpha = 0.5f),
            AgriSecondary,
            AgriSecondary
        )
        StockStatus.LOW_STOCK -> Triple(
            AgriTertiaryFixed.copy(alpha = 0.7f),
            AgriTertiaryContainer,
            AgriTertiaryContainer
        )
        StockStatus.COMING_SOON -> Triple(
            AgriSurfaceContainerHigh,
            MaterialTheme.colorScheme.onSurfaceVariant,
            AgriTertiaryContainer
        )
        StockStatus.SOLD_OUT -> Triple(
            AgriErrorContainer.copy(alpha = 0.6f),
            AgriError,
            AgriError
        )
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status.kannadaLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
