package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import androidx.core.view.MotionEventCompat;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.common.zzc;

/* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
/* loaded from: classes.dex */
public interface IFragmentWrapper extends IInterface {

    /* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
    /* loaded from: classes.dex */
    public static abstract class Stub extends com.google.android.gms.internal.common.zzb implements IFragmentWrapper {
        public Stub() {
            super("com.google.android.gms.dynamic.IFragmentWrapper");
        }

        public static IFragmentWrapper asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            return queryLocalInterface instanceof IFragmentWrapper ? (IFragmentWrapper) queryLocalInterface : new zza(iBinder);
        }

        @Override // com.google.android.gms.internal.common.zzb
        protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 2:
                    IObjectWrapper zzg = zzg();
                    parcel2.writeNoException();
                    zzc.zzf(parcel2, zzg);
                    return true;
                case 3:
                    Bundle zzd = zzd();
                    parcel2.writeNoException();
                    zzc.zze(parcel2, zzd);
                    return true;
                case 4:
                    int zzb = zzb();
                    parcel2.writeNoException();
                    parcel2.writeInt(zzb);
                    return true;
                case 5:
                    IFragmentWrapper zze = zze();
                    parcel2.writeNoException();
                    zzc.zzf(parcel2, zze);
                    return true;
                case 6:
                    IObjectWrapper zzh = zzh();
                    parcel2.writeNoException();
                    zzc.zzf(parcel2, zzh);
                    return true;
                case 7:
                    boolean zzs = zzs();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzs);
                    return true;
                case 8:
                    String zzj = zzj();
                    parcel2.writeNoException();
                    parcel2.writeString(zzj);
                    return true;
                case 9:
                    IFragmentWrapper zzf = zzf();
                    parcel2.writeNoException();
                    zzc.zzf(parcel2, zzf);
                    return true;
                case 10:
                    int zzc = zzc();
                    parcel2.writeNoException();
                    parcel2.writeInt(zzc);
                    return true;
                case 11:
                    boolean zzt = zzt();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzt);
                    return true;
                case 12:
                    IObjectWrapper zzi = zzi();
                    parcel2.writeNoException();
                    zzc.zzf(parcel2, zzi);
                    return true;
                case 13:
                    boolean zzu = zzu();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzu);
                    return true;
                case 14:
                    boolean zzv = zzv();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzv);
                    return true;
                case 15:
                    boolean zzw = zzw();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzw);
                    return true;
                case 16:
                    boolean zzx = zzx();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzx);
                    return true;
                case 17:
                    boolean zzy = zzy();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzy);
                    return true;
                case 18:
                    boolean zzz = zzz();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzz);
                    return true;
                case 19:
                    boolean zzA = zzA();
                    parcel2.writeNoException();
                    zzc.zzc(parcel2, zzA);
                    return true;
                case 20:
                    IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(parcel.readStrongBinder());
                    zzc.zzb(parcel);
                    zzk(asInterface);
                    parcel2.writeNoException();
                    return true;
                case 21:
                    boolean zzg2 = zzc.zzg(parcel);
                    zzc.zzb(parcel);
                    zzl(zzg2);
                    parcel2.writeNoException();
                    return true;
                case 22:
                    boolean zzg3 = zzc.zzg(parcel);
                    zzc.zzb(parcel);
                    zzm(zzg3);
                    parcel2.writeNoException();
                    return true;
                case 23:
                    boolean zzg4 = zzc.zzg(parcel);
                    zzc.zzb(parcel);
                    zzn(zzg4);
                    parcel2.writeNoException();
                    return true;
                case 24:
                    boolean zzg5 = zzc.zzg(parcel);
                    zzc.zzb(parcel);
                    zzo(zzg5);
                    parcel2.writeNoException();
                    return true;
                case 25:
                    zzc.zzb(parcel);
                    zzp((Intent) zzc.zza(parcel, Intent.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case 26:
                    int readInt = parcel.readInt();
                    zzc.zzb(parcel);
                    zzq((Intent) zzc.zza(parcel, Intent.CREATOR), readInt);
                    parcel2.writeNoException();
                    return true;
                case MotionEventCompat.AXIS_RELATIVE_X /* 27 */:
                    IObjectWrapper asInterface2 = IObjectWrapper.Stub.asInterface(parcel.readStrongBinder());
                    zzc.zzb(parcel);
                    zzr(asInterface2);
                    parcel2.writeNoException();
                    return true;
                default:
                    return false;
            }
        }
    }

    boolean zzA() throws RemoteException;

    int zzb() throws RemoteException;

    int zzc() throws RemoteException;

    Bundle zzd() throws RemoteException;

    IFragmentWrapper zze() throws RemoteException;

    IFragmentWrapper zzf() throws RemoteException;

    IObjectWrapper zzg() throws RemoteException;

    IObjectWrapper zzh() throws RemoteException;

    IObjectWrapper zzi() throws RemoteException;

    String zzj() throws RemoteException;

    void zzk(IObjectWrapper iObjectWrapper) throws RemoteException;

    void zzl(boolean z) throws RemoteException;

    void zzm(boolean z) throws RemoteException;

    void zzn(boolean z) throws RemoteException;

    void zzo(boolean z) throws RemoteException;

    void zzp(Intent intent) throws RemoteException;

    void zzq(Intent intent, int i) throws RemoteException;

    void zzr(IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean zzs() throws RemoteException;

    boolean zzt() throws RemoteException;

    boolean zzu() throws RemoteException;

    boolean zzv() throws RemoteException;

    boolean zzw() throws RemoteException;

    boolean zzx() throws RemoteException;

    boolean zzy() throws RemoteException;

    boolean zzz() throws RemoteException;
}
