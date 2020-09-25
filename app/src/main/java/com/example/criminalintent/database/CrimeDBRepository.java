package com.example.criminalintent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.Schema.CrimeDBSchema;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.IRepository;

import static com.example.criminalintent.Schema.CrimeDBSchema.CrimeTable.Columns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository<Crime> {
    public static CrimeDBRepository sInstance;
    private SQLiteDatabase mDatabase;
    private List<Crime> mCrimes;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);
        return sInstance;
    }

    private CrimeDBRepository(Context context) {
        CrimeDBHelper crimeDBHelper = new CrimeDBHelper(context);

        mDatabase = crimeDBHelper.getWritableDatabase();
        mCrimes=getList();
    }

    @Override
    public List<Crime> getList() {
        List<Crime> crimeList=new ArrayList<>();

        Cursor cursor=mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor==null || cursor.getCount()== 0)
            return crimeList;

        try {
            cursor.moveToFirst();

            while (cursor.isAfterLast()){
                Crime crime = extractCursor(cursor);
                crimeList.add(crime);

                cursor.moveToNext();
            }

        }finally {
            cursor.close();
        }


        return crimeList;
    }

    @Override
    public Crime get(UUID id) {
        String whereClause=Columns.UUID+" =? ";
        String[] whereArgs=new String[]{id.toString()};
        Cursor cursor=mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        if(cursor==null || cursor.getCount()== 0)
            return null;

        try {
            cursor.moveToFirst();
            Crime crime=extractCursor(cursor);

            return crime;

        }finally {
            cursor.close();
        }
    }

    @Override
    public void insert(Crime crime) {
        ContentValues values = getContentValues(crime);
        getContentValues(crime);

        mDatabase.insert(CrimeDBSchema.CrimeTable.NAME
                , null, values);
    }

    @Override
    public void update(Crime crime) {
        ContentValues values=getContentValues(crime);
        String whereClause=Columns.UUID
                +" =?";
        String[] whereArgs=new String[]{crime.getId().toString()};

        mDatabase.update(CrimeDBSchema.CrimeTable.NAME,
                values,whereClause,whereArgs);
    }

    @Override
    public void delete(Crime crime) {
        String whereClause=Columns.UUID
                +" =?";
        String[] whereArgs=new String[]{crime.getId().toString()};

        mDatabase.delete(CrimeDBSchema.CrimeTable.NAME
        ,whereClause,whereArgs);

    }

    @Override
    public int getPosition(UUID id) {
        List<Crime> crimeList=getList();

        for (int i = 0; i < crimeList.size(); i++) {
            if(crimeList.get(i).getId()==id)
                return i;
        }
        return -1;
    }

    public Crime getCrime(UUID id){
        List<Crime> crimeList=getList();

        for (int i = 0; i < crimeList.size(); i++) {
            if(crimeList.get(i).getId()==id)
                return crimeList.get(i);
        }
        return new Crime(id);
    }

    public Crime getFirst(){
        return mCrimes.get(0);
    }

    public Crime getLast(){
        return mCrimes.get(mCrimes.size()-1);
    }

    public Crime getNext(UUID uuid){
        int crimeIndex=getPosition(uuid);
        if(crimeIndex<mCrimes.size()-1) {
            return mCrimes.get(crimeIndex + 1);
        } else {
            return mCrimes.get(0);
        }
    }

    public Crime getPrev(UUID uuid){
        int crimeIndex=getPosition(uuid);
        if(crimeIndex>0)
            return mCrimes.get(crimeIndex-1);
        else
            return mCrimes.get(mCrimes.size()-1);
    }

    private Crime extractCursor(Cursor cursor) {

        UUID uuid=UUID.fromString(cursor.getString
                (cursor.getColumnIndex(Columns.UUID)));
        String title=cursor.getString(cursor.getColumnIndex(
                Columns.TITLE
        ));
        Date date=new Date(cursor.getLong(cursor.getColumnIndex(
                Columns.DATE
        )));
        boolean isSolved=cursor.getInt(
                cursor.getColumnIndex(Columns.SOLVED))!=0;

        return new Crime(uuid,title,date,isSolved);
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Columns.UUID, crime.getId().toString());
        values.put(Columns.TITLE, crime.getTitle());
        values.put(Columns.DATE, crime.getDate().toString());
        values.put(Columns.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }
}
