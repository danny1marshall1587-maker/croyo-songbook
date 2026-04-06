package com.cryoprompter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavType;
import androidx.navigation.compose.*;
import com.cryoprompter.audio.AudioBridge;
import com.cryoprompter.data.AppDatabase;
import com.cryoprompter.importer.ChordProParser;
import kotlinx.coroutines.Dispatchers;
import com.cryoprompter.data.SongEntity;
import com.cryoprompter.service.CryoPrompterService;
import com.cryoprompter.ui.*;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0014J\b\u0010\f\u001a\u00020\tH\u0002J\b\u0010\r\u001a\u00020\tH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/cryoprompter/MainActivity;", "Landroidx/activity/ComponentActivity;", "<init>", "()V", "audioBridge", "Lcom/cryoprompter/audio/AudioBridge;", "chordProParser", "Lcom/cryoprompter/importer/ChordProParser;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "startAudioService", "onDestroy", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    @org.jetbrains.annotations.NotNull()
    private final com.cryoprompter.audio.AudioBridge audioBridge = null;
    @org.jetbrains.annotations.NotNull()
    private final com.cryoprompter.importer.ChordProParser chordProParser = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void startAudioService() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}