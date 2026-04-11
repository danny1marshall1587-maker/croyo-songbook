#!/bin/bash
# Cryo-Prompter: Automated Build Environment for CachyOS (Arch-based)
# Designed for fresh installs with Waydroid.

echo "--- Initializing Cryo-Prompter Build Environment (CachyOS) ---"

# 1. Update and Install Core Dependencies (Arch-specific)
echo "Updating pacman and installing core dependencies..."
# Core build tools, JDK 17, and 32-bit libraries for the SDK/NDK
sudo pacman -Syu --noconfirm
sudo pacman -S --needed --noconfirm \
    base-devel \
    openjdk17-src \
    jdk17-openjdk \
    wget \
    unzip \
    git \
    lib32-libstdc++5 \
    lib32-gcc-libs \
    lib32-ncurses \
    android-udev

# 2. Setup Android SDK Directory (pointing to /home/dan/Android/Sdk as in local.properties)
export ANDROID_HOME=$HOME/Android/Sdk
mkdir -p $ANDROID_HOME/cmdline-tools

# 3. Download official Command Line Tools if not exists
if [ ! -d "$ANDROID_HOME/cmdline-tools/latest" ]; then
    echo "Fetching Android Command Line Tools..."
    cd $ANDROID_HOME/cmdline-tools
    wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O tools.zip
    unzip tools.zip
    mv cmdline-tools latest
    rm tools.zip
    cd -
fi

# 4. Persistence for Environment Variables
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
if ! grep -q "ANDROID_HOME" ~/.bashrc; then
    echo "export ANDROID_HOME=$HOME/Android/Sdk" >> ~/.bashrc
    echo "export PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools" >> ~/.bashrc
fi

# 5. Install Required SDK components and NDK (Oboe C++)
# versioning based on build.gradle.kts
echo "Installing Android 34, Build Tools, and NDK 25..."
yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-34" "build-tools;34.0.0" "ndk;25.1.8937393"

# 6. Final Build Command
echo "--- Compiling Production APK ---"
cd opensong_tablet
chmod +x gradlew
./gradlew app:installMobileOnlyDebug

echo ""
echo "--- Build Complete: opensong_tablet/app/build/outputs/apk/mobileOnly/debug/app-mobileOnly-debug.apk ---"
echo ""
echo "--- Installed on Device ---"
