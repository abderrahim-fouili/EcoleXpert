package by.chemerisuk.cordova.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/* loaded from: classes.dex */
public class FirebaseMessagingPluginService extends FirebaseMessagingService {
    public static final String ACTION_FCM_MESSAGE = "by.chemerisuk.cordova.firebase.ACTION_FCM_MESSAGE";
    public static final String ACTION_FCM_TOKEN = "by.chemerisuk.cordova.firebase.ACTION_FCM_TOKEN";
    public static final String EXTRA_FCM_MESSAGE = "by.chemerisuk.cordova.firebase.EXTRA_FCM_MESSAGE";
    public static final String EXTRA_FCM_TOKEN = "by.chemerisuk.cordova.firebase.EXTRA_FCM_TOKEN";
    public static final String NOTIFICATION_CHANNEL_KEY = "com.google.firebase.messaging.default_notification_channel_id";
    public static final String NOTIFICATION_COLOR_KEY = "com.google.firebase.messaging.default_notification_color";
    public static final String NOTIFICATION_ICON_KEY = "com.google.firebase.messaging.default_notification_icon";
    private static final String TAG = "FCMPluginService";
    private LocalBroadcastManager broadcastManager;
    private String defaultNotificationChannel;
    private int defaultNotificationColor;
    private int defaultNotificationIcon;
    private NotificationManager notificationManager;

    @Override // android.app.Service
    public void onCreate() {
        this.broadcastManager = LocalBroadcastManager.getInstance(this);
        this.notificationManager = (NotificationManager) ContextCompat.getSystemService(this, NotificationManager.class);
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), 128);
            this.defaultNotificationIcon = applicationInfo.metaData.getInt("com.google.firebase.messaging.default_notification_icon", applicationInfo.icon);
            this.defaultNotificationChannel = applicationInfo.metaData.getString("com.google.firebase.messaging.default_notification_channel_id", "default");
            this.defaultNotificationColor = ContextCompat.getColor(this, applicationInfo.metaData.getInt("com.google.firebase.messaging.default_notification_color"));
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "Failed to load meta-data", e);
        } catch (Resources.NotFoundException e2) {
            Log.d(TAG, "Failed to load notification color", e2);
        }
        if (Build.VERSION.SDK_INT < 26 || this.notificationManager.getNotificationChannel(this.defaultNotificationChannel) != null) {
            return;
        }
        this.notificationManager.createNotificationChannel(new NotificationChannel(this.defaultNotificationChannel, "Firebase", 4));
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String str) {
        FirebaseMessagingPlugin.sendToken(str);
        Intent intent = new Intent(ACTION_FCM_TOKEN);
        intent.putExtra(EXTRA_FCM_TOKEN, str);
        this.broadcastManager.sendBroadcast(intent);
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification;
        FirebaseMessagingPlugin.sendNotification(remoteMessage);
        Intent intent = new Intent(ACTION_FCM_MESSAGE);
        intent.putExtra(EXTRA_FCM_MESSAGE, remoteMessage);
        this.broadcastManager.sendBroadcast(intent);
        if (!FirebaseMessagingPlugin.isForceShow() || (notification = remoteMessage.getNotification()) == null) {
            return;
        }
        showAlert(notification);
    }

    private void showAlert(RemoteMessage.Notification notification) {
        this.notificationManager.notify(0, new NotificationCompat.Builder(this, getNotificationChannel(notification)).setSound(getNotificationSound(notification.getSound())).setContentTitle(notification.getTitle()).setContentText(notification.getBody()).setGroup(notification.getTag()).setSmallIcon(this.defaultNotificationIcon).setColor(this.defaultNotificationColor).setPriority(1).build());
        new Handler(getMainLooper()).postDelayed(new Runnable() { // from class: by.chemerisuk.cordova.firebase.FirebaseMessagingPluginService$$ExternalSyntheticLambda0
            {
                FirebaseMessagingPluginService.this = this;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FirebaseMessagingPluginService.this.m44xdf64ba7();
            }
        }, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showAlert$0$by-chemerisuk-cordova-firebase-FirebaseMessagingPluginService  reason: not valid java name */
    public /* synthetic */ void m44xdf64ba7() {
        this.notificationManager.cancel(0);
    }

    private String getNotificationChannel(RemoteMessage.Notification notification) {
        String channelId = notification.getChannelId();
        return channelId == null ? this.defaultNotificationChannel : channelId;
    }

    private Uri getNotificationSound(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        if (str.equals("default")) {
            return RingtoneManager.getDefaultUri(2);
        }
        return Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/" + str);
    }
}
