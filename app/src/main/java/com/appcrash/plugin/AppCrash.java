package com.appcrash.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

/* loaded from: classes.dex */
public class AppCrash extends CordovaPlugin {
    @Override // org.apache.cordova.CordovaPlugin
    public boolean execute(String str, JSONArray jSONArray, CallbackContext callbackContext) throws RuntimeException {
        if ("crash".equals(str)) {
            crashAlreadyYouApp();
            return false;
        }
        callbackContext.error("could not throw");
        return false;
    }

    public void crashAlreadyYouApp() {
        this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.appcrash.plugin.AppCrash.1
            @Override // java.lang.Runnable
            public void run() {
                throw new RuntimeException("This is a crash");
            }
        });
    }
}
