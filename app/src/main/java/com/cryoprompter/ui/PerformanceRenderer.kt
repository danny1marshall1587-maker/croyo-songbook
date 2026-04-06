package com.cryoprompter.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cryoprompter.audio.AudioBridge
import com.cryoprompter.audio.VoskVoiceEngine
import com.cryoprompter.audio.FuzzyMatcher
import com.cryoprompter.importer.Song
import kotlinx.coroutines.delay
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*

val ChordColorMap = mapOf(
    "C" to Color(0xFF3B82F6), // Blue
    "G" to Color(0xFF22C55E), // Green
    "D" to Color(0xFFF97316), // Orange
    "A" to Color(0xFFEF4444), // Red
    "E" to Color(0xFFA855F7), // Purple
    "F" to Color(0xFFEAB308), // Yellow
    "B" to Color(0xFFEC4899)  // Pink
)

@Composable
fun PerformanceRenderer(audioBridge: AudioBridge, song: Song) {
    var scale by remember { mutableStateOf(1f) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var isCalibrating by remember { mutableStateOf(false) }
    var calibrationProgress by remember { mutableStateOf(0f) }
    var showIntroPulse by remember { mutableStateOf(true) }
    var useColorBlocks by remember { mutableStateOf(true) } // Mocked setting
    
    val listState = rememberLazyListState()
    var currentLineIndex by remember { mutableStateOf(0) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val voiceEngine = remember { VoskVoiceEngine(context) }

    // --- The "True Follow-Along" Engine ---
    LaunchedEffect(Unit) {
        voiceEngine.initModel { success ->
            if (success) {
                voiceEngine.startListening(emptyList(), object : VoskVoiceEngine.RecognitionListener {
                    override fun onResult(text: String) {
                        // Pace-Tracking: Find the best matching line in the window
                        FuzzyMatcher.findBestMatch(
                            normalizedLyrics = song.lines.map { it.lyrics },
                            recognizedWords = text,
                            currentLineIndex = currentLineIndex
                        )?.let { matchedIndex ->
                            currentLineIndex = matchedIndex
                        }
                    }

                    override fun onPartialResult(text: String) {}

                    override fun onCommand(command: FuzzyMatcher.Command) {
                        when (command) {
                            FuzzyMatcher.Command.GO_TOP -> currentLineIndex = 0
                            FuzzyMatcher.Command.GO_NEXT -> if (currentLineIndex < song.lines.size - 1) currentLineIndex++
                            FuzzyMatcher.Command.GO_BACK -> if (currentLineIndex > 0) currentLineIndex--
                            FuzzyMatcher.Command.GO_CHORUS -> {
                                // Find first {c: Chorus} or similar
                                val chorusIndex = song.lines.indexOfFirst { it.lyrics.lowercase().contains("chorus") }
                                if (chorusIndex != -1) currentLineIndex = chorusIndex
                            }
                        }
                    }

                    override fun onError(e: Exception) {}
                })
            }
        }
    }

    // Static Snapping Animation (Keeps the line in Focus Zone)
    LaunchedEffect(currentLineIndex) {
        listState.animateScrollToItem(currentLineIndex, scrollOffset = -250)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ -> scale *= zoom }
            }
    ) {
        // Tri-Color Focus Renderer (Focal Flip)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(rotationAngle),
            contentPadding = PaddingValues(vertical = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(song.lines.size) { index ->
                val line = song.lines[index]
                val isPresent = index == currentLineIndex
                val isPast = index < currentLineIndex
                val focalScale = if (isPresent) 1.5f else 0.8f
                val focalAlpha = if (isPresent) 1f else if (isPast) 0.2f else 0.5f

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .alpha(focalAlpha)
                        .clickable { currentLineIndex = index },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Chords (Color-Blocks or Text)
                    if (isPresent && line.chords.isNotEmpty()) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            line.chords.split(" ").filter { it.isNotEmpty() }.forEach { chord ->
                                if (useColorBlocks) {
                                    ChordBlock(chord = chord, scale = scale)
                                    Spacer(modifier = Modifier.width(8.dp))
                                } else {
                                    Text(
                                        text = chord,
                                        color = Color(0xFFF97316),
                                        fontSize = (32 * scale).sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                }
                            }
                        }
                    }

                    // Lyrics (Focal Enlargement)
                    Text(
                        text = line.lyrics,
                        color = if (isPresent) Color(0xFFFF69B4) else if (isPast) Color.Gray else Color(0xFF87CEEB),
                        fontSize = (48 * scale * focalScale).sp,
                        fontWeight = if (isPresent) FontWeight.Black else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        lineHeight = (48 * scale * focalScale).sp
                    )
                }
            }
        }

        // --- Focus Zone (The Static Reading Strip) ---
        // This is a fixed, semi-transparent bar that stays in the top 1/3
        // where the current line "snaps" to.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.TopCenter)
                .padding(top = 220.dp)
                .background(Color.White.copy(alpha = 0.05f))
        )

        // Overlays & HUD
        PerformanceHUD(
            title = song.title,
            isCalibrating = isCalibrating,
            progress = calibrationProgress,
            onCalibrate = { isCalibrating = true },
            onZoomIn = { scale += 0.1f },
            onZoomOut = { scale -= 0.1f },
            onPlayToggle = { /* Trigger auto-scroll toggling logic */ }
        )
    }
}

@Composable
fun BoxScope.PerformanceHUD(
    title: String,
    isCalibrating: Boolean,
    progress: Float,
    onCalibrate: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onPlayToggle: () -> Unit
) {
    // Top Bar (Metadata & Menu)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.8f))
            .padding(16.dp)
            .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(androidx.compose.material.icons.Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "Key: D", color = Color(0xFFFF69B4), fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }

    // Right-side Live Buttons HUD (Stack)
    Column(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LiveButton(icon = androidx.compose.material.icons.Icons.Default.ZoomIn, onClick = onZoomIn)
        LiveButton(icon = androidx.compose.material.icons.Icons.Default.PlayArrow, onClick = onPlayToggle)
        LiveButton(icon = androidx.compose.material.icons.Icons.Default.GraphicEq, onClick = {}) // Metronome
        LiveButton(icon = androidx.compose.material.icons.Icons.Default.Brush, onClick = {}) // Annotate
        LiveButton(icon = androidx.compose.material.icons.Icons.Default.Menu, onClick = {}) // Scroll
    }

    // Bottom Status Bar
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(Color(0xFF111111))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onCalibrate, colors = ButtonDefaults.buttonColors(containerColor = if (isCalibrating) Color.Red else Color.DarkGray)) {
            Text(if (isCalibrating) "LEARNING: ${(progress * 100).toInt()}%" else "LISTEN")
        }
        Text("JSx Mode", color = Color.Green, fontSize = 12.sp)
    }
}

@Composable
fun ChordBlock(chord: String, scale: Float) {
    val root = chord.takeWhile { it.isLetter() }.uppercase()
    val blockColor = ChordColorMap[root] ?: Color.DarkGray
    
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(width = (60 * scale).dp, height = (30 * scale).dp)
            .background(blockColor, shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = chord,
            color = Color.White,
            fontSize = (12 * scale).sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun LiveButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(64.dp)
            .clickable { onClick() },
        shape = androidx.compose.foundation.shape.CircleShape,
        color = Color.DarkGray.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(32.dp))
        }
    }
}
