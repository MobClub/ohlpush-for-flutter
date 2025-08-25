package com.ohaola.sdk.flutter.ohlpush_plugin_example;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.ohaola.sdk.flutter.ohlpush_plugin.OhlpushPlugin;
import com.ohaola.sdk.flutter.ohlpush_plugin_example.utils.PlayloadDelegate;
import com.ohaola.sdk.push.OHLPush;
import com.ohaola.sdk.push.OHLPushUtils;

import io.flutter.embedding.android.FlutterFragmentActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterFragmentActivity {
	@Override
	public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
		super.configureFlutterEngine(flutterEngine);
		GeneratedPluginRegistrant.registerWith(flutterEngine);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OhlpushPlugin.parseManufacturerMessage(getIntent());
		dealPushResponse(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//需要调用setIntent方法，不然后面获取到的getIntent都试上一次传的数据
		setIntent(intent);
		OhlpushPlugin.parseManufacturerMessage(intent);
		dealPushResponse(intent);
	}

	private void dealPushResponse(Intent intent) {
		Bundle bundle = null;
		if (intent != null) {
			OHLPush.notificationClickAck(intent);
			OHLPushUtils.parseMainPluginPushIntent(intent);
			bundle = intent.getExtras();
			new PlayloadDelegate().playload(this, bundle);
		}
	}
}
