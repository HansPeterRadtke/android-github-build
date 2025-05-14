#!/bin/bash

# Monkey-patch buildozer to comment out sdkmanager line safely
TARGET_FILE=$(find $HOME -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  sed -i '/sdkmanager/ s/^/        # /' "$TARGET_FILE"
else
  echo "android.py not found. Patch skipped."
fi