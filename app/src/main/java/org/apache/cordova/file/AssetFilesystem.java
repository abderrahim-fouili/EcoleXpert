package org.apache.cordova.file;

import android.content.res.AssetManager;
import android.net.Uri;
import com.google.firebase.sessions.settings.RemoteSettings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AssetFilesystem extends Filesystem {
    private static final String LOG_TAG = "AssetFilesystem";
    private static Map<String, Long> lengthCache;
    private static Map<String, String[]> listCache;
    private static boolean listCacheFromFile;
    private static Object listCacheLock = new Object();
    private final AssetManager assetManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.cordova.file.Filesystem
    public LocalFilesystemURL URLforFilesystemPath(String str) {
        return null;
    }

    @Override // org.apache.cordova.file.Filesystem
    public boolean canRemoveFileAtLocalURL(LocalFilesystemURL localFilesystemURL) {
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0067 A[Catch: all -> 0x008a, TRY_LEAVE, TryCatch #5 {, blocks: (B:4:0x0003, B:9:0x0028, B:34:0x0063, B:36:0x0067, B:12:0x002d, B:13:0x0033, B:30:0x0057, B:33:0x005c, B:40:0x0079, B:44:0x0087, B:43:0x007e, B:21:0x0042, B:24:0x0047, B:45:0x0088), top: B:56:0x0003, inners: #2, #8, #9, #10 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0079 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void lazyInitCaches() {
        /*
            r6 = this;
            java.lang.Object r0 = org.apache.cordova.file.AssetFilesystem.listCacheLock
            monitor-enter(r0)
            java.util.Map<java.lang.String, java.lang.String[]> r1 = org.apache.cordova.file.AssetFilesystem.listCache     // Catch: java.lang.Throwable -> L8a
            if (r1 != 0) goto L88
            r1 = 0
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L40 java.lang.ClassNotFoundException -> L4e
            android.content.res.AssetManager r3 = r6.assetManager     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L40 java.lang.ClassNotFoundException -> L4e
            java.lang.String r4 = "cdvasset.manifest"
            java.io.InputStream r3 = r3.open(r4)     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L40 java.lang.ClassNotFoundException -> L4e
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L3b java.io.IOException -> L40 java.lang.ClassNotFoundException -> L4e
            java.lang.Object r1 = r2.readObject()     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            java.util.Map r1 = (java.util.Map) r1     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            org.apache.cordova.file.AssetFilesystem.listCache = r1     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            java.lang.Object r1 = r2.readObject()     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            java.util.Map r1 = (java.util.Map) r1     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            org.apache.cordova.file.AssetFilesystem.lengthCache = r1     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            r1 = 1
            org.apache.cordova.file.AssetFilesystem.listCacheFromFile = r1     // Catch: java.io.IOException -> L37 java.lang.ClassNotFoundException -> L39 java.lang.Throwable -> L76
            r2.close()     // Catch: java.io.IOException -> L2c java.lang.Throwable -> L8a
            goto L63
        L2c:
            r1 = move-exception
            java.lang.String r2 = "AssetFilesystem"
            java.lang.String r1 = r1.getLocalizedMessage()     // Catch: java.lang.Throwable -> L8a
        L33:
            org.apache.cordova.LOG.d(r2, r1)     // Catch: java.lang.Throwable -> L8a
            goto L63
        L37:
            r1 = r2
            goto L40
        L39:
            r1 = move-exception
            goto L52
        L3b:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r5
            goto L77
        L40:
            if (r1 == 0) goto L63
            r1.close()     // Catch: java.io.IOException -> L46 java.lang.Throwable -> L8a
            goto L63
        L46:
            r1 = move-exception
            java.lang.String r2 = "AssetFilesystem"
            java.lang.String r1 = r1.getLocalizedMessage()     // Catch: java.lang.Throwable -> L8a
            goto L33
        L4e:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r5
        L52:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L76
            if (r2 == 0) goto L63
            r2.close()     // Catch: java.io.IOException -> L5b java.lang.Throwable -> L8a
            goto L63
        L5b:
            r1 = move-exception
            java.lang.String r2 = "AssetFilesystem"
            java.lang.String r1 = r1.getLocalizedMessage()     // Catch: java.lang.Throwable -> L8a
            goto L33
        L63:
            java.util.Map<java.lang.String, java.lang.String[]> r1 = org.apache.cordova.file.AssetFilesystem.listCache     // Catch: java.lang.Throwable -> L8a
            if (r1 != 0) goto L88
            java.lang.String r1 = "AssetFilesystem"
            java.lang.String r2 = "Asset manifest not found. Recursive copies and directory listing will be slow."
            org.apache.cordova.LOG.w(r1, r2)     // Catch: java.lang.Throwable -> L8a
            java.util.HashMap r1 = new java.util.HashMap     // Catch: java.lang.Throwable -> L8a
            r1.<init>()     // Catch: java.lang.Throwable -> L8a
            org.apache.cordova.file.AssetFilesystem.listCache = r1     // Catch: java.lang.Throwable -> L8a
            goto L88
        L76:
            r1 = move-exception
        L77:
            if (r2 == 0) goto L87
            r2.close()     // Catch: java.io.IOException -> L7d java.lang.Throwable -> L8a
            goto L87
        L7d:
            r2 = move-exception
            java.lang.String r3 = "AssetFilesystem"
            java.lang.String r2 = r2.getLocalizedMessage()     // Catch: java.lang.Throwable -> L8a
            org.apache.cordova.LOG.d(r3, r2)     // Catch: java.lang.Throwable -> L8a
        L87:
            throw r1     // Catch: java.lang.Throwable -> L8a
        L88:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L8a
            return
        L8a:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L8a
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.cordova.file.AssetFilesystem.lazyInitCaches():void");
    }

    private String[] listAssets(String str) throws IOException {
        if (str.startsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str = str.substring(1);
        }
        if (str.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str = str.substring(0, str.length() - 1);
        }
        lazyInitCaches();
        String[] strArr = listCache.get(str);
        if (strArr == null) {
            if (listCacheFromFile) {
                return new String[0];
            }
            String[] list = this.assetManager.list(str);
            listCache.put(str, list);
            return list;
        }
        return strArr;
    }

    private long getAssetSize(String str) throws FileNotFoundException {
        if (str.startsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str = str.substring(1);
        }
        lazyInitCaches();
        Map<String, Long> map = lengthCache;
        if (map != null) {
            Long l = map.get(str);
            if (l == null) {
                throw new FileNotFoundException("Asset not found: " + str);
            }
            return l.longValue();
        }
        CordovaResourceApi.OpenForReadResult openForReadResult = null;
        try {
            try {
                openForReadResult = this.resourceApi.openForRead(nativeUriForFullPath(str));
                long j = openForReadResult.length;
                if (j < 0) {
                    j = openForReadResult.inputStream.available();
                }
                return j;
            } finally {
                if (openForReadResult != null) {
                    try {
                        openForReadResult.inputStream.close();
                    } catch (IOException e) {
                        LOG.d(LOG_TAG, e.getLocalizedMessage());
                    }
                }
            }
        } catch (IOException e2) {
            FileNotFoundException fileNotFoundException = new FileNotFoundException("File not found: " + str);
            fileNotFoundException.initCause(e2);
            throw fileNotFoundException;
        }
    }

    public AssetFilesystem(AssetManager assetManager, CordovaResourceApi cordovaResourceApi, CordovaPreferences cordovaPreferences) {
        super(Uri.parse("file:///android_asset/"), "assets", cordovaResourceApi, cordovaPreferences);
        this.assetManager = assetManager;
    }

    @Override // org.apache.cordova.file.Filesystem
    public Uri toNativeUri(LocalFilesystemURL localFilesystemURL) {
        return nativeUriForFullPath(localFilesystemURL.path);
    }

    @Override // org.apache.cordova.file.Filesystem
    public LocalFilesystemURL toLocalUri(Uri uri) {
        if ("file".equals(uri.getScheme())) {
            Uri fromFile = Uri.fromFile(new File(uri.getPath()));
            String encodedPath = this.rootUri.getEncodedPath();
            String substring = encodedPath.substring(0, encodedPath.length() - 1);
            if (fromFile.getEncodedPath().startsWith(substring)) {
                String substring2 = fromFile.getEncodedPath().substring(substring.length());
                if (!substring2.isEmpty()) {
                    substring2 = substring2.substring(1);
                }
                Uri.Builder createLocalUriBuilder = createLocalUriBuilder();
                if (!substring2.isEmpty()) {
                    createLocalUriBuilder.appendEncodedPath(substring2);
                }
                if (isDirectory(substring2) || uri.getPath().endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
                    createLocalUriBuilder.appendEncodedPath("");
                }
                return LocalFilesystemURL.parse(createLocalUriBuilder.build());
            }
            return null;
        }
        return null;
    }

    private boolean isDirectory(String str) {
        try {
            return listAssets(str).length != 0;
        } catch (IOException unused) {
            return false;
        }
    }

    @Override // org.apache.cordova.file.Filesystem
    public LocalFilesystemURL[] listChildren(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException {
        String substring = localFilesystemURL.path.substring(1);
        if (substring.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            substring = substring.substring(0, substring.length() - 1);
        }
        try {
            String[] listAssets = listAssets(substring);
            LocalFilesystemURL[] localFilesystemURLArr = new LocalFilesystemURL[listAssets.length];
            for (int i = 0; i < listAssets.length; i++) {
                localFilesystemURLArr[i] = localUrlforFullPath(new File(localFilesystemURL.path, listAssets[i]).getPath());
            }
            return localFilesystemURLArr;
        } catch (IOException e) {
            FileNotFoundException fileNotFoundException = new FileNotFoundException();
            fileNotFoundException.initCause(e);
            throw fileNotFoundException;
        }
    }

    @Override // org.apache.cordova.file.Filesystem
    public JSONObject getFileForLocalURL(LocalFilesystemURL localFilesystemURL, String str, JSONObject jSONObject, boolean z) throws FileExistsException, IOException, TypeMismatchException, EncodingException, JSONException {
        LocalFilesystemURL localUrlforFullPath;
        if (jSONObject != null && jSONObject.optBoolean("create")) {
            throw new UnsupportedOperationException("Assets are read-only");
        }
        if (z && !str.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str = str + RemoteSettings.FORWARD_SLASH_STRING;
        }
        if (!str.startsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            localUrlforFullPath = localUrlforFullPath(normalizePath(localFilesystemURL.path + RemoteSettings.FORWARD_SLASH_STRING + str));
        } else {
            localUrlforFullPath = localUrlforFullPath(normalizePath(str));
        }
        getFileMetadataForLocalURL(localUrlforFullPath);
        boolean isDirectory = isDirectory(localUrlforFullPath.path);
        if (!z || isDirectory) {
            if (!z && isDirectory) {
                throw new TypeMismatchException("path doesn't exist or is directory");
            }
            return makeEntryForURL(localUrlforFullPath);
        }
        throw new TypeMismatchException("path doesn't exist or is file");
    }

    @Override // org.apache.cordova.file.Filesystem
    public JSONObject getFileMetadataForLocalURL(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("size", localFilesystemURL.isDirectory ? 0L : getAssetSize(localFilesystemURL.path));
            jSONObject.put("type", localFilesystemURL.isDirectory ? "text/directory" : this.resourceApi.getMimeType(toNativeUri(localFilesystemURL)));
            jSONObject.put("name", new File(localFilesystemURL.path).getName());
            jSONObject.put("fullPath", localFilesystemURL.path);
            jSONObject.put("lastModifiedDate", 0);
            return jSONObject;
        } catch (JSONException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.cordova.file.Filesystem
    public long writeToFileAtURL(LocalFilesystemURL localFilesystemURL, String str, int i, boolean z) throws NoModificationAllowedException, IOException {
        throw new NoModificationAllowedException("Assets are read-only");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.cordova.file.Filesystem
    public long truncateFileAtURL(LocalFilesystemURL localFilesystemURL, long j) throws IOException, NoModificationAllowedException {
        throw new NoModificationAllowedException("Assets are read-only");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.cordova.file.Filesystem
    public String filesystemPathForURL(LocalFilesystemURL localFilesystemURL) {
        return new File(this.rootUri.getPath(), localFilesystemURL.path).toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.cordova.file.Filesystem
    public boolean removeFileAtLocalURL(LocalFilesystemURL localFilesystemURL) throws InvalidModificationException, NoModificationAllowedException {
        throw new NoModificationAllowedException("Assets are read-only");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.cordova.file.Filesystem
    public boolean recursiveRemoveFileAtLocalURL(LocalFilesystemURL localFilesystemURL) throws NoModificationAllowedException {
        throw new NoModificationAllowedException("Assets are read-only");
    }
}
