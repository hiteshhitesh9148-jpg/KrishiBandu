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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CropListing
import com.example.data.model.StockStatus
import com.example.ui.components.CropCard
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriSurfaceContainer

@Composable
fun MyCropsScreen(
    crops: List<CropListing>,
    onAddCropClick: () -> Unit,
    onEditPrice: (CropListing) -> Unit,
    onViewInquiries: (CropListing) -> Unit,
    onRelist: (CropListing) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ಎಲ್ಲವೂ (All)") }
    val filterOptions = listOf(
        "ಎಲ್ಲವೂ (All)",
        "ಲಭ್ಯವಿದೆ (In Stock)",
        "ಕಡಿಮೆ ದಾಸ್ತಾನು (Low Stock)",
        "ಖಾಲಿಯಾಗಿದೆ (Sold Out)"
    )

    val filteredCrops = remember(crops, selectedFilter) {
        when (selectedFilter) {
            "ಲಭ್ಯವಿದೆ (In Stock)" -> crops.filter { it.stockStatus == StockStatus.IN_STOCK }
            "ಕಡಿಮೆ ದಾಸ್ತಾನು (Low Stock)" -> crops.filter { it.stockStatus == StockStatus.LOW_STOCK }
            "ಖಾಲಿಯಾಗಿದೆ (Sold Out)" -> crops.filter { it.stockStatus == StockStatus.SOLD_OUT }
            else -> crops
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_crops_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ನನ್ನ ಬೆಳೆಗಳು (My Crops)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "ಒಟ್ಟು ${crops.size} ಬೆಳೆಗಳು ನೋಂದಾಯಿತವಾಗಿವೆ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onAddCropClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgriPrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ಸೇರಿಸಿ", fontSize = 12.sp)
                }
            }
        }

        // Filter Pills
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) AgriPrimary else AgriSurfaceContainer)
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Crops List
        items(filteredCrops, key = { it.id }) { crop ->
            CropCard(
                crop = crop,
                onEditPrice = { onEditPrice(crop) },
                onViewInquiries = { onViewInquiries(crop) },
                onRelist = { onRelist(crop) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
