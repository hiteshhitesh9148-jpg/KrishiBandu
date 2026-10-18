package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compost
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.model.CropCategory
import com.example.data.model.FarmingMethod
import com.example.data.model.StockStatus
import com.example.data.repository.KrishiRepository
import com.example.ui.theme.AgriError
import com.example.ui.theme.AgriOutlineVariant
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryContainer
import com.example.ui.theme.AgriPrimaryFixed
import com.example.ui.theme.AgriSecondary
import com.example.ui.theme.AgriSecondaryContainer
import com.example.ui.theme.AgriSecondaryFixed
import com.example.ui.theme.AgriSurfaceContainer
import com.example.ui.theme.AgriSurfaceContainerHigh
import com.example.ui.theme.AgriSurfaceContainerHighest
import com.example.ui.theme.AgriSurfaceContainerLow
import com.example.ui.theme.AgriSurfaceContainerLowest
import com.example.ui.theme.AgriTertiary
import com.example.ui.theme.AgriTertiaryContainer
import com.example.ui.theme.AgriTertiaryFixed
import com.example.ui.viewmodel.AddCropFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCropScreen(
    formState: AddCropFormState,
    onBack: () -> Unit,
    onCategoryChange: (CropCategory) -> Unit,
    onCropNameChange: (String, String) -> Unit,
    onVarietyChange: (String) -> Unit,
    onFarmingMethodChange: (FarmingMethod) -> Unit,
    onRemovePhoto: (Int) -> Unit,
    onAddPhotoClick: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onQuantityUnitChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onMoqChange: (String) -> Unit,
    onHarvestDateChange: (String) -> Unit,
    onStockStatusChange: (StockStatus) -> Unit,
    onTogglePhoneCalls: (Boolean) -> Unit,
    onToggleWhatsApp: (Boolean) -> Unit,
    onToggleFarmPickup: (Boolean) -> Unit,
    onToggleHomeDelivery: (Boolean) -> Unit,
    onSaveDraft: () -> Unit,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("add_crop_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("add_crop_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        AsyncImage(
                            model = KrishiRepository.KRISHI_LOGO_URL,
                            contentDescription = "Emblem",
                            modifier = Modifier
                                .height(32.dp)
                                .width(32.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Add Crop",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp
                            )
                        )
                    }

                    // Farmer Profile
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

            // Scrollable Form Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stepper Tracker
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AgriSurfaceContainerLow),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(AgriPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "3/3",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ಅಂತಿಮ ಹಂತ (Final Details)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = AgriSecondary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "೮೫% ಪೂರ್ಣಗೊಂಡಿದೆ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { 0.85f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = AgriPrimary,
                                trackColor = AgriSurfaceContainerHighest
                            )
                        }
                    }
                }

                // Section 1: Crop Category & Variety
                item {
                    FormSectionCard(
                        stepLabel = "ಹಂತ ೧",
                        icon = Icons.Default.Spa,
                        title = "೧. ಬೆಳೆಯ ವಿವರ (Category & Variety)",
                        subtitle = "ಬೆಳೆ ಪ್ರಕಾರ ಮತ್ತು ತಳಿ ಆಯ್ಕೆಮಾಡಿ"
                    ) {
                        // Category Selection Horizontal Chips
                        Text(
                            text = "ವರ್ಗ ಆಯ್ಕೆ (Select Category)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(CropCategory.values()) { category ->
                                val isSelected = category == formState.selectedCategory
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(if (isSelected) AgriPrimary else AgriSurfaceContainer)
                                        .clickable { onCategoryChange(category) }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                        .testTag("category_chip_${category.name}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${category.kannada} (${category.english})",
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Crop Name Dropdown
                        Text(
                            text = "ಬೆಳೆಯ ಹೆಸರು (Crop Name) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        var cropMenuExpanded by remember { mutableStateOf(false) }
                        val cropOptions = listOf(
                            Pair("ರಾಗಿ", "Ragi / Finger Millet (ರಾಗಿ)"),
                            Pair("ಭತ್ತ", "Paddy / Rice (ಭತ್ತ / ಅಕ್ಕಿ)"),
                            Pair("ಜೋಳ", "Jowar / Sorghum (ಜೋಳ)"),
                            Pair("ಮೆಕ್ಕೆಜೋಳ", "Maize / Corn (ಮೆಕ್ಕೆಜೋಳ)"),
                            Pair("ಗೋಧಿ", "Wheat (ಗೋಧಿ)")
                        )

                        ExposedDropdownMenuBox(
                            expanded = cropMenuExpanded,
                            onExpandedChange = { cropMenuExpanded = !cropMenuExpanded }
                        ) {
                            OutlinedTextField(
                                value = "${formState.cropNameKannada} / ${formState.cropNameEnglish}",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(AgriPrimaryFixed)
                                                .clickable { /* Trigger voice */ },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Mic,
                                                contentDescription = "Voice",
                                                tint = AgriPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropMenuExpanded)
                                    }
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Spa, contentDescription = null, tint = AgriPrimary)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("crop_name_selector")
                            )

                            ExposedDropdownMenu(
                                expanded = cropMenuExpanded,
                                onDismissRequest = { cropMenuExpanded = false }
                            ) {
                                cropOptions.forEach { (kannada, fullLabel) ->
                                    DropdownMenuItem(
                                        text = { Text(fullLabel, fontSize = 13.sp) },
                                        onClick = {
                                            onCropNameChange(kannada, fullLabel)
                                            cropMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Variety Input
                        Text(
                            text = "ತಳಿ / ಬೀಜದ ವಿಧ (Variety / Seed Type)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = formState.variety,
                            onValueChange = onVarietyChange,
                            leadingIcon = {
                                Icon(Icons.Default.Category, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("crop_variety_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "ಹೆಚ್ಚಿನ ಬೇಡಿಕೆ ಇರುವ ಮಂಡ್ಯ ಸ್ಥಳೀಯ ತಳಿ (Popular local Mandya variety)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Farming Method Radio Grid
                        Text(
                            text = "ಕೃಷಿ ವಿಧಾನ (Farming Method) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            FarmingMethod.values().forEach { method ->
                                val isSelected = method == formState.farmingMethod
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) AgriSecondaryContainer
                                            else AgriSurfaceContainerLow
                                        )
                                        .clickable { onFarmingMethodChange(method) }
                                        .padding(12.dp)
                                        .testTag("farming_method_${method.name}"),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = when (method) {
                                                FarmingMethod.ORGANIC -> Icons.Default.Verified
                                                FarmingMethod.NATURAL -> Icons.Default.Compost
                                                FarmingMethod.CONVENTIONAL -> Icons.Default.Agriculture
                                            },
                                            contentDescription = null,
                                            tint = if (isSelected) AgriPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = method.kannada,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) AgriPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = method.english,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = AgriPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 2: Crop Photos
                item {
                    FormSectionCard(
                        stepLabel = "ಶಿಫಾರಸು",
                        icon = Icons.Default.PhotoCamera,
                        title = "೨. ಬೆಳೆಯ ಚಿತ್ರಗಳು (Crop Photos)",
                        subtitle = "ಸ್ಪಷ್ಟ ಫೋಟೋಗಳಿಂದ ಬೇಗನೆ ಗ್ರಾಹಕರು ಸಿಗುತ್ತಾರೆ"
                    ) {
                        // 3 Column Photo Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            formState.photos.forEachIndexed { index, url ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AgriSurfaceContainer)
                                ) {
                                    AsyncImage(
                                        model = url,
                                        contentDescription = "Photo $index",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    if (index == 0) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .background(AgriPrimary.copy(alpha = 0.85f))
                                                .padding(vertical = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "ಮುಖ್ಯ ಫೋಟೋ",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(AgriError)
                                            .clickable { onRemovePhoto(index) }
                                            .testTag("delete_photo_$index"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Delete",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }

                            // Add More Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AgriSurfaceContainerHigh)
                                    .clickable { onAddPhotoClick() }
                                    .padding(6.dp)
                                    .testTag("add_photo_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.AddAPhoto,
                                        contentDescription = "Add",
                                        tint = AgriPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ಇನ್ನೊಂದು ಸೇರಿಸಿ\n(Add More)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriPrimary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Camera Tip Banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(AgriSurfaceContainerLow)
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TipsAndUpdates,
                                    contentDescription = null,
                                    tint = AgriSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ಫೋಟೋ ಇರುವ ಜಾಹೀರಾತುಗಳಿಗೆ ೩ ಪಟ್ಟು (3x) ಹೆಚ್ಚು ನೇರ ವ್ಯಾಪಾರ ಕರೆಗಳು ಬರುತ್ತವೆ!",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Button(
                                onClick = onAddPhotoClick,
                                colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("camera_trigger_button")
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ಕ್ಯಾಮೆರಾ", fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Section 3: Quantity & Pricing Details
                item {
                    FormSectionCard(
                        stepLabel = null,
                        icon = Icons.Default.Payments,
                        title = "೩. ಪ್ರಮಾಣ ಮತ್ತು ಬೆಲೆ (Quantity & Price)",
                        subtitle = "ನಿಮ್ಮ ನಿರೀಕ್ಷಿತ ದರ ನಿಗದಿಪಡಿಸಿ"
                    ) {
                        // Total Available Quantity + Unit
                        Text(
                            text = "ಲಭ್ಯವಿರುವ ಒಟ್ಟು ಪ್ರಮಾಣ (Total Available Quantity) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = formState.quantity,
                                onValueChange = onQuantityChange,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("quantity_input"),
                                singleLine = true
                            )

                            var unitMenuExpanded by remember { mutableStateOf(false) }
                            val units = listOf(
                                "ಕ್ವಿಂಟಾಲ್ (Quintals)",
                                "ಕೆಜಿ (Kilograms)",
                                "ಚೀಲ (Bags / 50kg)",
                                "ಕ್ರೇಟ್ (Crates)"
                            )

                            ExposedDropdownMenuBox(
                                expanded = unitMenuExpanded,
                                onExpandedChange = { unitMenuExpanded = !unitMenuExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = formState.quantityUnit,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitMenuExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = unitMenuExpanded,
                                    onDismissRequest = { unitMenuExpanded = false }
                                ) {
                                    units.forEach { u ->
                                        DropdownMenuItem(
                                            text = { Text(u, fontSize = 12.sp) },
                                            onClick = {
                                                onQuantityUnitChange(u)
                                                unitMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Expected Price
                        Text(
                            text = "ನಿರೀಕ್ಷಿತ ದರ (Expected Price per Unit) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = formState.price,
                            onValueChange = onPriceChange,
                            leadingIcon = {
                                Text(
                                    text = "₹",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AgriPrimary,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            },
                            trailingIcon = {
                                Box(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AgriSurfaceContainer)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "ಪ್ರತಿ ಕ್ವಿಂಟಾಲ್‌ಗೆ",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("price_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Smart APMC Guide Helper Banner
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(AgriSurfaceContainerHigh)
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(AgriSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "ಮಂಡ್ಯ APMC ಇಂದಿನ ದರ ಮಾಹಿತಿ",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(AgriSecondary)
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "ಮಂಡ್ಯ ಎಪಿಎಂಸಿ ಮಾರುಕಟ್ಟೆಯಲ್ಲಿ ಇಂದಿನ ರಾಗಿ ದರ: ₹೩,೧೦೦ - ₹೩,೨೫೦. ನಿಮ್ಮ ಬೆಳೆ ಸಾವಯವವಾಗಿರುವುದರಿಂದ ₹೩,೩೫೦ - ₹೩,೪೫೦ ಸೂಕ್ತ ಮತ್ತು ಲಾಭದಾಯಕ ಬೆಲೆಯಾಗಿದೆ.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Minimum Order Quantity (MOQ)
                        Text(
                            text = "ಕನಿಷ್ಠ ಖರೀದಿ ಪ್ರಮಾಣ (Minimum Order Quantity - MOQ)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = formState.moq,
                            onValueChange = onMoqChange,
                            leadingIcon = {
                                Icon(Icons.Default.ShoppingBasket, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("moq_input"),
                            singleLine = true
                        )
                    }
                }

                // Section 4: Harvest & Availability Timeline
                item {
                    FormSectionCard(
                        stepLabel = null,
                        icon = Icons.Default.CalendarMonth,
                        title = "೪. ಕೊಯ್ಲು & ಲಭ್ಯತೆ (Harvest & Stock)",
                        subtitle = "ದಾಸ್ತಾನು ವಿವರ ದಾಖಲಿಸಿ"
                    ) {
                        Text(
                            text = "ಕೊಯ್ಲು ಮಾಡಿದ ದಿನಾಂಕ (Harvest Date)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = formState.harvestDate,
                            onValueChange = onHarvestDateChange,
                            leadingIcon = {
                                Icon(Icons.Default.EventAvailable, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("harvest_date_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stock Status Radio Options
                        Text(
                            text = "ದಾಸ್ತಾನಿನ ಸ್ಥಿತಿ (Current Stock Status) *",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Ready in Stock Option
                            val isInStock = formState.stockStatus == StockStatus.IN_STOCK
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isInStock) AgriSecondaryContainer
                                        else AgriSurfaceContainerLow
                                    )
                                    .clickable { onStockStatusChange(StockStatus.IN_STOCK) }
                                    .padding(10.dp)
                                    .testTag("stock_status_in_stock")
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(AgriSecondary)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "ಲಭ್ಯವಿದೆ",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AgriPrimary
                                            )
                                        }
                                        if (isInStock) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = AgriSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Ready in Stock",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Coming Soon Option
                            val isComingSoon = formState.stockStatus == StockStatus.COMING_SOON
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isComingSoon) AgriTertiaryFixed
                                        else AgriSurfaceContainerLow
                                    )
                                    .clickable { onStockStatusChange(StockStatus.COMING_SOON) }
                                    .padding(10.dp)
                                    .testTag("stock_status_coming_soon")
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(AgriTertiaryContainer)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "ಶೀಘ್ರದಲ್ಲಿ ಲಭ್ಯ",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AgriTertiaryContainer
                                            )
                                        }
                                        if (isComingSoon) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = AgriTertiaryContainer,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "In 7-10 Days",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 5: Farm Location & Contact Preferences
                item {
                    FormSectionCard(
                        stepLabel = null,
                        icon = Icons.Default.PinDrop,
                        title = "೫. ಜಮೀನಿನ ಸ್ಥಳ ಮತ್ತು ಸಂಪರ್ಕ (Location & Contact)",
                        subtitle = "ಖರೀದಿದಾರರು ಸಂಪರ್ಕಿಸಲು ವಿವರ"
                    ) {
                        // Pre-filled Farm Location Card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(AgriSurfaceContainerLow)
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = AgriPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "ನೋಂದಾಯಿತ ತೋಟ (Registered Farm)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "ಬದಲಾಯಿಸಿ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriPrimary,
                                        modifier = Modifier.clickable { /* Change address */ }
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "ಶಿವಳ್ಳಿ ಗ್ರಾಮ, ಮಂಡ್ಯ ತಾಲ್ಲೂಕು",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Shivalli Village, Mandya District, Karnataka - 571405",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Location Map Preview Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AgriSurfaceContainer)
                        ) {
                            AsyncImage(
                                model = KrishiRepository.MAP_PREVIEW_URL,
                                contentDescription = "Map Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Distance Badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.92f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Route,
                                        contentDescription = null,
                                        tint = AgriPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "ಮಂಡ್ಯ APMC ಮಾರುಕಟ್ಟೆಯಿಂದ ೮ ಕಿ.ಮೀ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Contact Preferences Checklist
                        Text(
                            text = "ಸಂಪರ್ಕ ಆದ್ಯತೆಗಳು (Contact Preferences)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            // 1. Phone Calls
                            PreferenceCheckboxItem(
                                title = "ನೇರ ಮೊಬೈಲ್ ಕರೆಗಳು (Phone Calls)",
                                subtitle = "+91 98450 XXXXX (ನೋಂದಾಯಿತ ಸಂಖ್ಯೆ)",
                                icon = Icons.Default.Call,
                                iconColor = AgriPrimary,
                                checked = formState.phoneCallsEnabled,
                                onCheckedChange = onTogglePhoneCalls,
                                tag = "pref_phone_calls"
                            )

                            // 2. WhatsApp Inquiries
                            PreferenceCheckboxItem(
                                title = "ವಾಟ್ಸಾಪ್ ಸಂದೇಶಗಳು (WhatsApp Inquiries)",
                                subtitle = "ಖರೀದಿದಾರರು ನೇರವಾಗಿ ಚಾಟ್ ಮಾಡಬಹುದು",
                                icon = Icons.Default.Chat,
                                iconColor = AgriSecondary,
                                checked = formState.whatsAppEnabled,
                                onCheckedChange = onToggleWhatsApp,
                                tag = "pref_whatsapp"
                            )

                            // 3. Farm Pickup
                            PreferenceCheckboxItem(
                                title = "ತೋಟದಲ್ಲೇ ಸಂಗ್ರಹಿಸಲು ಅವಕಾಶ (Farm Pickup)",
                                subtitle = "ಖರೀದಿದಾರರೇ ನೇರವಾಗಿ ಬಂದು ಕೊಂಡೊಯ್ಯಬಹುದು",
                                icon = Icons.Default.Store,
                                iconColor = AgriPrimary,
                                checked = formState.farmPickupEnabled,
                                onCheckedChange = onToggleFarmPickup,
                                tag = "pref_farm_pickup"
                            )

                            // 4. Home Delivery
                            PreferenceCheckboxItem(
                                title = "ಮನೆ ಬಾಗಿಲಿಗೆ ಸಾಗಾಣಿಕೆ (Home Delivery)",
                                subtitle = "೨೫ ಕಿ.ಮೀ ವ್ಯಾಪ್ತಿಯಲ್ಲಿ ಸಾಗಾಣಿಕೆ ವೆಚ್ಚ ಪ್ರತ್ಯೇಕ",
                                icon = Icons.Default.LocalShipping,
                                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                checked = formState.homeDeliveryEnabled,
                                onCheckedChange = onToggleHomeDelivery,
                                tag = "pref_home_delivery"
                            )
                        }
                    }
                }

                // Delightful Encouragement: Krishi Assured
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AgriSurfaceContainerHigh),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(AgriSecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Handshake,
                                    contentDescription = null,
                                    tint = AgriPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ರೈತ ಮಿತ್ರ ಭರವಸೆ (Krishi Assured)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "ಯಾವುದೇ ಮಧ್ಯವರ್ತಿಗಳಿಲ್ಲದೆ ೧೦೦% ಪಾರದರ್ಶಕ ನೇರ ವ್ಯಾಪಾರ.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Buffer for sticky bottom bar
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }

            // Sticky Fixed Bottom Action Toolbar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_crop_bottom_toolbar"),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Save Draft Button
                    OutlinedButton(
                        onClick = onSaveDraft,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("save_draft_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AgriSurfaceContainerLowest
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = AgriPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (formState.isDraftSaved) "ಉಳಿಸಲಾಗಿದೆ!" else "ಡ್ರಾಫ್ಟ್ (Draft)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriPrimary
                        )
                    }

                    // Publish Button
                    Button(
                        onClick = onPublish,
                        enabled = !formState.isPublishing,
                        modifier = Modifier
                            .weight(2f)
                            .height(52.dp)
                            .testTag("publish_crop_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgriPrimaryContainer)
                    ) {
                        if (formState.isPublishing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ಪ್ರಕಟಿಸಲಾಗುತ್ತಿದೆ...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ಬೆಳೆ ಪ್ರಕಟಿಸಿ (Publish)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSectionCard(
    stepLabel: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriSecondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = AgriPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (stepLabel != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AgriPrimaryFixed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stepLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}

@Composable
private fun PreferenceCheckboxItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(AgriSurfaceContainerLow)
            .clickable { onCheckedChange(!checked) }
            .padding(10.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = AgriPrimary)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
