package org.apache.cordova.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import com.google.firebase.sessions.settings.RemoteSettings;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaResourceApi;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LocalFilesystem extends Filesystem {
    private final Context context;

    public LocalFilesystem(String str, Context context, CordovaResourceApi cordovaResourceApi, File file, CordovaPreferences cordovaPreferences) {
        super(Uri.fromFile(file).buildUpon().appendEncodedPath("").build(), str, cordovaResourceApi, cordovaPreferences);
        this.context = context;
    }

    public String filesystemPathForFullPath(String str) {
        return new File(this.rootUri.getPath(), str).toString();
    }

    @Override // org.apache.cordova.file.Filesystem
    public String filesystemPathForURL(LocalFilesystemURL localFilesystemURL) {
        return filesystemPathForFullPath(localFilesystemURL.path);
    }

    private String fullPathForFilesystemPath(String str) {
        if (str == null || !str.startsWith(this.rootUri.getPath())) {
            return null;
        }
        return str.substring(this.rootUri.getPath().length() - 1);
    }

    @Override // org.apache.cordova.file.Filesystem
    public Uri toNativeUri(LocalFilesystemURL localFilesystemURL) {
        return nativeUriForFullPath(localFilesystemURL.path);
    }

    @Override // org.apache.cordova.file.Filesystem
    public LocalFilesystemURL toLocalUri(Uri uri) {
        if ("file".equals(uri.getScheme())) {
            File file = new File(uri.getPath());
            Uri fromFile = Uri.fromFile(file);
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
                if (file.isDirectory()) {
                    createLocalUriBuilder.appendEncodedPath("");
                }
                return LocalFilesystemURL.parse(createLocalUriBuilder.build());
            }
            return null;
        }
        return null;
    }

    @Override // org.apache.cordova.file.Filesystem
    public LocalFilesystemURL URLforFilesystemPath(String str) {
        return localUrlforFullPath(fullPathForFilesystemPath(str));
    }

    @Override // org.apache.cordova.file.Filesystem
    public JSONObject getFileForLocalURL(LocalFilesystemURL localFilesystemURL, String str, JSONObject jSONObject, boolean z) throws FileExistsException, IOException, TypeMismatchException, EncodingException, JSONException {
        boolean z2;
        LocalFilesystemURL localUrlforFullPath;
        if (jSONObject != null) {
            boolean optBoolean = jSONObject.optBoolean("create");
            z2 = optBoolean ? jSONObject.optBoolean("exclusive") : false;
            r0 = optBoolean;
        } else {
            z2 = false;
        }
        if (str.contains(":")) {
            throw new EncodingException("This path has an invalid \":\" in it.");
        }
        if (z && !str.endsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            str = str + RemoteSettings.FORWARD_SLASH_STRING;
        }
        if (!str.startsWith(RemoteSettings.FORWARD_SLASH_STRING)) {
            localUrlforFullPath = localUrlforFullPath(normalizePath(localFilesystemURL.path + RemoteSettings.FORWARD_SLASH_STRING + str));
        } else {
            localUrlforFullPath = localUrlforFullPath(normalizePath(str));
        }
        File file = new File(filesystemPathForURL(localUrlforFullPath));
        if (r0) {
            if (z2 && file.exists()) {
                throw new FileExistsException("create/exclusive fails");
            }
            if (z) {
                file.mkdir();
            } else {
                file.createNewFile();
            }
            if (!file.exists()) {
                throw new FileExistsException("create fails");
            }
        } else if (!file.exists()) {
            throw new FileNotFoundException("path does not exist");
        } else {
            if (z) {
                if (file.isFile()) {
                    throw new TypeMismatchException("path doesn't exist or is file");
                }
            } else if (file.isDirectory()) {
                throw new TypeMismatchException("path doesn't exist or is directory");
            }
        }
        return makeEntryForURL(localUrlforFullPath);
    }

    @Override // org.apache.cordova.file.Filesystem
    public boolean removeFileAtLocalURL(LocalFilesystemURL localFilesystemURL) throws InvalidModificationException {
        File file = new File(filesystemPathForURL(localFilesystemURL));
        if (file.isDirectory() && file.list().length > 0) {
            throw new InvalidModificationException("You can't delete a directory that is not empty.");
        }
        return file.delete();
    }

    @Override // org.apache.cordova.file.Filesystem
    public boolean exists(LocalFilesystemURL localFilesystemURL) {
        return new File(filesystemPathForURL(localFilesystemURL)).exists();
    }

    @Override // org.apache.cordova.file.Filesystem
    public long getFreeSpaceInBytes() {
        return DirectoryManager.getFreeSpaceInBytes(this.rootUri.getPath());
    }

    @Override // org.apache.cordova.file.Filesystem
    public boolean recursiveRemoveFileAtLocalURL(LocalFilesystemURL localFilesystemURL) throws FileExistsException {
        return removeDirRecursively(new File(filesystemPathForURL(localFilesystemURL)));
    }

    protected boolean removeDirRecursively(File file) throws FileExistsException {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                removeDirRecursively(file2);
            }
        }
        if (file.delete()) {
            return true;
        }
        throw new FileExistsException("could not delete: " + file.getName());
    }

    @Override // org.apache.cordova.file.Filesystem
    public LocalFilesystemURL[] listChildren(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException {
        File file = new File(filesystemPathForURL(localFilesystemURL));
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return null;
        }
        LocalFilesystemURL[] localFilesystemURLArr = new LocalFilesystemURL[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            localFilesystemURLArr[i] = URLforFilesystemPath(listFiles[i].getPath());
        }
        return localFilesystemURLArr;
    }

    @Override // org.apache.cordova.file.Filesystem
    public JSONObject getFileMetadataForLocalURL(LocalFilesystemURL localFilesystemURL) throws FileNotFoundException {
        File file = new File(filesystemPathForURL(localFilesystemURL));
        if (!file.exists()) {
            throw new FileNotFoundException("File at " + localFilesystemURL.uri + " does not exist.");
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("size", file.isDirectory() ? 0L : file.length());
            jSONObject.put("type", this.resourceApi.getMimeType(Uri.fromFile(file)));
            jSONObject.put("name", file.getName());
            jSONObject.put("fullPath", localFilesystemURL.path);
            jSONObject.put("lastModifiedDate", file.lastModified());
            return jSONObject;
        } catch (JSONException unused) {
            return null;
        }
    }

    private void copyFile(Filesystem filesystem, LocalFilesystemURL localFilesystemURL, File file, boolean z) throws IOException, InvalidModificationException, NoModificationAllowedException {
        String filesystemPathForURL;
        if (z && (filesystemPathForURL = filesystem.filesystemPathForURL(localFilesystemURL)) != null && new File(filesystemPathForURL).renameTo(file)) {
            return;
        }
        this.resourceApi.copyResource(this.resourceApi.openForRead(filesystem.toNativeUri(localFilesystemURL)), new FileOutputStream(file));
        if (z) {
            filesystem.removeFileAtLocalURL(localFilesystemURL);
        }
    }

    private void copyDirectory(Filesystem filesystem, LocalFilesystemURL localFilesystemURL, File file, boolean z) throws IOException, NoModificationAllowedException, InvalidModificationException, FileExistsException {
        LocalFilesystemURL[] listChildren;
        String filesystemPathForURL;
        if (z && (filesystemPathForURL = filesystem.filesystemPathForURL(localFilesystemURL)) != null) {
            File file2 = new File(filesystemPathForURL);
            if (file.exists()) {
                if (file.list().length > 0) {
                    throw new InvalidModificationException("directory is not empty");
                }
                file.delete();
            }
            if (file2.renameTo(file)) {
                return;
            }
        }
        if (file.exists()) {
            if (file.list().length > 0) {
                throw new InvalidModificationException("directory is not empty");
            }
        } else if (!file.mkdir()) {
            throw new NoModificationAllowedException("Couldn't create the destination directory");
        }
        for (LocalFilesystemURL localFilesystemURL2 : filesystem.listChildren(localFilesystemURL)) {
            File file3 = new File(file, new File(localFilesystemURL2.path).getName());
            if (localFilesystemURL2.isDirectory) {
                copyDirectory(filesystem, localFilesystemURL2, file3, false);
            } else {
                copyFile(filesystem, localFilesystemURL2, file3, false);
            }
        }
        if (z) {
            filesystem.recursiveRemoveFileAtLocalURL(localFilesystemURL);
        }
    }

    @Override // org.apache.cordova.file.Filesystem
    public JSONObject copyFileToURL(LocalFilesystemURL localFilesystemURL, String str, Filesystem filesystem, LocalFilesystemURL localFilesystemURL2, boolean z) throws IOException, InvalidModificationException, JSONException, NoModificationAllowedException, FileExistsException {
        if (!new File(filesystemPathForURL(localFilesystemURL)).exists()) {
            throw new FileNotFoundException("The source does not exist");
        }
        LocalFilesystemURL makeDestinationURL = makeDestinationURL(str, localFilesystemURL2, localFilesystemURL, localFilesystemURL2.isDirectory);
        Uri nativeUri = toNativeUri(makeDestinationURL);
        Uri nativeUri2 = filesystem.toNativeUri(localFilesystemURL2);
        if (nativeUri.equals(nativeUri2)) {
            throw new InvalidModificationException("Can't copy onto itself");
        }
        if (z && !filesystem.canRemoveFileAtLocalURL(localFilesystemURL2)) {
            throw new InvalidModificationException("Source URL is read-only (cannot move)");
        }
        File file = new File(nativeUri.getPath());
        if (file.exists()) {
            if (!localFilesystemURL2.isDirectory && file.isDirectory()) {
                throw new InvalidModificationException("Can't copy/move a file to an existing directory");
            }
            if (localFilesystemURL2.isDirectory && file.isFile()) {
                throw new InvalidModificationException("Can't copy/move a directory to an existing file");
            }
        }
        if (localFilesystemURL2.isDirectory) {
            if (nativeUri.toString().startsWith(nativeUri2.toString() + '/')) {
                throw new InvalidModificationException("Can't copy directory into itself");
            }
            copyDirectory(filesystem, localFilesystemURL2, file, z);
        } else {
            copyFile(filesystem, localFilesystemURL2, file, z);
        }
        return makeEntryForURL(makeDestinationURL);
    }

    @Override // org.apache.cordova.file.Filesystem
    public long writeToFileAtURL(LocalFilesystemURL localFilesystemURL, String str, int i, boolean z) throws IOException, NoModificationAllowedException {
        boolean z2;
        byte[] bytes;
        if (i > 0) {
            truncateFileAtURL(localFilesystemURL, i);
            z2 = true;
        } else {
            z2 = false;
        }
        if (z) {
            bytes = Base64.decode(str, 0);
        } else {
            bytes = str.getBytes(Charset.defaultCharset());
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            int length = bytes.length;
            byte[] bArr = new byte[length];
            String filesystemPathForURL = filesystemPathForURL(localFilesystemURL);
            FileOutputStream fileOutputStream = new FileOutputStream(filesystemPathForURL, z2);
            byteArrayInputStream.read(bArr, 0, length);
            fileOutputStream.write(bArr, 0, bytes.length);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (isPublicDirectory(filesystemPathForURL)) {
                broadcastNewFile(Uri.fromFile(new File(filesystemPathForURL)));
            }
            return bytes.length;
        } catch (NullPointerException e) {
            NoModificationAllowedException noModificationAllowedException = new NoModificationAllowedException(localFilesystemURL.toString());
            noModificationAllowedException.initCause(e);
            throw noModificationAllowedException;
        }
    }

    private boolean isPublicDirectory(String str) {
        File[] externalMediaDirs;
        for (File file : this.context.getExternalMediaDirs()) {
            if (file != null && str.startsWith(file.getAbsolutePath())) {
                return true;
            }
        }
        return str.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    private void broadcastNewFile(Uri uri) {
        this.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", uri));
    }

    @Override // org.apache.cordova.file.Filesystem
    public long truncateFileAtURL(LocalFilesystemURL localFilesystemURL, long j) throws IOException {
        if (!new File(filesystemPathForURL(localFilesystemURL)).exists()) {
            throw new FileNotFoundException("File at " + localFilesystemURL.uri + " does not exist.");
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(filesystemPathForURL(localFilesystemURL), "rw");
        try {
            if (randomAccessFile.length() >= j) {
                randomAccessFile.getChannel().truncate(j);
                return j;
            }
            return randomAccessFile.length();
        } finally {
            randomAccessFile.close();
        }
    }

    @Override // org.apache.cordova.file.Filesystem
    public boolean canRemoveFileAtLocalURL(LocalFilesystemURL localFilesystemURL) {
        return new File(filesystemPathForURL(localFilesystemURL)).exists();
    }
}
