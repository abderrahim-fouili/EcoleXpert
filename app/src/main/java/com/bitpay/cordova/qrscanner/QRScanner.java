package com.bitpay.cordova.qrscanner;

import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.app.ActivityCompat;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class QRScanner extends CordovaPlugin implements BarcodeCallback {
    private static Boolean flashAvailable;
    private boolean authorized;
    private CallbackContext callbackContext;
    private boolean cameraClosing;
    private boolean cameraPreviewing;
    private boolean denied;
    private BarcodeView mBarcodeView;
    private CallbackContext nextScanCallback;
    private boolean restricted;
    private boolean shouldScanAgain;
    private boolean lightOn = false;
    private boolean showing = false;
    private boolean prepared = false;
    private int currentCameraId = 0;
    private String[] permissions = {"android.permission.CAMERA"};
    private boolean previewing = false;
    private boolean switchFlashOn = false;
    private boolean switchFlashOff = false;
    private boolean scanning = false;
    private boolean oneTime = true;
    private boolean keepDenied = false;
    private boolean appPausedWithActivePreview = false;

    @Override // com.journeyapps.barcodescanner.BarcodeCallback
    public void possibleResultPoints(List<ResultPoint> list) {
    }

    /* loaded from: classes.dex */
    static class QRScannerError {
        private static final int BACK_CAMERA_UNAVAILABLE = 3;
        private static final int CAMERA_ACCESS_DENIED = 1;
        private static final int CAMERA_ACCESS_RESTRICTED = 2;
        private static final int CAMERA_UNAVAILABLE = 5;
        private static final int FRONT_CAMERA_UNAVAILABLE = 4;
        private static final int LIGHT_UNAVAILABLE = 7;
        private static final int OPEN_SETTINGS_UNAVAILABLE = 8;
        private static final int SCAN_CANCELED = 6;
        private static final int UNEXPECTED_ERROR = 0;

        QRScannerError() {
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public boolean execute(String str, final JSONArray jSONArray, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        try {
            if (str.equals("show")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.show(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("scan")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.2
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.scan(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("cancelScan")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.cancelScan(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("openSettings")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.openSettings(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("pausePreview")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.5
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.pausePreview(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("useCamera")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.6
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.switchCamera(callbackContext, jSONArray);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("resumePreview")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.7
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.resumePreview(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("hide")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.8
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.hide(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("enableLight")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.9
                    @Override // java.lang.Runnable
                    public void run() {
                        while (QRScanner.this.cameraClosing) {
                            try {
                                try {
                                    Thread.sleep(10L);
                                } catch (InterruptedException unused) {
                                }
                            } catch (Exception e) {
                                FirebaseCrashlytics.getInstance().recordException(e);
                                return;
                            }
                        }
                        QRScanner.this.switchFlashOn = true;
                        if (QRScanner.this.hasFlash()) {
                            if (!QRScanner.this.hasPermission()) {
                                QRScanner.this.requestPermission(33);
                                return;
                            } else {
                                QRScanner.this.enableLight(callbackContext);
                                return;
                            }
                        }
                        callbackContext.error(7);
                    }
                });
                return true;
            } else if (str.equals("disableLight")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.10
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.switchFlashOff = true;
                            if (QRScanner.this.hasFlash()) {
                                if (!QRScanner.this.hasPermission()) {
                                    QRScanner.this.requestPermission(33);
                                } else {
                                    QRScanner.this.disableLight(callbackContext);
                                }
                            } else {
                                callbackContext.error(7);
                            }
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("prepare")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.11
                    @Override // java.lang.Runnable
                    public void run() {
                        QRScanner.this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.11.1
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    QRScanner.this.currentCameraId = jSONArray.getInt(0);
                                } catch (JSONException unused) {
                                }
                                try {
                                    QRScanner.this.prepare(callbackContext);
                                } catch (Exception e) {
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }
                            }
                        });
                    }
                });
                return true;
            } else if (str.equals("destroy")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.12
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.destroy(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else if (str.equals("getStatus")) {
                this.cordova.getThreadPool().execute(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.13
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            QRScanner.this.getStatus(callbackContext);
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
                return true;
            } else {
                return false;
            }
        } catch (Exception unused) {
            callbackContext.error(0);
            return false;
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onPause(boolean z) {
        if (this.previewing) {
            this.appPausedWithActivePreview = true;
            pausePreview(null);
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onResume(boolean z) {
        if (this.appPausedWithActivePreview) {
            this.appPausedWithActivePreview = false;
            resumePreview(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasFlash() {
        if (flashAvailable == null) {
            int i = 0;
            flashAvailable = false;
            FeatureInfo[] systemAvailableFeatures = this.cordova.getActivity().getPackageManager().getSystemAvailableFeatures();
            int length = systemAvailableFeatures.length;
            while (true) {
                if (i >= length) {
                    break;
                } else if ("android.hardware.camera.flash".equalsIgnoreCase(systemAvailableFeatures[i].name)) {
                    flashAvailable = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        return flashAvailable.booleanValue();
    }

    private void switchFlash(boolean z, CallbackContext callbackContext) {
        try {
            if (hasFlash()) {
                doswitchFlash(z, callbackContext);
            } else {
                callbackContext.error(7);
            }
        } catch (Exception e) {
            this.lightOn = false;
            callbackContext.error(7);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private String boolToNumberString(Boolean bool) {
        if (bool.booleanValue()) {
            return "1";
        }
        return "0";
    }

    private void doswitchFlash(final boolean z, final CallbackContext callbackContext) throws IOException, CameraAccessException {
        try {
            if (getCurrentCameraId() == 1) {
                callbackContext.error(7);
                return;
            }
            if (!this.prepared) {
                if (z) {
                    this.lightOn = true;
                } else {
                    this.lightOn = false;
                }
                prepare(callbackContext);
            }
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.14
                @Override // java.lang.Runnable
                public void run() {
                    if (QRScanner.this.mBarcodeView != null) {
                        QRScanner.this.mBarcodeView.setTorch(z);
                        if (z) {
                            QRScanner.this.lightOn = true;
                        } else {
                            QRScanner.this.lightOn = false;
                        }
                    }
                    QRScanner.this.getStatus(callbackContext);
                }
            });
        } catch (Exception e) {
            this.lightOn = false;
            callbackContext.error(7);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public int getCurrentCameraId() {
        return this.currentCameraId;
    }

    private boolean canChangeCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (1 == cameraInfo.facing) {
                return true;
            }
        }
        return false;
    }

    public void switchCamera(CallbackContext callbackContext, JSONArray jSONArray) {
        int i;
        try {
            try {
                i = jSONArray.getInt(0);
            } catch (JSONException unused) {
                callbackContext.error(0);
                i = 0;
            }
            this.currentCameraId = i;
            if (this.scanning) {
                this.scanning = false;
                this.prepared = false;
                if (this.cameraPreviewing) {
                    this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.15
                        @Override // java.lang.Runnable
                        public void run() {
                            ((ViewGroup) QRScanner.this.mBarcodeView.getParent()).removeView(QRScanner.this.mBarcodeView);
                            QRScanner.this.cameraPreviewing = false;
                        }
                    });
                }
                closeCamera();
                prepare(callbackContext);
                scan(this.nextScanCallback);
                return;
            }
            prepare(callbackContext);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override // org.apache.cordova.CordovaPlugin
    public void onRequestPermissionResult(int i, String[] strArr, int[] iArr) throws JSONException {
        try {
            this.oneTime = false;
            if (i == 33) {
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    String str = strArr[i2];
                    int i3 = iArr[i2];
                    if (i3 == -1) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.cordova.getActivity(), str)) {
                            this.denied = true;
                            this.authorized = false;
                            this.callbackContext.error(1);
                            return;
                        }
                        this.authorized = false;
                        this.denied = false;
                        this.callbackContext.error(1);
                        return;
                    }
                    if (i3 == 0) {
                        this.authorized = true;
                        this.denied = false;
                        if (i == 33) {
                            if (this.switchFlashOn && !this.scanning && !this.switchFlashOff) {
                                switchFlash(true, this.callbackContext);
                            } else if (this.switchFlashOff && !this.scanning) {
                                switchFlash(false, this.callbackContext);
                            } else {
                                setupCamera(this.callbackContext);
                                if (!this.scanning) {
                                    getStatus(this.callbackContext);
                                }
                            }
                        }
                    } else {
                        this.authorized = false;
                        this.denied = false;
                        this.restricted = false;
                    }
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public boolean hasPermission() {
        for (String str : this.permissions) {
            if (!PermissionHelper.hasPermission(this, str)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPermission(int i) {
        PermissionHelper.requestPermissions(this, i, this.permissions);
    }

    private void closeCamera() {
        try {
            this.cameraClosing = true;
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.16
                @Override // java.lang.Runnable
                public void run() {
                    if (QRScanner.this.mBarcodeView != null) {
                        QRScanner.this.mBarcodeView.pause();
                    }
                    QRScanner.this.cameraClosing = false;
                }
            });
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void makeOpaque() {
        try {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.17
                @Override // java.lang.Runnable
                public void run() {
                    QRScanner.this.webView.getView().setBackgroundColor(-1);
                }
            });
            this.showing = false;
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private boolean hasCamera() {
        return this.cordova.getActivity().getPackageManager().hasSystemFeature("android.hardware.camera");
    }

    private boolean hasFrontCamera() {
        return this.cordova.getActivity().getPackageManager().hasSystemFeature("android.hardware.camera.front");
    }

    private void setupCamera(CallbackContext callbackContext) {
        try {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.18
                @Override // java.lang.Runnable
                public void run() {
                    QRScanner.this.mBarcodeView = new BarcodeView(QRScanner.this.cordova.getActivity());
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(BarcodeFormat.QR_CODE);
                    arrayList.add(BarcodeFormat.CODE_39);
                    arrayList.add(BarcodeFormat.CODE_93);
                    arrayList.add(BarcodeFormat.CODE_128);
                    QRScanner.this.mBarcodeView.setDecoderFactory(new DefaultDecoderFactory(arrayList, null, null, 0));
                    CameraSettings cameraSettings = new CameraSettings();
                    cameraSettings.setRequestedCameraId(QRScanner.this.getCurrentCameraId());
                    QRScanner.this.mBarcodeView.setCameraSettings(cameraSettings);
                    ((ViewGroup) QRScanner.this.webView.getView().getParent()).addView(QRScanner.this.mBarcodeView, new FrameLayout.LayoutParams(-2, -2));
                    QRScanner.this.cameraPreviewing = true;
                    QRScanner.this.webView.getView().bringToFront();
                    QRScanner.this.mBarcodeView.resume();
                }
            });
            this.prepared = true;
            this.previewing = true;
            if (this.shouldScanAgain) {
                scan(callbackContext);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override // com.journeyapps.barcodescanner.BarcodeCallback
    public void barcodeResult(BarcodeResult barcodeResult) {
        try {
            if (this.nextScanCallback == null) {
                return;
            }
            if (barcodeResult.getText() != null) {
                this.scanning = false;
                this.nextScanCallback.success(barcodeResult.getText());
                this.nextScanCallback = null;
            } else {
                scan(this.nextScanCallback);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepare(CallbackContext callbackContext) {
        try {
            if (!this.prepared) {
                int i = this.currentCameraId;
                if (i == 0) {
                    if (hasCamera()) {
                        if (!hasPermission()) {
                            requestPermission(33);
                            return;
                        }
                        setupCamera(callbackContext);
                        if (this.scanning) {
                            return;
                        }
                        getStatus(callbackContext);
                        return;
                    }
                    callbackContext.error(3);
                    return;
                } else if (i == 1) {
                    if (hasFrontCamera()) {
                        if (!hasPermission()) {
                            requestPermission(33);
                            return;
                        }
                        setupCamera(callbackContext);
                        if (this.scanning) {
                            return;
                        }
                        getStatus(callbackContext);
                        return;
                    }
                    callbackContext.error(4);
                    return;
                } else {
                    callbackContext.error(5);
                    return;
                }
            }
            this.prepared = false;
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.19
                @Override // java.lang.Runnable
                public void run() {
                    QRScanner.this.mBarcodeView.pause();
                }
            });
            if (this.cameraPreviewing) {
                this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.20
                    @Override // java.lang.Runnable
                    public void run() {
                        ((ViewGroup) QRScanner.this.mBarcodeView.getParent()).removeView(QRScanner.this.mBarcodeView);
                        QRScanner.this.cameraPreviewing = false;
                    }
                });
                this.previewing = true;
                this.lightOn = false;
            }
            setupCamera(callbackContext);
            getStatus(callbackContext);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scan(CallbackContext callbackContext) {
        try {
            this.scanning = true;
            if (!this.prepared) {
                this.shouldScanAgain = true;
                if (hasCamera()) {
                    if (!hasPermission()) {
                        requestPermission(33);
                        return;
                    } else {
                        setupCamera(callbackContext);
                        return;
                    }
                }
                return;
            }
            if (!this.previewing) {
                this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.21
                    @Override // java.lang.Runnable
                    public void run() {
                        if (QRScanner.this.mBarcodeView != null) {
                            QRScanner.this.mBarcodeView.resume();
                            QRScanner.this.previewing = true;
                            if (QRScanner.this.switchFlashOn) {
                                QRScanner.this.lightOn = true;
                            }
                        }
                    }
                });
            }
            this.shouldScanAgain = false;
            this.nextScanCallback = callbackContext;
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.22
                @Override // java.lang.Runnable
                public void run() {
                    if (QRScanner.this.mBarcodeView != null) {
                        QRScanner.this.mBarcodeView.decodeSingle(this);
                    }
                }
            });
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelScan(CallbackContext callbackContext) {
        try {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.23
                @Override // java.lang.Runnable
                public void run() {
                    QRScanner.this.scanning = false;
                    if (QRScanner.this.mBarcodeView != null) {
                        QRScanner.this.mBarcodeView.stopDecoding();
                    }
                }
            });
            CallbackContext callbackContext2 = this.nextScanCallback;
            if (callbackContext2 != null) {
                callbackContext2.error(6);
            }
            this.nextScanCallback = null;
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void show(final CallbackContext callbackContext) {
        try {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.24
                @Override // java.lang.Runnable
                public void run() {
                    QRScanner.this.webView.getView().setBackgroundColor(0);
                    QRScanner.this.showing = true;
                    QRScanner.this.getStatus(callbackContext);
                }
            });
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hide(CallbackContext callbackContext) {
        try {
            makeOpaque();
            getStatus(callbackContext);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pausePreview(final CallbackContext callbackContext) {
        try {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.25
                @Override // java.lang.Runnable
                public void run() {
                    if (QRScanner.this.mBarcodeView != null) {
                        QRScanner.this.mBarcodeView.pause();
                        QRScanner.this.previewing = false;
                        if (QRScanner.this.lightOn) {
                            QRScanner.this.lightOn = false;
                        }
                    }
                    CallbackContext callbackContext2 = callbackContext;
                    if (callbackContext2 != null) {
                        QRScanner.this.getStatus(callbackContext2);
                    }
                }
            });
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumePreview(final CallbackContext callbackContext) {
        try {
            this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.26
                @Override // java.lang.Runnable
                public void run() {
                    if (QRScanner.this.mBarcodeView != null) {
                        QRScanner.this.mBarcodeView.resume();
                        QRScanner.this.previewing = true;
                        if (QRScanner.this.switchFlashOn) {
                            QRScanner.this.lightOn = true;
                        }
                    }
                    CallbackContext callbackContext2 = callbackContext;
                    if (callbackContext2 != null) {
                        QRScanner.this.getStatus(callbackContext2);
                    }
                }
            });
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableLight(CallbackContext callbackContext) {
        try {
            this.lightOn = true;
            if (hasPermission()) {
                switchFlash(true, callbackContext);
            } else {
                callbackContext.error(1);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableLight(CallbackContext callbackContext) {
        try {
            this.lightOn = false;
            this.switchFlashOn = false;
            if (hasPermission()) {
                switchFlash(false, callbackContext);
            } else {
                callbackContext.error(1);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openSettings(CallbackContext callbackContext) {
        try {
            this.oneTime = true;
            if (this.denied) {
                this.keepDenied = true;
            }
            try {
                this.denied = false;
                this.authorized = false;
                boolean z = this.prepared;
                boolean z2 = this.lightOn;
                boolean z3 = this.showing;
                if (z) {
                    destroy(callbackContext);
                }
                this.lightOn = false;
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setFlags(268435456);
                intent.setData(Uri.fromParts("package", this.cordova.getActivity().getPackageName(), null));
                this.cordova.getActivity().getApplicationContext().startActivity(intent);
                getStatus(callbackContext);
                if (z) {
                    prepare(callbackContext);
                }
                if (z2) {
                    enableLight(callbackContext);
                }
                if (z3) {
                    show(callbackContext);
                }
            } catch (Exception e) {
                callbackContext.error(8);
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } catch (Exception e2) {
            FirebaseCrashlytics.getInstance().recordException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getStatus(CallbackContext callbackContext) {
        try {
            boolean z = false;
            if (this.oneTime) {
                boolean hasPermission = hasPermission();
                this.authorized = false;
                if (hasPermission) {
                    this.authorized = true;
                }
                if (this.keepDenied && !this.authorized) {
                    this.denied = true;
                } else {
                    this.denied = false;
                }
                this.restricted = false;
            }
            boolean hasFlash = hasFlash();
            if (this.currentCameraId != 1) {
                z = hasFlash;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("authorized", boolToNumberString(Boolean.valueOf(this.authorized)));
            hashMap.put("denied", boolToNumberString(Boolean.valueOf(this.denied)));
            hashMap.put("restricted", boolToNumberString(Boolean.valueOf(this.restricted)));
            hashMap.put("prepared", boolToNumberString(Boolean.valueOf(this.prepared)));
            hashMap.put("scanning", boolToNumberString(Boolean.valueOf(this.scanning)));
            hashMap.put("previewing", boolToNumberString(Boolean.valueOf(this.previewing)));
            hashMap.put("showing", boolToNumberString(Boolean.valueOf(this.showing)));
            hashMap.put("lightEnabled", boolToNumberString(Boolean.valueOf(this.lightOn)));
            hashMap.put("canOpenSettings", boolToNumberString(true));
            hashMap.put("canEnableLight", boolToNumberString(Boolean.valueOf(z)));
            hashMap.put("canChangeCamera", boolToNumberString(Boolean.valueOf(canChangeCamera())));
            hashMap.put("currentCamera", Integer.toString(getCurrentCameraId()));
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, new JSONObject(hashMap)));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroy(CallbackContext callbackContext) {
        try {
            this.prepared = false;
            makeOpaque();
            this.previewing = false;
            if (this.scanning) {
                this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.27
                    @Override // java.lang.Runnable
                    public void run() {
                        QRScanner.this.scanning = false;
                        if (QRScanner.this.mBarcodeView != null) {
                            QRScanner.this.mBarcodeView.stopDecoding();
                        }
                    }
                });
                this.nextScanCallback = null;
            }
            if (this.cameraPreviewing) {
                this.cordova.getActivity().runOnUiThread(new Runnable() { // from class: com.bitpay.cordova.qrscanner.QRScanner.28
                    @Override // java.lang.Runnable
                    public void run() {
                        ((ViewGroup) QRScanner.this.mBarcodeView.getParent()).removeView(QRScanner.this.mBarcodeView);
                        QRScanner.this.cameraPreviewing = false;
                    }
                });
            }
            if (this.currentCameraId != 1 && this.lightOn) {
                switchFlash(false, callbackContext);
            }
            closeCamera();
            this.currentCameraId = 0;
            getStatus(callbackContext);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
