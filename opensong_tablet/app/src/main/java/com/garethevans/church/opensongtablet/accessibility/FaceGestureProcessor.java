package com.garethevans.church.opensongtablet.accessibility;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker;
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult;

import java.util.Optional;

public class FaceGestureProcessor {

    private static final String TAG = "FaceGestureProcessor";
    private static final String MODEL_PATH = "face_landmarker.task";
    
    private FaceLandmarker faceLandmarker;
    private final Context context;
    private final FaceGestureListener listener;
    
    private boolean isPaused = false;
    private long lastTriggerTime = 0;
    private static final long COOLDOWN_MS = 1500;

    // Thresholds for gestures
    private static final float MOUTH_OPEN_THRESHOLD = 0.5f; // jawOpen
    private static final float EYE_BLINK_THRESHOLD = 0.8f;  // eyeBlinkLeft/Right

    public interface FaceGestureListener {
        void onMouthOpen();
        void onEyebrowRaise();
        void onWinkLeft();
        void onWinkRight();
        void onHeadNod();
        void onHeadRaise();
    }

    public FaceGestureProcessor(Context context, FaceGestureListener listener) {
        this.context = context;
        this.listener = listener;
        setupFaceLandmarker();
    }

    private void setupFaceLandmarker() {
        try {
            BaseOptions baseOptions = BaseOptions.builder()
                    .setModelAssetPath(MODEL_PATH)
                    .build();

            FaceLandmarker.FaceLandmarkerOptions options = FaceLandmarker.FaceLandmarkerOptions.builder()
                    .setBaseOptions(baseOptions)
                    .setRunningMode(RunningMode.LIVE_STREAM)
                    .setResultListener(this::processResult)
                    .setErrorListener(this::processError)
                    .setOutputFaceBlendshapes(true)
                    .build();

            faceLandmarker = FaceLandmarker.createFromOptions(context, options);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize FaceLandmarker: " + e.getMessage());
        }
    }

    private void processResult(FaceLandmarkerResult result, com.google.mediapipe.framework.image.MPImage image) {
        if (isPaused || result.faceBlendshapes().isEmpty()) return;

        // Check blendshapes for gestures
        // Index 0 of faceBlendshapes corresponds to the first detected face
        result.faceBlendshapes().get().get(0).forEach(category -> {
            String label = category.categoryName();
            float score = category.score();

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTriggerTime < COOLDOWN_MS) return;

            // Gesture Logic: Eyebrow Raise (browInnerUp) -> Custom Action
            if (label.equals("browInnerUp") && score > 0.6f) {
                Log.d(TAG, "Eyebrow Raise detected! Score: " + score);
                lastTriggerTime = currentTime;
                listener.onEyebrowRaise();
                return;
            }

            // Gesture Logic: Mouth Open (jawOpen) -> Flip Next (Custom Action)
            if (label.equals("jawOpen") && score > MOUTH_OPEN_THRESHOLD) {
                Log.d(TAG, "Mouth Open detected! Score: " + score);
                lastTriggerTime = currentTime;
                listener.onMouthOpen();
                return;
            }

            // Gesture Logic: Wink Left (eyeBlinkLeft)
            if (label.equals("eyeBlinkLeft") && score > EYE_BLINK_THRESHOLD) {
                // Check if it's asymmetrical (other eye is open)
                float otherEyeScore = getScoreForLabel(result, "eyeBlinkRight");
                if (otherEyeScore < 0.3f) {
                    Log.d(TAG, "Wink Left detected! Score: " + score);
                    lastTriggerTime = currentTime;
                    listener.onWinkLeft();
                    return;
                }
            }

            // Gesture Logic: Wink Right (eyeBlinkRight)
            if (label.equals("eyeBlinkRight") && score > EYE_BLINK_THRESHOLD) {
                // Check if it's asymmetrical
                float otherEyeScore = getScoreForLabel(result, "eyeBlinkLeft");
                if (otherEyeScore < 0.3f) {
                    Log.d(TAG, "Wink Right detected! Score: " + score);
                    lastTriggerTime = currentTime;
                    listener.onWinkRight();
                    return;
                }
            }
        });

        // Head Nod Detection (Using landmarks directly) -> Scroll Down
        if (result.faceLandmarks() != null && !result.faceLandmarks().isEmpty()) {
            var landmarks = result.faceLandmarks().get(0);
            // Nose tip is index 1, Eyes average can be derived
            float noseY = landmarks.get(1).y();
            float leftEyeY = landmarks.get(33).y(); // Left eye outer corner
            float rightEyeY = landmarks.get(263).y(); // Right eye outer corner
            float eyeCenterY = (leftEyeY + rightEyeY) / 2.0f;

            // Simple delta tracking for a "nod" (fast downward movement)
            float nodValue = noseY - eyeCenterY;
            if (nodValue > 0.15f) { // Nose significantly below eyes relative to neutral
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTriggerTime > COOLDOWN_MS) {
                    Log.d(TAG, "Head Nod detected! Delta: " + nodValue);
                    lastTriggerTime = currentTime;
                    listener.onHeadNod();
                }
            } else if (nodValue < -0.05f) { // Simple check for head raise
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTriggerTime > COOLDOWN_MS) {
                    Log.d(TAG, "Head Raise detected! Delta: " + nodValue);
                    lastTriggerTime = currentTime;
                    listener.onHeadRaise();
                }
            }
        }
    }

    private float getScoreForLabel(FaceLandmarkerResult result, String label) {
        if (result.faceBlendshapes().isEmpty()) return 0f;
        for (var category : result.faceBlendshapes().get().get(0)) {
            if (category.categoryName().equals(label)) {
                return category.score();
            }
        }
        return 0f;
    }

    private void processError(RuntimeException error) {
        Log.e(TAG, "Mediapipe Error: " + error.getMessage());
    }

    public void processImage(com.google.mediapipe.framework.image.MPImage image, long timestamp) {
        if (faceLandmarker != null && !isPaused) {
            faceLandmarker.detectAsync(image, timestamp);
        }
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void close() {
        if (faceLandmarker != null) {
            faceLandmarker.close();
        }
    }
}
