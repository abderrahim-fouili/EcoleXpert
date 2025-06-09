package org.apache.cordova;

import android.net.Uri;
import androidx.webkit.ProxyConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class AllowList {
    public static final String TAG = "CordovaAllowList";
    private ArrayList<URLPattern> allowList = new ArrayList<>();

    /* loaded from: classes.dex */
    private static class URLPattern {
        public Pattern host;
        public Pattern path;
        public Integer port;
        public Pattern scheme;

        private String regexFromPattern(String str, boolean z) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                if (charAt == '*' && z) {
                    sb.append(".");
                } else if ("\\.[]{}()^$?+|".indexOf(charAt) > -1) {
                    sb.append('\\');
                }
                sb.append(charAt);
            }
            return sb.toString();
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0026 A[Catch: NumberFormatException -> 0x0089, TryCatch #0 {NumberFormatException -> 0x0089, blocks: (B:4:0x000c, B:7:0x0013, B:9:0x0020, B:11:0x0026, B:17:0x0059, B:20:0x0060, B:23:0x0071, B:26:0x007a, B:27:0x0086, B:21:0x006d, B:12:0x0029, B:14:0x0031, B:15:0x004d, B:8:0x001e), top: B:31:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0029 A[Catch: NumberFormatException -> 0x0089, TryCatch #0 {NumberFormatException -> 0x0089, blocks: (B:4:0x000c, B:7:0x0013, B:9:0x0020, B:11:0x0026, B:17:0x0059, B:20:0x0060, B:23:0x0071, B:26:0x007a, B:27:0x0086, B:21:0x006d, B:12:0x0029, B:14:0x0031, B:15:0x004d, B:8:0x001e), top: B:31:0x000c }] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0071 A[Catch: NumberFormatException -> 0x0089, TryCatch #0 {NumberFormatException -> 0x0089, blocks: (B:4:0x000c, B:7:0x0013, B:9:0x0020, B:11:0x0026, B:17:0x0059, B:20:0x0060, B:23:0x0071, B:26:0x007a, B:27:0x0086, B:21:0x006d, B:12:0x0029, B:14:0x0031, B:15:0x004d, B:8:0x001e), top: B:31:0x000c }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public URLPattern(java.lang.String r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) throws java.net.MalformedURLException {
            /*
                r6 = this;
                java.lang.String r0 = "([a-z0-9.-]*\\.)?"
                r6.<init>()
                r1 = 0
                java.lang.String r2 = "*"
                r3 = 2
                r4 = 0
                if (r7 == 0) goto L1e
                boolean r5 = r2.equals(r7)     // Catch: java.lang.NumberFormatException -> L89
                if (r5 == 0) goto L13
                goto L1e
            L13:
                java.lang.String r7 = r6.regexFromPattern(r7, r1)     // Catch: java.lang.NumberFormatException -> L89
                java.util.regex.Pattern r7 = java.util.regex.Pattern.compile(r7, r3)     // Catch: java.lang.NumberFormatException -> L89
                r6.scheme = r7     // Catch: java.lang.NumberFormatException -> L89
                goto L20
            L1e:
                r6.scheme = r4     // Catch: java.lang.NumberFormatException -> L89
            L20:
                boolean r7 = r2.equals(r8)     // Catch: java.lang.NumberFormatException -> L89
                if (r7 == 0) goto L29
                r6.host = r4     // Catch: java.lang.NumberFormatException -> L89
                goto L57
            L29:
                java.lang.String r7 = "*."
                boolean r7 = r8.startsWith(r7)     // Catch: java.lang.NumberFormatException -> L89
                if (r7 == 0) goto L4d
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.NumberFormatException -> L89
                r7.<init>(r0)     // Catch: java.lang.NumberFormatException -> L89
                java.lang.String r8 = r8.substring(r3)     // Catch: java.lang.NumberFormatException -> L89
                java.lang.String r8 = r6.regexFromPattern(r8, r1)     // Catch: java.lang.NumberFormatException -> L89
                java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.NumberFormatException -> L89
                java.lang.String r7 = r7.toString()     // Catch: java.lang.NumberFormatException -> L89
                java.util.regex.Pattern r7 = java.util.regex.Pattern.compile(r7, r3)     // Catch: java.lang.NumberFormatException -> L89
                r6.host = r7     // Catch: java.lang.NumberFormatException -> L89
                goto L57
            L4d:
                java.lang.String r7 = r6.regexFromPattern(r8, r1)     // Catch: java.lang.NumberFormatException -> L89
                java.util.regex.Pattern r7 = java.util.regex.Pattern.compile(r7, r3)     // Catch: java.lang.NumberFormatException -> L89
                r6.host = r7     // Catch: java.lang.NumberFormatException -> L89
            L57:
                if (r9 == 0) goto L6d
                boolean r7 = r2.equals(r9)     // Catch: java.lang.NumberFormatException -> L89
                if (r7 == 0) goto L60
                goto L6d
            L60:
                r7 = 10
                int r7 = java.lang.Integer.parseInt(r9, r7)     // Catch: java.lang.NumberFormatException -> L89
                java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.NumberFormatException -> L89
                r6.port = r7     // Catch: java.lang.NumberFormatException -> L89
                goto L6f
            L6d:
                r6.port = r4     // Catch: java.lang.NumberFormatException -> L89
            L6f:
                if (r10 == 0) goto L86
                java.lang.String r7 = "/*"
                boolean r7 = r7.equals(r10)     // Catch: java.lang.NumberFormatException -> L89
                if (r7 == 0) goto L7a
                goto L86
            L7a:
                r7 = 1
                java.lang.String r7 = r6.regexFromPattern(r10, r7)     // Catch: java.lang.NumberFormatException -> L89
                java.util.regex.Pattern r7 = java.util.regex.Pattern.compile(r7)     // Catch: java.lang.NumberFormatException -> L89
                r6.path = r7     // Catch: java.lang.NumberFormatException -> L89
                goto L88
            L86:
                r6.path = r4     // Catch: java.lang.NumberFormatException -> L89
            L88:
                return
            L89:
                java.net.MalformedURLException r7 = new java.net.MalformedURLException
                java.lang.String r8 = "Port must be a number"
                r7.<init>(r8)
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.cordova.AllowList.URLPattern.<init>(java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
        }

        public boolean matches(Uri uri) {
            try {
                Pattern pattern = this.scheme;
                if (pattern == null || pattern.matcher(uri.getScheme()).matches()) {
                    Pattern pattern2 = this.host;
                    if (pattern2 == null || pattern2.matcher(uri.getHost()).matches()) {
                        Integer num = this.port;
                        if (num == null || num.equals(Integer.valueOf(uri.getPort()))) {
                            Pattern pattern3 = this.path;
                            if (pattern3 != null) {
                                if (!pattern3.matcher(uri.getPath()).matches()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            } catch (Exception e) {
                LOG.d(AllowList.TAG, e.toString());
                return false;
            }
        }
    }

    public void addAllowListEntry(String str, boolean z) {
        String str2 = ProxyConfig.MATCH_ALL_SCHEMES;
        if (this.allowList != null) {
            try {
                if (str.compareTo(ProxyConfig.MATCH_ALL_SCHEMES) == 0) {
                    LOG.d(TAG, "Unlimited access to network resources");
                    this.allowList = null;
                    return;
                }
                Matcher matcher = Pattern.compile("^((\\*|[A-Za-z-]+):(//)?)?(\\*|((\\*\\.)?[^*/:]+))?(:(\\d+))?(/.*)?").matcher(str);
                if (matcher.matches()) {
                    String group = matcher.group(2);
                    String group2 = matcher.group(4);
                    if ((!"file".equals(group) && !"content".equals(group)) || group2 != null) {
                        str2 = group2;
                    }
                    String group3 = matcher.group(8);
                    String group4 = matcher.group(9);
                    if (group == null) {
                        this.allowList.add(new URLPattern(ProxyConfig.MATCH_HTTP, str2, group3, group4));
                        this.allowList.add(new URLPattern(ProxyConfig.MATCH_HTTPS, str2, group3, group4));
                        return;
                    }
                    this.allowList.add(new URLPattern(group, str2, group3, group4));
                }
            } catch (Exception unused) {
                LOG.d(TAG, "Failed to add origin %s", str);
            }
        }
    }

    public boolean isUrlAllowListed(String str) {
        if (this.allowList == null) {
            return true;
        }
        Uri parse = Uri.parse(str);
        Iterator<URLPattern> it = this.allowList.iterator();
        while (it.hasNext()) {
            if (it.next().matches(parse)) {
                return true;
            }
        }
        return false;
    }
}
