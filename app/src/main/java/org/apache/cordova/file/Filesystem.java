package org.apache.cordova.file;

import android.net.Uri;
import com.google.firebase.sessions.settings.RemoteSettings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaResourceApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class Filesystem {
    static String DEFAULT_HOSTNAME = "localhost";
    static String SCHEME_HTTPS = "https";
    public final String name;
    protected final CordovaPreferences preferences;
    protected final CordovaResourceApi resourceApi;
    private JSONObject rootEntry;
    protected final Uri rootUri;

    /* loaded from: classes.dex */
    public interface ReadFileCallback {
        void handleData(InputStream inputStream, String str) throws IOException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract LocalFilesystemURL URLforFilesystemPath(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean canRemoveFileAtLocalURL(LocalFilesystemURL localFilesystemURL);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract String filesystemPathForURL(LocalFilesystemURL localFilesystemURL);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract JSONObject getFileForLocalURL(LocalFilesystemURL localFilesystemURL, String str, JSONObject jSONObject, boolean z) throws FileExistsException, IOException, TypeMismatchException, EncodingException, JSONException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract JSONObject getFileMetadataForLocalURL(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException;

    public long getFreeSpaceInBytes() {
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract LocalFilesystemURL[] listChildren(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean recursiveRemoveFileAtLocalURL(LocalFilesystemURL localFilesystemURL) throws FileExistsException, NoModificationAllowedException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean removeFileAtLocalURL(LocalFilesystemURL localFilesystemURL) throws InvalidModificationException, NoModificationAllowedException;

    public abstract LocalFilesystemURL toLocalUri(Uri uri);

    public abstract Uri toNativeUri(LocalFilesystemURL localFilesystemURL);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long truncateFileAtURL(LocalFilesystemURL localFilesystemURL, long j) throws IOException, NoModificationAllowedException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract long writeToFileAtURL(LocalFilesystemURL localFilesystemURL, String str, int i, boolean z) throws NoModificationAllowedException, IOException;

    public Filesystem(Uri uri, String str, CordovaResourceApi cordovaResourceApi, CordovaPreferences cordovaPreferences) {
        this.rootUri = uri;
        this.name = str;
        this.resourceApi = cordovaResourceApi;
        this.preferences = cordovaPreferences;
    }

    public static JSONObject makeEntryForURL(LocalFilesystemURL localFilesystemURL, Uri uri) {
        String[] split;
        try {
            String str = localFilesystemURL.path;
            String str2 = str.substring(0, str.length() - (str.endsWith(RemoteSettings.FORWARD_SLASH_STRING) ? 1 : 0)).split("/+")[split.length - 1];
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("isFile", !localFilesystemURL.isDirectory);
            jSONObject.put("isDirectory", localFilesystemURL.isDirectory);
            jSONObject.put("name", str2);
            jSONObject.put("fullPath", str);
            jSONObject.put("filesystemName", localFilesystemURL.fsName);
            jSONObject.put("filesystem", !"temporary".equals(localFilesystemURL.fsName) ? 1 : 0);
            String uri2 = uri.toString();
            if (localFilesystemURL.isDirectory && !uri2.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
                uri2 = uri2 + RemoteSettings.FORWARD_SLASH_STRING;
            }
            jSONObject.put("nativeURL", uri2);
            return jSONObject;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public JSONObject makeEntryForURL(LocalFilesystemURL localFilesystemURL) {
        Uri nativeUri = toNativeUri(localFilesystemURL);
        if (nativeUri == null) {
            return null;
        }
        return makeEntryForURL(localFilesystemURL, nativeUri);
    }

    public JSONObject makeEntryForNativeUri(Uri uri) {
        LocalFilesystemURL localUri = toLocalUri(uri);
        if (localUri == null) {
            return null;
        }
        return makeEntryForURL(localUri, uri);
    }

    public JSONObject getEntryForLocalURL(LocalFilesystemURL localFilesystemURL) throws IOException {
        return makeEntryForURL(localFilesystemURL);
    }

    public JSONObject makeEntryForFile(File file) {
        return makeEntryForNativeUri(Uri.fromFile(file));
    }

    public final JSONArray readEntriesAtLocalURL(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException {
        LocalFilesystemURL[] listChildren = listChildren(localFilesystemURL);
        JSONArray jSONArray = new JSONArray();
        if (listChildren != null) {
            for (LocalFilesystemURL localFilesystemURL2 : listChildren) {
                jSONArray.put(makeEntryForURL(localFilesystemURL2));
            }
        }
        return jSONArray;
    }

    public Uri getRootUri() {
        return this.rootUri;
    }

    public boolean exists(LocalFilesystemURL localFilesystemURL) {
        try {
            getFileMetadataForLocalURL(localFilesystemURL);
            return true;
        } catch (FileNotFoundException unused) {
            return false;
        }
    }

    public Uri nativeUriForFullPath(String str) {
        if (str != null) {
            String encodedPath = Uri.fromFile(new File(str)).getEncodedPath();
            if (encodedPath.startsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
                encodedPath = encodedPath.substring(1);
            }
            return this.rootUri.buildUpon().appendEncodedPath(encodedPath).build();
        }
        return null;
    }

    public LocalFilesystemURL localUrlforFullPath(String str) {
        Uri nativeUriForFullPath = nativeUriForFullPath(str);
        if (nativeUriForFullPath != null) {
            return toLocalUri(nativeUriForFullPath);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String normalizePath(String str) {
        boolean startsWith = str.startsWith(RemoteSettings.FORWARD_SLASH_STRING);
        if (startsWith) {
            str = str.replaceFirst("/+", "");
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(str.split("/+")));
        int i = 0;
        while (i < arrayList.size()) {
            if (((String) arrayList.get(i)).equals("..")) {
                arrayList.remove(i);
                if (i > 0) {
                    arrayList.remove(i - 1);
                    i--;
                }
            }
            i++;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append(RemoteSettings.FORWARD_SLASH_STRING);
            sb.append((String) it.next());
        }
        if (startsWith) {
            return sb.toString();
        }
        return sb.toString().substring(1);
    }

    public JSONObject getRootEntry() {
        if (this.rootEntry == null) {
            this.rootEntry = makeEntryForNativeUri(this.rootUri);
        }
        return this.rootEntry;
    }

    public JSONObject getParentForLocalURL(LocalFilesystemURL localFilesystemURL) throws IOException {
        Uri uri = localFilesystemURL.uri;
        String parent = new File(localFilesystemURL.uri.getPath()).getParent();
        if (!RemoteSettings.FORWARD_SLASH_STRING.equals(parent)) {
            uri = localFilesystemURL.uri.buildUpon().path(parent + '/').build();
        }
        return getEntryForLocalURL(LocalFilesystemURL.parse(uri));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LocalFilesystemURL makeDestinationURL(String str, LocalFilesystemURL localFilesystemURL, LocalFilesystemURL localFilesystemURL2, boolean z) {
        String str2;
        if ("null".equals(str) || "".equals(str)) {
            str = localFilesystemURL.uri.getLastPathSegment();
        }
        String uri = localFilesystemURL2.uri.toString();
        if (!uri.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str2 = uri + RemoteSettings.FORWARD_SLASH_STRING + str;
        } else {
            str2 = uri + str;
        }
        if (z) {
            str2 = str2 + '/';
        }
        return LocalFilesystemURL.parse(str2);
    }

    public JSONObject copyFileToURL(LocalFilesystemURL localFilesystemURL, String str, Filesystem filesystem, LocalFilesystemURL localFilesystemURL2, boolean z) throws IOException, InvalidModificationException, JSONException, NoModificationAllowedException, FileExistsException {
        if (z && !filesystem.canRemoveFileAtLocalURL(localFilesystemURL2)) {
            throw new NoModificationAllowedException("Cannot move file at source URL");
        }
        LocalFilesystemURL makeDestinationURL = makeDestinationURL(str, localFilesystemURL2, localFilesystemURL, localFilesystemURL2.isDirectory);
        CordovaResourceApi.OpenForReadResult openForRead = this.resourceApi.openForRead(filesystem.toNativeUri(localFilesystemURL2));
        try {
            this.resourceApi.copyResource(openForRead, getOutputStreamForURL(makeDestinationURL));
            if (z) {
                filesystem.removeFileAtLocalURL(localFilesystemURL2);
            }
            return getEntryForLocalURL(makeDestinationURL);
        } catch (IOException e) {
            openForRead.inputStream.close();
            throw e;
        }
    }

    public OutputStream getOutputStreamForURL(LocalFilesystemURL localFilesystemURL) throws IOException {
        return this.resourceApi.openOutputStream(toNativeUri(localFilesystemURL));
    }

    public void readFileAtURL(LocalFilesystemURL localFilesystemURL, long j, long j2, ReadFileCallback readFileCallback) throws IOException {
        CordovaResourceApi.OpenForReadResult openForRead = this.resourceApi.openForRead(toNativeUri(localFilesystemURL));
        if (j2 < 0) {
            j2 = openForRead.length;
        }
        long j3 = j2 - j;
        if (j > 0) {
            try {
                openForRead.inputStream.skip(j);
            } finally {
                openForRead.inputStream.close();
            }
        }
        InputStream inputStream = openForRead.inputStream;
        if (j2 < openForRead.length) {
            inputStream = new LimitedInputStream(inputStream, j3);
        }
        readFileCallback.handleData(inputStream, openForRead.mimeType);
    }

    /* loaded from: classes.dex */
    protected class LimitedInputStream extends FilterInputStream {
        long numBytesToRead;

        public LimitedInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.numBytesToRead = j;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            long j = this.numBytesToRead;
            if (j <= 0) {
                return -1;
            }
            this.numBytesToRead = j - 1;
            return this.in.read();
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            long j = this.numBytesToRead;
            if (j <= 0) {
                return -1;
            }
            if (i2 > j) {
                i2 = (int) j;
            }
            int read = this.in.read(bArr, i, i2);
            this.numBytesToRead -= read;
            return read;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Uri.Builder createLocalUriBuilder() {
        String lowerCase = this.preferences.getString("scheme", SCHEME_HTTPS).toLowerCase();
        String lowerCase2 = this.preferences.getString("hostname", DEFAULT_HOSTNAME).toLowerCase();
        return new Uri.Builder().scheme(lowerCase).authority(lowerCase2).path(LocalFilesystemURL.fsNameToCdvKeyword(this.name));
    }
}
