from buildozer.targets.android import TargetAndroid as Original

class PatchedAndroidTarget(Original):
    def _sdkmanager(self, *args, **kwargs):
        self.logger.info("_sdkmanager() call skipped by patch")
        return 0

# override exported symbol
get_target = lambda buildozer: PatchedAndroidTarget(buildozer)