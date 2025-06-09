package androidx.webkit.internal;

import androidx.webkit.WebMessageCompat;
import org.chromium.support_lib_boundary.WebMessagePayloadBoundaryInterface;

/* loaded from: classes.dex */
public class WebMessagePayloadAdapter implements WebMessagePayloadBoundaryInterface {
    private final WebMessageCompat mMessageCompat;

    public WebMessagePayloadAdapter(WebMessageCompat webMessageCompat) {
        this.mMessageCompat = webMessageCompat;
    }

    @Override // org.chromium.support_lib_boundary.FeatureFlagHolderBoundaryInterface
    public String[] getSupportedFeatures() {
        return new String[0];
    }

    @Override // org.chromium.support_lib_boundary.WebMessagePayloadBoundaryInterface
    public int getType() {
        int type = this.mMessageCompat.getType();
        if (type != 0) {
            if (type == 1) {
                return 1;
            }
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
        return 0;
    }

    @Override // org.chromium.support_lib_boundary.WebMessagePayloadBoundaryInterface
    public String getAsString() {
        return this.mMessageCompat.getData();
    }

    @Override // org.chromium.support_lib_boundary.WebMessagePayloadBoundaryInterface
    public byte[] getAsArrayBuffer() {
        return this.mMessageCompat.getArrayBuffer();
    }
}
