#!/bin/bash

# Locate android.py
TARGET_FILE=$(find / -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  echo "Replacing $TARGET_FILE with verified version"
  cp .github/scripts/android.py "$TARGET_FILE"
else
  echo "android.py not found. Replacement skipped."
  exit 1
fi