package com.example.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.criminalintent.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static final String ARG_CRIME_DATE = "Crime Date";
    public static final String EXTRA_TIME_SELECTED = "com.example.criminalintent.Time User Selected";
    private TimePicker mTimePicker;
    private MaterialButton mButtonOk, mButtonCancel;

    private Calendar mCalendar = Calendar.getInstance();

    private Date mDate;

    public static TimePickerFragment newInstance(Date date) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_CRIME_DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_picker, container, false);
        findViews(view);
        initView();
        setListener();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(android.R.string.dialog_alert_title);
    }

    private void findViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker);
        mButtonOk = view.findViewById(R.id.btn_ok_time_picker);
        mButtonCancel = view.findViewById(R.id.btn_cancel_time_picker);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        mCalendar.setTime(mDate);
        mTimePicker.setHour(mCalendar.get(Calendar.HOUR));
        mTimePicker.setMinute(mCalendar.get(Calendar.MINUTE));
    }

    private void setListener() {
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                sendTime();
                dismiss();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendTime() {
        Fragment fragment = getTargetFragment();
        Intent data = new Intent();

        mCalendar.set(Calendar.HOUR,mTimePicker.getHour());
        mCalendar.set(Calendar.MINUTE,mTimePicker.getMinute());
        data.putExtra(EXTRA_TIME_SELECTED, mCalendar);

        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }
}