package org.apache.cordova;

import java.security.SecureRandom;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
public class CordovaBridge {
    private static final String LOG_TAG = "CordovaBridge";
    private volatile int expectedBridgeSecret = -1;
    private NativeToJsMessageQueue jsMessageQueue;
    private PluginManager pluginManager;

    public CordovaBridge(PluginManager pluginManager, NativeToJsMessageQueue nativeToJsMessageQueue) {
        this.pluginManager = pluginManager;
        this.jsMessageQueue = nativeToJsMessageQueue;
    }

    public String jsExec(int i, String str, String str2, String str3, String str4) throws JSONException, IllegalAccessException {
        String str5;
        if (verifySecret("exec()", i)) {
            if (str4 == null) {
                return "@Null arguments.";
            }
            this.jsMessageQueue.setPaused(true);
            try {
                CordovaResourceApi.jsThread = Thread.currentThread();
                this.pluginManager.exec(str, str2, str3, str4);
                str5 = this.jsMessageQueue.popAndEncode(false);
            } finally {
                try {
                    return str5;
                } finally {
                }
            }
            return str5;
        }
        return null;
    }

    public void jsSetNativeToJsBridgeMode(int i, int i2) throws IllegalAccessException {
        if (verifySecret("setNativeToJsBridgeMode()", i)) {
            this.jsMessageQueue.setBridgeMode(i2);
        }
    }

    public String jsRetrieveJsMessages(int i, boolean z) throws IllegalAccessException {
        if (verifySecret("retrieveJsMessages()", i)) {
            return this.jsMessageQueue.popAndEncode(z);
        }
        return null;
    }

    private boolean verifySecret(String str, int i) throws IllegalAccessException {
        if (!this.jsMessageQueue.isBridgeEnabled()) {
            if (i == -1) {
                LOG.d(LOG_TAG, str + " call made before bridge was enabled.");
                return false;
            }
            LOG.d(LOG_TAG, "Ignoring " + str + " from previous page load.");
            return false;
        } else if (this.expectedBridgeSecret < 0 || i != this.expectedBridgeSecret) {
            LOG.e(LOG_TAG, "Bridge access attempt with wrong secret token, possibly from malicious code. Disabling exec() bridge!");
            clearBridgeSecret();
            throw new IllegalAccessException();
        } else {
            return true;
        }
    }

    void clearBridgeSecret() {
        this.expectedBridgeSecret = -1;
    }

    public boolean isSecretEstablished() {
        return this.expectedBridgeSecret != -1;
    }

    int generateBridgeSecret() {
        this.expectedBridgeSecret = new SecureRandom().nextInt(Integer.MAX_VALUE);
        return this.expectedBridgeSecret;
    }

    public void reset() {
        this.jsMessageQueue.reset();
        clearBridgeSecret();
    }

    public String promptOnJsPrompt(String str, String str2, String str3) {
        if (str3 != null && str3.startsWith("gap:")) {
            try {
                JSONArray jSONArray = new JSONArray(str3.substring(4));
                String jsExec = jsExec(jSONArray.getInt(0), jSONArray.getString(1), jSONArray.getString(2), jSONArray.getString(3), str2);
                return jsExec == null ? "" : jsExec;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return "";
            } catch (JSONException e2) {
                e2.printStackTrace();
                return "";
            }
        } else if (str3 != null && str3.startsWith("gap_bridge_mode:")) {
            try {
                jsSetNativeToJsBridgeMode(Integer.parseInt(str3.substring(16)), Integer.parseInt(str2));
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (NumberFormatException e4) {
                e4.printStackTrace();
            }
            return "";
        } else if (str3 != null && str3.startsWith("gap_poll:")) {
            try {
                String jsRetrieveJsMessages = jsRetrieveJsMessages(Integer.parseInt(str3.substring(9)), "1".equals(str2));
                return jsRetrieveJsMessages == null ? "" : jsRetrieveJsMessages;
            } catch (IllegalAccessException e5) {
                e5.printStackTrace();
                return "";
            }
        } else if (str3 == null || !str3.startsWith("gap_init:")) {
            return null;
        } else {
            if (this.pluginManager.shouldAllowBridgeAccess(str)) {
                this.jsMessageQueue.setBridgeMode(Integer.parseInt(str3.substring(9)));
                return "" + generateBridgeSecret();
            }
            LOG.e(LOG_TAG, "gap_init called from restricted origin: " + str);
            return "";
        }
    }
}
