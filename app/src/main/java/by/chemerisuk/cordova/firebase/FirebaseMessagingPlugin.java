package by.chemerisuk.cordova.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import by.chemerisuk.cordova.support.CordovaMethod;
import by.chemerisuk.cordova.support.ExecutionThread;
import by.chemerisuk.cordova.support.ReflectiveCordovaPlugin;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import me.leolin.shortcutbadger.ShortcutBadger;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class FirebaseMessagingPlugin extends ReflectiveCordovaPlugin {
    private static final String TAG = "FCMPlugin";
    private static FirebaseMessagingPlugin instance;
    private CallbackContext backgroundCallback;
    private FirebaseMessaging firebaseMessaging;
    private CallbackContext foregroundCallback;
    private JSONObject lastBundle;
    private NotificationManager notificationManager;
    private CallbackContext tokenRefreshCallback;
    private boolean isBackground = false;
    private boolean forceShow = false;

    @Override // org.apache.cordova.CordovaPlugin
    protected void pluginInitialize() {
        instance = this;
        this.firebaseMessaging = FirebaseMessaging.getInstance();
        this.notificationManager = (NotificationManager) ContextCompat.getSystemService(this.cordova.getActivity(), NotificationManager.class);
        this.lastBundle = getNotificationData(this.cordova.getActivity().getIntent());
    }

    @CordovaMethod(ExecutionThread.WORKER)
    private void subscribe(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws Exception {
        Tasks.await(this.firebaseMessaging.subscribeToTopic(cordovaArgs.getString(0)));
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.WORKER)
    private void unsubscribe(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws Exception {
        Tasks.await(this.firebaseMessaging.unsubscribeFromTopic(cordovaArgs.getString(0)));
        callbackContext.success();
    }

    @CordovaMethod
    private void clearNotifications(CallbackContext callbackContext) {
        this.notificationManager.cancelAll();
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.WORKER)
    private void deleteToken(CallbackContext callbackContext) throws Exception {
        Tasks.await(this.firebaseMessaging.deleteToken());
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.WORKER)
    private void getToken(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws Exception {
        if (!cordovaArgs.getString(0).isEmpty()) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, (String) null));
        } else {
            callbackContext.success((String) Tasks.await(this.firebaseMessaging.getToken()));
        }
    }

    @CordovaMethod
    private void onTokenRefresh(CallbackContext callbackContext) {
        instance.tokenRefreshCallback = callbackContext;
    }

    @CordovaMethod
    private void onMessage(CallbackContext callbackContext) {
        instance.foregroundCallback = callbackContext;
    }

    @CordovaMethod
    private void onBackgroundMessage(CallbackContext callbackContext) {
        instance.backgroundCallback = callbackContext;
        JSONObject jSONObject = this.lastBundle;
        if (jSONObject != null) {
            sendNotification(jSONObject, callbackContext);
            this.lastBundle = null;
        }
    }

    @CordovaMethod
    private void setBadge(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        int i = cordovaArgs.getInt(0);
        if (i >= 0) {
            ShortcutBadger.applyCount(this.cordova.getActivity().getApplicationContext(), i);
            callbackContext.success();
            return;
        }
        callbackContext.error("Badge value can't be negative");
    }

    @CordovaMethod
    private void getBadge(CallbackContext callbackContext) {
        callbackContext.success(this.cordova.getActivity().getSharedPreferences("badge", 0).getInt("badge", 0));
    }

    @CordovaMethod
    private void requestPermission(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        JSONObject jSONObject = cordovaArgs.getJSONObject(0);
        Context applicationContext = this.cordova.getActivity().getApplicationContext();
        this.forceShow = jSONObject.optBoolean("forceShow");
        if (NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()) {
            callbackContext.success();
        } else {
            callbackContext.error("Notifications permission is not granted");
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onNewIntent(Intent intent) {
        JSONObject notificationData = getNotificationData(intent);
        FirebaseMessagingPlugin firebaseMessagingPlugin = instance;
        if (firebaseMessagingPlugin == null || notificationData == null) {
            return;
        }
        sendNotification(notificationData, firebaseMessagingPlugin.backgroundCallback);
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onPause(boolean z) {
        this.isBackground = true;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onResume(boolean z) {
        this.isBackground = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendNotification(RemoteMessage remoteMessage) {
        JSONObject jSONObject = new JSONObject(remoteMessage.getData());
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            try {
                jSONObject.put(Constants.MessageTypes.MESSAGE, toJSON(notification));
            } catch (JSONException e) {
                Log.e(TAG, "sendNotification", e);
                return;
            }
        }
        jSONObject.put(Constants.MessagePayloadKeys.MSGID, remoteMessage.getMessageId());
        jSONObject.put(Constants.MessagePayloadKeys.SENT_TIME, remoteMessage.getSentTime());
        FirebaseMessagingPlugin firebaseMessagingPlugin = instance;
        if (firebaseMessagingPlugin != null) {
            firebaseMessagingPlugin.sendNotification(jSONObject, firebaseMessagingPlugin.isBackground ? firebaseMessagingPlugin.backgroundCallback : firebaseMessagingPlugin.foregroundCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendToken(String str) {
        FirebaseMessagingPlugin firebaseMessagingPlugin = instance;
        if (firebaseMessagingPlugin == null || firebaseMessagingPlugin.tokenRefreshCallback == null || str == null) {
            return;
        }
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, str);
        pluginResult.setKeepCallback(true);
        instance.tokenRefreshCallback.sendPluginResult(pluginResult);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isForceShow() {
        FirebaseMessagingPlugin firebaseMessagingPlugin = instance;
        return firebaseMessagingPlugin != null && firebaseMessagingPlugin.forceShow;
    }

    private void sendNotification(JSONObject jSONObject, CallbackContext callbackContext) {
        if (callbackContext != null) {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jSONObject);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    }

    private JSONObject getNotificationData(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return null;
        }
        if (extras.containsKey(Constants.MessagePayloadKeys.MSGID) || extras.containsKey(Constants.MessagePayloadKeys.SENT_TIME)) {
            try {
                JSONObject jSONObject = new JSONObject();
                for (String str : extras.keySet()) {
                    jSONObject.put(str, extras.get(str));
                }
                return jSONObject;
            } catch (JSONException e) {
                Log.e(TAG, "getNotificationData", e);
                return null;
            }
        }
        return null;
    }

    private static JSONObject toJSON(RemoteMessage.Notification notification) throws JSONException {
        JSONObject put = new JSONObject().put("body", notification.getBody()).put("title", notification.getTitle()).put("sound", notification.getSound()).put("icon", notification.getIcon()).put("tag", notification.getTag()).put("color", notification.getColor()).put("clickAction", notification.getClickAction());
        Uri imageUrl = notification.getImageUrl();
        if (imageUrl != null) {
            put.put("imageUrl", imageUrl.toString());
        }
        return put;
    }
}
