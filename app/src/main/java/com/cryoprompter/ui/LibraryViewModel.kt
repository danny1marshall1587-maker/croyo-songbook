package com.cryoprompter.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cryoprompter.data.AppDatabase
import com.cryoprompter.data.SongEntity
import com.cryoprompter.importer.SongbookProImporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val importer = SongbookProImporter(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val songList: StateFlow<List<SongEntity>> = db.songDao().getAllSongs()
        .combine(_searchQuery) { songs, query ->
            if (query.isEmpty()) songs
            else songs.filter { it.title.contains(query, ignoreCase = true) || it.artist?.contains(query, ignoreCase = true) == true }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun importSbp(uri: Uri) {
        viewModelScope.launch {
            val importedSongs = importer.importBackup(uri)
            importedSongs.forEach { song ->
                db.songDao().insertSong(song)
            }
        }
    }
    
    fun deleteSong(song: SongEntity) {
        viewModelScope.launch {
            db.songDao().deleteSong(song)
        }
    }
}
