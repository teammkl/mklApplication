package com.example.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private final SQLiteDatabase db;
    private static final String TABLE_NAME = "restaurant_table";
    private static final String COL0 = "id";
    private static final String COL1 = "business_id";
    private static final String COL2 = "name";
    private static final String COL3 = "address";
    private static final String COL4 = "city";
    private static final String COL5 = "state";
    private static final String COL6 = "postal_code";
    private static final String COL7 = "latitude";
    private static final String COL8 = "longitude";
    private static final String COL9 = "stars";
    private static final String COL10 = "review_count";
    private static final String COL11 = "is_open";
    private static final String COL12 = "categories";
    private static final String COL13 = "hours";
    private static final String COL14 = "is_in_my_restaurants";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE restaurant_table (id INTEGER PRIMARY, name TEXT, stars TEXT);
//        db.execSQL("DROP TABLE " + TABLE_NAME);
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL1 + " TEXT, "
                + COL2 + " TEXT, "
                + COL3 + " TEXT, "
                + COL4 + " TEXT, "
                + COL5 + " TEXT, "
                + COL6 + " TEXT, "
                + COL7 + " TEXT, "
                + COL8 + " TEXT, "
                + COL9 + " REAL, "
                + COL10 + " INTEGER, "
                + COL11 + " INTEGER, "
                + COL12 + " TEXT, "
                + COL13 + " TEXT, "
                + COL14 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String business_id, String name, String address, String city, String state,
                           String postal_code, String latitude, String longitude, String stars,
                           String review_count, String is_open, String categories, String hours) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, business_id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, address);
        contentValues.put(COL4, city);
        contentValues.put(COL5, state);
        contentValues.put(COL6, postal_code);
        contentValues.put(COL7, latitude);
        contentValues.put(COL8, longitude);
        contentValues.put(COL9, stars);
        contentValues.put(COL10, review_count);
        contentValues.put(COL11, is_open);
        contentValues.put(COL12, categories);
        contentValues.put(COL13, hours);
        contentValues.put(COL14, 0);

//        Log.d(TAG, "addData: Adding " + name + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return (result != -1); // returns true if add was successful
    }

    public int deleteAllData() {
        return db.delete(TABLE_NAME, null, null);
    }

    public int resetAllData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL14, 0);
        return db.update(TABLE_NAME, contentValues, null, null);
    }

    public int updateData(int newVal, String business_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL14, newVal);
        String whereClause = "business_id = " + business_id;
        return db.update(TABLE_NAME, contentValues, "business_id = ?", new String[]{business_id});
    }


    public Cursor getData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int size() {
        return this.getData().getCount();
    }
}
