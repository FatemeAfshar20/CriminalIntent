package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.animation.DepthPageTransformer;
import com.example.criminalintent.controller.fragment.CrimeDetailFragment;
import com.example.criminalintent.database.CrimeDBRepository;
import com.example.criminalintent.model.Crime;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.crimeId";
    public static final String TAG = "CPA";
    private CrimeDBRepository mDatabase;
    private UUID mCrimeId;

    private ViewPager2 mViewPagerCrimes;

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mDatabase = CrimeDBRepository.getInstance(this);
        mCrimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        findViews();
        initViews();
    }

    private void findViews() {
        mViewPagerCrimes = findViewById(R.id.view_pager_crimes);
    }

    private void initViews() {
        List<Crime> crimes = mDatabase.getList();
        CrimePagerAdapter adapter = new CrimePagerAdapter(this, crimes);
        mViewPagerCrimes.setAdapter(adapter);

        int currentIndex = mDatabase.getPosition(mCrimeId);
        mViewPagerCrimes.setCurrentItem(currentIndex);
        mViewPagerCrimes.setPageTransformer(new DepthPageTransformer());
    }

    private class CrimePagerAdapter extends FragmentStateAdapter {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Crime> crimes) {
            super(fragmentActivity);
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d(TAG, "position: " + (position + 1));

            Crime crime = mCrimes.get(position);
            CrimeDetailFragment crimeDetailFragment =
                    CrimeDetailFragment.newInstance(crime.getId());

            return crimeDetailFragment;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}