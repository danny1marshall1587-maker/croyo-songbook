if(NOT TARGET oboe::oboe)
add_library(oboe::oboe SHARED IMPORTED)
set_target_properties(oboe::oboe PROPERTIES
    IMPORTED_LOCATION "/home/dan/.gradle/caches/8.13/transforms/b116367e7a14c2a4cf4038e5d83df61b/transformed/jetified-oboe-1.8.0/prefab/modules/oboe/libs/android.armeabi-v7a/liboboe.so"
    INTERFACE_INCLUDE_DIRECTORIES "/home/dan/.gradle/caches/8.13/transforms/b116367e7a14c2a4cf4038e5d83df61b/transformed/jetified-oboe-1.8.0/prefab/modules/oboe/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

