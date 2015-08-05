package com.example.gjha.runity.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class CompanyContentProvider extends ContentProvider {

    // All URIs share these parts
    public static final String AUTHORITY = "com.example.gjha.runity.provider.company";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String COMPANYS = SCHEME + AUTHORITY + "/company";
    public static final Uri URI_COMPANYS = Uri.parse(COMPANYS);
    // Used for a single person, just add the id to the end
    public static final String COMPANY_BASE = COMPANYS + "/";

    public CompanyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        if (URI_COMPANYS.equals(uri)) {
            result = DatabaseHandlerC
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(COMPANY.TABLE_NAME, COMPANY.FIELDS, null, null, null,
                            null, null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_COMPANYS);
        }
        else if (uri.toString().startsWith(COMPANY_BASE)) {
            final long id = Long.parseLong(uri.getLastPathSegment());
            result = DatabaseHandlerC
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(COMPANY.TABLE_NAME, COMPANY.FIELDS,
                            COMPANY.COL_ID + " IS ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_COMPANYS);
        }
        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
