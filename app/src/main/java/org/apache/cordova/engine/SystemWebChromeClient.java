package org.apache.cordova.engine;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.apache.cordova.CordovaDialogsHelper;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;

/* loaded from: classes.dex */
public class SystemWebChromeClient extends WebChromeClient {
    private static final int FILECHOOSER_RESULTCODE = 5173;
    private static final String LOG_TAG = "SystemWebChromeClient";
    private long MAX_QUOTA = 104857600;
    private Context appContext;
    private CordovaDialogsHelper dialogsHelper;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private View mVideoProgressView;
    protected final SystemWebViewEngine parentEngine;

    public SystemWebChromeClient(SystemWebViewEngine systemWebViewEngine) {
        this.parentEngine = systemWebViewEngine;
        this.appContext = systemWebViewEngine.webView.getContext();
        this.dialogsHelper = new CordovaDialogsHelper(this.appContext);
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
        this.dialogsHelper.showAlert(str2, new CordovaDialogsHelper.Result() { // from class: org.apache.cordova.engine.SystemWebChromeClient.1
            {
                SystemWebChromeClient.this = this;
            }

            @Override // org.apache.cordova.CordovaDialogsHelper.Result
            public void gotResult(boolean z, String str3) {
                if (z) {
                    jsResult.confirm();
                } else {
                    jsResult.cancel();
                }
            }
        });
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
        this.dialogsHelper.showConfirm(str2, new CordovaDialogsHelper.Result() { // from class: org.apache.cordova.engine.SystemWebChromeClient.2
            {
                SystemWebChromeClient.this = this;
            }

            @Override // org.apache.cordova.CordovaDialogsHelper.Result
            public void gotResult(boolean z, String str3) {
                if (z) {
                    jsResult.confirm();
                } else {
                    jsResult.cancel();
                }
            }
        });
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, final JsPromptResult jsPromptResult) {
        String promptOnJsPrompt = this.parentEngine.bridge.promptOnJsPrompt(str, str2, str3);
        if (promptOnJsPrompt != null) {
            jsPromptResult.confirm(promptOnJsPrompt);
            return true;
        }
        this.dialogsHelper.showPrompt(str2, str3, new CordovaDialogsHelper.Result() { // from class: org.apache.cordova.engine.SystemWebChromeClient.3
            {
                SystemWebChromeClient.this = this;
            }

            @Override // org.apache.cordova.CordovaDialogsHelper.Result
            public void gotResult(boolean z, String str4) {
                if (z) {
                    jsPromptResult.confirm(str4);
                } else {
                    jsPromptResult.cancel();
                }
            }
        });
        return true;
    }

    @Override // android.webkit.WebChromeClient
    public void onExceededDatabaseQuota(String str, String str2, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
        LOG.d(LOG_TAG, "onExceededDatabaseQuota estimatedSize: %d  currentQuota: %d  totalUsedQuota: %d", Long.valueOf(j2), Long.valueOf(j), Long.valueOf(j3));
        quotaUpdater.updateQuota(this.MAX_QUOTA);
    }

