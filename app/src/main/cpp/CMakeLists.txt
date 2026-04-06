cmake_minimum_required(VERSION 3.22.1)
project("cryoprompter")

find_package(oboe REQUIRED CONFIG)

add_library(cryoprompter SHARED
    AudioEngine.cpp
    JsxDeepCalibrator.cpp
    jni_bridge.cpp
)

target_link_libraries(cryoprompter
    oboe::oboe
    log
)
