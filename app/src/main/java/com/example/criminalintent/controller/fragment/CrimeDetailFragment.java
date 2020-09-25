package com.example.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.criminalintent.R;
import com.example.criminalintent.database.CrimeDBRepository;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;
import com.example.criminalintent.repository.IRepository;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String TAG = "CDF";
    public static final String ARGS_CRIME_ID = "crimeId";
    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final String FRAGMENT_TAG_TIME_PICKER = "fragment tag time picker";
    public static final int REQUEST_CODE_TIME_PICKER = 1;

    private EditText mEditTextTitle;
    private Button mButtonDate, mButtonTime,
            mButtonFirst,mButtonLast,mButtonNext,mButtonPrev;
    private CheckBox mCheckBoxSolved;
    private DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    private DateFormat mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);


    private Crime mCrime;
    //private IRepository mRepository;
    private UUID mCrimeId;
    private CrimeDBRepository mDatabase;

    public static CrimeDetailFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);

        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        mDatabase = CrimeDBRepository.getInstance(getContext());

        //this is storage of this fragment
        assert getArguments() != null;
        mCrimeId= (UUID) getArguments().getSerializable(ARGS_CRIME_ID);
        assert mCrimeId != null;
        mCrime = (Crime) mDatabase.get(mCrimeId);
    }

    /**
     * 1. Inflate the layout (or create layout in code)
     * 2. find all views
     * 3. logic for all views (like setListeners)
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);

        findViews(view);
        initViews();
        setListeners();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateCrime();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date userSelectedDate =
                    (Date) data.getSerializableExtra(
                            DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        }
        if(requestCode==REQUEST_CODE_TIME_PICKER){
            Calendar userSelectedTime=
                    (Calendar) data.getSerializableExtra(
                            TimePickerFragment.EXTRA_TIME_SELECTED);
            updateCrimeDate(userSelectedTime.getTime());
        }
    }

    private void findViews(View view) {
        mEditTextTitle = view.findViewById(R.id.crime_title);
        mButtonDate = view.findViewById(R.id.crime_date);
        mButtonTime = view.findViewById(R.id.crime_time);
        mButtonFirst=view.findViewById(R.id.btn_first);
        mButtonLast=view.findViewById(R.id.btn_last);
        mButtonNext=view.findViewById(R.id.btn_next);
        mButtonPrev=view.findViewById(R.id.btn_prev);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);

    }

    private void initViews() {
        //DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        mEditTextTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonDate.setText(mDateFormat.format(mCrime.getDate()));
        mButtonTime.setText(DateFormat.getTimeInstance().format(mCrime.getTime()));
    }

    private void setListeners() {
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s + ", " + start + ", " + before + ", " + count);

                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(mCrime.getDate());

                //create parent-child relations between CDF and DPF
                datePickerFragment.setTargetFragment(
                        CrimeDetailFragment.this,
                        REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DATE_PICKER);
            }
        });

        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment=
                        TimePickerFragment.newInstance(mCrime.getTime());

                timePickerFragment.setTargetFragment(
                        CrimeDetailFragment.this,
                        REQUEST_CODE_TIME_PICKER);

                timePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_TIME_PICKER);

            }
        });

        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mCrime= mDatabase.getFirst();
               mCrimeId=mCrime.getId();
                initViews();
            }
        });

        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrime= mDatabase.getLast();
                mCrimeId=mCrime.getId();
                initViews();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrime= mDatabase.getNext(mCrime.getId());
                mCrimeId=mCrime.getId();
                initViews();
            }
        });

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrime= mDatabase.getPrev(mCrime.getId());
                mCrimeId=mCrime.getId();
                initViews();
            }
        });
    }

    private void updateCrime() {
        mDatabase.update(mCrime);
    }

    private void updateCrimeDate(Date userSelectedDate) {
        mCrime.setDate(userSelectedDate);
        updateCrime();

        mButtonDate.setText(mDateFormat.format(mCrime.getDate()));
        mButtonTime.setText(mTimeFormat.format(mCrime.getDate()));
    }
}