package org.apache.cordova;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.gms.common.internal.ImagesContract;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.NativeToJsMessageQueue;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CordovaWebViewImpl implements CordovaWebView {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TAG = "CordovaWebViewImpl";
    private CoreAndroid appPlugin;
    private CordovaInterface cordova;
    protected final CordovaWebViewEngine engine;
    private boolean hasPausedEver;
    String loadedUrl;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private NativeToJsMessageQueue nativeToJsMessageQueue;
    private PluginManager pluginManager;
    private CordovaPreferences preferences;
    private CordovaResourceApi resourceApi;
    private int loadUrlTimeout = 0;
    private EngineClient engineClient = new EngineClient();
    private Set<Integer> boundKeyCodes = new HashSet();

    static /* synthetic */ int access$108(CordovaWebViewImpl cordovaWebViewImpl) {
        int i = cordovaWebViewImpl.loadUrlTimeout;
        cordovaWebViewImpl.loadUrlTimeout = i + 1;
        return i;
    }

    public static CordovaWebViewEngine createEngine(Context context, CordovaPreferences cordovaPreferences) {
        try {
            return (CordovaWebViewEngine) Class.forName(cordovaPreferences.getString("webview", SystemWebViewEngine.class.getCanonicalName())).getConstructor(Context.class, CordovaPreferences.class).newInstance(context, cordovaPreferences);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create webview. ", e);
        }
    }

    public CordovaWebViewImpl(CordovaWebViewEngine cordovaWebViewEngine) {
        this.engine = cordovaWebViewEngine;
    }

    public void init(CordovaInterface cordovaInterface) {
        init(cordovaInterface, new ArrayList(), new CordovaPreferences());
    }

    @Override // org.apache.cordova.CordovaWebView
    public void init(CordovaInterface cordovaInterface, List<PluginEntry> list, CordovaPreferences cordovaPreferences) {
        if (this.cordova != null) {
            throw new IllegalStateException();
        }
        this.cordova = cordovaInterface;
        this.preferences = cordovaPreferences;
        this.pluginManager = new PluginManager(this, this.cordova, list);
        this.resourceApi = new CordovaResourceApi(this.engine.getView().getContext(), this.pluginManager);
        NativeToJsMessageQueue nativeToJsMessageQueue = new NativeToJsMessageQueue();
        this.nativeToJsMessageQueue = nativeToJsMessageQueue;
        nativeToJsMessageQueue.addBridgeMode(new NativeToJsMessageQueue.NoOpBridgeMode());
        this.nativeToJsMessageQueue.addBridgeMode(new NativeToJsMessageQueue.LoadUrlBridgeMode(this.engine, cordovaInterface));
        if (cordovaPreferences.getBoolean("DisallowOverscroll", false)) {
            this.engine.getView().setOverScrollMode(2);
        }
        this.engine.init(this, cordovaInterface, this.engineClient, this.resourceApi, this.pluginManager, this.nativeToJsMessageQueue);
        this.pluginManager.addService(CoreAndroid.PLUGIN_NAME, "org.apache.cordova.CoreAndroid", true);
        this.pluginManager.init();
    }

    @Override // org.apache.cordova.CordovaWebView
    public boolean isInitialized() {
        return this.cordova != null;
    }

    @Override // org.apache.cordova.CordovaWebView
    public void loadUrlIntoView(final String str, boolean z) {
        LOG.d(TAG, ">>> loadUrl(" + str + ")");
        boolean z2 = false;
        if (str.equals("about:blank") || str.startsWith("javascript:")) {
            this.engine.loadUrl(str, false);
            return;
        }
        final boolean z3 = (z || this.loadedUrl == null) ? true : true;
        if (z3) {
            if (this.loadedUrl != null) {
                this.appPlugin = null;
                this.pluginManager.init();
            }
            this.loadedUrl = str;
        }
        final int i = this.loadUrlTimeout;
        final int integer = this.preferences.getInteger("LoadUrlTimeoutValue", AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);
        final Runnable runnable = new Runnable() { // from class: org.apache.cordova.CordovaWebViewImpl.1
            {
                CordovaWebViewImpl.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                CordovaWebViewImpl.this.stopLoading();
                LOG.e(CordovaWebViewImpl.TAG, "CordovaWebView: TIMEOUT ERROR!");
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("errorCode", -6);
                    jSONObject.put("description", "The connection to the server was unsuccessful.");
                    jSONObject.put(ImagesContract.URL, str);
                } catch (JSONException unused) {
                }
                CordovaWebViewImpl.this.pluginManager.postMessage("onReceivedError", jSONObject);
            }
        };
        final Runnable runnable2 = new Runnable() { // from class: org.apache.cordova.CordovaWebViewImpl.2
            {
                CordovaWebViewImpl.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    synchronized (this) {
                        wait(integer);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (CordovaWebViewImpl.this.loadUrlTimeout != i || CordovaWebViewImpl.this.cordova.getActivity() == null) {
                    if (CordovaWebViewImpl.this.cordova.getActivity() == null) {
                        LOG.d(CordovaWebViewImpl.TAG, "Cordova activity does not exist.");
                        return;
                    }
                    return;
                }
                CordovaWebViewImpl.this.cordova.getActivity().runOnUiThread(runnable);
            }
        };
        if (this.cordova.getActivity() != null) {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.CordovaWebViewImpl.3
                {
                    CordovaWebViewImpl.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (integer > 0) {
                        CordovaWebViewImpl.this.cordova.getThreadPool().execute(runnable2);
                    }
                    CordovaWebViewImpl.this.engine.loadUrl(str, z3);
                }
            });
        } else {
            LOG.d(TAG, "Cordova activity does not exist.");
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void loadUrl(String str) {
        loadUrlIntoView(str, true);
    }

    @Override // org.apache.cordova.CordovaWebView
    public void showWebPage(String str, boolean z, boolean z2, Map<String, Object> map) {
        Intent intent;
        LOG.d(TAG, "showWebPage(%s, %b, %b, HashMap)", str, Boolean.valueOf(z), Boolean.valueOf(z2));
        if (z2) {
            this.engine.clearHistory();
        }
        if (!z) {
            if (this.pluginManager.shouldAllowNavigation(str)) {
                loadUrlIntoView(str, true);
            } else {
                LOG.w(TAG, "showWebPage: Refusing to load URL into webview since it is not in the <allow-navigation> allow list. URL=" + str);
            }
        } else if (!this.pluginManager.shouldOpenExternalUrl(str).booleanValue()) {
            LOG.w(TAG, "showWebPage: Refusing to send intent for URL since it is not in the <allow-intent> allow list. URL=" + str);
        } else {
            Intent intent2 = null;
            try {
                try {
                    if (str.startsWith("intent://")) {
                        intent = Intent.parseUri(str, 1);
                    } else {
                        intent = new Intent("android.intent.action.VIEW");
                        try {
                            intent.addCategory("android.intent.category.BROWSABLE");
                            Uri parse = Uri.parse(str);
                            if ("file".equals(parse.getScheme())) {
                                intent.setDataAndType(parse, this.resourceApi.getMimeType(parse));
                            } else {
                                intent.setData(parse);
                            }
                        } catch (ActivityNotFoundException e) {
                            intent2 = intent;
                            e = e;
                            if (str.startsWith("intent://") && intent2 != null && intent2.getStringExtra("browser_fallback_url") != null) {
                                showWebPage(intent2.getStringExtra("browser_fallback_url"), z, z2, map);
                                return;
                            } else {
                                LOG.e(TAG, "Error loading url " + str, e);
                                return;
                            }
                        }
                    }
                    Intent intent3 = intent;
                    if (this.cordova.getActivity() != null) {
                        this.cordova.getActivity().startActivity(intent3);
                    } else {
                        LOG.d(TAG, "Cordova activity does not exist.");
                    }
                } catch (ActivityNotFoundException e2) {
                    e = e2;
                }
            } catch (URISyntaxException e3) {
                LOG.e(TAG, "Error parsing url " + str, e3);
            }
        }
    }

    /* loaded from: classes.dex */
    private static class WrapperView extends FrameLayout {
        private final CordovaWebViewEngine engine;

        public WrapperView(Context context, CordovaWebViewEngine cordovaWebViewEngine) {
            super(context);
            this.engine = cordovaWebViewEngine;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            boolean dispatchKeyEvent = this.engine.getView().dispatchKeyEvent(keyEvent);
            return !dispatchKeyEvent ? super.dispatchKeyEvent(keyEvent) : dispatchKeyEvent;
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    @Deprecated
    public void showCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        LOG.d(TAG, "showing Custom View");
        if (this.mCustomView != null) {
            customViewCallback.onCustomViewHidden();
            return;
        }
        WrapperView wrapperView = new WrapperView(getContext(), this.engine);
        wrapperView.addView(view);
        this.mCustomView = wrapperView;
        this.mCustomViewCallback = customViewCallback;
        ViewGroup viewGroup = (ViewGroup) this.engine.getView().getParent();
        viewGroup.addView(wrapperView, new FrameLayout.LayoutParams(-1, -1, 17));
        this.engine.getView().setVisibility(8);
        viewGroup.setVisibility(0);
        viewGroup.bringToFront();
    }

    @Override // org.apache.cordova.CordovaWebView
    @Deprecated
    public void hideCustomView() {
        if (this.mCustomView == null) {
            return;
        }
        LOG.d(TAG, "Hiding Custom View");
        this.mCustomView.setVisibility(8);
        ((ViewGroup) this.engine.getView().getParent()).removeView(this.mCustomView);
        this.mCustomView = null;
        this.mCustomViewCallback.onCustomViewHidden();
        this.engine.getView().setVisibility(0);
        this.engine.getView().requestFocus();
    }

    @Override // org.apache.cordova.CordovaWebView
    @Deprecated
    public boolean isCustomViewShowing() {
        return this.mCustomView != null;
    }

    @Override // org.apache.cordova.CordovaWebView
    @Deprecated
    public void sendJavascript(String str) {
        this.nativeToJsMessageQueue.addJavaScript(str);
    }

    @Override // org.apache.cordova.CordovaWebView
    public void sendPluginResult(PluginResult pluginResult, String str) {
        this.nativeToJsMessageQueue.addPluginResult(pluginResult, str);
    }

    @Override // org.apache.cordova.CordovaWebView
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override // org.apache.cordova.CordovaWebView
    public CordovaPreferences getPreferences() {
        return this.preferences;
    }

    @Override // org.apache.cordova.CordovaWebView
    public ICordovaCookieManager getCookieManager() {
        return this.engine.getCookieManager();
    }

    @Override // org.apache.cordova.CordovaWebView
    public CordovaResourceApi getResourceApi() {
        return this.resourceApi;
    }

    @Override // org.apache.cordova.CordovaWebView
    public CordovaWebViewEngine getEngine() {
        return this.engine;
    }

    @Override // org.apache.cordova.CordovaWebView
    public View getView() {
        return this.engine.getView();
    }

    @Override // org.apache.cordova.CordovaWebView
    public Context getContext() {
        return this.engine.getView().getContext();
    }

    public void sendJavascriptEvent(String str) {
        if (this.appPlugin == null) {
            this.appPlugin = (CoreAndroid) this.pluginManager.getPlugin(CoreAndroid.PLUGIN_NAME);
        }
        CoreAndroid coreAndroid = this.appPlugin;
        if (coreAndroid == null) {
            LOG.w(TAG, "Unable to fire event without existing plugin");
        } else {
            coreAndroid.fireJavascriptEvent(str);
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void setButtonPlumbedToJs(int i, boolean z) {
        if (i != 4 && i != 82 && i != 24 && i != 25) {
            throw new IllegalArgumentException("Unsupported keycode: " + i);
        }
        if (z) {
            this.boundKeyCodes.add(Integer.valueOf(i));
        } else {
            this.boundKeyCodes.remove(Integer.valueOf(i));
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public boolean isButtonPlumbedToJs(int i) {
        return this.boundKeyCodes.contains(Integer.valueOf(i));
    }

    @Override // org.apache.cordova.CordovaWebView
    public Object postMessage(String str, Object obj) {
        return this.pluginManager.postMessage(str, obj);
    }

    @Override // org.apache.cordova.CordovaWebView
    public String getUrl() {
        return this.engine.getUrl();
    }

    @Override // org.apache.cordova.CordovaWebView
    public void stopLoading() {
        this.loadUrlTimeout++;
    }

    @Override // org.apache.cordova.CordovaWebView
    public boolean canGoBack() {
        return this.engine.canGoBack();
    }

    @Override // org.apache.cordova.CordovaWebView
    public void clearCache() {
        this.engine.clearCache();
    }

    @Override // org.apache.cordova.CordovaWebView
    @Deprecated
    public void clearCache(boolean z) {
        this.engine.clearCache();
    }

    @Override // org.apache.cordova.CordovaWebView
    public void clearHistory() {
        this.engine.clearHistory();
    }

    @Override // org.apache.cordova.CordovaWebView
    public boolean backHistory() {
        return this.engine.goBack();
    }

    @Override // org.apache.cordova.CordovaWebView
    public void onNewIntent(Intent intent) {
        PluginManager pluginManager = this.pluginManager;
        if (pluginManager != null) {
            pluginManager.onNewIntent(intent);
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void handlePause(boolean z) {
        if (isInitialized()) {
            this.hasPausedEver = true;
            this.pluginManager.onPause(z);
            sendJavascriptEvent("pause");
            if (z) {
                return;
            }
            this.engine.setPaused(true);
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void handleResume(boolean z) {
        if (isInitialized()) {
            this.engine.setPaused(false);
            this.pluginManager.onResume(z);
            if (this.hasPausedEver) {
                sendJavascriptEvent("resume");
            }
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void handleStart() {
        if (isInitialized()) {
            this.pluginManager.onStart();
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void handleStop() {
        if (isInitialized()) {
            this.pluginManager.onStop();
        }
    }

    @Override // org.apache.cordova.CordovaWebView
    public void handleDestroy() {
        if (isInitialized()) {
            this.loadUrlTimeout++;
            this.pluginManager.onDestroy();
            loadUrl("about:blank");
            this.engine.destroy();
            hideCustomView();
        }
    }

    /* loaded from: classes.dex */
    public class EngineClient implements CordovaWebViewEngine.Client {
        protected EngineClient() {
            CordovaWebViewImpl.this = r1;
        }

        @Override // org.apache.cordova.CordovaWebViewEngine.Client
        public void clearLoadTimeoutTimer() {
            CordovaWebViewImpl.access$108(CordovaWebViewImpl.this);
        }

        @Override // org.apache.cordova.CordovaWebViewEngine.Client
        public void onPageStarted(String str) {
            LOG.d(CordovaWebViewImpl.TAG, "onPageDidNavigate(" + str + ")");
            CordovaWebViewImpl.this.boundKeyCodes.clear();
            CordovaWebViewImpl.this.pluginManager.onReset();
            CordovaWebViewImpl.this.pluginManager.postMessage("onPageStarted", str);
        }

        @Override // org.apache.cordova.CordovaWebViewEngine.Client
        public void onReceivedError(int i, String str, String str2) {
            clearLoadTimeoutTimer();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("errorCode", i);
                jSONObject.put("description", str);
                jSONObject.put(ImagesContract.URL, str2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CordovaWebViewImpl.this.pluginManager.postMessage("onReceivedError", jSONObject);
        }

        @Override // org.apache.cordova.CordovaWebViewEngine.Client
        public void onPageFinishedLoading(String str) {
            LOG.d(CordovaWebViewImpl.TAG, "onPageFinished(" + str + ")");
            clearLoadTimeoutTimer();
            CordovaWebViewImpl.this.pluginManager.postMessage("onPageFinished", str);
            if (CordovaWebViewImpl.this.engine.getView().getVisibility() != 0) {
                new Thread(new Runnable() { // from class: org.apache.cordova.CordovaWebViewImpl.EngineClient.1
                    {
                        EngineClient.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Thread.sleep(2000L);
                            if (CordovaWebViewImpl.this.cordova.getActivity() != null) {
                                CordovaWebViewImpl.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.CordovaWebViewImpl.EngineClient.1.1
                                    {
                                        AnonymousClass1.this = this;
                                    }

                                    @Override // java.lang.Runnable
                                    public void run() {
                                        CordovaWebViewImpl.this.pluginManager.postMessage("spinner", "stop");
                                    }
                                });
                            } else {
                                LOG.d(CordovaWebViewImpl.TAG, "Cordova activity does not exist.");
                            }
                        } catch (InterruptedException unused) {
                        }
                    }
                }).start();
            }
            if (str.equals("about:blank")) {
                CordovaWebViewImpl.this.pluginManager.postMessage("exit", null);
            }
        }

        @Override // org.apache.cordova.CordovaWebViewEngine.Client
        public Boolean onDispatchKeyEvent(KeyEvent keyEvent) {
            String str;
            int keyCode = keyEvent.getKeyCode();
            boolean z = keyCode == 4;
            if (keyEvent.getAction() == 0) {
                if ((z && CordovaWebViewImpl.this.mCustomView != null) || CordovaWebViewImpl.this.boundKeyCodes.contains(Integer.valueOf(keyCode))) {
                    return true;
                }
                if (z) {
                    return Boolean.valueOf(CordovaWebViewImpl.this.engine.canGoBack());
                }
            } else if (keyEvent.getAction() == 1) {
                if (!z || CordovaWebViewImpl.this.mCustomView == null) {
                    if (CordovaWebViewImpl.this.boundKeyCodes.contains(Integer.valueOf(keyCode))) {
                        if (keyCode == 4) {
                            str = "backbutton";
                        } else if (keyCode == 82) {
                            str = "menubutton";
                        } else if (keyCode == 84) {
                            str = "searchbutton";
                        } else if (keyCode != 24) {
                            str = keyCode != 25 ? null : "volumedownbutton";
                        } else {
                            str = "volumeupbutton";
                        }
                        if (str != null) {
                            CordovaWebViewImpl.this.sendJavascriptEvent(str);
                            return true;
                        }
                    } else if (z) {
                        return Boolean.valueOf(CordovaWebViewImpl.this.engine.goBack());
                    }
                } else {
                    CordovaWebViewImpl.this.hideCustomView();
                    return true;
                }
            }
            return null;
        }

        @Override // org.apache.cordova.CordovaWebViewEngine.Client
        public boolean onNavigationAttempt(String str) {
            if (CordovaWebViewImpl.this.pluginManager.onOverrideUrlLoading(str)) {
                return true;
            }
            if (CordovaWebViewImpl.this.pluginManager.shouldAllowNavigation(str)) {
                return false;
            }
            if (CordovaWebViewImpl.this.pluginManager.shouldOpenExternalUrl(str).booleanValue()) {
                CordovaWebViewImpl.this.showWebPage(str, true, false, null);
                return true;
            }
            LOG.w(CordovaWebViewImpl.TAG, "Blocked (possibly sub-frame) navigation to non-allowed URL: " + str);
            return true;
        }
    }
}
