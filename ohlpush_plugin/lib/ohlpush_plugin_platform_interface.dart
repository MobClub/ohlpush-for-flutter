import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'ohlpush_plugin_method_channel.dart';

abstract class OhlpushPluginPlatform extends PlatformInterface {
  /// Constructs a OhlpushPluginPlatform.
  OhlpushPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static OhlpushPluginPlatform _instance = MethodChannelOhlpushPlugin();

  /// The default instance of [OhlpushPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelOhlpushPlugin].
  static OhlpushPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [OhlpushPluginPlatform] when
  /// they register themselves.
  static set instance(OhlpushPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
