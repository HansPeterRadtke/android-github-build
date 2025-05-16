#!/bin/bash

# Locate android.py
TARGET_FILE=$(find / -type f -path "*/buildozer/targets/android.py" 2>/dev/null | head -n 1)

if [ -f "$TARGET_FILE" ]; then
  echo "Patching $TARGET_FILE with clean method-level replacement"

  cp .github/scripts/original_android.py "$TARGET_FILE"

  # Replace only _sdkmanager method safely
  sed -i '/def _sdkmanager/,/^    def /{s/.*/    def _sdkmanager(self, *args, **kwargs):\n        self.logger.info(\"_sdkmanager call skipped\")\n        return 0/;b};/^    def /q' "$TARGET_FILE"

  echo "Patched file written."
else
  echo "android.py not found. Patch aborted."
  exit 1
fi