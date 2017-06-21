package com.application.nikita.softteco.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.application.nikita.softteco.database.contracts.AddressContract.AddressEntry;
import static com.application.nikita.softteco.database.contracts.CompanyContract.CompanyEntry;
import static com.application.nikita.softteco.database.contracts.UserContract.UserEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ADDRESS_TABLE =
            "CREATE TABLE " + AddressEntry.TABLE_NAME + " (" +
                    AddressEntry._ID + " INTEGER PRIMARY KEY," +
                    AddressEntry.COLUMN_NAME_ADDRESS_STREET + TEXT_TYPE + COMMA_SEP +
                    AddressEntry.COLUMN_NAME_ADDRESS_SUITE + TEXT_TYPE + COMMA_SEP +
                    AddressEntry.COLUMN_NAME_ADDRESS_CITY + TEXT_TYPE + COMMA_SEP +
                    AddressEntry.COLUMN_NAME_ADDRESS_ZIPCODE + TEXT_TYPE + COMMA_SEP +
                    AddressEntry.COLUMN_NAME_ADDRESS_LATITUDE + REAL_TYPE + COMMA_SEP +
                    AddressEntry.COLUMN_NAME_ADDRESS_LONGITUDE + REAL_TYPE +
                    ");";
    private static final String SQL_CREATE_COMPANY_TABLE =
            "CREATE TABLE " + CompanyEntry.TABLE_NAME + " (" +
                    CompanyEntry._ID + " INTEGER PRIMARY KEY," +
                    CompanyEntry.COLUMN_NAME_COMPANY_NAME + TEXT_TYPE + COMMA_SEP +
                    CompanyEntry.COLUMN_NAME_COMPANY_CATCH_PHRASE + TEXT_TYPE + COMMA_SEP +
                    CompanyEntry.COLUMN_NAME_COMPANY_BS + TEXT_TYPE +
                    ");";
    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_USER_ID + INT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_USERNAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_EMAIL + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_ADDRESS + INT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_PHONE + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_WEBSITE + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_COMPANY + INT_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + UserEntry.COLUMN_NAME_USER_ADDRESS + ") REFERENCES " + AddressEntry.TABLE_NAME + " (" + AddressEntry._ID + ")" + COMMA_SEP +
                    "FOREIGN KEY (" + UserEntry.COLUMN_NAME_USER_COMPANY + ") REFERENCES " + CompanyEntry.TABLE_NAME + " (" + CompanyEntry._ID + ")" +
                    ");";
    private static final String SQL_DELETE_ADDRESS_TABLE =
            "DROP TABLE IF EXISTS " + AddressEntry.TABLE_NAME;
    private static final String SQL_DELETE_COMPANY_TABLE =
            "DROP TABLE IF EXISTS " + CompanyEntry.TABLE_NAME;
    private static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ADDRESS_TABLE);
        Log.d("DBHelper", AddressEntry.TABLE_NAME + " table created");
        db.execSQL(SQL_CREATE_COMPANY_TABLE);
        Log.d("DBHelper", CompanyEntry.TABLE_NAME + " table created");
        db.execSQL(SQL_CREATE_USER_TABLE);
        Log.d("DBHelper", UserEntry.TABLE_NAME + " table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_TABLE);
        db.execSQL(SQL_DELETE_COMPANY_TABLE);
        db.execSQL(SQL_DELETE_ADDRESS_TABLE);
        onCreate(db);
    }
}
