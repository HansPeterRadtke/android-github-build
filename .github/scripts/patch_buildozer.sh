#!/bin/bash

# Monkey-patch buildozer to skip sdkmanager license prompts
sed -i "/sdkmanager/s/^/echo SKIP; exit 0 # /" \"$HOME/.local/lib/python3.10/site-packages/buildozer/targets/android.py\"

