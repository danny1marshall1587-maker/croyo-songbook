package com.cryoprompter.ui;

import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import com.cryoprompter.audio.AudioBridge;
import com.cryoprompter.audio.VoskVoiceEngine;
import com.cryoprompter.audio.FuzzyMatcher;
import com.cryoprompter.importer.Song;
import androidx.compose.material.icons.*;
import androidx.compose.material.icons.filled.*;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\\\u0010\f\u001a\u00020\u0007*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00070\u0014H\u0007\u001a\u0018\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\u0007\u001a\u001e\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00070\u0014H\u0007\"\u001d\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u001f"}, d2 = {"ChordColorMap", "", "", "Landroidx/compose/ui/graphics/Color;", "getChordColorMap", "()Ljava/util/Map;", "PerformanceRenderer", "", "audioBridge", "Lcom/cryoprompter/audio/AudioBridge;", "song", "Lcom/cryoprompter/importer/Song;", "PerformanceHUD", "Landroidx/compose/foundation/layout/BoxScope;", "title", "isCalibrating", "", "progress", "", "onCalibrate", "Lkotlin/Function0;", "onZoomIn", "onZoomOut", "onPlayToggle", "ChordBlock", "chord", "scale", "LiveButton", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "onClick", "app_debug"})
public final class PerformanceRendererKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, androidx.compose.ui.graphics.Color> ChordColorMap = null;
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.Map<java.lang.String, androidx.compose.ui.graphics.Color> getChordColorMap() {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PerformanceRenderer(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.audio.AudioBridge audioBridge, @org.jetbrains.annotations.NotNull()
    com.cryoprompter.importer.Song song) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PerformanceHUD(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.BoxScope $this$PerformanceHUD, @org.jetbrains.annotations.NotNull()
    java.lang.String title, boolean isCalibrating, float progress, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCalibrate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onZoomIn, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onZoomOut, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPlayToggle) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ChordBlock(@org.jetbrains.annotations.NotNull()
    java.lang.String chord, float scale) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LiveButton(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.graphics.vector.ImageVector icon, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}