package com.cryoprompter.data;

import androidx.room.*;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J#\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR$\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001b"}, d2 = {"Lcom/cryoprompter/data/SetlistWithSongs;", "", "setlist", "Lcom/cryoprompter/data/SetlistEntity;", "songs", "", "Lcom/cryoprompter/data/SongEntity;", "<init>", "(Lcom/cryoprompter/data/SetlistEntity;Ljava/util/List;)V", "getSetlist", "()Lcom/cryoprompter/data/SetlistEntity;", "setSetlist", "(Lcom/cryoprompter/data/SetlistEntity;)V", "getSongs", "()Ljava/util/List;", "setSongs", "(Ljava/util/List;)V", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class SetlistWithSongs {
    @androidx.room.Embedded()
    @org.jetbrains.annotations.NotNull()
    private com.cryoprompter.data.SetlistEntity setlist;
    @androidx.room.Relation(parentColumn = "id", entityColumn = "id", associateBy = @androidx.room.Junction(value = com.cryoprompter.data.SetlistSongCrossRef.class, parentColumn = "setlistId", entityColumn = "songId"))
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.cryoprompter.data.SongEntity> songs;
    
    public SetlistWithSongs(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SetlistEntity setlist, @org.jetbrains.annotations.NotNull()
    java.util.List<com.cryoprompter.data.SongEntity> songs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.cryoprompter.data.SetlistEntity getSetlist() {
        return null;
    }
    
    public final void setSetlist(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SetlistEntity p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.cryoprompter.data.SongEntity> getSongs() {
        return null;
    }
    
    public final void setSongs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.cryoprompter.data.SongEntity> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.cryoprompter.data.SetlistEntity component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.cryoprompter.data.SongEntity> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.cryoprompter.data.SetlistWithSongs copy(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.data.SetlistEntity setlist, @org.jetbrains.annotations.NotNull()
    java.util.List<com.cryoprompter.data.SongEntity> songs) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}