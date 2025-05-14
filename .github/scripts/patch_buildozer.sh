#!/bin/bash

# Locate android.py
TARGET_FILE=$(find / -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  echo "Patching $TARGET_FILE"
  echo "Before patch:" && grep sdkmanager "$TARGET_FILE"

  INDENT=$(grep -m1 sdkmanager "$TARGET_FILE" | sed -E 's/(^\s*).*sdkmanager.*/\1/')
  sed -i "/sdkmanager/ s/^.*sdkmanager.*/${INDENT}pass  # sdkmanager patched out/" "$TARGET_FILE"

  echo "After patch:" && grep "sdkmanager\|pass  # sdkmanager patched out" "$TARGET_FILE"
else
  echo "android.py not found. Patch skipped."
  exit 1
fi