package com.garethevans.church.opensongtablet.audio;

import android.util.Log;

/**
 * Cyber-Denoiser LITE (5-Band Forensic - V92 Master)
 * Ported from JSx for OpenSongTablet
 * Author: Danny Marshall (ported by AI)
 */
public class JsxDeepCalibrator {
    private static final String TAG = "JsxDeepCalibrator";
    
    // Configurable Parameters (mapped from JSx sliders)
    private float lastMasterThreshold = -60.0f;
    private float[] bandThresholds = new float[]{-60f, -60f, -60f, -60f, -60f}; // slider1-5
    private float floor = 0.05f;      // slider13
    private float gradient = 6.0f;    // slider15
    private float hysteresis = 3.0f;  // slider21
    private float attackMs = 5.0f;    // slider11
    private float releaseMs = 100.0f; // slider12
    private boolean rmsMode = true;   // slider17
    private boolean lowCut = true;    // slider18

    // Internal State
    private float srate = 44100.0f;
    private float att, rel, sm_f, rms_f;
    private float hp_in = 0;
    private float lp1, lp2, lp3, lp4;
    private float[] r = new float[5];
    private float[] e = new float[5];
    private float[] g = new float[]{1f, 1f, 1f, 1f, 1f};
    private float[] tg = new float[]{1f, 1f, 1f, 1f, 1f};
    private int[] states = new int[5];

    private boolean isCalibrating = false;
    private long samplesCaptured = 0;
    private final long targetSamples = 882000; // 20 Seconds @ 44.1kHz
    private float[] maxNoiseBands = new float[5];

    public JsxDeepCalibrator() {
        updateParams();
    }

    private void updateParams() {
        att = (float) Math.exp(-1.0 / (attackMs * srate / 1000.0));
        rel = (releaseMs == 0) ? 0 : (float) Math.exp(-1.0 / (releaseMs * srate / 1000.0));
        sm_f = 1.0f - (float) Math.exp(-1.0 / (15.0 * srate / 1000.0));
        rms_f = 1.0f - (float) Math.exp(-1.0 / (0.050 * srate));
    }

    public void setSrate(float sampleRate) {
        this.srate = sampleRate;
        updateParams();
    }

    public void setMasterThreshold(float newMaster) {
        float diff = newMaster - lastMasterThreshold;
        for (int i = 0; i < 5; i++) {
            bandThresholds[i] = Math.max(-100, Math.min(0, bandThresholds[i] + diff));
        }
        lastMasterThreshold = newMaster;
    }

    public void start20SecondListen() {
        isCalibrating = true;
        samplesCaptured = 0;
        for (int i = 0; i < 5; i++) maxNoiseBands[i] = 0.000001f;
        Log.d(TAG, "Starting 5-band JSx calibration...");
    }

    public float process(float s0) {
        // 1. Low Cut
        if (lowCut) {
            float hp_f = (float) (40.0 * 2.0 * Math.PI / srate);
            hp_in += (s0 - hp_in) * hp_f;
            s0 -= hp_in;
        }

        // 2. 5-Band Crossover
        float f = 150.0f / srate;
        lp1 += (s0 - lp1) * f;
        float b1 = lp1;
        lp2 += (s0 - lp2) * f * 4.0f;
        float b2 = lp2 - lp1;
        lp3 += (s0 - lp3) * f * 20.0f;
        float b3 = lp3 - lp2;
        lp4 += (s0 - lp4) * f * 53.0f;
        float b4 = lp4 - lp3;
        float b5 = s0 - lp4;

        float[] bands = {b1, b2, b3, b4, b5};

        // 3. Calibration / Detection
        for (int i = 0; i < 5; i++) {
            if (rmsMode) {
                e[i] += (bands[i] * bands[i] - e[i]) * rms_f;
                r[i] = (float) Math.sqrt(Math.max(0, e[i]));
            } else {
                r[i] = r[i] * rel + Math.abs(bands[i]) * (1.0f - rel);
            }

            if (isCalibrating) {
                if (r[i] > maxNoiseBands[i]) maxNoiseBands[i] = r[i];
            }
        }

        if (isCalibrating) {
            samplesCaptured++;
            if (samplesCaptured >= targetSamples) {
                isCalibrating = false;
                // Auto-set thresholds based on noise fingerprint (plus 3dB safety)
                for (int i = 0; i < 5; i++) {
                    bandThresholds[i] = (float) (20.0 * Math.log10(maxNoiseBands[i] + 0.000001) + 3.0);
                    bandThresholds[i] = Math.min(0, Math.max(-100, bandThresholds[i]));
                }
                Log.d(TAG, "Calibration complete. Thresholds set per band.");
            }
            return 0.0f;
        }

        // 4. Ghost Gain Calculation
        for (int i = 0; i < 5; i++) {
            tg[i] = tg[i] * att + calcGhostGain(r[i], bandThresholds[i], hysteresis, gradient, floor, i) * (1.0f - att);
            g[i] += (tg[i] - g[i]) * sm_f;
        }

        // 5. Reconstruction
        return b1 * g[0] + b2 * g[1] + b3 * g[2] + b4 * g[3] + b5 * g[4];
    }

    private float calcGhostGain(float r_in, float t_db, float h_db, float gn, float f_lv, int st_idx) {
        float r_db = (float) (20.0 * Math.log10(r_in + 0.000001));
        
        // Hysteresis State
        if (r_db > t_db) {
            states[st_idx] = 1;
        } else if (r_db < (t_db - h_db)) {
            states[st_idx] = 0;
        }

        if (states[st_idx] == 1) return 1.0f;
        
        // Knee transition
        if (r_db <= (t_db - gn)) return f_lv;
        
        return f_lv + (1.0f - f_lv) * ((r_db - (t_db - gn)) / gn);
    }

    public boolean isCalibrating() { return isCalibrating; }
    public float getProgress() { return (float) samplesCaptured / targetSamples; }
}
