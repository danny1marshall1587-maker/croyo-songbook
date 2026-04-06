package com.cryoprompter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cryoprompter.audio.VoskVoiceEngine
import com.cryoprompter.audio.FuzzyMatcher
import com.cryoprompter.importer.Song
import kotlinx.coroutines.delay

@Composable
fun TrainingScreen(voskEngine: VoskVoiceEngine, song: Song, onComplete: (String) -> Unit) {
    var isRecording by remember { mutableStateOf(false) }
    var currentLineIndex by remember { mutableStateOf(0) }
    var recognizedText by remember { mutableStateOf("") }
    val syncMap = remember { mutableStateMapOf<Int, Long>() }
    var startTime by remember { mutableStateOf(0L) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("TRAINING MODE: ${song.title}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        // Display current line to sing
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = song.lines.getOrNull(currentLineIndex)?.lyrics ?: "END",
                color = Color(0xFFFF69B4),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("HEARD: $recognizedText", color = Color.LightGray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(64.dp))

        FloatingActionButton(
            onClick = {
                if (!isRecording) {
                    startTime = System.currentTimeMillis()
                    voskEngine.startListening(
                        grammar = song.lines.getOrNull(currentLineIndex)?.lyrics?.split(" ") ?: emptyList(),
                        listener = object : VoskVoiceEngine.RecognitionListener {
                            override fun onResult(text: String) {
                                recognizedText = text
                                // Simple mapping: if any word matches, mark timestamp
                                syncMap[currentLineIndex] = System.currentTimeMillis() - startTime
                                if (currentLineIndex < song.lines.size - 1) {
                                    currentLineIndex++
                                }
                            }
                            override fun onPartialResult(text: String) { recognizedText = text }
                            override fun onCommand(command: FuzzyMatcher.Command) { 
                                // Commands are ignored during training
                            }
                            override fun onError(e: Exception) { recognizedText = "Error: ${e.message}" }
                        }
                    )
                } else {
                    voskEngine.stopListening()
                    onComplete(syncMap.toString())
                }
                isRecording = !isRecording
            },
            containerColor = if (isRecording) Color.Red else Color(0xFFF97316)
        ) {
            Icon(if (isRecording) Icons.Default.Stop else Icons.Default.Mic, contentDescription = null, tint = Color.White)
        }

        if (isRecording) {
            Text("SING THE LINE THEN PAUSE", color = Color.Gray, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
