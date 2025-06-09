package com.google.android.gms.cloudmessaging;

import android.os.Bundle;
import com.google.firebase.messaging.Constants;

/* compiled from: com.google.android.gms:play-services-cloud-messaging@@17.0.0 */
/* loaded from: classes.dex */
final class zzr extends zzp<Bundle> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzr(int i, int i2, Bundle bundle) {
        super(i, 1, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.cloudmessaging.zzp
    public final void zza(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle(Constants.ScionAnalytics.MessageType.DATA_MESSAGE);
        if (bundle2 == null) {
            bundle2 = Bundle.EMPTY;
        }
        zzd(bundle2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.cloudmessaging.zzp
    public final boolean zzb() {
        return false;
    }
}
