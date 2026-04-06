#include "JsxDeepCalibrator.h"
#include <cmath>

void JsxDeepCalibrator::start20SecondListen() {
    isCalibrating = true;
    samplesCaptured = 0;
    runningSumSquares = 0.0f;
}

float JsxDeepCalibrator::process(float sample) {
    if (isCalibrating) {
        runningSumSquares += (sample * sample);
        samplesCaptured++;
        
        if (samplesCaptured >= targetSamples) {
            float rmsFloor = std::sqrt(runningSumSquares / targetSamples);
            threshold = rmsFloor * 1.585f; 
            isCalibrating = false;
        }
        return 0.0f; 
    }

    float absValue = std::abs(sample);
    if (absValue > envelope) {
        envelope = absValue * (1.0f - attack) + envelope * attack;
    } else {
        envelope = absValue * (1.0f - release) + envelope * release;
    }

    return (envelope > threshold) ? sample : 0.0f;
}

float JsxDeepCalibrator::getProgress() const {
    return (float)samplesCaptured / targetSamples;
}

bool JsxDeepCalibrator::getIsCalibrating() const {
    return isCalibrating;
}
