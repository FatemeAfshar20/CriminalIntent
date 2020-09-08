package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.criminalintent.controller.fragment.CrimeListFragment;

import java.util.UUID;

public class CrimeListActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.Crime Id";

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, CrimeListActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,uuid);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        Intent intent=new Intent();
        UUID uuid=
                (UUID) intent.getSerializableExtra
                        (EXTRA_CRIME_ID);
        return CrimeListFragment.newInstance(uuid);
    }
}