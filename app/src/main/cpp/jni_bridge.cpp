#include <jni.h>
#include "AudioEngine.h"

static AudioEngine* engine = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_com_cryoprompter_audio_AudioBridge_startEngine(JNIEnv *env, jobject thiz) {
    if (!engine) {
        engine = new AudioEngine();
        engine->start();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_cryoprompter_audio_AudioBridge_stopEngine(JNIEnv *env, jobject thiz) {
    if (engine) {
        engine->stop();
        delete engine;
        engine = nullptr;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_cryoprompter_audio_AudioBridge_startCalibration(JNIEnv *env, jobject thiz) {
    if (engine) {
        engine->startCalibration();
    }
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_cryoprompter_audio_AudioBridge_getCalibrationProgress(JNIEnv *env, jobject thiz) {
    if (engine) {
        return engine->getCalibrationProgress();
    }
    return 0.0f;
}
