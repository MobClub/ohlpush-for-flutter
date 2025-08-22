
import 'ohlpush_plugin_platform_interface.dart';

class OhlpushPlugin {
  Future<String?> getPlatformVersion() {
    return OhlpushPluginPlatform.instance.getPlatformVersion();
  }
}
