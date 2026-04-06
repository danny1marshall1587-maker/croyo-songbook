#include "AudioEngine.h"

AudioEngine::AudioEngine() {}

AudioEngine::~AudioEngine() {
    stop();
}

bool AudioEngine::start() {
    oboe::AudioStreamBuilder builder;
    builder.setDirection(oboe::Direction::Input);
    builder.setPerformanceMode(oboe::PerformanceMode::LowLatency);
    builder.setFormat(oboe::AudioFormat::Float);
    builder.setChannelCount(1);
    builder.setCallback(this);

    oboe::Result result = builder.openStream(&stream);
    if (result != oboe::Result::OK) return false;

    result = stream->requestStart();
    return result == oboe::Result::OK;
}

void AudioEngine::stop() {
    if (stream) {
        stream->stop();
        stream->close();
        stream = nullptr;
    }
}

void AudioEngine::startCalibration() {
    calibrator.start20SecondListen();
}

float AudioEngine::getCalibrationProgress() {
    return calibrator.getProgress();
}

oboe::DataCallbackResult AudioEngine::onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) {
    float *floatData = static_cast<float*>(audioData);
    for (int i = 0; i < numFrames; ++i) {
        floatData[i] = calibrator.process(floatData[i]);
    }
    return oboe::DataCallbackResult::Continue;
}
