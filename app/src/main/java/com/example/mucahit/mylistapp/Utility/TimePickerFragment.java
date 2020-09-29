package com.example.mucahit.mylistapp.Utility;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mucahit.mylistapp.R;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener{

    Date tv1;
    TextView tv2;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        Calendar dateTime = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateTime.set(Calendar.MINUTE, minute);
        TextView tv1=(TextView)getActivity().findViewById(R.id.txtTIME);
        TextView tv2=(TextView)getActivity().findViewById(R.id.txtDATE);
        String date = (String) Calendar.getInstance().getTime().toString();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        String nowDate = mDay + "/"
                + (mMonth+1) + "/" + mYear;

        String tx2Date = tv2.getText().toString();

        if(tx2Date.equals(nowDate)){
            int hour = hourOfDay %24;
            if(dateTime.getTimeInMillis() >= c.getTimeInMillis()){
                tv1.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                        minute, hourOfDay < 12 ? "am" : "pm"));
            }

        }else{

        }

    }


}
