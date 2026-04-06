/**
 * CRYO-PROMPTER: JSx-Style Denoise Engine
 * 20-Second Deep Noise Fingerprint Logic
 */

#include <cmath>
#include <vector>

class JsxDeepCalibrator {
private:
    float threshold = 0.001f;
    bool isCalibrating = false;
    long samplesCaptured = 0;
    const long targetSamples = 882000; // 20 Seconds @ 44.1kHz
    float runningSumSquares = 0.0f;

    // JSx Smoothing parameters for natural gate feel
    float attack = 0.9997f;  
    float release = 0.994f;  
    float envelope = 0.0f;

public:
    void start20SecondListen() {
        isCalibrating = true;
        samplesCaptured = 0;
        runningSumSquares = 0.0f;
    }

    float process(float sample) {
        if (isCalibrating) {
            runningSumSquares += (sample * sample);
            samplesCaptured++;
            
            if (samplesCaptured >= targetSamples) {
                float rmsFloor = std::sqrt(runningSumSquares / targetSamples);
                // Set threshold 4dB above noise floor for high-gain contact mic safety
                threshold = rmsFloor * 1.585f; 
                isCalibrating = false;
            }
            return 0.0f; // Silence during the 20s learning window
        }

        // JSx Gate Processing
        float absValue = std::abs(sample);
        if (absValue > envelope) {
            envelope = absValue * (1.0f - attack) + envelope * attack;
        } else {
            envelope = absValue * (1.0f - release) + envelope * release;
        }

        // Return clean signal if above fingerprint threshold, else total silence
        return (envelope > threshold) ? sample : 0.0f;
    }

    float getProgress() const { return (float)samplesCaptured / targetSamples; }
};
