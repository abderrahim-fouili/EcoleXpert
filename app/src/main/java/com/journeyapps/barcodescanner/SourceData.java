package com.journeyapps.barcodescanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ResultPoint;
import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class SourceData {
    private Rect cropRect;
    private RawImageData data;
    private int imageFormat;
    private boolean previewMirrored;
    private int rotation;
    private int scalingFactor = 1;

    public SourceData(byte[] bArr, int i, int i2, int i3, int i4) {
        this.data = new RawImageData(bArr, i, i2);
        this.rotation = i4;
        this.imageFormat = i3;
        if (i * i2 > bArr.length) {
            throw new IllegalArgumentException("Image data does not match the resolution. " + i + "x" + i2 + " > " + bArr.length);
        }
    }

    public Rect getCropRect() {
        return this.cropRect;
    }

    public void setCropRect(Rect rect) {
        this.cropRect = rect;
    }

    public boolean isPreviewMirrored() {
        return this.previewMirrored;
    }

    public void setPreviewMirrored(boolean z) {
        this.previewMirrored = z;
    }

    public int getScalingFactor() {
        return this.scalingFactor;
    }

    public void setScalingFactor(int i) {
        this.scalingFactor = i;
    }

    public byte[] getData() {
        return this.data.getData();
    }

    public int getDataWidth() {
        return this.data.getWidth();
    }

    public int getDataHeight() {
        return this.data.getHeight();
    }

    public ResultPoint translateResultPoint(ResultPoint resultPoint) {
        float x = (resultPoint.getX() * this.scalingFactor) + this.cropRect.left;
        float y = (resultPoint.getY() * this.scalingFactor) + this.cropRect.top;
        if (this.previewMirrored) {
            x = this.data.getWidth() - x;
        }
        return new ResultPoint(x, y);
    }

    public boolean isRotated() {
        return this.rotation % 180 != 0;
    }

    public int getImageFormat() {
        return this.imageFormat;
    }

    public PlanarYUVLuminanceSource createSource() {
        RawImageData cropAndScale = this.data.rotateCameraPreview(this.rotation).cropAndScale(this.cropRect, this.scalingFactor);
        return new PlanarYUVLuminanceSource(cropAndScale.getData(), cropAndScale.getWidth(), cropAndScale.getHeight(), 0, 0, cropAndScale.getWidth(), cropAndScale.getHeight(), false);
    }

    public Bitmap getBitmap() {
        return getBitmap(1);
    }

    public Bitmap getBitmap(int i) {
        return getBitmap(this.cropRect, i);
    }

    public Bitmap getBitmap(Rect rect, int i) {
        if (rect == null) {
            rect = new Rect(0, 0, this.data.getWidth(), this.data.getHeight());
        } else if (isRotated()) {
            rect = new Rect(rect.top, rect.left, rect.bottom, rect.right);
        }
        YuvImage yuvImage = new YuvImage(this.data.getData(), this.imageFormat, this.data.getWidth(), this.data.getHeight(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(rect, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = i;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        if (this.rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(this.rotation);
            return Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, false);
        }
        return decodeByteArray;
    }
}
