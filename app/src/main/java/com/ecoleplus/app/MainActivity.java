package com.ecoleplus.app;

import android.os.Bundle;
import org.apache.cordova.CordovaActivity;

/* loaded from: classes.dex */
public class MainActivity extends CordovaActivity {
    @Override // org.apache.cordova.CordovaActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        loadUrl(this.launchUrl);
    }
}
