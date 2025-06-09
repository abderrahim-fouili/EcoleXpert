package org.apache.cordova.geolocation;

import android.os.Build;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
public class Geolocation extends CordovaPlugin {
    CallbackContext context;
    String[] permissionsToCheck;
    String[] permissionsToRequest;
    String TAG = "GeolocationPlugin";
    String[] highAccuracyPermissions = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    String[] lowAccuracyPermissions = {"android.permission.ACCESS_COARSE_LOCATION"};

    @Override // org.apache.cordova.CordovaPlugin
    public boolean execute(String str, JSONArray jSONArray, CallbackContext callbackContext) throws JSONException {
        LOG.d(this.TAG, "We are entering execute");
        this.context = callbackContext;
        if (str.equals("getPermission")) {
            this.permissionsToCheck = jSONArray.getBoolean(0) ? this.highAccuracyPermissions : this.lowAccuracyPermissions;
            this.permissionsToRequest = Build.VERSION.SDK_INT <= 31 ? this.highAccuracyPermissions : this.permissionsToCheck;
            if (hasPermisssion(this.permissionsToCheck)) {
                this.context.sendPluginResult(new PluginResult(PluginResult.Status.OK, Build.VERSION.SDK_INT));
                return true;
            }
            PermissionHelper.requestPermissions(this, 0, this.permissionsToRequest);
            return true;
        }
        return false;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onRequestPermissionResult(int i, String[] strArr, int[] iArr) throws JSONException {
        if (this.context != null) {
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = iArr[i2];
                String str = strArr[i2];
                if (i3 == -1 && arrayContains(this.permissionsToCheck, str)) {
                    LOG.d(this.TAG, "Permission Denied!");
                    this.context.sendPluginResult(new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION));
                    return;
                }
            }
            this.context.sendPluginResult(new PluginResult(PluginResult.Status.OK));
        }
    }

    public boolean hasPermisssion(String[] strArr) {
        for (String str : strArr) {
            if (!PermissionHelper.hasPermission(this, str)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void requestPermissions(int i) {
        PermissionHelper.requestPermissions(this, i, this.permissionsToRequest);
    }

    private <T> boolean arrayContains(T[] tArr, T t) {
        if (t == null) {
            for (T t2 : tArr) {
                if (t2 == null) {
                    return true;
                }
            }
        } else {
            for (Object obj : tArr) {
                if (obj == t || t.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }
}
