package nl.xservices.plugins;

import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.text.Html;
import android.util.Base64;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.net.MailTo;
import androidx.webkit.ProxyConfig;
import androidx.webkit.internal.AssetHelper;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.sessions.settings.RemoteSettings;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SocialSharing extends CordovaPlugin {
    private static final String ACTION_AVAILABLE_EVENT = "available";
    private static final String ACTION_CAN_SHARE_VIA = "canShareVia";
    private static final String ACTION_CAN_SHARE_VIA_EMAIL = "canShareViaEmail";
    private static final String ACTION_SHARE_EVENT = "share";
    private static final String ACTION_SHARE_VIA = "shareVia";
    private static final String ACTION_SHARE_VIA_EMAIL_EVENT = "shareViaEmail";
    private static final String ACTION_SHARE_VIA_FACEBOOK_EVENT = "shareViaFacebook";
    private static final String ACTION_SHARE_VIA_FACEBOOK_WITH_PASTEMESSAGEHINT = "shareViaFacebookWithPasteMessageHint";
    private static final String ACTION_SHARE_VIA_INSTAGRAM_EVENT = "shareViaInstagram";
    private static final String ACTION_SHARE_VIA_SMS_EVENT = "shareViaSMS";
    private static final String ACTION_SHARE_VIA_TWITTER_EVENT = "shareViaTwitter";
    private static final String ACTION_SHARE_VIA_WHATSAPP_EVENT = "shareViaWhatsApp";
    private static final String ACTION_SHARE_WITH_OPTIONS_EVENT = "shareWithOptions";
    private static final int ACTIVITY_CODE_SENDVIAEMAIL = 3;
    private static final int ACTIVITY_CODE_SENDVIAWHATSAPP = 4;
    private static final int ACTIVITY_CODE_SEND__BOOLRESULT = 1;
    private static final int ACTIVITY_CODE_SEND__OBJECT = 2;
    private static final Map<String, String> MIME_Map;
    private CallbackContext _callbackContext;
    private String pasteMessage;

    /* loaded from: classes.dex */
    private abstract class SocialSharingRunnable implements Runnable {
        public CallbackContext callbackContext;

        SocialSharingRunnable(CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public boolean execute(String str, JSONArray jSONArray, CallbackContext callbackContext) throws JSONException {
        this._callbackContext = callbackContext;
        this.pasteMessage = null;
        if (ACTION_AVAILABLE_EVENT.equals(str)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            return true;
        } else if (ACTION_SHARE_EVENT.equals(str)) {
            return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), null, null, false, true);
        } else {
            if (ACTION_SHARE_WITH_OPTIONS_EVENT.equals(str)) {
                return shareWithOptions(callbackContext, jSONArray.getJSONObject(0));
            }
            if (ACTION_SHARE_VIA_TWITTER_EVENT.equals(str)) {
                return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), "twitter", null, false, true);
            }
            if (ACTION_SHARE_VIA_FACEBOOK_EVENT.equals(str)) {
                return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.isNull(3) ? jSONArray.getJSONArray(2) : new JSONArray(), jSONArray.getString(3), "com.facebook.katana", null, false, true, "com.facebook.composer.shareintent");
            } else if (ACTION_SHARE_VIA_FACEBOOK_WITH_PASTEMESSAGEHINT.equals(str)) {
                this.pasteMessage = jSONArray.getString(4);
                return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.isNull(3) ? jSONArray.getJSONArray(2) : new JSONArray(), jSONArray.getString(3), "com.facebook.katana", null, false, true, "com.facebook.composer.shareintent");
            } else if (ACTION_SHARE_VIA_WHATSAPP_EVENT.equals(str)) {
                if (notEmpty(jSONArray.getString(4))) {
                    return shareViaWhatsAppDirectly(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), jSONArray.getString(4));
                }
                if (notEmpty(jSONArray.getString(5))) {
                    return shareViaWhatsAppDirectly(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), jSONArray.getString(5));
                }
                return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), "whatsapp", null, false, true);
            } else if (ACTION_SHARE_VIA_INSTAGRAM_EVENT.equals(str)) {
                if (notEmpty(jSONArray.getString(0))) {
                    copyHintToClipboard(jSONArray.getString(0), "Instagram paste message");
                }
                return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), "com.instagram.android", null, false, true, "com.instagram.share.handleractivity.ShareHandlerActivity");
            } else if (ACTION_CAN_SHARE_VIA.equals(str)) {
                return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), jSONArray.getString(4), null, true, true);
            } else {
                if (ACTION_CAN_SHARE_VIA_EMAIL.equals(str)) {
                    if (isEmailAvailable()) {
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                        return true;
                    }
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "not available"));
                    return false;
                } else if (ACTION_SHARE_VIA.equals(str)) {
                    return doSendIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.getString(3), jSONArray.getString(4), null, false, true);
                } else {
                    if (ACTION_SHARE_VIA_SMS_EVENT.equals(str)) {
                        return invokeSMSIntent(callbackContext, jSONArray.getJSONObject(0), jSONArray.getString(1));
                    }
                    if (ACTION_SHARE_VIA_EMAIL_EVENT.equals(str)) {
                        return invokeEmailIntent(callbackContext, jSONArray.getString(0), jSONArray.getString(1), jSONArray.getJSONArray(2), jSONArray.isNull(3) ? null : jSONArray.getJSONArray(3), jSONArray.isNull(4) ? null : jSONArray.getJSONArray(4), jSONArray.isNull(5) ? null : jSONArray.getJSONArray(5));
                    }
                    callbackContext.error("socialSharing." + str + " is not a supported function. Did you mean 'share'?");
                    return false;
                }
            }
        }
    }

    private boolean isEmailAvailable() {
        return this.cordova.getActivity().getPackageManager().queryIntentActivities(new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "someone@domain.com", null)), 0).size() > 0;
    }

    private boolean invokeEmailIntent(CallbackContext callbackContext, final String str, final String str2, final JSONArray jSONArray, final JSONArray jSONArray2, final JSONArray jSONArray3, final JSONArray jSONArray4) throws JSONException {
        this.cordova.getThreadPool().execute(new SocialSharingRunnable(callbackContext) { // from class: nl.xservices.plugins.SocialSharing.1
            @Override // java.lang.Runnable
            public void run() {
                String downloadDir;
                Intent intent = new Intent("android.intent.action.SENDTO");
                if (SocialSharing.notEmpty(str)) {
                    if (Pattern.compile(".*\\<[^>]+>.*", 32).matcher(str).matches()) {
                        intent.putExtra("android.intent.extra.TEXT", Html.fromHtml(str));
                        intent.setType("text/html");
                    } else {
                        intent.putExtra("android.intent.extra.TEXT", str);
                        intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
                    }
                }
                if (SocialSharing.notEmpty(str2)) {
                    intent.putExtra("android.intent.extra.SUBJECT", str2);
                }
                try {
                    JSONArray jSONArray5 = jSONArray;
                    if (jSONArray5 != null && jSONArray5.length() > 0) {
                        intent.putExtra("android.intent.extra.EMAIL", SocialSharing.toStringArray(jSONArray));
                    }
                    JSONArray jSONArray6 = jSONArray2;
                    if (jSONArray6 != null && jSONArray6.length() > 0) {
                        intent.putExtra("android.intent.extra.CC", SocialSharing.toStringArray(jSONArray2));
                    }
                    JSONArray jSONArray7 = jSONArray3;
                    if (jSONArray7 != null && jSONArray7.length() > 0) {
                        intent.putExtra("android.intent.extra.BCC", SocialSharing.toStringArray(jSONArray3));
                    }
                    if (jSONArray4.length() > 0 && (downloadDir = SocialSharing.this.getDownloadDir()) != null) {
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < jSONArray4.length(); i++) {
                            Uri uriForFile = FileProvider.getUriForFile(SocialSharing.this.webView.getContext(), SocialSharing.this.cordova.getActivity().getPackageName() + ".sharing.provider", new File(SocialSharing.this.getFileUriAndSetType(intent, downloadDir, jSONArray4.getString(i), str2, i).getPath()));
                            if (uriForFile != null) {
                                arrayList.add(uriForFile);
                            }
                        }
                        if (!arrayList.isEmpty()) {
                            intent.putExtra("android.intent.extra.STREAM", arrayList);
                        }
                    }
                    intent.addFlags(268435456);
                    intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
                    List<ResolveInfo> queryIntentActivities = SocialSharing.this.cordova.getActivity().getPackageManager().queryIntentActivities(intent, 0);
                    ArrayList arrayList2 = new ArrayList();
                    for (ResolveInfo resolveInfo : queryIntentActivities) {
                        intent.setAction("android.intent.action.SEND_MULTIPLE");
                        intent.setType("application/octet-stream");
                        intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                        arrayList2.add(new LabeledIntent(intent, resolveInfo.activityInfo.packageName, resolveInfo.loadLabel(SocialSharing.this.cordova.getActivity().getPackageManager()), resolveInfo.icon));
                    }
                    final Intent createChooser = Intent.createChooser((Intent) arrayList2.remove(arrayList2.size() - 1), "Choose Email App");
                    createChooser.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) arrayList2.toArray(new LabeledIntent[arrayList2.size()]));
                    SocialSharing.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: nl.xservices.plugins.SocialSharing.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SocialSharing.this.cordova.startActivityForResult(this, createChooser, 3);
                        }
                    });
                } catch (Exception e) {
                    this.callbackContext.error(e.getMessage());
                }
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDownloadDir() throws IOException {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            String str = this.webView.getContext().getExternalFilesDir(null) + "/socialsharing-downloads";
            createOrCleanDir(str);
            return str;
        }
        return null;
    }

    private boolean shareWithOptions(CallbackContext callbackContext, JSONObject jSONObject) {
        return doSendIntent(callbackContext, jSONObject.optString("message", null), jSONObject.optString("subject", null), jSONObject.optJSONArray("files") == null ? new JSONArray() : jSONObject.optJSONArray("files"), jSONObject.optString(ImagesContract.URL, null), jSONObject.optString("appPackageName", null), jSONObject.optString("chooserTitle", null), false, false);
    }

    private boolean doSendIntent(CallbackContext callbackContext, String str, String str2, JSONArray jSONArray, String str3, String str4, String str5, boolean z, boolean z2) {
        return doSendIntent(callbackContext, str, str2, jSONArray, str3, str4, str5, z, z2, null);
    }

    private boolean doSendIntent(CallbackContext callbackContext, String str, String str2, JSONArray jSONArray, String str3, String str4, String str5, boolean z, boolean z2, String str6) {
        this.cordova.getThreadPool().execute(new AnonymousClass2(callbackContext, str, jSONArray, str2, str3, str4, str6, z, this.cordova, this, str5, z2));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: nl.xservices.plugins.SocialSharing$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends SocialSharingRunnable {
        final /* synthetic */ String val$appName;
        final /* synthetic */ String val$appPackageName;
        final /* synthetic */ boolean val$boolResult;
        final /* synthetic */ String val$chooserTitle;
        final /* synthetic */ JSONArray val$files;
        final /* synthetic */ String val$msg;
        final /* synthetic */ CordovaInterface val$mycordova;
        final /* synthetic */ boolean val$peek;
        final /* synthetic */ CordovaPlugin val$plugin;
        final /* synthetic */ String val$subject;
        final /* synthetic */ String val$url;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(CallbackContext callbackContext, String str, JSONArray jSONArray, String str2, String str3, String str4, String str5, boolean z, CordovaInterface cordovaInterface, CordovaPlugin cordovaPlugin, String str6, boolean z2) {
            super(callbackContext);
            this.val$msg = str;
            this.val$files = jSONArray;
            this.val$subject = str2;
            this.val$url = str3;
            this.val$appPackageName = str4;
            this.val$appName = str5;
            this.val$peek = z;
            this.val$mycordova = cordovaInterface;
            this.val$plugin = cordovaPlugin;
            this.val$chooserTitle = str6;
            this.val$boolResult = z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            String str = this.val$msg;
            boolean z = this.val$files.length() > 1;
            final Intent intent = new Intent(z ? "android.intent.action.SEND_MULTIPLE" : "android.intent.action.SEND");
            final PendingIntent broadcast = PendingIntent.getBroadcast(SocialSharing.this.cordova.getActivity().getApplicationContext(), 0, new Intent(SocialSharing.this.cordova.getActivity().getApplicationContext(), ShareChooserPendingIntent.class), 201326592);
            intent.addFlags(524288);
            String str2 = null;
            try {
                if (this.val$files.length() > 0 && !"".equals(this.val$files.getString(0))) {
                    String downloadDir = SocialSharing.this.getDownloadDir();
                    if (downloadDir != null) {
                        ArrayList arrayList = new ArrayList();
                        Uri uri = null;
                        for (int i = 0; i < this.val$files.length(); i++) {
                            uri = FileProvider.getUriForFile(SocialSharing.this.webView.getContext(), SocialSharing.this.cordova.getActivity().getPackageName() + ".sharing.provider", new File(SocialSharing.this.getFileUriAndSetType(intent, downloadDir, this.val$files.getString(i), this.val$subject, i).getPath()));
                            arrayList.add(uri);
                        }
                        if (!arrayList.isEmpty()) {
                            if (z) {
                                intent.putExtra("android.intent.extra.STREAM", arrayList);
                            } else {
                                intent.putExtra("android.intent.extra.STREAM", uri);
                            }
                        }
                    } else {
                        intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
                    }
                } else {
                    intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
                }
            } catch (Exception e) {
                this.callbackContext.error(e.getMessage());
            }
            if (SocialSharing.notEmpty(this.val$subject)) {
                intent.putExtra("android.intent.extra.SUBJECT", this.val$subject);
            }
            if (SocialSharing.notEmpty(this.val$url)) {
                if (SocialSharing.notEmpty(str)) {
                    str = str + " " + this.val$url;
                } else {
                    str = this.val$url;
                }
            }
            if (SocialSharing.notEmpty(str)) {
                intent.putExtra("android.intent.extra.TEXT", str);
            }
            intent.addFlags(268435456);
            String str3 = this.val$appPackageName;
            if (str3 != null) {
                if (str3.contains(RemoteSettings.FORWARD_SLASH_STRING)) {
                    String[] split = this.val$appPackageName.split(RemoteSettings.FORWARD_SLASH_STRING);
                    String str4 = split[0];
                    str2 = split[1];
                    str3 = str4;
                }
                ActivityInfo activity = SocialSharing.this.getActivity(this.callbackContext, intent, str3, this.val$appName);
                if (activity != null) {
                    if (this.val$peek) {
                        this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                        return;
                    }
                    intent.addCategory("android.intent.category.LAUNCHER");
                    String str5 = activity.applicationInfo.packageName;
                    if (str2 == null) {
                        str2 = activity.name;
                    }
                    intent.setComponent(new ComponentName(str5, str2));
                    SocialSharing.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: nl.xservices.plugins.SocialSharing.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass2.this.val$mycordova.startActivityForResult(AnonymousClass2.this.val$plugin, intent, 0);
                        }
                    });
                    if (SocialSharing.this.pasteMessage != null) {
                        new Timer().schedule(new TimerTask() { // from class: nl.xservices.plugins.SocialSharing.2.2
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                SocialSharing.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: nl.xservices.plugins.SocialSharing.2.2.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        SocialSharing.this.copyHintToClipboard(AnonymousClass2.this.val$msg, SocialSharing.this.pasteMessage);
                                        SocialSharing.this.showPasteMessage(SocialSharing.this.pasteMessage);
                                    }
                                });
                            }
                        }, 2000L);
                    }
                }
            } else if (this.val$peek) {
                this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            } else {
                SocialSharing.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: nl.xservices.plugins.SocialSharing.2.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass2.this.val$mycordova.startActivityForResult(AnonymousClass2.this.val$plugin, Intent.createChooser(intent, AnonymousClass2.this.val$chooserTitle, broadcast.getIntentSender()), AnonymousClass2.this.val$boolResult ? 1 : 2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void copyHintToClipboard(String str, String str2) {
        ((ClipboardManager) this.cordova.getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(str2, str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPasteMessage(String str) {
        Toast makeText = Toast.makeText(this.webView.getContext(), str, 1);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Uri getFileUriAndSetType(Intent intent, String str, String str2, String str3, int i) throws IOException {
        String str4;
        String str5;
        if (str2.endsWith("mp4") || str2.endsWith("mov") || str2.endsWith("3gp")) {
            intent.setType("video/*");
        } else if (str2.endsWith("mp3")) {
            intent.setType("audio/x-mpeg");
        } else {
            intent.setType("image/*");
        }
        if (str2.startsWith(ProxyConfig.MATCH_HTTP) || str2.startsWith("www/")) {
            String fileName = getFileName(str2);
            String str6 = "file://" + str + RemoteSettings.FORWARD_SLASH_STRING + fileName.replaceAll("[^a-zA-Z0-9._-]", "");
            if (str2.startsWith(ProxyConfig.MATCH_HTTP)) {
                URLConnection openConnection = new URL(str2).openConnection();
                String headerField = openConnection.getHeaderField("Content-Disposition");
                if (headerField != null) {
                    Matcher matcher = Pattern.compile("filename=([^;]+)").matcher(headerField);
                    if (matcher.find()) {
                        String replaceAll = matcher.group(1).replaceAll("[^a-zA-Z0-9._-]", "");
                        String str7 = replaceAll.length() != 0 ? replaceAll : "file";
                        str6 = "file://" + str + RemoteSettings.FORWARD_SLASH_STRING + str7;
                        fileName = str7;
                    }
                }
                saveFile(getBytes(openConnection.getInputStream()), str, fileName);
                intent.setType(getMIMEType(str2));
            } else {
                saveFile(getBytes(this.webView.getContext().getAssets().open(str2)), str, fileName);
            }
            str4 = str6;
        } else if (str2.startsWith("data:")) {
            if (!str2.contains(";base64,")) {
                intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
                return null;
            }
            String substring = str2.substring(str2.indexOf(";base64,") + 8);
            if (!str2.contains("data:image/")) {
                intent.setType(str2.substring(str2.indexOf("data:") + 5, str2.indexOf(";base64")));
            }
            String substring2 = str2.substring(str2.indexOf(RemoteSettings.FORWARD_SLASH_STRING) + 1, str2.indexOf(";base64"));
            if (notEmpty(str3)) {
                str5 = sanitizeFilename(str3) + (i != 0 ? "_" + i : "") + "." + substring2;
            } else {
                str5 = "file" + (i != 0 ? "_" + i : "") + "." + substring2;
            }
            saveFile(Base64.decode(substring, 0), str, str5);
            str4 = "file://" + str + RemoteSettings.FORWARD_SLASH_STRING + str5;
        } else if (str2.startsWith("df:")) {
            if (!str2.contains(";base64,")) {
                intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
                return null;
            }
            String substring3 = str2.substring(str2.indexOf("df:") + 3, str2.indexOf(";data:"));
            String substring4 = str2.substring(str2.indexOf(";data:") + 6, str2.indexOf(";base64,"));
            String substring5 = str2.substring(str2.indexOf(";base64,") + 8);
            intent.setType(substring4);
            saveFile(Base64.decode(substring5, 0), str, sanitizeFilename(substring3));
            str4 = "file://" + str + RemoteSettings.FORWARD_SLASH_STRING + sanitizeFilename(substring3);
        } else if (!str2.startsWith("file://")) {
            throw new IllegalArgumentException("URL_NOT_SUPPORTED");
        } else {
            intent.setType(getMIMEType(str2));
            str4 = str2;
        }
        return Uri.parse(str4);
    }

    private String getMIMEType(String str) {
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "*/*";
        }
        String str2 = MIME_Map.get(str.substring(lastIndexOf + 1, str.length()).toLowerCase());
        return str2 == null ? "*/*" : str2;
    }

    static {
        HashMap hashMap = new HashMap();
        MIME_Map = hashMap;
        hashMap.put("3gp", "video/3gpp");
        hashMap.put("apk", "application/vnd.android.package-archive");
        hashMap.put("asf", "video/x-ms-asf");
        hashMap.put("avi", "video/x-msvideo");
        hashMap.put("bin", "application/octet-stream");
        hashMap.put("bmp", "image/bmp");
        hashMap.put("c", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("class", "application/octet-stream");
        hashMap.put("conf", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("cpp", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("doc", "application/msword");
        hashMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        hashMap.put("xls", "application/vnd.ms-excel");
        hashMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        hashMap.put("exe", "application/octet-stream");
        hashMap.put("gif", "image/gif");
        hashMap.put("gtar", "application/x-gtar");
        hashMap.put("gz", "application/x-gzip");
        hashMap.put("h", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("htm", "text/html");
        hashMap.put("html", "text/html");
        hashMap.put("jar", "application/java-archive");
        hashMap.put("java", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("jpeg", "image/jpeg");
        hashMap.put("jpg", "image/*");
        hashMap.put("js", "application/x-javascript");
        hashMap.put("log", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("m3u", "audio/x-mpegurl");
        hashMap.put("m4a", "audio/mp4a-latm");
        hashMap.put("m4b", "audio/mp4a-latm");
        hashMap.put("m4p", "audio/mp4a-latm");
        hashMap.put("m4u", "video/vnd.mpegurl");
        hashMap.put("m4v", "video/x-m4v");
        hashMap.put("mov", "video/quicktime");
        hashMap.put("mp2", "audio/x-mpeg");
        hashMap.put("mp3", "audio/x-mpeg");
        hashMap.put("mp4", "video/mp4");
        hashMap.put("mpc", "application/vnd.mpohun.certificate");
        hashMap.put("mpe", "video/mpeg");
        hashMap.put("mpeg", "video/mpeg");
        hashMap.put("mpg", "video/mpeg");
        hashMap.put("mpg4", "video/mp4");
        hashMap.put("mpga", "audio/mpeg");
        hashMap.put(NotificationCompat.CATEGORY_MESSAGE, "application/vnd.ms-outlook");
        hashMap.put("ogg", "audio/ogg");
        hashMap.put("pdf", "application/pdf");
        hashMap.put("png", "image/png");
        hashMap.put("pps", "application/vnd.ms-powerpoint");
        hashMap.put("ppt", "application/vnd.ms-powerpoint");
        hashMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        hashMap.put("prop", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("rc", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("rmvb", "audio/x-pn-realaudio");
        hashMap.put("rtf", "application/rtf");
        hashMap.put("sh", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("tar", "application/x-tar");
        hashMap.put("tgz", "application/x-compressed");
        hashMap.put("txt", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("wav", "audio/x-wav");
        hashMap.put("wma", "audio/x-ms-wma");
        hashMap.put("wmv", "audio/x-ms-wmv");
        hashMap.put("wps", "application/vnd.ms-works");
        hashMap.put("xml", AssetHelper.DEFAULT_MIME_TYPE);
        hashMap.put("z", "application/x-compress");
        hashMap.put("zip", "application/x-zip-compressed");
        hashMap.put("", "*/*");
    }

    private boolean shareViaWhatsAppDirectly(CallbackContext callbackContext, String str, String str2, JSONArray jSONArray, String str3, final String str4) {
        final String str5;
        if (notEmpty(str3)) {
            if (notEmpty(str)) {
                str = str + " " + str3;
            } else {
                str5 = str3;
                this.cordova.getThreadPool().execute(new SocialSharingRunnable(callbackContext) { // from class: nl.xservices.plugins.SocialSharing.3
                    @Override // java.lang.Runnable
                    public void run() {
                        final Intent intent = new Intent("android.intent.action.VIEW");
                        try {
                            intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + str4 + "&text=" + URLEncoder.encode(str5, "UTF-8")));
                            intent.addFlags(268435456);
                            SocialSharing.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: nl.xservices.plugins.SocialSharing.3.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    try {
                                        SocialSharing.this.cordova.startActivityForResult(this, intent, 4);
                                    } catch (Exception e) {
                                        AnonymousClass3.this.callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                        } catch (Exception e) {
                            this.callbackContext.error(e.getMessage());
                        }
                    }
                });
                return true;
            }
        }
        str5 = str;
        this.cordova.getThreadPool().execute(new SocialSharingRunnable(callbackContext) { // from class: nl.xservices.plugins.SocialSharing.3
            @Override // java.lang.Runnable
            public void run() {
                final Intent intent = new Intent("android.intent.action.VIEW");
                try {
                    intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + str4 + "&text=" + URLEncoder.encode(str5, "UTF-8")));
                    intent.addFlags(268435456);
                    SocialSharing.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: nl.xservices.plugins.SocialSharing.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                SocialSharing.this.cordova.startActivityForResult(this, intent, 4);
                            } catch (Exception e) {
                                AnonymousClass3.this.callbackContext.error(e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    this.callbackContext.error(e.getMessage());
                }
            }
        });
        return true;
    }

    private boolean invokeSMSIntent(CallbackContext callbackContext, JSONObject jSONObject, String str) {
        final String optString = jSONObject.optString("message");
        final String phoneNumbersWithManufacturerSpecificSeparators = getPhoneNumbersWithManufacturerSpecificSeparators(str);
        this.cordova.getThreadPool().execute(new SocialSharingRunnable(callbackContext) { // from class: nl.xservices.plugins.SocialSharing.4
            @Override // java.lang.Runnable
            public void run() {
                Intent intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse("smsto:" + (SocialSharing.notEmpty(phoneNumbersWithManufacturerSpecificSeparators) ? phoneNumbersWithManufacturerSpecificSeparators : "")));
                intent.putExtra("sms_body", optString);
                intent.putExtra("sms_subject", r5);
                try {
                    String str2 = r6;
                    if (str2 != null && !"".equals(str2)) {
                        SocialSharing socialSharing = SocialSharing.this;
                        Uri fileUriAndSetType = socialSharing.getFileUriAndSetType(intent, socialSharing.getDownloadDir(), r6, r5, 0);
                        if (fileUriAndSetType != null) {
                            intent.putExtra("android.intent.extra.STREAM", fileUriAndSetType);
                        }
                    }
                    intent.addFlags(268435456);
                    SocialSharing.this.cordova.startActivityForResult(this, intent, 0);
                } catch (Exception e) {
                    this.callbackContext.error(e.getMessage());
                }
            }
        });
        return true;
    }

    private static String getPhoneNumbersWithManufacturerSpecificSeparators(String str) {
        if (notEmpty(str)) {
            char c = Build.MANUFACTURER.equalsIgnoreCase("samsung") ? ',' : ';';
            return str.replace(';', c).replace(',', c);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ActivityInfo getActivity(CallbackContext callbackContext, Intent intent, String str, String str2) {
        List<ResolveInfo> queryIntentActivities = this.webView.getContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            if (resolveInfo.activityInfo.packageName.contains(str) && (str2 == null || resolveInfo.activityInfo.name.contains(str2))) {
                return resolveInfo.activityInfo;
            }
        }
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, getShareActivities(queryIntentActivities)));
        return null;
    }

    private JSONArray getShareActivities(List<ResolveInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : list) {
            arrayList.add(resolveInfo.activityInfo.packageName);
        }
        return new JSONArray((Collection) arrayList);
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        CallbackContext callbackContext = this._callbackContext;
        if (callbackContext != null) {
            if (i == 1) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, i2 == -1));
            } else if (i == 2) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("completed", i2 == -1);
                    jSONObject.put("app", ShareChooserPendingIntent.chosenComponent != null ? ShareChooserPendingIntent.chosenComponent : "");
                    this._callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, jSONObject));
                } catch (JSONException e) {
                    this._callbackContext.error(e.getMessage());
                }
            } else {
                callbackContext.success();
            }
        }
    }

    private void createOrCleanDir(String str) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("CREATE_DIRS_FAILED");
            }
            return;
        }
        cleanupOldFiles(file);
    }

    private static String getFileName(String str) {
        if (str.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str = str.substring(0, str.length() - 1);
        }
        Matcher matcher = Pattern.compile(".*/([^?#]+)?").matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "file";
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            int read = inputStream.read(bArr, 0, 16384);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    private void saveFile(byte[] bArr, String str, String str2) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(new File(str), str2));
        fileOutputStream.write(bArr);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private void cleanupOldFiles(File file) {
        for (File file2 : file.listFiles()) {
            file2.delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean notEmpty(String str) {
        return (str == null || "".equals(str) || "null".equalsIgnoreCase(str)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] toStringArray(JSONArray jSONArray) throws JSONException {
        String[] strArr = new String[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            strArr[i] = jSONArray.getString(i);
        }
        return strArr;
    }

    public static String sanitizeFilename(String str) {
        return str.replaceAll("[:\\\\/*?|<> ]", "_");
    }
}
