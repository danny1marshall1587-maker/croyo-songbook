package com.cryoprompter.importer

import android.content.Context
import android.net.Uri
import com.cryoprompter.data.SongEntity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.ZipInputStream

class SongbookProImporter(private val context: Context) {

    /**
     * Imports a Songbook Pro backup (.sbpbackup) which is a ZIP file.
     * Extracts all songs and returns them as a list of SongEntities.
     */
    fun importBackup(uri: Uri): List<SongEntity> {
        val songs = mutableListOf<SongEntity>()
        
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            ZipInputStream(inputStream).use { zipStream ->
                var entry = zipStream.nextEntry
                while (entry != null) {
                    if (!entry.isDirectory && entry.name.endsWith(".pro", ignoreCase = true)) {
                        val content = readEntry(zipStream)
                        songs.add(parseChordPro(content))
                    }
                    entry = zipStream.nextEntry
                }
            }
        }
        return songs
    }

    private fun readEntry(zipStream: ZipInputStream): String {
        val reader = BufferedReader(InputStreamReader(zipStream))
        val sb = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        return sb.toString()
    }

    private fun parseChordPro(content: String): SongEntity {
        // Basic Metadata extraction from ChordPro
        val titleMatch = Regex("\\{title:\\s*(.*?)\\}").find(content)
        val artistMatch = Regex("\\{artist:\\s*(.*?)\\}").find(content)
        val bpmMatch = Regex("\\{bpm:\\s*(\\d+)\\}").find(content)
        
        return SongEntity(
            title = titleMatch?.groupValues?.get(1) ?: "Imported Song",
            artist = artistMatch?.groupValues?.get(1),
            bpm = bpmMatch?.groupValues?.get(1)?.toIntOrNull() ?: 120,
            chordProContent = content,
            originalKey = "G", // Default, should be parsed correctly in real logic
            displayKey = "G",
            capo = 0,
            durationSeconds = 180,
            syncMap = null,
            isTrained = false,
            isFavorite = false,
            lastAccessed = System.currentTimeMillis()
        )
    }
}
