package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.checkerframework.checker.initialization.qual.NotOnlyInitialized;

/* compiled from: com.google.android.gms:play-services-base@@18.0.1 */
/* loaded from: classes.dex */
public final class zabq<O extends Api.ApiOptions> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, zau {
    final /* synthetic */ GoogleApiManager zaa;
    @NotOnlyInitialized
    private final Api.Client zac;
    private final ApiKey<O> zad;
    private final zaad zae;
    private final int zah;
    private final zact zai;
    private boolean zaj;
    private final Queue<zai> zab = new LinkedList();
    private final Set<zal> zaf = new HashSet();
    private final Map<ListenerHolder.ListenerKey<?>, zaci> zag = new HashMap();
    private final List<zabs> zak = new ArrayList();
    private ConnectionResult zal = null;
    private int zam = 0;

    public zabq(GoogleApiManager googleApiManager, GoogleApi<O> googleApi) {
        Handler handler;
        Context context;
        Handler handler2;
        this.zaa = googleApiManager;
        handler = googleApiManager.zat;
        Api.Client zab = googleApi.zab(handler.getLooper(), this);
        this.zac = zab;
        this.zad = googleApi.getApiKey();
        this.zae = new zaad();
        this.zah = googleApi.zaa();
        if (!zab.requiresSignIn()) {
            this.zai = null;
            return;
        }
        context = googleApiManager.zak;
        handler2 = googleApiManager.zat;
        this.zai = googleApi.zac(context, handler2);
    }

    private final Feature zaB(Feature[] featureArr) {
        if (featureArr != null && featureArr.length != 0) {
            Feature[] availableFeatures = this.zac.getAvailableFeatures();
            if (availableFeatures == null) {
                availableFeatures = new Feature[0];
            }
            ArrayMap arrayMap = new ArrayMap(availableFeatures.length);
            for (Feature feature : availableFeatures) {
                arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
            }
            for (Feature feature2 : featureArr) {
                Long l = (Long) arrayMap.get(feature2.getName());
                if (l == null || l.longValue() < feature2.getVersion()) {
                    return feature2;
                }
            }
        }
        return null;
    }

