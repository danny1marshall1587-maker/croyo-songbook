package com.garethevans.church.opensongtablet.audio;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.SpeechService;
import org.vosk.android.StorageService;

import java.io.IOException;
import java.util.List;

/**
 * Vosk Voice Recognition Engine ported from Kotlin.
 * Handles model initialization and speech service management.
 */
public class VoskVoiceEngine {
    private static final String TAG = "VoskVoiceEngine";
    private final Context context;
    private Model model;
    private SpeechService speechService;
    private RecognitionListener listener;

    public interface RecognitionListener {
        void onResult(String text);
        void onPartialResult(String text);
        void onCommand(FuzzyMatcher.Command command);
        void onError(Exception e);
    }

    public VoskVoiceEngine(Context context) {
        this.context = context;
    }

    public void initModel(final OnModelLoadedCallback callback) {
        StorageService.unpack(context, "model-en-us", "model",
                model -> {
                    this.model = model;
                    callback.onComplete(true);
                },
                e -> {
                    Log.e(TAG, "Failed to unpack Vosk model", e);
                    callback.onComplete(false);
                }
        );
    }

    public interface OnModelLoadedCallback {
        void onComplete(boolean success);
    }

    public void startListening(List<String> grammar, RecognitionListener listener) {
        this.listener = listener;
        if (model == null) {
            listener.onError(new Exception("Model not initialized"));
            return;
        }

        try {
            // Initialize Vosk Recognizer (16k sample rate is recommended for Vosk)
            Recognizer rec = new Recognizer(model, 16000.0f);
            
            speechService = new SpeechService(rec, 16000.0f);
            speechService.startListening(new org.vosk.android.RecognitionListener() {
                @Override
                public void onResult(String hypothesis) {
                    String text = parseHypothesis(hypothesis);
                    if (text.isEmpty()) return;

                    // I. Check for commands first
                    FuzzyMatcher.Command command = FuzzyMatcher.detectCommand(text);
                    if (command != null) {
                        listener.onCommand(command);
                    } else {
                        // II. Process as general result
                        listener.onResult(text);
                    }
                }

                @Override
                public void onPartialResult(String hypothesis) {
                    listener.onPartialResult(parseHypothesis(hypothesis));
                }

                @Override
                public void onFinalResult(String hypothesis) {
                    // Final results are usually handled in onResult
                }

                @Override
                public void onError(Exception e) {
                    listener.onError(e);
                }

                @Override
                public void onTimeout() {
                    // Stage-Safe: Auto-restart if silence is too long
                    stopListening();
                    startListening(grammar, listener);
                }
            });
        } catch (IOException e) {
            listener.onError(e);
        }
    }

    public void stopListening() {
        if (speechService != null) {
            speechService.stop();
            speechService = null;
        }
    }

    private String parseHypothesis(String hypothesis) {
        try {
            JSONObject obj = new JSONObject(hypothesis);
            if (obj.has("text")) {
                return obj.getString("text");
            } else if (obj.has("partial")) {
                return obj.getString("partial");
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse hypothesis: " + hypothesis);
        }
        return "";
    }
}
