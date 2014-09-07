#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

adb shell monkey -p com.nilhcem.xebia.essentials -v 2000

echo "done"
