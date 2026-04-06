package com.cryoprompter.importer;

import android.content.Context;
import android.net.Uri;
import com.cryoprompter.data.SongEntity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/cryoprompter/importer/SongbookProImporter;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "importBackup", "", "Lcom/cryoprompter/data/SongEntity;", "uri", "Landroid/net/Uri;", "readEntry", "", "zipStream", "Ljava/util/zip/ZipInputStream;", "parseChordPro", "content", "app_debug"})
public final class SongbookProImporter {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    public SongbookProImporter(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Imports a Songbook Pro backup (.sbpbackup) which is a ZIP file.
     * Extracts all songs and returns them as a list of SongEntities.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.cryoprompter.data.SongEntity> importBackup(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    private final java.lang.String readEntry(java.util.zip.ZipInputStream zipStream) {
        return null;
    }
    
    private final com.cryoprompter.data.SongEntity parseChordPro(java.lang.String content) {
        return null;
    }
}