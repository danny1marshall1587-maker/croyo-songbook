package com.cryoprompter.data;

import androidx.room.*;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0018\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00032\u0006\u0010\u0007\u001a\u00020\bH\'J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0005H\'J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0005H\'J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0005H\'J\u0010\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\'J\u0010\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013H\'J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u00040\u0003H\'J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0017\u001a\u00020\bH\'\u00a8\u0006\u0018\u00c0\u0006\u0003"}, d2 = {"Lcom/cryoprompter/data/SongDao;", "", "getAllSongs", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/cryoprompter/data/SongEntity;", "getSongById", "id", "", "insertSong", "", "song", "updateSong", "deleteSong", "insertSetlist", "setlist", "Lcom/cryoprompter/data/SetlistEntity;", "addSongToSetlist", "crossRef", "Lcom/cryoprompter/data/SetlistSongCrossRef;", "getSetlistsWithSongs", "Lcom/cryoprompter/data/SetlistWithSongs;", "getSongsForSetlist", "setlistId", "app_debug"})
@androidx.room.Dao()
public abstract interface SongDao {
    
    @androidx.room.Query(value = "SELECT * FROM songs ORDER BY title ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.cryoprompter.data.SongEntity>> getAllSongs();
    
    @androidx.room.Query(value = "SELECT * FROM songs WHERE id = :id")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.cryoprompter.data.SongEntity> getSongById(int id);
    
    @androidx.room.Insert(onConflict = 1)
    public abstract void insertSong(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SongEntity song);
    
    @androidx.room.Update()
    public abstract int updateSong(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SongEntity song);
    
    @androidx.room.Delete()
    public abstract int deleteSong(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SongEntity song);
    
    @androidx.room.Insert(onConflict = 1)
    public abstract void insertSetlist(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SetlistEntity setlist);
    
    @androidx.room.Insert(onConflict = 1)
    public abstract void addSongToSetlist(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SetlistSongCrossRef crossRef);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM setlists ORDER BY lastModified DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.cryoprompter.data.SetlistWithSongs>> getSetlistsWithSongs();
    
    @androidx.room.Query(value = "SELECT * FROM songs WHERE id IN (SELECT songId FROM setlist_song_cross_ref WHERE setlistId = :setlistId) ORDER BY (SELECT orderIndex FROM setlist_song_cross_ref WHERE setlistId = :setlistId AND songId = songs.id) ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.cryoprompter.data.SongEntity>> getSongsForSetlist(int setlistId);
}