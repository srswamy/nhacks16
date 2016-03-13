package com.nhacks.share.Fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.nhacks.share.Adapters.TimeViewAdapter;
import com.nhacks.share.DateDialog;
import com.nhacks.share.Objects.AvailableTime;
import com.nhacks.share.Objects.Book;
import com.nhacks.share.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sagar on 3/12/2016.
 */
public class AddBookDetailsFragment extends Fragment {
    String category;
    private FloatingActionButton mFloatingSaveButton;
    private int lastRecordedReps = 0;
    private double lastRecordedWeight = 0;
    private EditText bookName;
    private EditText bookEdition;
    private EditText schoolName;
    TextView mDateView;
    ImageView calenderView;
    private RecyclerView mRecyclerView;

    Date mSelectedDate;
    ImageView mNextDay;
    ImageView mPrevDay;
    int curYear;
    private TimeViewAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.l_add_book_data, container, false);
        Bundle b = getArguments();
        if (b != null) {
            category = b.getString("category");
        }
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(category);
        mDateView = (TextView) layout.findViewById(R.id.dateText);

        bookName = (EditText) layout.findViewById(R.id.bookName);
        bookEdition = (EditText) layout.findViewById(R.id.bookEdition);
        schoolName = (EditText) layout.findViewById(R.id.schoolName);
        calenderView = (ImageView) layout.findViewById(R.id.datePicker);
        mNextDay = (ImageView) layout.findViewById(R.id.nextDayBtn);
        mPrevDay = (ImageView) layout.findViewById(R.id.prevDayBtn);
        Button addBook = (Button) layout.findViewById(R.id.add_btn);
        Button cancel = (Button) layout.findViewById(R.id.cancel_btn);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.logsRecyclerView);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                String name = bookName.getText().toString();
                String edition = bookEdition.getText().toString();
                String school = schoolName.getText().toString();

                Toast toast;
                if (name.equals("")) {
                    toast = Toast.makeText(getContext(), "Please enter Book Name!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    book.setName(name);
                }
                if (!edition.equals("")) {
                    book.setEdition(Integer.valueOf(edition));

                }
                if (!school.equals("")) {

                }
            }
        });

        mFloatingSaveButton = (FloatingActionButton) layout.findViewById(R.id.floatingSaveBookButton);
        mFloatingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Calendar c = Calendar.getInstance();
        final int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH)-1;
        curYear = c.get(Calendar.YEAR);
        Date d = new Date(curYear, month, day);

        mSelectedDate = new Date();

        updateDateView(d, curYear, month, day);

        final DateDialog dateDialog = new DateDialog() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date d = new Date(year-1900, monthOfYear, dayOfMonth);
                mSelectedDate = d;
                updateDateView(d, year, monthOfYear, dayOfMonth);
            }
        };

        calenderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                dateDialog.show(ft, "DatePicker");
            }
        });

        mPrevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedDate = getPrevDayDate(mSelectedDate);
            }
        });

        mNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedDate = getNextDayDate(mSelectedDate);
            }
        });

        //mAdapter = new TimeViewAdapter(getActivity(), null);
        //mRecyclerView.setAdapter(mAdapter);

    }

    public Date getPrevDayDate(Date curDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DATE, -1);
        Date d = new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        updateDateView(d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        return d;
    }

    public Date getNextDayDate(Date curDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DATE, 1);

        Date d = new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        updateDateView(d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        return d;
    }

    public void updateDateView(Date date, int year, int monthOfYear, int dayOfMonth){

        String[] monthInStr = getResources().getStringArray(R.array.monthNames);
        DateFormat format2 = new SimpleDateFormat("EEEE");
        String weekDayString = format2.format(date);
        String dateStr;

        if (curYear == year) {
            dateStr = weekDayString + ", " + monthInStr[monthOfYear] + " " + dayOfMonth;
        } else {
            dateStr = weekDayString + ", " + monthInStr[monthOfYear] + " " + dayOfMonth + " " + year;
        }
        mDateView.setText(dateStr);
    }

    public List<AvailableTime> getTimes(){

        List<AvailableTime> times = new ArrayList<>();

        return times;
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }
}
