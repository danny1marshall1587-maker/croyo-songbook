package com.cryoprompter.importer

data class ChordLyricLine(
    val chords: String,
    val lyrics: String,
    val isPreemptiveLead: Boolean = false,
    val isSectionHeader: Boolean = false
)

data class Song(
    val title: String,
    val artist: String? = null,
    val bpm: Int = 120,
    val lines: List<ChordLyricLine>,
    val rawContent: String
)

class ChordProParser {
    fun parse(content: String): Song {
        var title = "Unknown Song"
        var artist: String? = null
        var bpm = 120
        val lines = mutableListOf<ChordLyricLine>()

        content.lines().forEach { rawLine ->
            val line = rawLine.trim()
            if (line.isEmpty()) return@forEach

            // Handle Directives {tag: value}
            if (line.startsWith("{") && line.endsWith("}")) {
                val directive = line.substring(1, line.length - 1)
                val split = directive.indexOf(':')
                if (split != -1) {
                    val key = directive.substring(0, split).trim().lowercase()
                    val value = directive.substring(split + 1).trim()
                    
                    when (key) {
                        "title", "t" -> title = value
                        "artist", "st" -> artist = value
                        "bpm" -> bpm = value.toIntOrNull() ?: 120
                        "comment", "c" -> lines.add(ChordLyricLine("", value, isSectionHeader = true))
                    }
                }
            } else {
                // Parse chord/lyric line [G]Hello [C]World
                val renderedChords = StringBuilder()
                val renderedLyrics = StringBuilder()
                var currentChord = StringBuilder()
                var insideChord = false
                
                var charIndex = 0
                while (charIndex < rawLine.length) {
                    val char = rawLine[charIndex]
                    if (char == '[') {
                        insideChord = true
                    } else if (char == ']') {
                        insideChord = false
                        // Pad chords to match lyric position
                        while (renderedChords.length < renderedLyrics.length) {
                            renderedChords.append(" ")
                        }
                        renderedChords.append(currentChord.toString())
                        currentChord = StringBuilder()
                    } else {
                        if (insideChord) {
                            currentChord.append(char)
                        } else {
                            renderedLyrics.append(char)
                        }
                    }
                    charIndex++
                }
                
                if (renderedChords.isNotEmpty() || renderedLyrics.isNotBlank()) {
                    lines.add(ChordLyricLine(renderedChords.toString(), renderedLyrics.toString()))
                }
            }
        }

        return Song(title, artist, bpm, lines, content)
    }
}
