from buildozer.targets.android import AndroidTarget as BaseAndroidTarget

class AndroidTarget(BaseAndroidTarget):
    def _sdkmanager(self, *args, **kwargs):
        print("sdkmanager skipped")
        return 0