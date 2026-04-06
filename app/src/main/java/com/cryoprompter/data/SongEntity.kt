package com.cryoprompter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    val title: String,
    val artist: String?,
    val bpm: Int,
    val chordProContent: String,
    val originalKey: String,
    val displayKey: String,
    val capo: Int,
    val durationSeconds: Int,
    val syncMap: String?,
    val isTrained: Boolean,
    val isFavorite: Boolean,
    val lastAccessed: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
