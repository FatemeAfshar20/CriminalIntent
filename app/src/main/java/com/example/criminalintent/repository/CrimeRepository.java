package com.example.criminalintent.repository;

import com.example.criminalintent.model.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeRepository implements IRepository<Crime> {

    private static final int CRIME_SIZE = 100;
    private static CrimeRepository sInstance;

    public static CrimeRepository getInstance() {
        if (sInstance == null)
            sInstance = new CrimeRepository();

        return sInstance;
    }

    private List<Crime> mCrimes;

    private CrimeRepository() {
        //create dummy object for test.
        mCrimes = new ArrayList<>();
        for (int i = 0; i < CRIME_SIZE; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime#" + (i + 1));
            crime.setSolved(i % 2 == 0);

            mCrimes.add(crime);
        }
    }

    @Override
    public List<Crime> getList() {
        return mCrimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    @Override
    public Crime get(UUID Id) {
        for (Crime crime: mCrimes) {
            if (crime.getId().equals(Id))
                return crime;
        }

        return null;
    }

    @Override
    public void insert(Crime crime) {
        mCrimes.add(crime);
    }

    @Override
    public void delete(Crime crime) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crime.getId())) {
                mCrimes.remove(i);
                return;
            }
        }
    }

    @Override
    public void update(Crime crime) {
        Crime findCrime = get(crime.getId());
        findCrime.setTitle(crime.getTitle());
        findCrime.setSolved(crime.isSolved());
        findCrime.setDate(crime.getDate());
    }

    @Override
    public int getPosition(UUID id) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(id))
                return i;
        }

        return 0;
    }

    public Crime getFirst(){
        return mCrimes.get(0);
    }

    public Crime getLast(){
        return mCrimes.get(CRIME_SIZE-1);
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
            return mCrimes.get(CRIME_SIZE-1);
    }
}
