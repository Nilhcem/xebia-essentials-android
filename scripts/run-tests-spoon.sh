#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

./gradlew clean :essentials-android:spoonDebugTest

echo "test reports: $(pwd)/essentials-android/build/spoon/debug/index.html"
