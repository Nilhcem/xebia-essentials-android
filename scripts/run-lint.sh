#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

./gradlew :essentials-android:lintRelease

echo "test reports: $(pwd)/essentials-android/build/outputs/lint-results-release.html"
