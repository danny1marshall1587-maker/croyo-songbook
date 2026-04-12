package com.garethevans.church.opensongtablet.ai;

import androidx.annotation.NonNull;

/**
 * Definition of available AI Agents in the Cryo environment.
 */
public enum AiAgent {
    GEMINI_3_FLASH("Gemini 3 Flash", "gemini-3-flash", true),
    GEMMA_4("Gemma 4", "gemma-4b-it-gpu", false);

    private final String displayName;
    private final String modelId;
    private final boolean isCloud;

    AiAgent(String displayName, String modelId, boolean isCloud) {
        this.displayName = displayName;
        this.modelId = modelId;
        this.isCloud = isCloud;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getModelId() {
        return modelId;
    }

    public boolean isCloud() {
        return isCloud;
    }

    @NonNull
    @Override
    public String toString() {
        return displayName;
    }

    public static AiAgent fromString(String name) {
        for (AiAgent agent : AiAgent.values()) {
            if (agent.name().equalsIgnoreCase(name) || agent.displayName.equalsIgnoreCase(name)) {
                return agent;
            }
        }
        return GEMINI_3_FLASH; // Default
    }
}
