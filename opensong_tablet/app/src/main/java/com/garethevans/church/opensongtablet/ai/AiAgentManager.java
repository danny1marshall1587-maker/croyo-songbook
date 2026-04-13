package com.garethevans.church.opensongtablet.ai;

import android.content.Context;
import android.util.Log;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;
import java.util.List;

/**
 * Singleton Manager for AI Services in Cryo-Songbook.
 */
public class AiAgentManager {
    private static final String TAG = "AiAgentManager";
    private static AiAgentManager instance;
    private final Context context;
    private AiAgent activeAgent = AiAgent.GEMINI_3_FLASH;
    private List<String> currentSongLines;

    private AiAgentManager(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
            loadActiveAgent(context);
        } else {
            this.context = null;
            Log.e(TAG, "AiAgentManager initialized with NULL context!");
        }
    }

    public static synchronized AiAgentManager getInstance(Context context) {
        if (instance == null && context != null) {
            instance = new AiAgentManager(context);
        }
        return instance;
    }

    private void loadActiveAgent(Context initContext) {
        if (initContext instanceof MainActivityInterface) {
            MainActivityInterface mainInterface = (MainActivityInterface) initContext;
            if (mainInterface.getPreferences() != null) {
                String savedAgent = mainInterface.getPreferences()
                        .getMyPreferenceString("aiAgentActive", AiAgent.GEMINI_3_FLASH.name());
                activeAgent = AiAgent.fromString(savedAgent);
            }
        } else {
            // Fallback: Default agent already set as FIELD GEMINI_3_FLASH
            Log.d(TAG, "loadActiveAgent: Context is not MainActivityInterface or Preferences missing.");
        }
    }

    public void setActiveAgent(AiAgent agent) {
        this.activeAgent = agent;
        Log.d(TAG, "Active AI Agent switched to: " + agent.getDisplayName());
        
        if (agent == AiAgent.GEMMA_4) {
            initGemma();
        } else {
            initGemini();
        }
    }

    public AiAgent getActiveAgent() {
        return activeAgent;
    }

    public void setSongContext(List<String> lines) {
        this.currentSongLines = lines;
        Log.d(TAG, "Song context set: " + (lines != null ? lines.size() : 0) + " lines.");
    }

    /**
     * Uses the AI agent to determine the current line index based on recognized text.
     */
    public void findCurrentLine(String recognizedText, int lastIndex, AiCallback callback) {
        if (currentSongLines == null || currentSongLines.isEmpty()) {
            callback.onError(new Exception("No song context available"));
            return;
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a song prompter assistant. Given the following song lyrics (indexed):\n");
        for (int i = 0; i < currentSongLines.size(); i++) {
            prompt.append(i).append(": ").append(currentSongLines.get(i)).append("\n");
        }
        prompt.append("\nThe user is currently singing: \"").append(recognizedText).append("\"\n");
        prompt.append("The last known position was index ").append(lastIndex).append(".\n");
        prompt.append("Identify the index of the line currently being sung. ");
        prompt.append("Return ONLY the integer index. If uncertain, return the last known index.");

        generateResponse(prompt.toString(), callback);
    }

    private void initGemma() {
        Log.i(TAG, "Initializing Gemma 4 (Local Inference Context)...");
    }

    private void initGemini() {
        Log.i(TAG, "Initializing Gemini 3 Flash (Cloud API Context)...");
    }

    public void generateResponse(String prompt, AiCallback callback) {
        Log.d(TAG, "Generating response with " + activeAgent.getDisplayName() + ": " + prompt);
        // Simulate response for now
        // In a real scenario, this would call MediaPipe or Gemini API
        callback.onResult("0"); 
    }

    public interface AiCallback {
        void onResult(String text);
        void onError(Exception e);
    }
}
