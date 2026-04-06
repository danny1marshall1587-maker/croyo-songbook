package com.cryoprompter.audio

import kotlin.math.min

object FuzzyMatcher {
    /**
     * Calculates the best matching line index in a window of lyrics.
     * @param normalizedLyrics List of lines (lowercase, no punctuation)
     * @param recognizedWords The stream of recognized words from Vosk
     * @param currentLineIndex The last known position
     * @param windowSize How many lines ahead to look
     */
    fun findBestMatch(
        normalizedLyrics: List<String>,
        recognizedWords: String,
        currentLineIndex: Int,
        windowSize: Int = 3
    ): Int? {
        val words = recognizedWords.lowercase().split(" ").filter { it.length > 3 }
        if (words.isEmpty()) return null

        val startIndex = currentLineIndex
        val endIndex = min(currentLineIndex + windowSize, normalizedLyrics.size - 1)

        for (i in startIndex..endIndex) {
            val line = normalizedLyrics[i].lowercase()
            // High-velocity "Contains" check for performance
            for (word in words) {
                if (line.contains(word)) {
                    return i
                }
            }
        }
        return null
    }

    /**
     * Detects "Computer" prefix commands.
     * returns Command enum or null
     */
    fun detectCommand(text: String): Command? {
        val t = text.lowercase()
        return when {
            t.contains("computer top") || t.contains("computer reset") -> Command.GO_TOP
            t.contains("computer next") || t.contains("computer snap") -> Command.GO_NEXT
            t.contains("computer back") || t.contains("computer previous") -> Command.GO_BACK
            t.contains("computer chorus") -> Command.GO_CHORUS
            else -> null
        }
    }

    enum class Command {
        GO_TOP, GO_NEXT, GO_BACK, GO_CHORUS
    }
}
