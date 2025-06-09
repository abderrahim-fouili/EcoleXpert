package org.apache.cordova.engine;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.MimeTypeMap;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.ServiceWorkerClient;
import android.webkit.ServiceWorkerController;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.internal.AssetHelper;
import com.google.firebase.sessions.settings.RemoteSettings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.cordova.AuthenticationToken;
import org.apache.cordova.CordovaClientCertRequest;
import org.apache.cordova.CordovaHttpAuthHandler;
import org.apache.cordova.CordovaPluginPathHandler;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginManager;

/* loaded from: classes.dex */
public class SystemWebViewClient extends WebViewClient {
    private static final String TAG = "SystemWebViewClient";
    private final WebViewAssetLoader assetLoader;
    boolean isCurrentlyLoading;
    protected final SystemWebViewEngine parentEngine;
    private boolean doClearHistory = false;
    private Hashtable<String, AuthenticationToken> authenticationTokens = new Hashtable<>();

    public SystemWebViewClient(final SystemWebViewEngine systemWebViewEngine) {
        this.parentEngine = systemWebViewEngine;
        WebViewAssetLoader.Builder httpAllowed = new WebViewAssetLoader.Builder().setDomain(systemWebViewEngine.preferences.getString("hostname", "localhost").toLowerCase()).setHttpAllowed(true);
        httpAllowed.addPathHandler(RemoteSettings.FORWARD_SLASH_STRING, new WebViewAssetLoader.PathHandler() { // from class: org.apache.cordova.engine.SystemWebViewClient$$ExternalSyntheticLambda0
            {
                SystemWebViewClient.this = this;
            }

            @Override // androidx.webkit.WebViewAssetLoader.PathHandler
            public final WebResourceResponse handle(String str) {
                return SystemWebViewClient.this.m1695lambda$new$0$orgapachecordovaengineSystemWebViewClient(systemWebViewEngine, str);
            }
        });
        this.assetLoader = httpAllowed.build();
        if (systemWebViewEngine.preferences.getBoolean("ResolveServiceWorkerRequests", true)) {
            ServiceWorkerController.getInstance().setServiceWorkerClient(new ServiceWorkerClient() { // from class: org.apache.cordova.engine.SystemWebViewClient.1
                @Override // android.webkit.ServiceWorkerClient
                public WebResourceResponse shouldInterceptRequest(WebResourceRequest webResourceRequest) {
                    return SystemWebViewClient.this.assetLoader.shouldInterceptRequest(webResourceRequest.getUrl());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$org-apache-cordova-engine-SystemWebViewClient  reason: not valid java name */
    public /* synthetic */ WebResourceResponse m1695lambda$new$0$orgapachecordovaengineSystemWebViewClient(SystemWebViewEngine systemWebViewEngine, String str) {
        WebResourceResponse handle;
        try {
            PluginManager pluginManager = this.parentEngine.pluginManager;
            if (pluginManager != null) {
                Iterator<CordovaPluginPathHandler> it = pluginManager.getPluginPathHandlers().iterator();
                while (it.hasNext()) {
                    CordovaPluginPathHandler next = it.next();
                    if (next.getPathHandler() != null && (handle = next.getPathHandler().handle(str)) != null) {
                        return handle;
                    }
                }
            }
            if (str.isEmpty()) {
                str = "index.html";
            }
            InputStream open = systemWebViewEngine.webView.getContext().getAssets().open("www/" + str, 2);
            String str2 = "text/html";
            String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
            if (fileExtensionFromUrl != null) {
                if (!str.endsWith(".js") && !str.endsWith(".mjs")) {
                    if (str.endsWith(".wasm")) {
                        str2 = "application/wasm";
                    } else {
                        str2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
                    }
                }
                str2 = "application/javascript";
            }
            return new WebResourceResponse(str2, null, open);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return this.parentEngine.client.onNavigationAttempt(str);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        AuthenticationToken authenticationToken = getAuthenticationToken(str, str2);
        if (authenticationToken != null) {
            httpAuthHandler.proceed(authenticationToken.getUserName(), authenticationToken.getPassword());
            return;
        }
        PluginManager pluginManager = this.parentEngine.pluginManager;
        if (pluginManager != null && pluginManager.onReceivedHttpAuthRequest(null, new CordovaHttpAuthHandler(httpAuthHandler), str, str2)) {
            this.parentEngine.client.clearLoadTimeoutTimer();
        } else {
            super.onReceivedHttpAuthRequest(webView, httpAuthHandler, str, str2);
        }
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
        PluginManager pluginManager = this.parentEngine.pluginManager;
        if (pluginManager != null && pluginManager.onReceivedClientCertRequest(null, new CordovaClientCertRequest(clientCertRequest))) {
            this.parentEngine.client.clearLoadTimeoutTimer();
        } else {
            super.onReceivedClientCertRequest(webView, clientCertRequest);
        }
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        super.onPageStarted(webView, str, bitmap);
        this.isCurrentlyLoading = true;
        this.parentEngine.bridge.reset();
        this.parentEngine.client.onPageStarted(str);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (this.isCurrentlyLoading || str.startsWith("about:")) {
            this.isCurrentlyLoading = false;
            if (this.doClearHistory) {
                webView.clearHistory();
                this.doClearHistory = false;
            }
            this.parentEngine.client.onPageFinishedLoading(str);
        }
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (this.isCurrentlyLoading) {
            LOG.d(TAG, "CordovaWebViewClient.onReceivedError: Error code=%s Description=%s URL=%s", Integer.valueOf(i), str, str2);
            if (i == -10) {
                this.parentEngine.client.clearLoadTimeoutTimer();
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }
                super.onReceivedError(webView, i, str, str2);
            }
            this.parentEngine.client.onReceivedError(i, str, str2);
        }
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        try {
            if ((this.parentEngine.cordova.getActivity().getPackageManager().getApplicationInfo(this.parentEngine.cordova.getActivity().getPackageName(), 128).flags & 2) != 0) {
                sslErrorHandler.proceed();
            } else {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }

    public void setAuthenticationToken(AuthenticationToken authenticationToken, String str, String str2) {
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        this.authenticationTokens.put(str.concat(str2), authenticationToken);
    }

    public AuthenticationToken removeAuthenticationToken(String str, String str2) {
        return this.authenticationTokens.remove(str.concat(str2));
    }

    public AuthenticationToken getAuthenticationToken(String str, String str2) {
        AuthenticationToken authenticationToken = this.authenticationTokens.get(str.concat(str2));
        if (authenticationToken == null) {
            AuthenticationToken authenticationToken2 = this.authenticationTokens.get(str);
            if (authenticationToken2 == null) {
                authenticationToken2 = this.authenticationTokens.get(str2);
            }
            AuthenticationToken authenticationToken3 = authenticationToken2;
            return authenticationToken3 == null ? this.authenticationTokens.get("") : authenticationToken3;
        }
        return authenticationToken;
    }

    public void clearAuthenticationTokens() {
        this.authenticationTokens.clear();
    }

    @Override // android.webkit.WebViewClient
    public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        try {
            if (!this.parentEngine.pluginManager.shouldAllowRequest(str)) {
                LOG.w(TAG, "URL blocked by allow list: " + str);
                return new WebResourceResponse(AssetHelper.DEFAULT_MIME_TYPE, "UTF-8", null);
            }
            CordovaResourceApi cordovaResourceApi = this.parentEngine.resourceApi;
            Uri parse = Uri.parse(str);
            Uri remapUri = cordovaResourceApi.remapUri(parse);
            if (parse.equals(remapUri) && !needsSpecialsInAssetUrlFix(parse) && !needsContentUrlFix(parse)) {
                return null;
            }
            CordovaResourceApi.OpenForReadResult openForRead = cordovaResourceApi.openForRead(remapUri, true);
            return new WebResourceResponse(openForRead.mimeType, "UTF-8", openForRead.inputStream);
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                LOG.e(TAG, "Error occurred while loading a file (returning a 404).", e);
            }
            return new WebResourceResponse(AssetHelper.DEFAULT_MIME_TYPE, "UTF-8", null);
        }
    }

    private static boolean needsContentUrlFix(Uri uri) {
        return "content".equals(uri.getScheme());
    }

    private static boolean needsSpecialsInAssetUrlFix(Uri uri) {
        if (CordovaResourceApi.getUriType(uri) != 1) {
            return false;
        }
        if (uri.getQuery() == null && uri.getFragment() == null) {
            uri.toString().contains("%");
            return false;
        }
        return true;
    }

    @Override // android.webkit.WebViewClient
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        return this.assetLoader.shouldInterceptRequest(webResourceRequest.getUrl());
    }

    @Override // android.webkit.WebViewClient
    public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
        PluginManager pluginManager = this.parentEngine.pluginManager;
        if (pluginManager == null || !pluginManager.onRenderProcessGone(webView, renderProcessGoneDetail)) {
            return super.onRenderProcessGone(webView, renderProcessGoneDetail);
        }
        return true;
    }
}
