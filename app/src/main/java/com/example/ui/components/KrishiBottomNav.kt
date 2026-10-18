package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AgriPrimary
import com.example.ui.theme.AgriPrimaryContainer
import com.example.ui.theme.AgriTertiaryContainer
import com.example.ui.viewmodel.AppTab

@Composable
fun KrishiBottomNav(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("krishi_bottom_navigation"),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Dashboard
            BottomNavItem(
                label = "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = "Dashboard",
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = currentTab == AppTab.DASHBOARD,
                onClick = { onTabSelected(AppTab.DASHBOARD) },
                modifier = Modifier.weight(1f).testTag("tab_dashboard")
            )

            // 2. My Crops
            BottomNavItem(
                label = "ನನ್ನ ಬೆಳೆಗಳು",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Yard,
                        contentDescription = "My Crops",
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = currentTab == AppTab.MY_CROPS,
                onClick = { onTabSelected(AppTab.MY_CROPS) },
                modifier = Modifier.weight(1f).testTag("tab_my_crops")
            )

            // 3. Center Elevated Add Crop Action Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(y = (-10).dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(AgriPrimaryContainer)
                            .clickable { onTabSelected(AppTab.ADD_CROP) }
                            .testTag("tab_add_crop_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Crop",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ಬೆಳೆ ಸೇರಿಸಿ",
                        fontSize = 11.sp,
                        fontWeight = if (currentTab == AppTab.ADD_CROP) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentTab == AppTab.ADD_CROP) AgriPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 4. Inquiries
            BottomNavItem(
                label = "ವಿಚಾರಣೆಗಳು",
                icon = {
                    Box {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Inquiries",
                            modifier = Modifier.size(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-4).dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(AgriTertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "2",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                selected = currentTab == AppTab.INQUIRIES,
                onClick = { onTabSelected(AppTab.INQUIRIES) },
                modifier = Modifier.weight(1f).testTag("tab_inquiries")
            )

            // 5. Logistics / Transport
            BottomNavItem(
                label = "ಸಾರಿಗೆ",
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Logistics",
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = currentTab == AppTab.LOGISTICS,
                onClick = { onTabSelected(AppTab.LOGISTICS) },
                modifier = Modifier.weight(1f).testTag("tab_logistics")
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            val contentColor = if (selected) AgriPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides contentColor
            ) {
                icon()
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) AgriPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
