#pragma once
#include <oboe/Oboe.h>
#include "JsxDeepCalibrator.h"

class AudioEngine : public oboe::AudioStreamCallback {
public:
    AudioEngine();
    ~AudioEngine();
    bool start();
    void stop();
    void startCalibration();
    float getCalibrationProgress();

    oboe::DataCallbackResult onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) override;

private:
    oboe::AudioStream *stream = nullptr;
    JsxDeepCalibrator calibrator;
};
