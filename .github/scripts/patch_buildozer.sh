#!/bin/bash

# Locate android.py
TARGET_FILE=$(find / -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  echo "Replacing $TARGET_FILE with patched version"
  cp .github/scripts/android.py "$TARGET_FILE"
  echo "Replacement done. Contents of patched file:"
  cat "$TARGET_FILE"
else
  echo "android.py not found. Replacement skipped."
  exit 1
fi