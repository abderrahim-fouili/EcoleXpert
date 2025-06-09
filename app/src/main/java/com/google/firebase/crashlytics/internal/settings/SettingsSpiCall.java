package com.google.firebase.crashlytics.internal.settings;

import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface SettingsSpiCall {
    JSONObject invoke(SettingsRequest settingsRequest, boolean z);
}
