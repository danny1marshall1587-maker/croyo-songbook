package com.cryoprompter.audio;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0010B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J5\u0010\u0004\u001a\u0004\u0018\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\b\u00a8\u0006\u0011"}, d2 = {"Lcom/cryoprompter/audio/FuzzyMatcher;", "", "<init>", "()V", "findBestMatch", "", "normalizedLyrics", "", "", "recognizedWords", "currentLineIndex", "windowSize", "(Ljava/util/List;Ljava/lang/String;II)Ljava/lang/Integer;", "detectCommand", "Lcom/cryoprompter/audio/FuzzyMatcher$Command;", "text", "Command", "app_debug"})
public final class FuzzyMatcher {
    @org.jetbrains.annotations.NotNull()
    public static final com.cryoprompter.audio.FuzzyMatcher INSTANCE = null;
    
    private FuzzyMatcher() {
        super();
    }
    
    /**
     * Calculates the best matching line index in a window of lyrics.
     * @param normalizedLyrics List of lines (lowercase, no punctuation)
     * @param recognizedWords The stream of recognized words from Vosk
     * @param currentLineIndex The last known position
     * @param windowSize How many lines ahead to look
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer findBestMatch(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> normalizedLyrics, @org.jetbrains.annotations.NotNull()
    java.lang.String recognizedWords, int currentLineIndex, int windowSize) {
        return null;
    }
    
    /**
     * Detects "Computer" prefix commands.
     * returns Command enum or null
     */
    @org.jetbrains.annotations.Nullable()
    public final com.cryoprompter.audio.FuzzyMatcher.Command detectCommand(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/cryoprompter/audio/FuzzyMatcher$Command;", "", "<init>", "(Ljava/lang/String;I)V", "GO_TOP", "GO_NEXT", "GO_BACK", "GO_CHORUS", "app_debug"})
    public static enum Command {
        /*public static final*/ GO_TOP /* = new GO_TOP() */,
        /*public static final*/ GO_NEXT /* = new GO_NEXT() */,
        /*public static final*/ GO_BACK /* = new GO_BACK() */,
        /*public static final*/ GO_CHORUS /* = new GO_CHORUS() */;
        
        Command() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.cryoprompter.audio.FuzzyMatcher.Command> getEntries() {
            return null;
        }
    }
}