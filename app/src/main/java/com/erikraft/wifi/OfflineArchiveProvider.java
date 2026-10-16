package com.erikraft.wifi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Read-only, per-URI-grant provider for sharing an exported offline page.
 * Only files inside this app's private offline-sites directory are exposed.
 */
public final class OfflineArchiveProvider extends ContentProvider {
    static Uri uriFor(Context context, File file) throws IOException {
        File root = new File(context.getFilesDir(), "offline-sites").getCanonicalFile();
        File target = file.getCanonicalFile();
        if (!target.getPath().startsWith(root.getPath() + File.separator) || !target.isFile()) {
            throw new IOException("The selected file is not an offline archive.");
        }
        return new Uri.Builder()
                .scheme("content")
                .authority(context.getPackageName() + ".offlinearchives")
                .appendPath(target.getName())
                .build();
    }

    private File fileFor(Uri uri) throws FileNotFoundException {
        if (getContext() == null || uri.getLastPathSegment() == null
                || uri.getPathSegments().size() != 1) {
            throw new FileNotFoundException("Invalid offline archive URI.");
        }
        try {
            File root = new File(getContext().getFilesDir(), "offline-sites").getCanonicalFile();
            File target = new File(root, uri.getLastPathSegment()).getCanonicalFile();
            if (!target.getPath().startsWith(root.getPath() + File.separator)
                    || !target.isFile()) {
                throw new FileNotFoundException("Offline archive not found.");
            }
            return target;
        } catch (IOException e) {
            FileNotFoundException failure = new FileNotFoundException("Could not resolve offline archive.");
            failure.initCause(e);
            throw failure;
        }
    }

    @Override public boolean onCreate() { return true; }

    @Override public String getType(Uri uri) {
        String name = uri.getLastPathSegment();
        if (name == null) return "application/octet-stream";
        String lower = name.toLowerCase(java.util.Locale.ROOT);
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html";
        if (lower.endsWith(".mht") || lower.endsWith(".mhtml")) return "application/x-mimearchive";
        return "application/octet-stream";
    }

    @Override public Cursor query(Uri uri, String[] projection, String selection,
                                  String[] selectionArgs, String sortOrder) {
        try {
            File file = fileFor(uri);
            String[] columns = projection == null
                    ? new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE}
                    : projection;
            MatrixCursor cursor = new MatrixCursor(columns);
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                if (OpenableColumns.DISPLAY_NAME.equals(columns[i])) values[i] = file.getName();
                else if (OpenableColumns.SIZE.equals(columns[i])) values[i] = file.length();
            }
            cursor.addRow(values);
            return cursor;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (!"r".equals(mode)) throw new FileNotFoundException("Offline archives are read-only.");
        return ParcelFileDescriptor.open(fileFor(uri), ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Override public Uri insert(Uri uri, ContentValues values) { return null; }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
