#!/bin/bash

# Monkey-patch buildozer to skip sdkmanager license prompts
TARGET_FILE=$(find $HOME -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  sed -i "s/^\(.*sdkmanager.*\)$/# \1\necho SKIP\nexit 0/" "$TARGET_FILE"
else
  echo "android.py not found. Patch skipped."
fi