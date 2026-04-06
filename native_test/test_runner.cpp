#include <iostream>
#include <vector>
#include "../app/src/main/cpp/JsxDeepCalibrator.h"

int main() {
    std::cout << "Starting Audio Calibration Test..." << std::endl;
    JsxDeepCalibrator calibrator;
    calibrator.start20SecondListen();
    
    // Simulate audio frames
    std::vector<float> audioBuffer(1024, 0.5f); // 1024 frames of dummy audio
    for(float sample : audioBuffer) {
        calibrator.process(sample);
    }
    
    std::cout << "Test completed without crashing!" << std::endl;
    return 0;
}
