package com.example.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.R;
import com.example.criminalintent.controller.activity.CrimePagerActivity;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;
import com.example.criminalintent.repository.IRepository;

import java.text.DateFormat;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {

    public static final String TAG = "CLF";
    public static final String ARG_CRIME_ID = "Crime Id";
    private DateFormat mDateFormat=DateFormat.getDateInstance(DateFormat.SHORT);
    private DateFormat mTimeFormat=DateFormat.getTimeInstance(DateFormat.SHORT);

    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    private IRepository mRepository;

    private UUID mCrimeId;

    public static CrimeListFragment newInstance(UUID uuid) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,uuid);
        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimeId= (UUID) getArguments().
                get(ARG_CRIME_ID);

        mRepository = CrimeRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        findViews(view);
        initViews();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crime_list);
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void updateUI() {
        List<Crime> crimes = mRepository.getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyItemChanged(mRepository.getPosition(mCrimeId));
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_crime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    startActivity(intent);
                }
            });
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText("Date : "+ mDateFormat.format(mCrime.getDate())
                    +"   Time : "+mTimeFormat.format(mCrime.getTime()));
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_row_list, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + position);

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
    }
}