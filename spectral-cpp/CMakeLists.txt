cmake_minimum_required(VERSION 3.20)
project(spectral)

set(CMAKE_CXX_STANDARD 20)

if (MSVC)
    add_compile_options(
            $<$<CONFIG:>:/MT> #---------|
            $<$<CONFIG:Debug>:/MTd> #---|-- Statically link the runtime libraries
            $<$<CONFIG:Release>:/MT> #--|
    )
    add_compile_options(/EHa) # enable C++ EH (w/ SEH exceptions)
    add_compile_options(/Zi) # enable debugging information
endif ()

file(GLOB_RECURSE SOURCES ${CMAKE_BUILD_DIR} "src/*.cpp" "src/*.h" "src/*.hpp")

find_package(JNI REQUIRED)

include_directories(include/ ${JNI_INCLUDE_DIRS})
link_directories(lib/ ${JNI_LIBRARIES})

add_library(spectral SHARED ${SOURCES})

target_include_directories(spectral PRIVATE include/ ${JNI_INCLUDE_DIRS})
target_link_libraries(spectral PRIVATE ${JNI_LIBRARIES})