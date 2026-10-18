package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VehicleOption
import com.example.ui.theme.AgriOutlineVariant
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryContainer
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSecondaryFixed
import com.example.ui.theme.AgriSurfaceContainerHigh
import com.example.ui.theme.AgriSurfaceContainerLow
import com.example.ui.theme.AgriSurfaceContainerLowest
import com.example.ui.theme.AgriTertiary
import com.example.ui.theme.AgriTertiaryFixed

@Composable
fun TransportSection(
    isSharedPool: Boolean,
    selectedVehicleId: String,
    vehicleOptions: List<VehicleOption>,
    onToggleSharedPool: (Boolean) -> Unit,
    onSelectVehicle: (String) -> Unit,
    onCallDriver: () -> Unit,
    onBookTransport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("transport_logistics_section")
    ) {
        // Section Title
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
                        .background(AgriSecondary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ಸಾರಿಗೆ & ವಾಹನ ಸೌಲಭ್ಯ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(Logistics)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AgriSecondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Available",
                        tint = AgriSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "12 ವಾಹನ ಲಭ್ಯ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Transport Container Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Header & Subtitle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ಗದ್ದೆಯಿಂದ ಮಾರುಕಟ್ಟೆಗೆ ನೇರ ಸಾರಿಗೆ",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "Farm-to-Market & Consumer Direct Pickup in Mandya",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriPrimaryContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "ಸಬ್ಸಿಡಿ ಸೌಲಭ್ಯ",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Pooled / Shared Transport Toggle Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AgriSecondaryContainer.copy(alpha = 0.35f))
                        .border(1.dp, AgriSecondary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AgriSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShareLocation,
                                contentDescription = "Shared Pool",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ಹಂಚಿಕೆಯ ಸಾರಿಗೆ (Shared Pool)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AgriTertiaryFixed)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "40% ಉಳಿತಾಯ",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriTertiary
                                    )
                                }
                            }
                            Text(
                                text = "ಇತರೆ ರೈತರೊಂದಿಗೆ ವಾಹನ ಹಂಚಿಕೊಳ್ಳಿ • ಕಡಿಮೆ ಬಾಡಿಗೆ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isSharedPool,
                        onCheckedChange = onToggleSharedPool,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AgriPrimary
                        ),
                        modifier = Modifier.testTag("shared_pool_switch")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Route & Instant Freight Estimation Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AgriSurfaceContainerLow)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Route,
                                contentDescription = "Route",
                                tint = AgriPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ಮಾರ್ಗ ಅಂದಾಜು (Route & Freight)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriPrimary
                            )
                        }
                        Text(
                            text = "ದೂರ: 104 km",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriSurfaceContainerLowest)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AgriSecondary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ಶಿವಳ್ಳಿ ಗದ್ದೆ (Shivalli Farm)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(AgriTertiary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ಬೆಂಗಳೂರು KR Market / Hub",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            val priceText = if (isSharedPool) "₹1,450" else "₹2,400"
                            Text(
                                text = priceText,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriPrimary
                            )
                            if (isSharedPool) {
                                Text(
                                    text = "₹2,400",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textDecoration = TextDecoration.LineThrough
                                )
                                Text(
                                    text = "ಪೂಲ್ ದರ (Shared)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Vehicle Choices
                Text(
                    text = "ಲಭ್ಯವಿರುವ ಸ್ಥಳೀಯ ವಾಹನಗಳು (Select Vehicle)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    vehicleOptions.forEach { vehicle ->
                        val isSelected = vehicle.id == selectedVehicleId
                        val vIcon = when (vehicle.iconType) {
                            "fire_truck" -> Icons.Default.FireTruck
                            "airport_shuttle" -> Icons.Default.AirportShuttle
                            else -> Icons.Default.LocalShipping
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) AgriPrimary else AgriOutlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(
                                    if (isSelected) AgriSecondaryContainer.copy(alpha = 0.25f)
                                    else AgriSurfaceContainerLow
                                )
                                .clickable { onSelectVehicle(vehicle.id) }
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                                .testTag("vehicle_option_${vehicle.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(AgriPrimary)
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "ಆಯ್ಕೆ",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                }

                                Icon(
                                    imageVector = vIcon,
                                    contentDescription = vehicle.nameEnglish,
                                    tint = if (isSelected) AgriPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = vehicle.nameKannada,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = vehicle.capacity,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "₹${vehicle.ratePerKm}/km",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AgriPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Verified Driver Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(AgriSurfaceContainerLow)
                        .padding(10.dp),
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
                            Text(
                                text = "ರಾ",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ರಾಮಕೃಷ್ಣ (Ramakrishna)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Driver",
                                    tint = AgriSecondary,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = "ಮಂಡ್ಯ ಚಾಲಕ • KA-11-F-4219 • 4.9 ★",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriSecondaryFixed.copy(alpha = 0.4f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "5 ನಿಮಿಷ ದೂರ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Actions: Call Driver + Book Transport
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onCallDriver,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("call_driver_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AgriSurfaceContainerHigh
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = AgriPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ಚಾಲಕರ ಕರೆ (Call)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = onBookTransport,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("book_transport_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = "Book",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ವಾಹನ ಬುಕ್ ಮಾಡಿ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
