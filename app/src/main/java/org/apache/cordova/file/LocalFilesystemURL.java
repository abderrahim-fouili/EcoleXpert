package org.apache.cordova.file;

import android.net.Uri;

/* loaded from: classes.dex */
public class LocalFilesystemURL {
    public static final String CDVFILE_KEYWORD = "__cdvfile_";
    public static final String FILESYSTEM_PROTOCOL = "cdvfile";
    public final String fsName;
    public final boolean isDirectory;
    public final String path;
    public final Uri uri;

    private LocalFilesystemURL(Uri uri, String str, String str2, boolean z) {
        this.uri = uri;
        this.fsName = str;
        this.path = str2;
        this.isDirectory = z;
    }

    public static LocalFilesystemURL parse(Uri uri) {
        int indexOf;
        if (uri.toString().contains(CDVFILE_KEYWORD)) {
            String path = uri.getPath();
            if (path.length() >= 1 && (indexOf = path.indexOf(47, 1)) >= 0) {
                String substring = path.substring(1, indexOf).substring(10);
                String substring2 = substring.substring(0, substring.length() - 2);
                String substring3 = path.substring(indexOf);
                return new LocalFilesystemURL(uri, substring2, substring3, substring3.charAt(substring3.length() - 1) == '/');
            }
            return null;
        }
        return null;
    }

    public static LocalFilesystemURL parse(String str) {
        return parse(Uri.parse(str));
    }

    public static String fsNameToCdvKeyword(String str) {
        return CDVFILE_KEYWORD + str + "__";
    }

    public String toString() {
        return this.uri.toString();
    }
}
