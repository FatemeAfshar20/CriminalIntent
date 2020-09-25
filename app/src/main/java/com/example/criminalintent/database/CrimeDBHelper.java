package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.example.criminalintent.Schema.CrimeDBSchema.CrimeTable.Columns;
import com.example.criminalintent.Schema.CrimeDBSchema;

public class CrimeDBHelper extends SQLiteOpenHelper {
    private StringBuilder mQuery =new StringBuilder();
    public CrimeDBHelper(@Nullable Context context) {
        super(context, CrimeDBSchema.NAME, null, CrimeDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mQuery.append("CREATE TABLE "+CrimeDBSchema.CrimeTable.NAME+" ( ");
        mQuery.append(Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        mQuery.append(Columns.UUID + " TEXT NOT NULL, ");
        mQuery.append(Columns.TITLE + " TEXT, ");
        mQuery.append(Columns.DATE + " TEXT, ");
        mQuery.append(Columns.SOLVED + " INTEGER NOT NULL");
        mQuery.append(");");
        db.execSQL(mQuery.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
