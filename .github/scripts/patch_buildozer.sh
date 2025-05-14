#!/bin/bash

# Dynamically locate and patch android.py in GitHub runner's environment
TARGET_FILE=$(find / -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  echo "Patching $TARGET_FILE"
  sed -i '/sdkmanager/s/^\([ \t]*\)/\1# /' "$TARGET_FILE"
else
  echo "android.py not found. Patch skipped."
  exit 1
fi