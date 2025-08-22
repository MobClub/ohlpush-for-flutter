import 'package:flutter_test/flutter_test.dart';
import 'package:ohlpush_plugin/ohlpush_plugin.dart';
import 'package:ohlpush_plugin/ohlpush_plugin_platform_interface.dart';
import 'package:ohlpush_plugin/ohlpush_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockOhlpushPluginPlatform
    with MockPlatformInterfaceMixin
    implements OhlpushPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final OhlpushPluginPlatform initialPlatform = OhlpushPluginPlatform.instance;

  test('$MethodChannelOhlpushPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelOhlpushPlugin>());
  });

  test('getPlatformVersion', () async {
    OhlpushPlugin ohlpushPlugin = OhlpushPlugin();
    MockOhlpushPluginPlatform fakePlatform = MockOhlpushPluginPlatform();
    OhlpushPluginPlatform.instance = fakePlatform;

    expect(await ohlpushPlugin.getPlatformVersion(), '42');
  });
}
