if(NOT TARGET oboe::oboe)
add_library(oboe::oboe SHARED IMPORTED)
set_target_properties(oboe::oboe PROPERTIES
    IMPORTED_LOCATION "/home/dan/.gradle/caches/9.3.1/transforms/3229b9f2e645d5433b8a6f7963c77254/transformed/jetified-oboe-1.8.0/prefab/modules/oboe/libs/android.x86/liboboe.so"
    INTERFACE_INCLUDE_DIRECTORIES "/home/dan/.gradle/caches/9.3.1/transforms/3229b9f2e645d5433b8a6f7963c77254/transformed/jetified-oboe-1.8.0/prefab/modules/oboe/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

