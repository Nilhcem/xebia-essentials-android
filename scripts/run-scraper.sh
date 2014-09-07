#!/bin/bash

# avoid virtual desktop jumps at mac terminal on test run
export JAVA_TOOL_OPTIONS='-Djava.awt.headless=true'

./gradlew :essentials-scraper:clean :essentials-scraper:scraper

echo "output: $(pwd)/essentials-scraper/cards.xml"
