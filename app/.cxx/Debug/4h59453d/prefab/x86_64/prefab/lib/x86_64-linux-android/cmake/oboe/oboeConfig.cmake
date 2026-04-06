if(NOT TARGET oboe::oboe)
add_library(oboe::oboe SHARED IMPORTED)
set_target_properties(oboe::oboe PROPERTIES
    IMPORTED_LOCATION "/home/dan/.gradle/caches/transforms-3/e8a77bb17abccc35dffcd0c3df6754a0/transformed/jetified-oboe-1.8.0/prefab/modules/oboe/libs/android.x86_64/liboboe.so"
    INTERFACE_INCLUDE_DIRECTORIES "/home/dan/.gradle/caches/transforms-3/e8a77bb17abccc35dffcd0c3df6754a0/transformed/jetified-oboe-1.8.0/prefab/modules/oboe/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

