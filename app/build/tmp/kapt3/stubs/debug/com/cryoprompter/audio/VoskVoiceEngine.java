package com.cryoprompter.audio;

import android.content.Context;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.SpeechService;
import org.vosk.android.StorageService;
import java.io.IOException;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0018B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u001a\u0010\f\u001a\u00020\r2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\r0\u000fJ\u001c\u0010\u0011\u001a\u00020\r2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\u0015\u001a\u00020\rJ\u0010\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0014H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/cryoprompter/audio/VoskVoiceEngine;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "model", "Lorg/vosk/Model;", "speechService", "Lorg/vosk/android/SpeechService;", "listener", "Lcom/cryoprompter/audio/VoskVoiceEngine$RecognitionListener;", "initModel", "", "onComplete", "Lkotlin/Function1;", "", "startListening", "grammar", "", "", "stopListening", "parseHypothesis", "hypothesis", "RecognitionListener", "app_debug"})
public final class VoskVoiceEngine {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private org.vosk.Model model;
    @org.jetbrains.annotations.Nullable()
    private org.vosk.android.SpeechService speechService;
    @org.jetbrains.annotations.Nullable()
    private com.cryoprompter.audio.VoskVoiceEngine.RecognitionListener listener;
    
    public VoskVoiceEngine(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void initModel(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onComplete) {
    }
    
    public final void startListening(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> grammar, @org.jetbrains.annotations.NotNull()
    com.cryoprompter.audio.VoskVoiceEngine.RecognitionListener listener) {
    }
    
    public final void stopListening() {
    }
    
    private final java.lang.String parseHypothesis(java.lang.String hypothesis) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH&J\u0014\u0010\n\u001a\u00020\u00032\n\u0010\u000b\u001a\u00060\fj\u0002`\rH&\u00a8\u0006\u000e\u00c0\u0006\u0003"}, d2 = {"Lcom/cryoprompter/audio/VoskVoiceEngine$RecognitionListener;", "", "onResult", "", "text", "", "onPartialResult", "onCommand", "command", "Lcom/cryoprompter/audio/FuzzyMatcher$Command;", "onError", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "app_debug"})
    public static abstract interface RecognitionListener {
        
        public abstract void onResult(@org.jetbrains.annotations.NotNull()
        java.lang.String text);
        
        public abstract void onPartialResult(@org.jetbrains.annotations.NotNull()
        java.lang.String text);
        
        public abstract void onCommand(@org.jetbrains.annotations.NotNull()
        com.cryoprompter.audio.FuzzyMatcher.Command command);
        
        public abstract void onError(@org.jetbrains.annotations.NotNull()
        java.lang.Exception e);
    }
}