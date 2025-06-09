package org.apache.cordova;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
public class SplashScreenPlugin extends CordovaPlugin {
    private static final boolean DEFAULT_AUTO_HIDE = true;
    private static final int DEFAULT_DELAY_TIME = -1;
    private static final boolean DEFAULT_FADE = true;
    private static final int DEFAULT_FADE_TIME = 500;
    static final String PLUGIN_NAME = "CordovaSplashScreenPlugin";
    private boolean autoHide;
    private int delayTime;
    private int fadeDuration;
    private boolean isFadeEnabled;
    private boolean keepOnScreen = true;

    @Override // org.apache.cordova.CordovaPlugin
    protected void pluginInitialize() {
        this.autoHide = this.preferences.getBoolean("AutoHideSplashScreen", true);
        this.delayTime = this.preferences.getInteger("SplashScreenDelay", -1);
        LOG.d(PLUGIN_NAME, "Auto Hide: " + this.autoHide);
        if (this.delayTime != -1) {
            LOG.d(PLUGIN_NAME, "Delay: " + this.delayTime + "ms");
        }
        this.isFadeEnabled = this.preferences.getBoolean("FadeSplashScreen", true);
        this.fadeDuration = this.preferences.getInteger("FadeSplashScreenDuration", 500);
        LOG.d(PLUGIN_NAME, "Fade: " + this.isFadeEnabled);
        if (this.isFadeEnabled) {
            LOG.d(PLUGIN_NAME, "Fade Duration: " + this.fadeDuration + "ms");
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public boolean execute(String str, JSONArray jSONArray, CallbackContext callbackContext) throws JSONException {
        if (!str.equals("hide") || this.autoHide) {
            return false;
        }
        this.keepOnScreen = false;
        callbackContext.success();
        return true;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public Object onMessage(String str, Object obj) {
        str.hashCode();
        if (str.equals("onPageFinished")) {
            attemptCloseOnPageFinished();
            return null;
        } else if (str.equals("setupSplashScreen")) {
            setupSplashScreen((SplashScreen) obj);
            return null;
        } else {
            return null;
        }
    }

    private void setupSplashScreen(SplashScreen splashScreen) {
        splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() { // from class: org.apache.cordova.SplashScreenPlugin$$ExternalSyntheticLambda0
            {
                SplashScreenPlugin.this = this;
            }

            @Override // androidx.core.splashscreen.SplashScreen.KeepOnScreenCondition
            public final boolean shouldKeepOnScreen() {
                return SplashScreenPlugin.this.m1693lambda$setupSplashScreen$0$orgapachecordovaSplashScreenPlugin();
            }
        });
        if (this.autoHide && this.delayTime != -1) {
            new Handler(this.cordova.getContext().getMainLooper()).postDelayed(new Runnable() { // from class: org.apache.cordova.SplashScreenPlugin$$ExternalSyntheticLambda1
                {
                    SplashScreenPlugin.this = this;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SplashScreenPlugin.this.m1694lambda$setupSplashScreen$1$orgapachecordovaSplashScreenPlugin();
                }
            }, this.delayTime);
        }
        if (this.isFadeEnabled) {
            splashScreen.setOnExitAnimationListener(new SplashScreen.OnExitAnimationListener() { // from class: org.apache.cordova.SplashScreenPlugin.1
                @Override // androidx.core.splashscreen.SplashScreen.OnExitAnimationListener
                public void onSplashScreenExit(final SplashScreenViewProvider splashScreenViewProvider) {
                    splashScreenViewProvider.getView().animate().alpha(0.0f).setDuration(SplashScreenPlugin.this.fadeDuration).setStartDelay(0L).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() { // from class: org.apache.cordova.SplashScreenPlugin.1.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            splashScreenViewProvider.remove();
                        }
                    }).start();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setupSplashScreen$0$org-apache-cordova-SplashScreenPlugin  reason: not valid java name */
    public /* synthetic */ boolean m1693lambda$setupSplashScreen$0$orgapachecordovaSplashScreenPlugin() {
        return this.keepOnScreen;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setupSplashScreen$1$org-apache-cordova-SplashScreenPlugin  reason: not valid java name */
    public /* synthetic */ void m1694lambda$setupSplashScreen$1$orgapachecordovaSplashScreenPlugin() {
        this.keepOnScreen = false;
    }

    private void attemptCloseOnPageFinished() {
        if (this.autoHide && this.delayTime == -1) {
            this.keepOnScreen = false;
        }
    }
}
