package com.cryoprompter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cryoprompter.data.AppDatabase
import com.cryoprompter.data.SetlistWithSongs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetlistScreen(onSetClick: (Int) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val setlists by db.songDao().getSetlistsWithSongs().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Sets", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* New Set Logic */ },
                containerColor = Color(0xFFF97316) // Gig-Orange
            ) {
                Icon(Icons.Default.PlaylistAdd, contentDescription = "Add Set", tint = Color.White)
            }
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(setlists) { setWithSongs ->
                SetlistItem(setWithSongs = setWithSongs, onClick = { onSetClick(setWithSongs.setlist.id) })
            }
        }
    }
}

@Composable
fun SetlistItem(setWithSongs: SetlistWithSongs, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Spatial Color Marker (from SetlistEntity)
            Box(
                modifier = Modifier
                    .size(12.dp, 40.dp)
                    .background(Color(android.graphics.Color.parseColor(setWithSongs.setlist.color)), shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = setWithSongs.setlist.name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${setWithSongs.songs.size} Songs",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
