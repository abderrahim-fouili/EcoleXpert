package org.apache.cordova.splashscreen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.core.view.ViewCompat;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
public class SplashScreen extends CordovaPlugin {
    private static final int DEFAULT_FADE_DURATION = 500;
    private static final int DEFAULT_SPLASHSCREEN_DURATION = 3000;
    private static final boolean HAS_BUILT_IN_SPLASH_SCREEN;
    private static final String LOG_TAG = "SplashScreen";
    private static boolean firstShow;
    private static boolean lastHideAfterDelay;
    private static ProgressDialog spinnerDialog;
    private static Dialog splashDialog;
    private int orientation;
    private ImageView splashImageView;

    static {
        HAS_BUILT_IN_SPLASH_SCREEN = Integer.valueOf(CordovaWebView.CORDOVA_VERSION.split("\\.")[0]).intValue() < 4;
        firstShow = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getView() {
        try {
            return (View) this.webView.getClass().getMethod("getView", new Class[0]).invoke(this.webView, new Object[0]);
        } catch (Exception unused) {
            return (View) this.webView;
        }
    }

    private int getSplashId() {
        String string = this.preferences.getString(LOG_TAG, "screen");
        if (string != null) {
            int identifier = this.cordova.getActivity().getResources().getIdentifier(string, "drawable", this.cordova.getActivity().getClass().getPackage().getName());
            return identifier == 0 ? this.cordova.getActivity().getResources().getIdentifier(string, "drawable", this.cordova.getActivity().getPackageName()) : identifier;
        }
        return 0;
    }

    @Override // org.apache.cordova.CordovaPlugin
    protected void pluginInitialize() {
        if (HAS_BUILT_IN_SPLASH_SCREEN) {
            return;
        }
        this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.1
            @Override // java.lang.Runnable
            public void run() {
                SplashScreen.this.getView().setVisibility(4);
            }
        });
        getSplashId();
        this.orientation = this.cordova.getActivity().getResources().getConfiguration().orientation;
        if (firstShow) {
            showSplashScreen(this.preferences.getBoolean("AutoHideSplashScreen", true));
        }
        if (this.preferences.getBoolean("SplashShowOnlyFirstTime", true)) {
            firstShow = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMaintainAspectRatio() {
        return this.preferences.getBoolean("SplashMaintainAspectRatio", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getFadeDuration() {
        int integer = this.preferences.getBoolean("FadeSplashScreen", true) ? this.preferences.getInteger("FadeSplashScreenDuration", 500) : 0;
        return integer < 30 ? integer * 1000 : integer;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onPause(boolean z) {
        if (HAS_BUILT_IN_SPLASH_SCREEN) {
            return;
        }
        removeSplashScreen(true);
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onDestroy() {
        if (HAS_BUILT_IN_SPLASH_SCREEN) {
            return;
        }
        removeSplashScreen(true);
    }

    @Override // org.apache.cordova.CordovaPlugin
    public boolean execute(String str, JSONArray jSONArray, CallbackContext callbackContext) throws JSONException {
        if (str.equals("hide")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.2
                @Override // java.lang.Runnable
                public void run() {
                    SplashScreen.this.webView.postMessage("splashscreen", "hide");
                }
            });
        } else if (!str.equals("show")) {
            return false;
        } else {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.3
                @Override // java.lang.Runnable
                public void run() {
                    SplashScreen.this.webView.postMessage("splashscreen", "show");
                }
            });
        }
        callbackContext.success();
        return true;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public Object onMessage(String str, Object obj) {
        if (HAS_BUILT_IN_SPLASH_SCREEN) {
            return null;
        }
        if ("splashscreen".equals(str)) {
            if ("hide".equals(obj.toString())) {
                removeSplashScreen(false);
            } else {
                showSplashScreen(false);
            }
        } else if ("spinner".equals(str)) {
            if ("stop".equals(obj.toString())) {
                getView().setVisibility(0);
                getView().requestFocus();
            }
        } else if ("onReceivedError".equals(str)) {
            spinnerStop();
        }
        return null;
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onConfigurationChanged(Configuration configuration) {
        int splashId;
        if (configuration.orientation != this.orientation) {
            this.orientation = configuration.orientation;
            if (this.splashImageView == null || (splashId = getSplashId()) == 0) {
                return;
            }
            this.splashImageView.setImageDrawable(this.cordova.getActivity().getResources().getDrawable(splashId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSplashScreen(final boolean z) {
        this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.4
            @Override // java.lang.Runnable
            public void run() {
                if (SplashScreen.splashDialog == null || SplashScreen.this.splashImageView == null || !SplashScreen.splashDialog.isShowing()) {
                    return;
                }
                int fadeDuration = SplashScreen.this.getFadeDuration();
                if (fadeDuration <= 0 || z) {
                    SplashScreen.this.spinnerStop();
                    SplashScreen.splashDialog.dismiss();
                    Dialog unused = SplashScreen.splashDialog = null;
                    SplashScreen.this.splashImageView = null;
                    return;
                }
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setInterpolator(new DecelerateInterpolator());
                alphaAnimation.setDuration(fadeDuration);
                SplashScreen.this.splashImageView.setAnimation(alphaAnimation);
                SplashScreen.this.splashImageView.startAnimation(alphaAnimation);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: org.apache.cordova.splashscreen.SplashScreen.4.1
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                        SplashScreen.this.spinnerStop();
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        if (SplashScreen.splashDialog == null || SplashScreen.this.splashImageView == null || !SplashScreen.splashDialog.isShowing()) {
                            return;
                        }
                        SplashScreen.splashDialog.dismiss();
                        Dialog unused2 = SplashScreen.splashDialog = null;
                        SplashScreen.this.splashImageView = null;
                    }
                });
            }
        });
    }

    private void showSplashScreen(final boolean z) {
        int integer = this.preferences.getInteger("SplashScreenDelay", 3000);
        final int splashId = getSplashId();
        final int max = Math.max(0, integer - getFadeDuration());
        lastHideAfterDelay = z;
        if (this.cordova.getActivity().isFinishing()) {
            return;
        }
        Dialog dialog = splashDialog;
        if ((dialog == null || !dialog.isShowing()) && splashId != 0) {
            if (integer > 0 || !z) {
                this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.5
                    @Override // java.lang.Runnable
                    public void run() {
                        Display defaultDisplay = SplashScreen.this.cordova.getActivity().getWindowManager().getDefaultDisplay();
                        Context context = SplashScreen.this.webView.getContext();
                        SplashScreen.this.splashImageView = new ImageView(context);
                        SplashScreen.this.splashImageView.setImageResource(splashId);
                        SplashScreen.this.splashImageView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                        SplashScreen.this.splashImageView.setMinimumHeight(defaultDisplay.getHeight());
                        SplashScreen.this.splashImageView.setMinimumWidth(defaultDisplay.getWidth());
                        SplashScreen.this.splashImageView.setBackgroundColor(SplashScreen.this.preferences.getInteger("backgroundColor", ViewCompat.MEASURED_STATE_MASK));
                        if (SplashScreen.this.isMaintainAspectRatio()) {
                            SplashScreen.this.splashImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } else {
                            SplashScreen.this.splashImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        Dialog unused = SplashScreen.splashDialog = new Dialog(context, 16973840);
                        int i = SplashScreen.this.cordova.getActivity().getWindow().getAttributes().flags;
                        boolean z2 = (i & 1024) == 1024;
                        boolean z3 = (i & 2048) == 2048;
                        if (!z2 && !z3) {
                            z2 = SplashScreen.this.preferences.getBoolean("Fullscreen", false);
                        }
                        Window window = SplashScreen.splashDialog.getWindow();
                        if (z2) {
                            window.setFlags(1024, 1024);
                        }
                        SplashScreen.splashDialog.setContentView(SplashScreen.this.splashImageView);
                        SplashScreen.splashDialog.setCancelable(false);
                        SplashScreen.splashDialog.show();
                        if (z2) {
                            window.getDecorView().setSystemUiVisibility(5894);
                        }
                        if (SplashScreen.this.preferences.getBoolean("ShowSplashScreenSpinner", true)) {
                            SplashScreen.this.spinnerStart();
                        }
                        if (z) {
                            new Handler().postDelayed(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.5.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (SplashScreen.lastHideAfterDelay) {
                                        SplashScreen.this.removeSplashScreen(false);
                                    }
                                }
                            }, max);
                        }
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void spinnerStart() {
        this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.6
            @Override // java.lang.Runnable
            public void run() {
                SplashScreen.this.spinnerStop();
                ProgressDialog unused = SplashScreen.spinnerDialog = new ProgressDialog(SplashScreen.this.webView.getContext());
                SplashScreen.spinnerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.apache.cordova.splashscreen.SplashScreen.6.1
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        ProgressDialog unused2 = SplashScreen.spinnerDialog = null;
                    }
                });
                SplashScreen.spinnerDialog.setCancelable(false);
                SplashScreen.spinnerDialog.setIndeterminate(true);
                RelativeLayout relativeLayout = new RelativeLayout(SplashScreen.this.cordova.getActivity());
                relativeLayout.setGravity(17);
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
                ProgressBar progressBar = new ProgressBar(SplashScreen.this.webView.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13, -1);
                progressBar.setLayoutParams(layoutParams);
                String string = SplashScreen.this.preferences.getString("SplashScreenSpinnerColor", null);
                if (string != null) {
                    int parseColor = Color.parseColor(string);
                    progressBar.setIndeterminateTintList(new ColorStateList(new int[][]{new int[]{16842910}, new int[]{-16842910}, new int[]{-16842912}, new int[]{16842919}}, new int[]{parseColor, parseColor, parseColor, parseColor}));
                }
                relativeLayout.addView(progressBar);
                SplashScreen.spinnerDialog.getWindow().clearFlags(2);
                SplashScreen.spinnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                SplashScreen.spinnerDialog.show();
                SplashScreen.spinnerDialog.setContentView(relativeLayout);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void spinnerStop() {
        this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: org.apache.cordova.splashscreen.SplashScreen.7
            @Override // java.lang.Runnable
            public void run() {
                if (SplashScreen.spinnerDialog == null || !SplashScreen.spinnerDialog.isShowing()) {
                    return;
                }
                SplashScreen.spinnerDialog.dismiss();
                ProgressDialog unused = SplashScreen.spinnerDialog = null;
            }
        });
    }
}
