package com.cryoprompter.audio

class AudioBridge {
    init {
        System.loadLibrary("cryoprompter")
    }

    external fun startEngine()
    external fun stopEngine()
    external fun startCalibration()
    external fun getCalibrationProgress(): Float
}