    private final void zaC(ConnectionResult connectionResult) {
        for (zal zalVar : this.zaf) {
            zalVar.zac(this.zad, connectionResult, Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS) ? this.zac.getEndpointPackageName() : null);
        }
        this.zaf.clear();
    }

    public final void zaD(Status status) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        zaE(status, null, false);
    }

    private final void zaE(Status status, Exception exc, boolean z) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if ((status == null) == (exc == null)) {
            throw new IllegalArgumentException("Status XOR exception should be null");
        }
        Iterator<zai> it = this.zab.iterator();
        while (it.hasNext()) {
            zai next = it.next();
            if (!z || next.zac == 2) {
                if (status != null) {
                    next.zad(status);
                } else {
                    next.zae(exc);
                }
                it.remove();
            }
        }
    }

    private final void zaF() {
        ArrayList arrayList = new ArrayList(this.zab);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            zai zaiVar = (zai) arrayList.get(i);
            if (!this.zac.isConnected()) {
                return;
            }
            if (zaL(zaiVar)) {
                this.zab.remove(zaiVar);
            }
        }
    }

    public final void zaG() {
        zan();
        zaC(ConnectionResult.RESULT_SUCCESS);
        zaK();
        Iterator<zaci> it = this.zag.values().iterator();
        while (it.hasNext()) {
            zaci next = it.next();
            if (zaB(next.zaa.getRequiredFeatures()) != null) {
                it.remove();
            } else {
                try {
                    next.zaa.registerListener(this.zac, new TaskCompletionSource<>());
                } catch (DeadObjectException unused) {
                    onConnectionSuspended(3);
                    this.zac.disconnect("DeadObjectException thrown while calling register listener method.");
                } catch (RemoteException unused2) {
                    it.remove();
                }
            }
        }
        zaF();
        zaI();
    }

    public final void zaH(int i) {
        Handler handler;
        Handler handler2;
        long j;
        Handler handler3;
        Handler handler4;
        long j2;
        com.google.android.gms.common.internal.zal zalVar;
        zan();
        this.zaj = true;
        this.zae.zae(i, this.zac.getLastDisconnectMessage());
        GoogleApiManager googleApiManager = this.zaa;
        handler = googleApiManager.zat;
        handler2 = googleApiManager.zat;
        Message obtain = Message.obtain(handler2, 9, this.zad);
        j = this.zaa.zae;
        handler.sendMessageDelayed(obtain, j);
        GoogleApiManager googleApiManager2 = this.zaa;
        handler3 = googleApiManager2.zat;
        handler4 = googleApiManager2.zat;
        Message obtain2 = Message.obtain(handler4, 11, this.zad);
        j2 = this.zaa.zaf;
        handler3.sendMessageDelayed(obtain2, j2);
        zalVar = this.zaa.zam;
        zalVar.zac();
        for (zaci zaciVar : this.zag.values()) {
            zaciVar.zac.run();
        }
    }

    private final void zaI() {
        Handler handler;
        Handler handler2;
        Handler handler3;
        long j;
        handler = this.zaa.zat;
        handler.removeMessages(12, this.zad);
        GoogleApiManager googleApiManager = this.zaa;
        handler2 = googleApiManager.zat;
        handler3 = googleApiManager.zat;
        Message obtainMessage = handler3.obtainMessage(12, this.zad);
        j = this.zaa.zag;
        handler2.sendMessageDelayed(obtainMessage, j);
    }

    private final void zaJ(zai zaiVar) {
        zaiVar.zag(this.zae, zaz());
        try {
            zaiVar.zaf(this);
        } catch (DeadObjectException unused) {
            onConnectionSuspended(1);
            this.zac.disconnect("DeadObjectException thrown while running ApiCallRunner.");
        }
    }

    private final void zaK() {
        Handler handler;
        Handler handler2;
        if (this.zaj) {
            handler = this.zaa.zat;
            handler.removeMessages(11, this.zad);
            handler2 = this.zaa.zat;
            handler2.removeMessages(9, this.zad);
            this.zaj = false;
        }
    }

    private final boolean zaL(zai zaiVar) {
        boolean z;
        Handler handler;
        Handler handler2;
        long j;
        Handler handler3;
        Handler handler4;
        long j2;
        Handler handler5;
        Handler handler6;
        Handler handler7;
        long j3;
        if (!(zaiVar instanceof zac)) {
            zaJ(zaiVar);
            return true;
        }
        zac zacVar = (zac) zaiVar;
        Feature zaB = zaB(zacVar.zab(this));
        if (zaB == null) {
            zaJ(zaiVar);
            return true;
        }
        String name = this.zac.getClass().getName();
        String name2 = zaB.getName();
        long version = zaB.getVersion();
        StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 77 + String.valueOf(name2).length());
        sb.append(name);
        sb.append(" could not execute call because it requires feature (");
        sb.append(name2);
        sb.append(", ");
        sb.append(version);
        sb.append(").");
        Log.w("GoogleApiManager", sb.toString());
        z = this.zaa.zau;
        if (!z || !zacVar.zaa(this)) {
            zacVar.zae(new UnsupportedApiCallException(zaB));
            return true;
        }
        zabs zabsVar = new zabs(this.zad, zaB, null);
        int indexOf = this.zak.indexOf(zabsVar);
        if (indexOf >= 0) {
            zabs zabsVar2 = this.zak.get(indexOf);
            handler5 = this.zaa.zat;
            handler5.removeMessages(15, zabsVar2);
            GoogleApiManager googleApiManager = this.zaa;
            handler6 = googleApiManager.zat;
            handler7 = googleApiManager.zat;
            Message obtain = Message.obtain(handler7, 15, zabsVar2);
            j3 = this.zaa.zae;
            handler6.sendMessageDelayed(obtain, j3);
            return false;
        }
        this.zak.add(zabsVar);
        GoogleApiManager googleApiManager2 = this.zaa;
        handler = googleApiManager2.zat;
        handler2 = googleApiManager2.zat;
        Message obtain2 = Message.obtain(handler2, 15, zabsVar);
        j = this.zaa.zae;
        handler.sendMessageDelayed(obtain2, j);
        GoogleApiManager googleApiManager3 = this.zaa;
        handler3 = googleApiManager3.zat;
        handler4 = googleApiManager3.zat;
        Message obtain3 = Message.obtain(handler4, 16, zabsVar);
        j2 = this.zaa.zaf;
        handler3.sendMessageDelayed(obtain3, j2);
        ConnectionResult connectionResult = new ConnectionResult(2, null);
        if (zaM(connectionResult)) {
            return false;
        }
        this.zaa.zaG(connectionResult, this.zah);
        return false;
    }

    private final boolean zaM(ConnectionResult connectionResult) {
        Object obj;
        zaae zaaeVar;
        Set set;
        zaae zaaeVar2;
        obj = GoogleApiManager.zac;
        synchronized (obj) {
            GoogleApiManager googleApiManager = this.zaa;
            zaaeVar = googleApiManager.zaq;
            if (zaaeVar != null) {
                set = googleApiManager.zar;
                if (set.contains(this.zad)) {
                    zaaeVar2 = this.zaa.zaq;
                    zaaeVar2.zah(connectionResult, this.zah);
                    return true;
                }
            }
            return false;
        }
    }

    public final boolean zaN(boolean z) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zac.isConnected() && this.zag.size() == 0) {
            if (!this.zae.zag()) {
                this.zac.disconnect("Timing out service connection.");
                return true;
            }
            if (z) {
                zaI();
            }
            return false;
        }
        return false;
    }

    public static /* bridge */ /* synthetic */ ApiKey zag(zabq zabqVar) {
        return zabqVar.zad;
    }

    public static /* bridge */ /* synthetic */ void zai(zabq zabqVar, Status status) {
        zabqVar.zaD(status);
    }

    public static /* bridge */ /* synthetic */ void zal(zabq zabqVar, zabs zabsVar) {
        if (zabqVar.zak.contains(zabsVar) && !zabqVar.zaj) {
            if (zabqVar.zac.isConnected()) {
                zabqVar.zaF();
            } else {
                zabqVar.zao();
            }
        }
    }

    public static /* bridge */ /* synthetic */ void zam(zabq zabqVar, zabs zabsVar) {
        Handler handler;
        Handler handler2;
        Feature feature;
        Feature[] zab;
        if (zabqVar.zak.remove(zabsVar)) {
            handler = zabqVar.zaa.zat;
            handler.removeMessages(15, zabsVar);
            handler2 = zabqVar.zaa.zat;
            handler2.removeMessages(16, zabsVar);
            feature = zabsVar.zab;
            ArrayList arrayList = new ArrayList(zabqVar.zab.size());
            for (zai zaiVar : zabqVar.zab) {
                if ((zaiVar instanceof zac) && (zab = ((zac) zaiVar).zab(zabqVar)) != null && ArrayUtils.contains(zab, feature)) {
                    arrayList.add(zaiVar);
                }
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                zai zaiVar2 = (zai) arrayList.get(i);
                zabqVar.zab.remove(zaiVar2);
                zaiVar2.zae(new UnsupportedApiCallException(feature));
            }
        }
    }

    public static /* bridge */ /* synthetic */ boolean zax(zabq zabqVar, boolean z) {
        return zabqVar.zaN(false);
    }

    @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        Handler handler;
        Handler handler2;
        Looper myLooper = Looper.myLooper();
        handler = this.zaa.zat;
        if (myLooper == handler.getLooper()) {
            zaG();
            return;
        }
        handler2 = this.zaa.zat;
        handler2.post(new zabm(this));
    }

    @Override // com.google.android.gms.common.api.internal.OnConnectionFailedListener
    public final void onConnectionFailed(ConnectionResult connectionResult) {
        zar(connectionResult, null);
    }

    @Override // com.google.android.gms.common.api.internal.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
        Handler handler;
        Handler handler2;
        Looper myLooper = Looper.myLooper();
        handler = this.zaa.zat;
        if (myLooper == handler.getLooper()) {
            zaH(i);
            return;
        }
        handler2 = this.zaa.zat;
        handler2.post(new zabn(this, i));
    }

    public final boolean zaA() {
        return zaN(true);
    }

    @Override // com.google.android.gms.common.api.internal.zau
    public final void zaa(ConnectionResult connectionResult, Api<?> api, boolean z) {
        throw null;
    }

    public final int zab() {
        return this.zah;
    }

    public final int zac() {
        return this.zam;
    }

    public final ConnectionResult zad() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        return this.zal;
    }

    public final Api.Client zaf() {
        return this.zac;
    }

    public final Map<ListenerHolder.ListenerKey<?>, zaci> zah() {
        return this.zag;
    }

    public final void zan() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        this.zal = null;
    }

    public final void zao() {
        Handler handler;
        com.google.android.gms.common.internal.zal zalVar;
        Context context;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zac.isConnected() || this.zac.isConnecting()) {
            return;
        }
        try {
            GoogleApiManager googleApiManager = this.zaa;
            zalVar = googleApiManager.zam;
            context = googleApiManager.zak;
            int zab = zalVar.zab(context, this.zac);
            if (zab == 0) {
                GoogleApiManager googleApiManager2 = this.zaa;
                Api.Client client = this.zac;
                zabu zabuVar = new zabu(googleApiManager2, client, this.zad);
                if (client.requiresSignIn()) {
                    ((zact) Preconditions.checkNotNull(this.zai)).zae(zabuVar);
                }
                try {
                    this.zac.connect(zabuVar);
                    return;
                } catch (SecurityException e) {
                    zar(new ConnectionResult(10), e);
                    return;
                }
            }
            ConnectionResult connectionResult = new ConnectionResult(zab, null);
            String name = this.zac.getClass().getName();
            String obj = connectionResult.toString();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 35 + obj.length());
            sb.append("The service for ");
            sb.append(name);
            sb.append(" is not available: ");
            sb.append(obj);
            Log.w("GoogleApiManager", sb.toString());
            zar(connectionResult, null);
        } catch (IllegalStateException e2) {
            zar(new ConnectionResult(10), e2);
        }
    }

    public final void zap(zai zaiVar) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zac.isConnected()) {
            if (zaL(zaiVar)) {
                zaI();
                return;
            } else {
                this.zab.add(zaiVar);
                return;
            }
        }
        this.zab.add(zaiVar);
        ConnectionResult connectionResult = this.zal;
        if (connectionResult == null || !connectionResult.hasResolution()) {
            zao();
        } else {
            zar(this.zal, null);
        }
    }

    public final void zaq() {
        this.zam++;
    }

    public final void zar(ConnectionResult connectionResult, Exception exc) {
        Handler handler;
        com.google.android.gms.common.internal.zal zalVar;
        boolean z;
        Status zaH;
        Status zaH2;
        Status zaH3;
        Handler handler2;
        Handler handler3;
        long j;
        Handler handler4;
        Status status;
        Handler handler5;
        Handler handler6;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        zact zactVar = this.zai;
        if (zactVar != null) {
            zactVar.zaf();
        }
        zan();
        zalVar = this.zaa.zam;
        zalVar.zac();
        zaC(connectionResult);
        if ((this.zac instanceof com.google.android.gms.common.internal.service.zap) && connectionResult.getErrorCode() != 24) {
            this.zaa.zah = true;
            GoogleApiManager googleApiManager = this.zaa;
            handler5 = googleApiManager.zat;
            handler6 = googleApiManager.zat;
            handler5.sendMessageDelayed(handler6.obtainMessage(19), 300000L);
        }
        if (connectionResult.getErrorCode() == 4) {
            status = GoogleApiManager.zab;
            zaD(status);
        } else if (this.zab.isEmpty()) {
            this.zal = connectionResult;
        } else if (exc != null) {
            handler4 = this.zaa.zat;
            Preconditions.checkHandlerThread(handler4);
            zaE(null, exc, false);
        } else {
            z = this.zaa.zau;
            if (z) {
                zaH2 = GoogleApiManager.zaH(this.zad, connectionResult);
                zaE(zaH2, null, true);
                if (this.zab.isEmpty() || zaM(connectionResult) || this.zaa.zaG(connectionResult, this.zah)) {
                    return;
                }
                if (connectionResult.getErrorCode() == 18) {
                    this.zaj = true;
                }
                if (!this.zaj) {
                    zaH3 = GoogleApiManager.zaH(this.zad, connectionResult);
                    zaD(zaH3);
                    return;
                }
                GoogleApiManager googleApiManager2 = this.zaa;
                handler2 = googleApiManager2.zat;
                handler3 = googleApiManager2.zat;
                Message obtain = Message.obtain(handler3, 9, this.zad);
                j = this.zaa.zae;
                handler2.sendMessageDelayed(obtain, j);
                return;
            }
            zaH = GoogleApiManager.zaH(this.zad, connectionResult);
            zaD(zaH);
        }
    }

    public final void zas(ConnectionResult connectionResult) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        Api.Client client = this.zac;
        String name = client.getClass().getName();
        String valueOf = String.valueOf(connectionResult);
        StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 25 + String.valueOf(valueOf).length());
        sb.append("onSignInFailed for ");
        sb.append(name);
        sb.append(" with ");
        sb.append(valueOf);
        client.disconnect(sb.toString());
        zar(connectionResult, null);
    }

    public final void zat(zal zalVar) {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        this.zaf.add(zalVar);
    }

    public final void zau() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zaj) {
            zao();
        }
    }

    public final void zav() {
        Handler handler;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        zaD(GoogleApiManager.zaa);
        this.zae.zaf();
        for (ListenerHolder.ListenerKey listenerKey : (ListenerHolder.ListenerKey[]) this.zag.keySet().toArray(new ListenerHolder.ListenerKey[0])) {
            zap(new zah(listenerKey, new TaskCompletionSource()));
        }
        zaC(new ConnectionResult(4));
        if (this.zac.isConnected()) {
            this.zac.onUserSignOut(new zabp(this));
        }
    }

    public final void zaw() {
        Handler handler;
        GoogleApiAvailability googleApiAvailability;
        Context context;
        Status status;
        handler = this.zaa.zat;
        Preconditions.checkHandlerThread(handler);
        if (this.zaj) {
            zaK();
            GoogleApiManager googleApiManager = this.zaa;
            googleApiAvailability = googleApiManager.zal;
            context = googleApiManager.zak;
            if (googleApiAvailability.isGooglePlayServicesAvailable(context) == 18) {
                status = new Status(21, "Connection timed out waiting for Google Play services update to complete.");
            } else {
                status = new Status(22, "API failed to connect while resuming due to an unknown error.");
            }
            zaD(status);
            this.zac.disconnect("Timing out connection while resuming.");
        }
    }

    public final boolean zay() {
        return this.zac.isConnected();
    }

    public final boolean zaz() {
        return this.zac.requiresSignIn();
    }
}
