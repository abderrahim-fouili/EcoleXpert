package com.google.firebase.sessions;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import com.google.firebase.FirebaseApp;
import com.google.firebase.encoders.DataEncoder;
import com.google.firebase.encoders.json.JsonDataEncoderBuilder;
import com.google.firebase.sessions.api.SessionSubscriber;
import com.google.firebase.sessions.settings.SessionsSettings;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SessionEvents.kt */
@Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ4\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0014\b\u0002\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00140\u0012J\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0014H\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0018"}, d2 = {"Lcom/google/firebase/sessions/SessionEvents;", "", "()V", "SESSION_EVENT_ENCODER", "Lcom/google/firebase/encoders/DataEncoder;", "getSESSION_EVENT_ENCODER$com_google_firebase_firebase_sessions", "()Lcom/google/firebase/encoders/DataEncoder;", "getApplicationInfo", "Lcom/google/firebase/sessions/ApplicationInfo;", "firebaseApp", "Lcom/google/firebase/FirebaseApp;", "startSession", "Lcom/google/firebase/sessions/SessionEvent;", "sessionDetails", "Lcom/google/firebase/sessions/SessionDetails;", "sessionsSettings", "Lcom/google/firebase/sessions/settings/SessionsSettings;", "subscribers", "", "Lcom/google/firebase/sessions/api/SessionSubscriber$Name;", "Lcom/google/firebase/sessions/api/SessionSubscriber;", "toDataCollectionState", "Lcom/google/firebase/sessions/DataCollectionState;", "subscriber", "com.google.firebase-firebase-sessions"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* loaded from: classes.dex */
public final class SessionEvents {
    public static final SessionEvents INSTANCE = new SessionEvents();
    private static final DataEncoder SESSION_EVENT_ENCODER;

    private SessionEvents() {
    }

    public final DataEncoder getSESSION_EVENT_ENCODER$com_google_firebase_firebase_sessions() {
        return SESSION_EVENT_ENCODER;
    }

    static {
        DataEncoder build = new JsonDataEncoderBuilder().configureWith(AutoSessionEventEncoder.CONFIG).ignoreNullValues(true).build();
        Intrinsics.checkNotNullExpressionValue(build, "JsonDataEncoderBuilder()…lues(true)\n      .build()");
        SESSION_EVENT_ENCODER = build;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ SessionEvent startSession$default(SessionEvents sessionEvents, FirebaseApp firebaseApp, SessionDetails sessionDetails, SessionsSettings sessionsSettings, Map map, int i, Object obj) {
        if ((i & 8) != 0) {
            map = MapsKt.emptyMap();
        }
        return sessionEvents.startSession(firebaseApp, sessionDetails, sessionsSettings, map);
    }

    public final SessionEvent startSession(FirebaseApp firebaseApp, SessionDetails sessionDetails, SessionsSettings sessionsSettings, Map<SessionSubscriber.Name, ? extends SessionSubscriber> subscribers) {
        Intrinsics.checkNotNullParameter(firebaseApp, "firebaseApp");
        Intrinsics.checkNotNullParameter(sessionDetails, "sessionDetails");
        Intrinsics.checkNotNullParameter(sessionsSettings, "sessionsSettings");
        Intrinsics.checkNotNullParameter(subscribers, "subscribers");
        return new SessionEvent(EventType.SESSION_START, new SessionInfo(sessionDetails.getSessionId(), sessionDetails.getFirstSessionId(), sessionDetails.getSessionIndex(), sessionDetails.getSessionStartTimestampUs(), new DataCollectionStatus(toDataCollectionState(subscribers.get(SessionSubscriber.Name.PERFORMANCE)), toDataCollectionState(subscribers.get(SessionSubscriber.Name.CRASHLYTICS)), sessionsSettings.getSamplingRate()), null, 32, null), getApplicationInfo(firebaseApp));
    }

    public final ApplicationInfo getApplicationInfo(FirebaseApp firebaseApp) {
        String valueOf;
        Intrinsics.checkNotNullParameter(firebaseApp, "firebaseApp");
        Context applicationContext = firebaseApp.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "firebaseApp.applicationContext");
        String packageName = applicationContext.getPackageName();
        PackageInfo packageInfo = applicationContext.getPackageManager().getPackageInfo(packageName, 0);
        if (Build.VERSION.SDK_INT >= 28) {
            valueOf = String.valueOf(packageInfo.getLongVersionCode());
        } else {
            valueOf = String.valueOf(packageInfo.versionCode);
        }
        String applicationId = firebaseApp.getOptions().getApplicationId();
        Intrinsics.checkNotNullExpressionValue(applicationId, "firebaseApp.options.applicationId");
        String MODEL = Build.MODEL;
        Intrinsics.checkNotNullExpressionValue(MODEL, "MODEL");
        String RELEASE = Build.VERSION.RELEASE;
        Intrinsics.checkNotNullExpressionValue(RELEASE, "RELEASE");
        LogEnvironment logEnvironment = LogEnvironment.LOG_ENVIRONMENT_PROD;
        Intrinsics.checkNotNullExpressionValue(packageName, "packageName");
        String str = packageInfo.versionName;
        if (str == null) {
            str = valueOf;
        }
        String MANUFACTURER = Build.MANUFACTURER;
        Intrinsics.checkNotNullExpressionValue(MANUFACTURER, "MANUFACTURER");
        return new ApplicationInfo(applicationId, MODEL, BuildConfig.VERSION_NAME, RELEASE, logEnvironment, new AndroidApplicationInfo(packageName, str, valueOf, MANUFACTURER));
    }

    private final DataCollectionState toDataCollectionState(SessionSubscriber sessionSubscriber) {
        if (sessionSubscriber == null) {
            return DataCollectionState.COLLECTION_SDK_NOT_INSTALLED;
        }
        if (sessionSubscriber.isDataCollectionEnabled()) {
            return DataCollectionState.COLLECTION_ENABLED;
        }
        return DataCollectionState.COLLECTION_DISABLED;
    }
}
