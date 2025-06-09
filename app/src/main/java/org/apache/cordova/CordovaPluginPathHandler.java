package org.apache.cordova;

import androidx.webkit.WebViewAssetLoader;

/* loaded from: classes.dex */
public class CordovaPluginPathHandler {
    private final WebViewAssetLoader.PathHandler handler;

    public CordovaPluginPathHandler(WebViewAssetLoader.PathHandler pathHandler) {
        this.handler = pathHandler;
    }

    public WebViewAssetLoader.PathHandler getPathHandler() {
        return this.handler;
    }
}
