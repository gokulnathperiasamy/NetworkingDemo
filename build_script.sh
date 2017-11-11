#!/bin/bash

echo "Build Script Triggered"

echo "Cleaning and Assembling Build"

./gradlew clean assemble

echo "Upload to HockeyApp"

./gradlew uploadProdDebugToHockeyApp