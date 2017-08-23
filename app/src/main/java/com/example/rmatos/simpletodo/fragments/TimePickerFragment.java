package com.example.rmatos.simpletodo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.rmatos.simpletodo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rmatos on 08/08/17.
 * TODO: Place in abstract
 */

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME = "com.example.rmatos.simpletodo.time";

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Used to set initial date to date passed through
        final Date date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);

        //Sets initial time, method depends on API level
        //TODO: Make it work
        if (Build.VERSION.SDK_INT >= 23) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Date of task")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour, minute;

                        //Sets time, method depends on API level
                        if (Build.VERSION.SDK_INT >= 23) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minute = mTimePicker.getCurrentMinute();
                        }

                        Date date = new GregorianCalendar(year, month, day, hour, minute, 0).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
       /* View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Date of task")
                .setPositiveButton(android.R.string.ok, null)
                .create();*/
    }

    private void sendResult(int resultCode, Date time) {
        //If called fragment no longer exists?
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
