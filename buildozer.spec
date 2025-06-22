[app]
title = My Application
package.name = myapp
package.domain = org.test
source.dir = .
source.include_exts = py,png,jpg,kv,atlas
version = 0.1
requirements = python3,kivy,cython
orientation = portrait
fullscreen = 0
android.api = 33
android.archs = arm64-v8a, armeabi-v7a
android.allow_backup = True

[buildozer]
log_level = 2
warn_on_root = 1

# Added for GitHub Actions compatibility
[p4a]
p4a.branch = develop
p4a.setup_py = true