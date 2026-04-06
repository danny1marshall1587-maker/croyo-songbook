package com.cryoprompter.data

import androidx.room.*

@Entity(tableName = "setlists")
data class SetlistEntity(
    var name: String,
    var color: String, // Color-coding for dyslexia/spatial recognition
    var lastModified: Long,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)

@Entity(
    tableName = "setlist_song_cross_ref",
    primaryKeys = ["setlistId", "songId"]
)
data class SetlistSongCrossRef(
    var setlistId: Int,
    var songId: Int,
    var orderIndex: Int // For manual song ordering in a set
)

data class SetlistWithSongs(
    @Embedded var setlist: SetlistEntity,
    @Relation(
        parentColumn = "id", // From SetlistEntity
        entityColumn = "id", // From SongEntity
        associateBy = Junction(
            value = SetlistSongCrossRef::class,
            parentColumn = "setlistId",
            entityColumn = "songId"
        )
    )
    var songs: List<SongEntity>
)
