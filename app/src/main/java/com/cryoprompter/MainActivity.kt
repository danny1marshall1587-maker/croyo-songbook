package com.cryoprompter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.cryoprompter.audio.AudioBridge
import com.cryoprompter.data.AppDatabase
import com.cryoprompter.importer.ChordProParser
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import com.cryoprompter.data.SongEntity
import com.cryoprompter.service.CryoPrompterService
import com.cryoprompter.ui.*

class MainActivity : ComponentActivity() {

    private val audioBridge = AudioBridge()
    private val chordProParser = ChordProParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        } else {
            startAudioService()
        }

        setContent {
            AppNavigation(audioBridge, chordProParser)
        }
    }

    private fun startAudioService() {
        val serviceIntent = Intent(this, CryoPrompterService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        audioBridge.startEngine()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioBridge.stopEngine()
        stopService(Intent(this, CryoPrompterService::class.java))
    }
}

@Composable
fun AppNavigation(audioBridge: AudioBridge, parser: ChordProParser) {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    
    // Seed data for the "Full App" experience on first run
    LaunchedEffect(Unit) {
        val songs = db.songDao().getAllSongs().take(1).flowOn(Dispatchers.IO).firstOrNull()
        if (songs == null || songs.isEmpty()) {
            withContext(Dispatchers.IO) {
                db.songDao().insertSong(
                    SongEntity(
                        title = "Welcome to Cryo-Prompter Pro",
                        artist = "Getting Started",
                        bpm = 85,
                        chordProContent = "{title: Welcome}\n{artist: Cryo-Prompter}\n{bpm: 85}\n[G]Welcome to the [C]Full App!\n[D]Tap the pink [G]button to import.\n[Am]Stay in the [Em]pink focus line.",
                        originalKey = "D",
                        displayKey = "D",
                        capo = 0,
                        durationSeconds = 180,
                        syncMap = null,
                        isTrained = false,
                        isFavorite = false,
                        lastAccessed = System.currentTimeMillis()
                    )
                )
            }
        }
    }
    
    var currentTab by remember { mutableStateOf("library") }

    Scaffold(
        bottomBar = {
            if (currentTab != "performance") {
                NavigationBar(containerColor = Color(0xFF111111)) {
                    NavigationBarItem(
                        selected = currentTab == "library",
                        onClick = { 
                            currentTab = "library"
                            navController.navigate("library") 
                        },
                        icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Songs") },
                        label = { Text("Songs") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF69B4), selectedTextColor = Color(0xFFFF69B4))
                    )
                    NavigationBarItem(
                        selected = currentTab == "sets",
                        onClick = { 
                            currentTab = "sets"
                            navController.navigate("sets") 
                        },
                        icon = { Icon(Icons.Default.List, contentDescription = "Sets") },
                        label = { Text("Sets") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF69B4), selectedTextColor = Color(0xFFFF69B4))
                    )
                    NavigationBarItem(
                        selected = currentTab == "settings",
                        onClick = { 
                            currentTab = "settings"
                            navController.navigate("settings") 
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF69B4), selectedTextColor = Color(0xFFFF69B4))
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = "library",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("library") {
                currentTab = "library"
                LibraryScreen(
                    onSongClick = { songId ->
                        currentTab = "performance"
                        navController.navigate("performance/$songId")
                    }
                )
            }
            composable("sets") {
                currentTab = "sets"
                com.cryoprompter.ui.SetlistScreen(onSetClick = { setId ->
                    // Navigation to Set detail coming in next phase
                })
            }
            composable("settings") {
                currentTab = "settings"
                SettingsScreen()
            }
            composable(
                "performance/{songId}",
                arguments = listOf(navArgument("songId") { type = NavType.IntType })
            ) { backStackEntry ->
                val songId = backStackEntry.arguments?.getInt("songId") ?: 0
                val songFlow = db.songDao().getSongById(songId).collectAsState(initial = null)
                
                songFlow.value?.let { song ->
                    PerformanceRenderer(
                        audioBridge = audioBridge, 
                        song = parser.parse(song.chordProContent)
                    )
                }
            }
        }
    }
}
