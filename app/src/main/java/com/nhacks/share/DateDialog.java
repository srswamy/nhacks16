package com.nhacks.share;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Sagar on 11/7/2015.
 */
public abstract class DateDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    int mYear;
    int weekDay;

    public DateDialog() {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        weekDay = c.get(Calendar.DAY_OF_WEEK);

        return new android.app.DatePickerDialog(getActivity(), this, mYear, month, day);
    }

}
