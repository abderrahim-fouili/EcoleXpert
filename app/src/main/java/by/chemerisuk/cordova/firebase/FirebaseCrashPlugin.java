package by.chemerisuk.cordova.firebase;

import android.util.Log;
import by.chemerisuk.cordova.support.CordovaMethod;
import by.chemerisuk.cordova.support.ExecutionThread;
import by.chemerisuk.cordova.support.ReflectiveCordovaPlugin;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.json.JSONException;

/* loaded from: classes.dex */
public class FirebaseCrashPlugin extends ReflectiveCordovaPlugin {
    private static final String TAG = "FirebaseCrashPlugin";
    private FirebaseCrashlytics firebaseCrashlytics;

    @Override // org.apache.cordova.CordovaPlugin
    protected void pluginInitialize() {
        Log.d(TAG, "Starting Firebase Crashlytics plugin");
        this.firebaseCrashlytics = FirebaseCrashlytics.getInstance();
    }

    @CordovaMethod(ExecutionThread.WORKER)
    private void log(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        this.firebaseCrashlytics.log(cordovaArgs.getString(0));
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void logError(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        this.firebaseCrashlytics.recordException(new Exception(cordovaArgs.getString(0)));
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void setUserId(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        this.firebaseCrashlytics.setUserId(cordovaArgs.getString(0));
        callbackContext.success();
    }

    @CordovaMethod
    private void setEnabled(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        this.firebaseCrashlytics.setCrashlyticsCollectionEnabled(cordovaArgs.getBoolean(0));
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void setStringValue(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        this.firebaseCrashlytics.setCustomKey(cordovaArgs.getString(0), cordovaArgs.getString(1));
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void setNumberValue(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        String string = cordovaArgs.getString(0);
        Object obj = cordovaArgs.get(1);
        if (obj instanceof Integer) {
            this.firebaseCrashlytics.setCustomKey(string, ((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            this.firebaseCrashlytics.setCustomKey(string, ((Long) obj).longValue());
        } else if (obj instanceof Float) {
            this.firebaseCrashlytics.setCustomKey(string, ((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            this.firebaseCrashlytics.setCustomKey(string, ((Double) obj).doubleValue());
        }
        callbackContext.success();
    }

    @CordovaMethod(ExecutionThread.UI)
    private void setBooleanValue(CordovaArgs cordovaArgs, CallbackContext callbackContext) throws JSONException {
        this.firebaseCrashlytics.setCustomKey(cordovaArgs.getString(0), cordovaArgs.getBoolean(1));
        callbackContext.success();
    }
}
