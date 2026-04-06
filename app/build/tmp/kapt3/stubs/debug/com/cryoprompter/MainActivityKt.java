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

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0007\u00a8\u0006\u0006"}, d2 = {"AppNavigation", "", "audioBridge", "Lcom/cryoprompter/audio/AudioBridge;", "parser", "Lcom/cryoprompter/importer/ChordProParser;", "app_debug"})
public final class MainActivityKt {
    
    @androidx.compose.runtime.Composable()
    public static final void AppNavigation(@org.jetbrains.annotations.NotNull()
    com.cryoprompter.audio.AudioBridge audioBridge, @org.jetbrains.annotations.NotNull()
    com.cryoprompter.importer.ChordProParser parser) {
    }
}