package androidx.webkit.internal;

import android.net.Uri;
import android.webkit.WebView;
import androidx.webkit.WebMessageCompat;
import androidx.webkit.WebViewCompat;
import java.lang.reflect.InvocationHandler;
import org.chromium.support_lib_boundary.WebMessageBoundaryInterface;
import org.chromium.support_lib_boundary.WebMessageListenerBoundaryInterface;
import org.chromium.support_lib_boundary.util.BoundaryInterfaceReflectionUtil;

/* loaded from: classes.dex */
public class WebMessageListenerAdapter implements WebMessageListenerBoundaryInterface {
    private WebViewCompat.WebMessageListener mWebMessageListener;

    public WebMessageListenerAdapter(WebViewCompat.WebMessageListener webMessageListener) {
        this.mWebMessageListener = webMessageListener;
    }

    @Override // org.chromium.support_lib_boundary.WebMessageListenerBoundaryInterface
    public void onPostMessage(WebView webView, InvocationHandler invocationHandler, Uri uri, boolean z, InvocationHandler invocationHandler2) {
        WebMessageCompat webMessageCompatFromBoundaryInterface = WebMessageAdapter.webMessageCompatFromBoundaryInterface((WebMessageBoundaryInterface) BoundaryInterfaceReflectionUtil.castToSuppLibClass(WebMessageBoundaryInterface.class, invocationHandler));
        if (webMessageCompatFromBoundaryInterface != null) {
            this.mWebMessageListener.onPostMessage(webView, webMessageCompatFromBoundaryInterface, uri, z, JavaScriptReplyProxyImpl.forInvocationHandler(invocationHandler2));
        }
    }

    @Override // org.chromium.support_lib_boundary.FeatureFlagHolderBoundaryInterface
    public String[] getSupportedFeatures() {
        return new String[]{"WEB_MESSAGE_LISTENER"};
    }
}
