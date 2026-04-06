package com.cryoprompter.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cryoprompter.data.SongEntity

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    onSongClick: (Int) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val songList by viewModel.songList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    // Pro Sorting: Group by first letter, sorted alphabetically
    val groupedSongs = songList
        .filter { it.title.contains(searchQuery, ignoreCase = true) || it.artist?.contains(searchQuery, ignoreCase = true) == true }
        .groupBy { it.title.firstOrNull()?.uppercaseChar() ?: '#' }
        .toSortedMap()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Songs", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
// ... (rest of Scaffold content)
        containerColor = Color.Black
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Search Bar (SBP Style)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search...", color = Color.Gray) },
                leadingIcon = null,
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFF69B4),
                    unfocusedBorderColor = Color.DarkGray
                ),
                singleLine = true
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groupedSongs.forEach { (initial, songs) ->
                    stickyHeader {
                        SectionHeader(initial.toString())
                    }
                    items(songs) { song ->
                        SongListItem(song = song, onClick = { onSongClick(song.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(letter: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1A1A1A)
    ) {
        Text(
            text = letter,
            color = Color(0xFF3B82F6), // Blue as seen in SBP
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun SongListItem(song: SongEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (!song.artist.isNullOrEmpty()) {
                Text(
                    text = song.artist,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
