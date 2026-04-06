package com.cryoprompter.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE id = :id")
    fun getSongById(id: Int): Flow<SongEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: SongEntity)

    @Update
    fun updateSong(song: SongEntity): Int

    @Delete
    fun deleteSong(song: SongEntity): Int

    // --- Setlist Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetlist(setlist: SetlistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSongToSetlist(crossRef: SetlistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM setlists ORDER BY lastModified DESC")
    fun getSetlistsWithSongs(): Flow<List<SetlistWithSongs>>

    @Query("SELECT * FROM songs WHERE id IN (SELECT songId FROM setlist_song_cross_ref WHERE setlistId = :setlistId) ORDER BY (SELECT orderIndex FROM setlist_song_cross_ref WHERE setlistId = :setlistId AND songId = songs.id) ASC")
    fun getSongsForSetlist(setlistId: Int): Flow<List<SongEntity>>
}
