package by.chemerisuk.cordova.support;

import android.util.Pair;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;

/* loaded from: classes.dex */
public class ReflectiveCordovaPlugin extends CordovaPlugin {
    private static final String TAG = "ReflectiveCordovaPlugin";
    private Map<String, Pair<Method, ExecutionThread>> commandFactories;

    @Override // org.apache.cordova.CordovaPlugin
    public final void initialize(CordovaInterface cordovaInterface, CordovaWebView cordovaWebView) {
        Method[] declaredMethods;
        this.commandFactories = new HashMap();
        for (Method method : getClass().getDeclaredMethods()) {
            CordovaMethod cordovaMethod = (CordovaMethod) method.getAnnotation(CordovaMethod.class);
            if (cordovaMethod != null) {
                String action = cordovaMethod.action();
                if (action.isEmpty()) {
                    action = method.getName();
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    if (!CallbackContext.class.equals(parameterTypes[0])) {
                        throw new RuntimeException("Cordova method " + action + " does not have valid parameters");
                    }
                    this.commandFactories.put(action, new Pair<>(method, cordovaMethod.value()));
                    method.setAccessible(true);
                } else {
                    if (parameterTypes.length == 2) {
                        if (CordovaArgs.class.equals(parameterTypes[0])) {
                            if (!CallbackContext.class.equals(parameterTypes[1])) {
                            }
                            this.commandFactories.put(action, new Pair<>(method, cordovaMethod.value()));
                            method.setAccessible(true);
                        }
                    }
                    throw new RuntimeException("Cordova method " + action + " does not have valid parameters");
                }
            }
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public final boolean execute(String str, CordovaArgs cordovaArgs, CallbackContext callbackContext) {
        Pair<Method, ExecutionThread> pair = this.commandFactories.get(str);
        if (pair != null) {
            Runnable createCommand = createCommand((Method) pair.first, cordovaArgs, callbackContext);
            ExecutionThread executionThread = (ExecutionThread) pair.second;
            if (executionThread == ExecutionThread.WORKER) {
                this.cordova.getThreadPool().execute(createCommand);
                return true;
            } else if (executionThread == ExecutionThread.UI) {
                this.cordova.getActivity().runOnUiThread(createCommand);
                return true;
            } else {
                createCommand.run();
                return true;
            }
        }
        return false;
    }

    private Runnable createCommand(final Method method, final CordovaArgs cordovaArgs, final CallbackContext callbackContext) {
        return new Runnable() { // from class: by.chemerisuk.cordova.support.ReflectiveCordovaPlugin.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (CordovaArgs.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        method.invoke(ReflectiveCordovaPlugin.this, cordovaArgs, callbackContext);
                    } else {
                        method.invoke(ReflectiveCordovaPlugin.this, callbackContext);
                    }
                } catch (Throwable th) {
                    th = th;
                    if (th instanceof InvocationTargetException) {
                        th = ((InvocationTargetException) th).getTargetException();
                    }
                    LOG.e(ReflectiveCordovaPlugin.TAG, "Uncaught exception at " + getClass().getSimpleName() + "#" + method.getName(), th);
                    callbackContext.error(th.getMessage());
                }
            }
        };
    }
}
