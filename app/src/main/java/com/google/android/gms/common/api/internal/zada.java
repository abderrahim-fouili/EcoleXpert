package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;

/* compiled from: com.google.android.gms:play-services-base@@18.0.1 */
/* loaded from: classes.dex */
public final class zada<R extends Result> extends TransformedResult<R> implements ResultCallback<R> {
    private final WeakReference<GoogleApiClient> zag;
    private final zacz zah;
    private ResultTransform<? super R, ? extends Result> zaa = null;
    private zada<? extends Result> zab = null;
    private volatile ResultCallbacks<? super R> zac = null;
    private PendingResult<R> zad = null;
    private final Object zae = new Object();
    private Status zaf = null;
    private boolean zai = false;

    public zada(WeakReference<GoogleApiClient> weakReference) {
        Preconditions.checkNotNull(weakReference, "GoogleApiClient reference must not be null");
        this.zag = weakReference;
        GoogleApiClient googleApiClient = weakReference.get();
        this.zah = new zacz(this, googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zaj(Status status) {
        synchronized (this.zae) {
            this.zaf = status;
            zal(status);
        }
    }

    private final void zak() {
        if (this.zaa == null && this.zac == null) {
            return;
        }
        GoogleApiClient googleApiClient = this.zag.get();
        if (!this.zai && this.zaa != null && googleApiClient != null) {
            googleApiClient.zao(this);
            this.zai = true;
        }
        Status status = this.zaf;
        if (status != null) {
            zal(status);
            return;
        }
        PendingResult<R> pendingResult = this.zad;
        if (pendingResult != null) {
            pendingResult.setResultCallback(this);
        }
    }

    private final void zal(Status status) {
        synchronized (this.zae) {
            ResultTransform<? super R, ? extends Result> resultTransform = this.zaa;
            if (resultTransform != null) {
                ((zada) Preconditions.checkNotNull(this.zab)).zaj((Status) Preconditions.checkNotNull(resultTransform.onFailure(status), "onFailure must not return null"));
            } else if (zam()) {
                ((ResultCallbacks) Preconditions.checkNotNull(this.zac)).onFailure(status);
            }
        }
    }

    private final boolean zam() {
        return (this.zac == null || this.zag.get() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void zan(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String valueOf = String.valueOf(result);
                String.valueOf(valueOf).length();
                Log.w("TransformedResultImpl", "Unable to release ".concat(String.valueOf(valueOf)), e);
            }
        }
    }

    @Override // com.google.android.gms.common.api.TransformedResult
    public final void andFinally(ResultCallbacks<? super R> resultCallbacks) {
        synchronized (this.zae) {
            boolean z = true;
            Preconditions.checkState(this.zac == null, "Cannot call andFinally() twice.");
            if (this.zaa != null) {
                z = false;
            }
            Preconditions.checkState(z, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zac = resultCallbacks;
            zak();
        }
    }

    @Override // com.google.android.gms.common.api.ResultCallback
    public final void onResult(R r) {
        synchronized (this.zae) {
            if (!r.getStatus().isSuccess()) {
                zaj(r.getStatus());
                zan(r);
            } else if (this.zaa != null) {
                zaco.zaa().submit(new zacy(this, r));
            } else if (zam()) {
                ((ResultCallbacks) Preconditions.checkNotNull(this.zac)).onSuccess(r);
            }
        }
    }

    @Override // com.google.android.gms.common.api.TransformedResult
    public final <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> resultTransform) {
        zada<? extends Result> zadaVar;
        synchronized (this.zae) {
            boolean z = true;
            Preconditions.checkState(this.zaa == null, "Cannot call then() twice.");
            if (this.zac != null) {
                z = false;
            }
            Preconditions.checkState(z, "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zaa = resultTransform;
            zadaVar = new zada<>(this.zag);
            this.zab = zadaVar;
            zak();
        }
        return zadaVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zah() {
        this.zac = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void zai(PendingResult<?> pendingResult) {
        synchronized (this.zae) {
            this.zad = pendingResult;
            zak();
        }
    }
}