    @Override // android.webkit.WebChromeClient
    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(str, callback);
        callback.invoke(str, true, false);
        CordovaPlugin plugin = this.parentEngine.pluginManager.getPlugin("Geolocation");
        if (plugin == null || plugin.hasPermisssion()) {
            return;
        }
        plugin.requestPermissions(0);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        this.parentEngine.getCordovaWebView().showCustomView(view, customViewCallback);
    }

    @Override // android.webkit.WebChromeClient
    public void onHideCustomView() {
        this.parentEngine.getCordovaWebView().hideCustomView();
    }

    @Override // android.webkit.WebChromeClient
    public View getVideoLoadingProgressView() {
        if (this.mVideoProgressView == null) {
            LinearLayout linearLayout = new LinearLayout(this.parentEngine.getView().getContext());
            linearLayout.setOrientation(1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            linearLayout.setLayoutParams(layoutParams);
            ProgressBar progressBar = new ProgressBar(this.parentEngine.getView().getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.gravity = 17;
            progressBar.setLayoutParams(layoutParams2);
            linearLayout.addView(progressBar);
            this.mVideoProgressView = linearLayout;
        }
        return this.mVideoProgressView;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:1|(1:3)|4|(1:6)|7|(2:9|(1:33)(9:13|14|16|17|18|(1:20)|21|22|23))|34|18|(0)|21|22|23|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00c1, code lost:
        r10 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00c2, code lost:
        org.apache.cordova.LOG.w(org.apache.cordova.engine.SystemWebChromeClient.LOG_TAG, "No activity found to handle file chooser intent.", r10);
        r11.onReceiveValue(null);
     */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00a4  */
    @Override // android.webkit.WebChromeClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onShowFileChooser(android.webkit.WebView r10, final android.webkit.ValueCallback<android.net.Uri[]> r11, android.webkit.WebChromeClient.FileChooserParams r12) {
        /*
            r9 = this;
            java.lang.String r10 = "Temporary photo capture URI: "
            java.lang.String r0 = "Temporary photo capture file: "
            android.content.Intent r1 = r12.createIntent()
            r2 = 0
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r2)
            int r4 = r12.getMode()
            r5 = 1
            if (r4 != r5) goto L18
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r5)
        L18:
            java.lang.String r4 = "android.intent.extra.ALLOW_MULTIPLE"
            r1.putExtra(r4, r3)
            java.lang.String[] r3 = r12.getAcceptTypes()
            int r4 = r3.length
            if (r4 <= r5) goto L2e
        */
        //  java.lang.String r4 = "*/*"
        /*
            r1.setType(r4)
            java.lang.String r4 = "android.intent.extra.MIME_TYPES"
            r1.putExtra(r4, r3)
        L2e:
            boolean r12 = r12.isCaptureEnabled()
            java.lang.String r3 = "SystemWebChromeClient"
            r4 = 0
            if (r12 == 0) goto L9c
            android.content.Intent r12 = new android.content.Intent
            java.lang.String r6 = "android.media.action.IMAGE_CAPTURE"
            r12.<init>(r6)
            org.apache.cordova.engine.SystemWebViewEngine r6 = r9.parentEngine
            android.view.View r6 = r6.getView()
            android.content.Context r6 = r6.getContext()
            android.content.pm.PackageManager r7 = r6.getPackageManager()
            java.lang.String r8 = "android.hardware.camera.any"
            boolean r7 = r7.hasSystemFeature(r8)
            if (r7 == 0) goto L97
            android.content.pm.PackageManager r7 = r6.getPackageManager()
            android.content.ComponentName r7 = r12.resolveActivity(r7)
            if (r7 == 0) goto L97
            java.io.File r7 = r9.createTempFile(r6)     // Catch: java.io.IOException -> L8e
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L8e
            r8.<init>(r0)     // Catch: java.io.IOException -> L8e
            java.lang.StringBuilder r0 = r8.append(r7)     // Catch: java.io.IOException -> L8e
            java.lang.String r0 = r0.toString()     // Catch: java.io.IOException -> L8e
            org.apache.cordova.LOG.d(r3, r0)     // Catch: java.io.IOException -> L8e
            android.net.Uri r0 = r9.createUriForFile(r6, r7)     // Catch: java.io.IOException -> L8e
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L8c
            r6.<init>(r10)     // Catch: java.io.IOException -> L8c
            java.lang.StringBuilder r10 = r6.append(r0)     // Catch: java.io.IOException -> L8c
            java.lang.String r10 = r10.toString()     // Catch: java.io.IOException -> L8c
            org.apache.cordova.LOG.d(r3, r10)     // Catch: java.io.IOException -> L8c
            java.lang.String r10 = "output"
            r12.putExtra(r10, r0)     // Catch: java.io.IOException -> L8c
            goto L9e
        L8c:
            r10 = move-exception
            goto L90
        L8e:
            r10 = move-exception
            r0 = r4
        L90:
            java.lang.String r12 = "Unable to create temporary file for photo capture"
            org.apache.cordova.LOG.e(r3, r12, r10)
            r12 = r4
            goto L9e
        L97:
            java.lang.String r10 = "Device does not support photo capture"
            org.apache.cordova.LOG.w(r3, r10)
        L9c:
            r12 = r4
            r0 = r12
        L9e:
            android.content.Intent r10 = android.content.Intent.createChooser(r1, r4)
            if (r12 == 0) goto Lad
            android.content.Intent[] r1 = new android.content.Intent[r5]
            r1[r2] = r12
            java.lang.String r12 = "android.intent.extra.INITIAL_INTENTS"
            r10.putExtra(r12, r1)
        Lad:
            java.lang.String r12 = "Starting intent for file chooser"
            org.apache.cordova.LOG.i(r3, r12)     // Catch: android.content.ActivityNotFoundException -> Lc1
            org.apache.cordova.engine.SystemWebViewEngine r12 = r9.parentEngine     // Catch: android.content.ActivityNotFoundException -> Lc1
            org.apache.cordova.CordovaInterface r12 = r12.cordova     // Catch: android.content.ActivityNotFoundException -> Lc1
            org.apache.cordova.engine.SystemWebChromeClient$4 r1 = new org.apache.cordova.engine.SystemWebChromeClient$4     // Catch: android.content.ActivityNotFoundException -> Lc1
            r1.<init>()     // Catch: android.content.ActivityNotFoundException -> Lc1
            r0 = 5173(0x1435, float:7.249E-42)
            r12.startActivityForResult(r1, r10, r0)     // Catch: android.content.ActivityNotFoundException -> Lc1
            goto Lca
        Lc1:
            r10 = move-exception
            java.lang.String r12 = "No activity found to handle file chooser intent."
            org.apache.cordova.LOG.w(r3, r12, r10)
            r11.onReceiveValue(r4)
        Lca:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.cordova.engine.SystemWebChromeClient.onShowFileChooser(android.webkit.WebView, android.webkit.ValueCallback, android.webkit.WebChromeClient$FileChooserParams):boolean");
    }

    private File createTempFile(Context context) throws IOException {
        return File.createTempFile("temp", ".jpg", context.getCacheDir());
    }

    private Uri createUriForFile(Context context, File file) throws IOException {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".cdv.core.file.provider", file);
    }

    @Override // android.webkit.WebChromeClient
    public void onPermissionRequest(PermissionRequest permissionRequest) {
        LOG.d(LOG_TAG, "onPermissionRequest: " + Arrays.toString(permissionRequest.getResources()));
        permissionRequest.grant(permissionRequest.getResources());
    }

    public void destroyLastDialog() {
        this.dialogsHelper.destroyLastDialog();
    }
}
