package com.cryoprompter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var isDarkMode by remember { mutableStateOf(true) }
    var accentColor by remember { mutableStateOf(Color(0xFF3B82F6)) } // Default Pro Blue
    var focalZoom by remember { mutableStateOf(1.5f) }
    var voiceSensitivity by remember { mutableStateOf(0.7f) }
    var useColorBlocks by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuration & Audio Lab", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = if (isDarkMode) Color.Black else Color.White
    ) { padding ->
        val textColor = if (isDarkMode) Color.White else Color.Black
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // --- PERSONALISATION SECTION ---
            SettingsHeader("GENERAL", accentColor)
            
            SettingsToggleItem("Dark Mode (Stage Safe)", isDarkMode, { isDarkMode = it }, textColor)

            SettingsHeader("APPEARANCE", accentColor)
            
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AccentColorCircle(Color(0xFF3B82F6), accentColor) { accentColor = it } // Blue
                AccentColorCircle(Color(0xFFFF69B4), accentColor) { accentColor = it } // Pink
                AccentColorCircle(Color(0xFFF97316), accentColor) { accentColor = it } // Orange
            }

            SettingsToggleItem("Spatial Color Chords", useColorBlocks, { useColorBlocks = it }, textColor)

            // --- PERFORMANCE SECTION ---
            SettingsHeader("PERFORMANCE", accentColor)
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Focal Zoom (x${"%.1f".format(focalZoom)})", color = Color.Gray, fontSize = 14.sp)
                Slider(
                    value = focalZoom,
                    onValueChange = { focalZoom = it },
                    valueRange = 1f..3f,
                    colors = SliderDefaults.colors(thumbColor = accentColor)
                )
            }

            // --- JSx AUDIO LAB ---
            SettingsHeader("JSx AUDIO LAB (VOSK)", accentColor)
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Voice Sensitivity (${(voiceSensitivity * 100).toInt()}%)", color = Color.Gray, fontSize = 14.sp)
                Slider(
                    value = voiceSensitivity,
                    onValueChange = { voiceSensitivity = it },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(thumbColor = accentColor)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SettingsHeader(title: String, accentColor: Color) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
        color = accentColor,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
}

@Composable
fun SettingsToggleItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = textColor, fontSize = 18.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun AccentColorCircle(color: Color, selectedColor: Color, onSelect: (Color) -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color, shape = androidx.compose.foundation.shape.CircleShape)
            .let { 
                if (color == selectedColor) it.background(Color.White.copy(alpha = 0.2f), shape = androidx.compose.foundation.shape.CircleShape).padding(4.dp)
                else it
            }
            .clickable { onSelect(color) }
    )
}
