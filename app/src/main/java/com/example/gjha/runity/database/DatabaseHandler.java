package com.example.gjha.runity.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gjha3 on 7/31/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler singleton;

    public static DatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NGOlist";

    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to use process context here
        this.context = context.getApplicationContext();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(NGO.CREATE_TABLE);

        NGO ngo = new NGO();
        ngo.ngoname = "Helpage India";
        ngo.bio = "An NGO to help elferly people";

        db.insert(NGO.TABLE_NAME, null, ngo.getContent());

        ngo.ngoname = "CRY";
        ngo.bio = "Child relief & You";

        db.insert(NGO.TABLE_NAME,null, ngo.getContent());

        ngo.ngoname = "Being Human";
        ngo.bio = "Supported by Salman Khan";

        db.insert(NGO.TABLE_NAME,null, ngo.getContent());

        ngo.ngoname = "Prayaas Blind School";
        ngo.bio = "School for the Blind children run by PRAYAAS";

        db.insert(NGO.TABLE_NAME,null, ngo.getContent());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized NGO getngo (final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(NGO.TABLE_NAME, NGO.FIELDS,
                NGO.COL_ID + " IS ?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        NGO item = null;
        if (cursor.moveToFirst()) {
            item = new NGO(cursor);
        }
        cursor.close();
        return item;
    }

    public synchronized boolean putngo (final NGO ngo) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if (ngo.id > -1) {
            result += db.update(NGO.TABLE_NAME, ngo.getContent(),
                    NGO.COL_ID + " IS ?",
                    new String[] { String.valueOf(ngo.id) });
        }

        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(NGO.TABLE_NAME, null,
                    ngo.getContent());

            if (id > -1) {
                ngo.id = id;
                success = true;
            }
        }

        if (success) {
            notifyProviderOnNGOChange();
        }

        return success;
    }

    public synchronized int removengo (final NGO ngo) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(NGO.TABLE_NAME,
                NGO.COL_ID + " IS ?",
                new String[] { Long.toString(ngo.id) });

        if (result > 0) {
            notifyProviderOnNGOChange();
        }
        return result;
    }

    private void notifyProviderOnNGOChange() {
        context.getContentResolver().notifyChange(
                NGOContentProvider.URI_NGOS, null, false);
    }
}
