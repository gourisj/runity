package com.example.gjha.runity.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by gjha3 on 7/31/15.
 */
public class NGO {
    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "NGO";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_NGONAME = "NGOname";
    public static final String COL_BIO = "bio";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_NGONAME, COL_BIO };

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_NGONAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_BIO + " TEXT NOT NULL DEFAULT ''"
                    + ")";

    // Fields corresponding to database columns
    public long id = -1;
    public String ngoname = "";
    public String bio = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public NGO () {
    }

    /**
     * Convert information from the database into a Person object.
     */
    public NGO (final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.ngoname = cursor.getString(1);
        this.bio = cursor.getString(2);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_NGONAME, ngoname);
        values.put(COL_BIO, bio);

        return values;
    }
}
