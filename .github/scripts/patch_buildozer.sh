#!/bin/bash

# Locate android.py
TARGET_FILE=$(find / -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  echo "Patching $TARGET_FILE"
  echo "Before patch:" && grep sdkmanager "$TARGET_FILE"

  # Override _sdkmanager with a no-op lambda
  sed -i "s/self\.sdkmanager/self\.sdkmanager_disabled/g" "$TARGET_FILE"
  echo -e "\n    def sdkmanager_disabled(self, *args, **kwargs):\n        print(\"sdkmanager bypassed\")\n        return 0" >> "$TARGET_FILE"

  echo "After patch:" && grep sdkmanager "$TARGET_FILE"
else
  echo "android.py not found. Patch skipped."
  exit 1
fi