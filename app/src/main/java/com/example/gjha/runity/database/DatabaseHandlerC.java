package com.example.gjha.runity.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gjha3 on 7/31/15.
 */
public class DatabaseHandlerC extends SQLiteOpenHelper {
    private static DatabaseHandlerC singleton;

    public static DatabaseHandlerC getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandlerC(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CompanyList";

    private final Context context;

    public DatabaseHandlerC(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to use process context here
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(COMPANY.CREATE_TABLE);

        COMPANY company = new COMPANY();
        company.companyname = "WIPRO";
        company.bioc = "Applying Thought";

        db.insert(COMPANY.TABLE_NAME, null, company.getContent());

        company.companyname = "INFOSYS";
        company.bioc = "Driven By Value";

        db.insert(COMPANY.TABLE_NAME,null, company.getContent());;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public synchronized COMPANY getcompany (final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(COMPANY.TABLE_NAME, COMPANY.FIELDS,
                COMPANY.COL_ID + " IS ?", new String[] { String.valueOf(id)},null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        COMPANY item = null;
        if (cursor.moveToFirst()) {
            item = new COMPANY(cursor);
        }
        cursor.close();
        return item;
    }

    public synchronized boolean putcompany (final COMPANY company) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if (company.id > -1) {
            result += db.update(COMPANY.TABLE_NAME, company.getContent(),
                    COMPANY.COL_ID + " IS ?",
                    new String[] { String.valueOf(company.id) });
        }

        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(COMPANY.TABLE_NAME, null,
                    company.getContent());

            if (id > -1) {
                company.id = id;
                success = true;
            }
        }

        if (success) {
            notifyProviderOnCOMPANYChange();
        }

        return success;
    }

    public synchronized int removecompany (final COMPANY company) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(COMPANY.TABLE_NAME,
                COMPANY.COL_ID + " IS ?",
                new String[] { Long.toString(company.id) });

        if (result > 0) {
            notifyProviderOnCOMPANYChange();
        }
        return result;
    }

    private void notifyProviderOnCOMPANYChange() {
        context.getContentResolver().notifyChange(
                CompanyContentProvider.URI_COMPANYS, null, false);
    }
}
