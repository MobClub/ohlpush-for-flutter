package com.ohaola.sdk.flutter.ohlpush_plugin.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ohaola.sdk.tool.wrapper.tools.MobLog;
import com.ohaola.sdk.tool.wrapper.tools.utils.Hashon;
import com.ohaola.sdk.tool.wrapper.tools.utils.UIHandler;
import com.ohaola.sdk.push.OHLPushCustomMessage;
import com.ohaola.sdk.push.OHLPushInAppMessage;
import com.ohaola.sdk.push.OHLPushNotifyMessage;
import com.ohaola.sdk.push.OHLPushReceiver;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.EventChannel;

public class StreamHandlerImpl implements EventChannel.StreamHandler, OnRemoveReceiverListener, OHLPushReceiver {

	private EventChannel.EventSink eventSink;
	private volatile boolean isRemoved = false;
	private final Object lock = new Object();
	private final Hashon hashon = new Hashon();
	private final ThreadPoolExecutor singleExecutor = new ThreadPoolExecutor(0, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	@Override
	public void onListen(final Object o, EventChannel.EventSink eventSink) {
		MobLog.getInstance().i("onListen");
		this.eventSink = eventSink;
		this.isRemoved = false;
		try {
			synchronized (lock) {
				lock.notifyAll();
			}
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}

	@Override
	public void onCancel(Object o) {

	}

	@Override
	public void onRemoveReceiver() {
		MobLog.getInstance().e("onRemoveReceiver");
		isRemoved = true;
		eventSink = null;
		try {
			synchronized (lock) {
				lock.notifyAll();
			}
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}

	@Override
	public void onCustomMessageReceive(Context context, OHLPushCustomMessage mobPushCustomMessage) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("action", 0);
			map.put("result", hashon.fromJson(hashon.fromObject(mobPushCustomMessage)));
			messageCallback(map);
		} catch (Throwable t) {
			MobLog.getInstance().e(t);
		}
	}

	@Override
	public void onNotifyMessageReceive(Context context, OHLPushNotifyMessage mobPushNotifyMessage) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("action", 1);
			map.put("result", hashon.fromJson(hashon.fromObject(mobPushNotifyMessage)));
			messageCallback(map);
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}

	@Override
	public void onNotifyMessageOpenedReceive(Context context, OHLPushNotifyMessage mobPushNotifyMessage) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("action", 2);
			map.put("result", hashon.fromJson(hashon.fromObject(mobPushNotifyMessage)));
			messageCallback(map);
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}

	@Override
	public void onTagsCallback(Context context, String[] strings, int i, int i1) {

	}

	@Override
	public void onAliasCallback(Context context, String s, int i, int i1) {

	}

	public void onInAppMessageReceive(OHLPushInAppMessage ohlPushInAppMessage) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("action", 3);
			map.put("result", hashon.fromJson(hashon.fromObject(ohlPushInAppMessage)));
			messageCallback(map);
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}

	public void onInAppMessageShow(OHLPushInAppMessage ohlPushInAppMessage) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("action", 4);
			map.put("result", hashon.fromJson(hashon.fromObject(ohlPushInAppMessage)));
			messageCallback(map);
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}

	public void onInAppMessageClick(OHLPushInAppMessage ohlPushInAppMessage) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("action", 5);
			map.put("result", hashon.fromJson(hashon.fromObject(ohlPushInAppMessage)));
			messageCallback(map);
		} catch (Throwable throwable) {
			MobLog.getInstance().e(throwable);
		}
	}


	public void messageCallback(final HashMap<String, Object> map) {
		singleExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					MobLog.getInstance().i("Stream messageCallback");
					if (isRemoved) {
						MobLog.getInstance().i("isRemoved");
						return;
					}
					if (eventSink == null) {
						synchronized (lock) {
							MobLog.getInstance().i("wait");
							lock.wait();
						}
					}
					MobLog.getInstance().i("isRemoved:" + isRemoved);
					if (eventSink != null && !isRemoved) {
						UIHandler.sendEmptyMessage(0, new Handler.Callback() {
							@Override
							public boolean handleMessage(Message message) {
								try {
									eventSink.success(hashon.fromHashMap(map));
									MobLog.getInstance().i("eventSink success");
								} catch (Throwable throwable) {
									MobLog.getInstance().e(throwable);
								}
								return false;
							}
						});
					}
				} catch (Throwable throwable) {
					MobLog.getInstance().e(throwable);
				}
			}
		});
	}
}
