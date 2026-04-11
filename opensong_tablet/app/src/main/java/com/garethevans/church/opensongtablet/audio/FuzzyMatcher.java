package com.garethevans.church.opensongtablet.audio;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Logic for matching recognized voice text against song lyrics and commands.
 */
public class FuzzyMatcher {

    public enum Command {
        GO_TOP, GO_NEXT, GO_BACK, GO_CHORUS
    }

    /**
     * Calculates the best matching line index in a window of lyrics.
     * @param normalizedLyrics List of lines (lowercase, no punctuation)
     * @param recognizedWords The stream of recognized words from Vosk
     * @param currentLineIndex The last known position
     * @param windowSize How many lines ahead to look
     * @return The index of the best matching line, or null if no match found.
     */
    public static Integer findBestMatch(
            List<String> normalizedLyrics,
            String recognizedWords,
            int currentLineIndex,
            int windowSize
    ) {
        if (recognizedWords == null || recognizedWords.isEmpty()) return null;
        
        // Filter words longer than 3 chars for performance and accuracy
        List<String> words = Arrays.stream(recognizedWords.toLowerCase().split(" "))
                .filter(w -> w.length() > 3)
                .collect(Collectors.toList());
                
        if (words.isEmpty()) return null;

        int startIndex = currentLineIndex;
        int endIndex = Math.min(currentLineIndex + windowSize, normalizedLyrics.size() - 1);

        for (int i = startIndex; i <= endIndex; i++) {
            String line = normalizedLyrics.get(i).toLowerCase();
            // High-velocity "Contains" check for performance
            for (String word : words) {
                if (line.contains(word)) {
                    return i;
                }
            }
        }
        return null;
    }

    /**
     * Detects "Computer" prefix commands.
     * @param text The recognized text
     * @return Command enum or null
     */
    public static Command detectCommand(String text) {
        if (text == null) return null;
        String t = text.toLowerCase();
        
        if (t.contains("computer top") || t.contains("computer reset")) {
            return Command.GO_TOP;
        } else if (t.contains("computer next") || t.contains("computer snap")) {
            return Command.GO_NEXT;
        } else if (t.contains("computer back") || t.contains("computer previous")) {
            return Command.GO_BACK;
        } else if (t.contains("computer chorus")) {
            return Command.GO_CHORUS;
        }
        
        return null;
    }
}
