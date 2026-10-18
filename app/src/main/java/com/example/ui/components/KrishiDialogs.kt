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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.BuyerInquiry
import com.example.data.model.CropListing
import com.example.data.repository.KrishiRepository
import com.example.ui.theme.AgriOutlineVariant
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryContainer
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSurfaceContainer
import com.example.ui.theme.AgriSurfaceContainerLow
import com.example.ui.theme.AgriTertiaryContainer

@Composable
fun EditPriceDialog(
    crop: CropListing,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var priceText by remember { mutableStateOf(crop.price.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = null,
                    tint = AgriPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${crop.nameKannada} - ದರ ಬದಲಿಸಿ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "ಪ್ರಸ್ತುತ ನಿಗದಿತ ದರ: ₹${crop.price} / ${crop.unitKannada}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("ಹೊಸ ನಿರೀಕ್ಷಿತ ದರ (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("price_input_field"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ಮಂಡ್ಯ APMC ಮಾರುಕಟ್ಟೆ ದರ: ₹${crop.mandiPrice}",
                    fontSize = 11.sp,
                    color = AgriSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceText.toIntOrNull() ?: crop.price
                    onSave(p)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary),
                modifier = Modifier.testTag("save_price_button")
            ) {
                Text("ಉಳಿಸಿ (Save)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ರದ್ದು (Cancel)")
            }
        }
    )
}

@Composable
fun PriceQuoteDialog(
    inquiry: BuyerInquiry,
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var quotePrice by remember { mutableStateOf("3200") }
    var note by remember { mutableStateOf("ಮನೆ ಬಾಗಿಲಿಗೆ ಸಾಗಾಣಿಕೆ ವೆಚ್ಚ ಸೇರಿ / Farm Direct Delivery Included") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Reply,
                    contentDescription = null,
                    tint = AgriPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${inquiry.buyerNameKannada} ಅವರಿಗೆ ದರ ಉಲ್ಲೇಖ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        text = {
            Column {
                Text(
                    text = inquiry.message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = quotePrice,
                    onValueChange = { quotePrice = it },
                    label = { Text("ನಿಮ್ಮ ದರ ಕೊಡುಗೆ (Quote Price ₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("ವಿವರಣೆ / ಷರಾ (Note)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSend(quotePrice) },
                colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary),
                modifier = Modifier.testTag("submit_quote_button")
            ) {
                Text("ಕಳುಹಿಸಿ (Send)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ರದ್ದು (Cancel)")
            }
        }
    )
}

@Composable
fun VoiceAssistantDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AgriPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "ಧ್ವನಿ ಸಹಾಯ (Voice Assistant)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(AgriSecondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = AgriPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "ಕೇಳಿಸಿಕೊಳ್ಳುತ್ತಿದೆ... (Listening in Kannada)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = AgriPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ಹೇಳಿ: 'ಹೊಸ ಬೆಳೆ ಸೇರಿಸಿ', 'ಇಂದಿನ ರಾಗಿ ಮಂಡಿ ದರ ಎಷ್ಟು?', ಅಥವಾ 'ವಾಹನ ಬುಕ್ ಮಾಡು'",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary)
            ) {
                Text("ಮುಗಿಸಿ (Done)")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = AgriPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ಸೂಚನೆಗಳು (Notifications)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val items = listOf(
                Pair("ಮಂಡ್ಯ APMC ರಾಗಿ ದರ ಏರಿಕೆ!", "ಇಂದು ರಾಗಿ ಗರಿಷ್ಠ ದರ ₹3,400 ತಲುಪಿದೆ. ನಿಮ್ಮ ಸ್ಟಾಕ್ ಮಾರಾಟಕ್ಕೆ ಸಕಾಲ. • 15 ನಿಮಿಷದ ಹಿಂದೆ"),
                Pair("ಬೆಂಗಳೂರಿನಿಂದ ಹೊಸ ಖರೀದಿ ವಿನಂತಿ", "ಸುರೇಶ್ ಕುಮಾರ್ ಅವರು 5 ಕ್ವಿಂಟಾಲ್ ರಾಗಿಗಾಗಿ ವಿಚಾರಿಸಿದ್ದಾರೆ. • 30 ನಿಮಿಷದ ಹಿಂದೆ"),
                Pair("ಸಾರಿಗೆ ಸಬ್ಸಿಡಿ ಅನುಮೋದನೆ", "ನಿಮ್ಮ ಶಿವಳ್ಳಿ ಗದ್ದೆಯ ಸಾರಿಗೆ ಪೂಲ್‌ಗೆ ₹1,000 ರಿಯಾಯಿತಿ ಸಿಕ್ಕಿದೆ. • 2 ಗಂಟೆ ಹಿಂದೆ")
            )

            items.forEach { (title, desc) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AgriSurfaceContainerLow)
                        .padding(12.dp)
                ) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AgriPrimary)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = KrishiRepository.FARMER_AVATAR_URL,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AgriPrimary),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ಬಸವರಾಜ್ ಗೌಡ (Basavaraj Gowda)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = AgriSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ಹಸಿರು ಪ್ರಮಾಣಿತ ರೈತ (Green Verified Farmer)",
                    fontSize = 12.sp,
                    color = AgriSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AgriSurfaceContainerLow)
                    .padding(14.dp)
            ) {
                Text(text = "ಜಮೀನಿನ ವಿವರ (Farm Details)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "ಗ್ರಾಮ: ಶಿವಳ್ಳಿ ಗ್ರಾಮ, ಮಂಡ್ಯ ತಾಲ್ಲೂಕು (Karnataka - 571405)", fontSize = 12.sp)
                Text(text = "ವಿಸ್ತೀರ್ಣ: 4 ಎಕರೆ ಸಾವಯವ ಕೃಷಿ ಭೂಮಿ", fontSize = 12.sp)
                Text(text = "ಸದಸ್ಯತ್ವ ID: KB-MND-84920", fontSize = 12.sp)
                Text(text = "ಸಂಪರ್ಕ: +91 98450 12345", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
