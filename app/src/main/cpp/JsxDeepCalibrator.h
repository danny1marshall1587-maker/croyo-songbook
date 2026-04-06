#pragma once
class JsxDeepCalibrator {
private:
    float threshold = 0.001f;
    bool isCalibrating = false;
    long samplesCaptured = 0;
    const long targetSamples = 882000; // 20 Seconds @ 44.1kHz
    float runningSumSquares = 0.0f;
    float attack = 0.9997f;  
    float release = 0.994f;  
    float envelope = 0.0f;
public:
    void start20SecondListen();
    float process(float sample);
    float getProgress() const;
    bool getIsCalibrating() const;
};
