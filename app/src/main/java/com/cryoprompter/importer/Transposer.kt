package com.cryoprompter.importer

object Transposer {
    private val SHARP_NOTES = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    private val FLAT_NOTES = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")

    fun transpose(chord: String, semitones: Int): String {
        if (semitones == 0) return chord
        
        // Handle complex chords (e.g., Gmaj7/B)
        if (chord.contains("/")) {
            val parts = chord.split("/")
            return transpose(parts[0], semitones) + "/" + transpose(parts[1], semitones)
        }

        val root = extractRoot(chord)
        val suffix = chord.substring(root.length)
        
        val notes = if (root.contains("b")) FLAT_NOTES else SHARP_NOTES
        val currentIndex = notes.indexOf(normalize(root))
        
        if (currentIndex == -1) return chord // Fallback

        var newIndex = (currentIndex + semitones) % 12
        if (newIndex < 0) newIndex += 12
        
        return notes[newIndex] + suffix
    }

    private fun extractRoot(chord: String): String {
        return if (chord.length > 1 && (chord[1] == '#' || chord[1] == 'b')) {
            chord.substring(0, 2)
        } else {
            chord.substring(0, 1)
        }
    }

    private fun normalize(root: String): String {
        return when (root) {
            "Db" -> "C#"
            "Eb" -> "D#"
            "Gb" -> "F#"
            "Ab" -> "G#"
            "Bb" -> "A#"
            else -> root
        }
    }

    fun transposeChordPro(content: String, semitones: Int): String {
        val regex = "\\[(.*?)\\]".toRegex()
        return regex.replace(content) { matchResult ->
            val chord = matchResult.groupValues[1]
            "[" + transpose(chord, semitones) + "]"
        }
    }
}
