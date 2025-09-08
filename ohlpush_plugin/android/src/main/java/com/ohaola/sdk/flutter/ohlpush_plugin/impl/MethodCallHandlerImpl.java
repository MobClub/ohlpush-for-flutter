package com.ohaola.sdk.flutter.ohlpush_plugin.impl;

import android.content.Context;

import com.mob.MobSDK;
import com.mob.OperationCallback;

import com.mob.tools.MobLog;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.ohaola.sdk.flutter.ohlpush_plugin.impl.req.SimulateRequest;
import com.ohaola.sdk.push.OHLPush;
import com.ohaola.sdk.push.OHLPushCallback;
import com.ohaola.sdk.push.OHLPushCustomMessage;
import com.ohaola.sdk.push.OHLPushLocalNotification;
import com.ohaola.sdk.push.OHLPushNotifyMessage;
import com.ohaola.sdk.push.OHLPushReceiver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler, OHLPushReceiver {
	private static final Hashon hashon = new Hashon();
	private OnRemoveReceiverListener removeReceiverListener;
	private static ArrayList<MethodChannel.Result> setAliasCallback = new ArrayList<>();
	private static ArrayList<MethodChannel.Result> getAliasCallback = new ArrayList<>();
	private static ArrayList<MethodChannel.Result> getTagsCallback = new ArrayList<>();
	private static ArrayList<MethodChannel.Result> deleteAliasCallback = new ArrayList<>();
	private static ArrayList<MethodChannel.Result> addTagsCallback = new ArrayList<>();
	private static ArrayList<MethodChannel.Result> deleteTagsCallback = new ArrayList<>();
	private static ArrayList<MethodChannel.Result> cleanTagsCallback = new ArrayList<>();

	@Override
	public void onMethodCall(MethodCall call, final MethodChannel.Result result) {
		try {
			Log.e("", call.method);
			if (call.method.equals("getPlatformVersion")) {
				result.success("Android " + android.os.Build.VERSION.RELEASE);
			} else if (call.method.equals("getSDKVersion")) {
				result.success(getMobPushSdkVersion());
			} else if (call.method.equals("getRegistrationId")) {
				OHLPush.getRegistrationId(new OHLPushCallback<String>() {
					@Override
					public void onCallback(String data) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("res", data);
						result.success(map);
					}
				});
			} else if (call.method.equals("removePushReceiver")) {
				if (removeReceiverListener != null) {
					removeReceiverListener.onRemoveReceiver();
				}
			} else if (call.method.equals("setClickNotificationToLaunchMainActivity")) {
				boolean enable = call.argument("enable");
				OHLPush.setClickNotificationToLaunchMainActivity(enable);
			} else if (call.method.equals("stopPush")) {
				OHLPush.stopPush();
			} else if (call.method.equals("restartPush")) {
				OHLPush.restartPush();
			} else if (call.method.equals("isPushStopped")) {
				OHLPush.isPushStopped(new OHLPushCallback<Boolean>() {
					@Override
					public void onCallback(Boolean aBoolean) {
						result.success(aBoolean);
					}
				});
			} else if (call.method.equals("setAlias")) {
				String alias = call.argument("alias");
				setAliasCallback.add(result);
				OHLPush.setAlias(alias);
			} else if (call.method.equals("getAlias")) {
				getAliasCallback.add(result);
				OHLPush.getAlias();
			} else if (call.method.equals("deleteAlias")) {
				deleteAliasCallback.add(result);
				OHLPush.deleteAlias();
			} else if (call.method.equals("addTags")) {
				ArrayList<String> tags = call.argument("tags");
				addTagsCallback.add(result);
				OHLPush.addTags(tags.toArray(new String[tags.size()]));
			} else if (call.method.equals("getTags")) {
				getTagsCallback.add(result);
				OHLPush.getTags();
			} else if (call.method.equals("deleteTags")) {
				ArrayList<String> tags = call.argument("tags");
				deleteTagsCallback.add(result);
				OHLPush.deleteTags(tags.toArray(new String[tags.size()]));
			} else if (call.method.equals("cleanTags")) {
				cleanTagsCallback.add(result);
				OHLPush.cleanTags();
			} else if (call.method.equals("setSilenceTime")) {
				int startHour = call.argument("startHour");
				int startMinute = call.argument("startMinute");
				int endHour = call.argument("endHour");
				int endMinute = call.argument("endMinute");
				OHLPush.setSilenceTime(startHour, startMinute, endHour, endMinute);
			} else if (call.method.equals("setTailorNotification")) {

			} else if (call.method.equals("removeLocalNotification")) {
				int notificationId = call.argument("notificationId");
				result.success(OHLPush.removeLocalNotification(notificationId));
			} else if (call.method.equals("addLocalNotification")) {
				String json = call.argument("localNotification");
				OHLPushLocalNotification notification = hashon.fromJson(json, OHLPushLocalNotification.class);
				result.success(OHLPush.addLocalNotification(notification));
			} else if (call.method.equals("clearLocalNotifications")) {
				result.success(OHLPush.clearLocalNotifications());
			} else if (call.method.equals("setNotifyIcon")) {
				String iconRes = call.argument("iconRes");
				int iconResId = ResHelper.getBitmapRes(MobSDK.getContext(), iconRes);
				if (iconResId > 0) {
					OHLPush.setNotifyIcon(iconResId);
				}
			} else if (call.method.equals("setAppForegroundHiddenNotification")) {
				boolean hidden = call.argument("hidden");
				OHLPush.setAppForegroundHiddenNotification(hidden);
			} else if (call.method.equals("setShowBadge")) {
				boolean show = call.argument("show");
				OHLPush.setShowBadge(show);
			} else if (call.method.equals("bindPhoneNum")) {
				String phoneNum = call.argument("phoneNum");
				OHLPush.bindPhoneNum(phoneNum, new OHLPushCallback<Boolean>() {
					@Override
					public void onCallback(Boolean data) {
						if (data != null) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("res", data.booleanValue() ? "success" : "failed");
							map.put("error", "");
							result.success(map);
						}
					}
				});
			} else if (call.method.equals("send")) {
				int type = call.argument("type");
				int space = call.argument("space");
				String content = call.argument("content");
				String extras = call.argument("extrasMap");
				SimulateRequest.sendPush(type, content, space, extras, new OHLPushCallback<Boolean>() {
					@Override
					public void onCallback(Boolean aBoolean) {
						if (aBoolean != null) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("res", aBoolean.booleanValue() ? "success" : "failed");
							map.put("error", "");
							result.success(map);
						}
					}
				});
			} else if (call.method.equals("updatePrivacyPermissionStatus")) {
				boolean status = call.argument("status");
				MobSDK.submitPolicyGrantResult(status, new OperationCallback<Void>() {
					@Override
					public void onComplete(Void aVoid) {
						result.success(true);
						System.out.println("updatePrivacyPermissionStatus onComplete");
					}

					@Override
					public void onFailure(Throwable throwable) {
						result.error(throwable.toString(), null, null);
						System.out.println("updatePrivacyPermissionStatus onFailure:" + throwable.getMessage());
					}
				});
			} else if (call.method.equals("getToken")) {
				OHLPush.getDeviceToken(new OHLPushCallback<String>() {
					@Override
					public void onCallback(String s) {
						result.success(s);
					}
				});
			} else if (call.method.equals("setToken")) {
				String channel = call.argument("channel");
				String token = call.argument("token");
				OHLPush.setDeviceTokenByUser(channel, token);
			} else if (call.method.equals("checkTcpStatus")) {
				OHLPush.checkTcpStatus(new OHLPushCallback<Boolean>() {
					@Override
					public void onCallback(Boolean o) {
						result.success(o);
					}
				});
			} else if (call.method.equals("startNotificationMonitor")) {
				OHLPush.startNotificationMonitor();
			} else if (call.method.equals("stopNotificationMonitor")) {
				OHLPush.stopNotificationMonitor();
			} else if (call.method.equals("isNotificationsEnabled")) {
				OHLPush.isNotificationsEnabled(new OHLPushCallback<Boolean>() {
					@Override
					public void onCallback(Boolean aBoolean) {
						result.success(aBoolean);
					}
				});
			} else if (call.method.equals("setUserLanguage")) {
				String language = call.argument("language");
				OHLPush.setUserLanguage(language);
			} else {
				result.notImplemented();
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	public void setRemoveReceiverListener(OnRemoveReceiverListener removeReceiverListener) {
		this.removeReceiverListener = removeReceiverListener;
	}

	private String getMobPushSdkVersion() {
		try {
			Class<?> mobpushClass = OHLPush.class;
			Field versionField = mobpushClass.getDeclaredField("SDK_VERSION_NAME");
			if (!versionField.isAccessible()) {
				versionField.setAccessible(true);
			}
			return (String) versionField.get(mobpushClass);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return OHLPush.SDK_VERSION_NAME;
	}

	@Override
	public void onCustomMessageReceive(Context context, OHLPushCustomMessage mobPushCustomMessage) {

	}

	@Override
	public void onNotifyMessageReceive(Context context, OHLPushNotifyMessage mobPushNotifyMessage) {

	}

	@Override
	public void onNotifyMessageOpenedReceive(Context context, OHLPushNotifyMessage mobPushNotifyMessage) {

	}

	@Override
	public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			MethodChannel.Result result = null;
			// 0 获取， 1 设置， 2 删除，3 清空
			switch (operation) {
				case 0:
					result = getTagsCallback.remove(0);
					map.put("res", tags == null ? new ArrayList<String>() : Arrays.asList(tags));
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
				case 1:
					result = addTagsCallback.remove(0);
					map.put("res", errorCode == 0 ? "success" : "failed");
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
				case 2:
					result = deleteTagsCallback.remove(0);
					map.put("res", errorCode == 0 ? "success" : "failed");
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
				case 3:
					result = cleanTagsCallback.remove(0);
					map.put("res", errorCode == 0 ? "success" : "failed");
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
			}
			if (result != null) {
				MobLog.getInstance().i("tag result success");
				result.success(map);
			}
		} catch (Throwable e) {
			MobLog.getInstance().e(e);
		}
	}

	@Override
	public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			MethodChannel.Result result = null;
			// 0 获取， 1 设置， 2 删除
			switch (operation) {
				case 0:
					result = getAliasCallback.remove(0);
					map.put("res", alias);
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
				case 1:
					result = setAliasCallback.remove(0);
					map.put("res", errorCode == 0 ? "success" : "failed");
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
				case 2:
					result = deleteAliasCallback.remove(0);
					map.put("res", errorCode == 0 ? "success" : "failed");
					map.put("error", "");
					map.put("errorCode", String.valueOf(errorCode));
					break;
			}
			if (result != null) {
				MobLog.getInstance().i("alias result success");
				result.success(map);
			}
		} catch (Throwable e) {
			MobLog.getInstance().e(e);
		}
	}
}
