package com.google.firebase.sessions;

import com.google.firebase.installations.FirebaseInstallationsApi;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SessionCoordinator.kt */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lcom/google/firebase/sessions/SessionCoordinator;", "", "firebaseInstallations", "Lcom/google/firebase/installations/FirebaseInstallationsApi;", "eventGDTLogger", "Lcom/google/firebase/sessions/EventGDTLoggerInterface;", "(Lcom/google/firebase/installations/FirebaseInstallationsApi;Lcom/google/firebase/sessions/EventGDTLoggerInterface;)V", "attemptLoggingSessionEvent", "", "sessionEvent", "Lcom/google/firebase/sessions/SessionEvent;", "(Lcom/google/firebase/sessions/SessionEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "com.google.firebase-firebase-sessions"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* loaded from: classes.dex */
public final class SessionCoordinator {
    public static final Companion Companion = new Companion(null);
    private static final String TAG = "SessionCoordinator";
    private final EventGDTLoggerInterface eventGDTLogger;
    private final FirebaseInstallationsApi firebaseInstallations;

    public SessionCoordinator(FirebaseInstallationsApi firebaseInstallations, EventGDTLoggerInterface eventGDTLogger) {
        Intrinsics.checkNotNullParameter(firebaseInstallations, "firebaseInstallations");
        Intrinsics.checkNotNullParameter(eventGDTLogger, "eventGDTLogger");
        this.firebaseInstallations = firebaseInstallations;
        this.eventGDTLogger = eventGDTLogger;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:1|(2:3|(9:5|6|(1:(3:9|10|11)(2:25|26))(4:27|28|29|(1:31)(1:32))|12|13|14|15|16|17))|36|6|(0)(0)|12|13|14|15|16|17|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b9, code lost:
        r8 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00ba, code lost:
        android.util.Log.e(com.google.firebase.sessions.SessionCoordinator.TAG, "Error logging Session Start event to DataTransport: ", r8);
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.Object attemptLoggingSessionEvent(com.google.firebase.sessions.SessionEvent r8, kotlin.coroutines.Continuation<? super kotlin.Unit> r9) {
        /*
            r7 = this;
            java.lang.String r0 = "Successfully logged Session Start event: "
            boolean r1 = r9 instanceof com.google.firebase.sessions.SessionCoordinator$attemptLoggingSessionEvent$1
            if (r1 == 0) goto L16
            r1 = r9
            com.google.firebase.sessions.SessionCoordinator$attemptLoggingSessionEvent$1 r1 = (com.google.firebase.sessions.SessionCoordinator$attemptLoggingSessionEvent$1) r1
            int r2 = r1.label
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            r2 = r2 & r3
            if (r2 == 0) goto L16
            int r9 = r1.label
            int r9 = r9 - r3
            r1.label = r9
            goto L1b
        L16:
            com.google.firebase.sessions.SessionCoordinator$attemptLoggingSessionEvent$1 r1 = new com.google.firebase.sessions.SessionCoordinator$attemptLoggingSessionEvent$1
            r1.<init>(r7, r9)
        L1b:
            java.lang.Object r9 = r1.result
            java.lang.Object r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r3 = r1.label
            r4 = 1
            java.lang.String r5 = "SessionCoordinator"
            if (r3 == 0) goto L48
            if (r3 != r4) goto L40
            java.lang.Object r8 = r1.L$3
            com.google.firebase.sessions.SessionInfo r8 = (com.google.firebase.sessions.SessionInfo) r8
            java.lang.Object r2 = r1.L$2
            com.google.firebase.sessions.SessionInfo r2 = (com.google.firebase.sessions.SessionInfo) r2
            java.lang.Object r3 = r1.L$1
            com.google.firebase.sessions.SessionEvent r3 = (com.google.firebase.sessions.SessionEvent) r3
            java.lang.Object r1 = r1.L$0
            com.google.firebase.sessions.SessionCoordinator r1 = (com.google.firebase.sessions.SessionCoordinator) r1
            kotlin.ResultKt.throwOnFailure(r9)     // Catch: java.lang.Exception -> L3e
            goto L70
        L3e:
            r8 = move-exception
            goto L7d
        L40:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r9)
            throw r8
        L48:
            kotlin.ResultKt.throwOnFailure(r9)
            com.google.firebase.sessions.SessionInfo r9 = r8.getSessionData()
            com.google.firebase.installations.FirebaseInstallationsApi r3 = r7.firebaseInstallations     // Catch: java.lang.Exception -> L78
            com.google.android.gms.tasks.Task r3 = r3.getId()     // Catch: java.lang.Exception -> L78
            java.lang.String r6 = "firebaseInstallations.id"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r6)     // Catch: java.lang.Exception -> L78
            r1.L$0 = r7     // Catch: java.lang.Exception -> L78
            r1.L$1 = r8     // Catch: java.lang.Exception -> L78
            r1.L$2 = r9     // Catch: java.lang.Exception -> L78
            r1.L$3 = r9     // Catch: java.lang.Exception -> L78
            r1.label = r4     // Catch: java.lang.Exception -> L78
            java.lang.Object r1 = kotlinx.coroutines.tasks.TasksKt.await(r3, r1)     // Catch: java.lang.Exception -> L78
            if (r1 != r2) goto L6b
            return r2
        L6b:
            r3 = r8
            r8 = r9
            r2 = r8
            r9 = r1
            r1 = r7
        L70:
            java.lang.String r4 = "{\n        firebaseInstallations.id.await()\n      }"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r4)     // Catch: java.lang.Exception -> L3e
            java.lang.String r9 = (java.lang.String) r9     // Catch: java.lang.Exception -> L3e
            goto L98
        L78:
            r1 = move-exception
            r3 = r8
            r2 = r9
            r8 = r1
            r1 = r7
        L7d:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r4 = "Error getting Firebase Installation ID: "
            r9.<init>(r4)
            java.lang.StringBuilder r8 = r9.append(r8)
            java.lang.String r9 = ". Using an empty ID"
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r8 = r8.toString()
            android.util.Log.e(r5, r8)
            java.lang.String r9 = ""
            r8 = r2
        L98:
            r8.setFirebaseInstallationId(r9)
            com.google.firebase.sessions.EventGDTLoggerInterface r8 = r1.eventGDTLogger     // Catch: java.lang.RuntimeException -> Lb9
            r8.log(r3)     // Catch: java.lang.RuntimeException -> Lb9
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.RuntimeException -> Lb9
            r8.<init>(r0)     // Catch: java.lang.RuntimeException -> Lb9
            com.google.firebase.sessions.SessionInfo r9 = r3.getSessionData()     // Catch: java.lang.RuntimeException -> Lb9
            java.lang.String r9 = r9.getSessionId()     // Catch: java.lang.RuntimeException -> Lb9
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch: java.lang.RuntimeException -> Lb9
            java.lang.String r8 = r8.toString()     // Catch: java.lang.RuntimeException -> Lb9
            android.util.Log.i(r5, r8)     // Catch: java.lang.RuntimeException -> Lb9
            goto Lc1
        Lb9:
            r8 = move-exception
            java.lang.String r9 = "Error logging Session Start event to DataTransport: "
            java.lang.Throwable r8 = (java.lang.Throwable) r8
            android.util.Log.e(r5, r9, r8)
        Lc1:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.sessions.SessionCoordinator.attemptLoggingSessionEvent(com.google.firebase.sessions.SessionEvent, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* compiled from: SessionCoordinator.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/google/firebase/sessions/SessionCoordinator$Companion;", "", "()V", "TAG", "", "com.google.firebase-firebase-sessions"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
