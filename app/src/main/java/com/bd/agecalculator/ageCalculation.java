package com.bd.agecalculator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ageCalculation extends AppCompatActivity {

    EditText cur_Date, dd, mm, yr;
    Date birthDate = null;
    TextView result_year, result_month, result_days, mDisplayDate, total_year, total_month, total_week, total_days, total_hours, total_minutes, total_seconds;
    TextView birthDate_dayofWeek, result_nextBirdate_month, result_nextBirdate_days, todays_datename;
    private String DateValue;
    private int day;
    private long totalDay;
    private int month;
    private long totalMonth;
    private int year;
    private int weeks;
    private long hours;
    private long minutes;
    private long seconds;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button btn_calculation, btn_clear, open_cal;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculation);

        cur_Date = findViewById(R.id.editTest_curDate);
        open_cal = findViewById(R.id.open_cal);
        btn_calculation = findViewById(R.id.btn_calculation);
        todays_datename = findViewById(R.id.dateName);
        birthDate_dayofWeek = findViewById(R.id.date_Name);
        btn_clear = findViewById(R.id.btn_clear);
        result_year = findViewById(R.id.result_year);
        result_month = findViewById(R.id.result_month);
        result_days = findViewById(R.id.result_days);

        result_nextBirdate_month = findViewById(R.id.result_nextBirdate_month);
        result_nextBirdate_days = findViewById(R.id.result_nextBirdate_days);


        total_year = findViewById(R.id.total_year);
        total_month = findViewById(R.id.total_months);
        total_week = findViewById(R.id.total_weeks);
        total_days = findViewById(R.id.total_days);
        total_hours = findViewById(R.id.total_hours);
        total_minutes = findViewById(R.id.total_minutes);
        total_seconds = findViewById(R.id.total_second);
        dd = findViewById(R.id.day);
        dd.setFilters(new InputFilter[]{new InputFilterMinMax("0", "31")});
        mm = findViewById(R.id.month);
        mm.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});
        yr = findViewById(R.id.year);


        // Banner Ad View
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //


        // Today's Date Name

        String dayNames[] = new DateFormatSymbols().getWeekdays();
        Calendar date2 = Calendar.getInstance();

        String dateName = dayNames[date2.get(Calendar.DAY_OF_WEEK)];
        todays_datename.setText(dateName);

        //End



        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        Log.d("CurDate: ", formattedDate.toString());
        cur_Date.setText(formattedDate.toString());
        // cur_Date.setText("sdfsdfdsf safasfas sfsadf");

        mDisplayDate = (TextView) findViewById(R.id.birthDate);

        open_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ageCalculation.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("Date:", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                DateValue = day + "/" + month + "/" + year;


                // date_of_birth.setText(DateValue);
                dd.setText(String.valueOf(day));
                mm.setText(String.valueOf(month));
                yr.setText(String.valueOf(year));
            }
        };

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorSky));
        }


        dd.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    mm.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!yr.getText().toString().equals("") && !mm.getText().toString().equals("")) {
                    Log.d("Test", "Check");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String ddValue = dd.getText().toString();
                    String mmValue = mm.getText().toString();
                    String yrValue = yr.getText().toString();

                    String newDateValue = ddValue + "/" + mmValue + "/" + yrValue;

                    Date birthDate = new Date();
                    try {
                        birthDate = sdf.parse(newDateValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //  getDateNameOFWeek(birthDate);
                    String dayofWeekValue = getDateNameOFWeek(birthDate);
                    birthDate_dayofWeek.setText(dayofWeekValue);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });


        mm.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mm.getText().toString().equals("1")||mm.getText().toString().equals("")) {
                    mm.requestFocus();
                } else {
                    yr.requestFocus();
                    if (s.length() == 2) {
                        yr.requestFocus();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!dd.getText().toString().equals("") && !yr.getText().toString().equals("")) {
                    Log.d("Test", "Check");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String ddValue = dd.getText().toString();
                    String mmValue = mm.getText().toString();
                    String yrValue = yr.getText().toString();

                    String newDateValue = ddValue + "/" + mmValue + "/" + yrValue;

                    Date birthDate = new Date();
                    try {
                        birthDate = sdf.parse(newDateValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //  getDateNameOFWeek(birthDate);
                    String dayofWeekValue = getDateNameOFWeek(birthDate);
                    birthDate_dayofWeek.setText(dayofWeekValue);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });


        yr.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(yr.getWindowToken(), 0);

                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!dd.getText().toString().equals("") && !mm.getText().toString().equals("")) {
                    Log.d("Test", "Check");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String ddValue = dd.getText().toString();
                    String mmValue = mm.getText().toString();
                    String yrValue = yr.getText().toString();

                    String newDateValue = ddValue + "/" + mmValue + "/" + yrValue;

                    Date birthDate = new Date();
                    try {
                        birthDate = sdf.parse(newDateValue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //  getDateNameOFWeek(birthDate);
                    String dayofWeekValue = getDateNameOFWeek(birthDate);
                    birthDate_dayofWeek.setText(dayofWeekValue);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });


    }

    //For internet connection

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    //End internet connection


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ratings) {

            if (isNetworkConnected()) {
                Intent i = new Intent(Intent.ACTION_VIEW);

                i.setData(Uri.parse("market://details?id=com.bd.agecalculator"));
                startActivity(i);
                //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }


        }

        if (id == R.id.menu_share) {

            if (isNetworkConnected()) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String share_body = "https://play.google.com/store/apps/details?id=com.bd.agecalculator";
                String share_subject = "Age Calculator App, please download it from the below link";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, share_subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, share_body);
                startActivity(Intent.createChooser(shareIntent, "Share Using"));
                //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }


        }


        if (id == R.id.action_update) {

            if (isNetworkConnected()) {
                Intent i = new Intent(Intent.ACTION_VIEW);

                i.setData(Uri.parse("market://details?id=com.bd.agecalculator"));
                startActivity(i);
                //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }


        }

        if (id == R.id.action_close) {

            finish();
            System.exit(0);

        }


        if (id == R.id.action_more) {

            if (isNetworkConnected()) {
                Intent devAccount = new Intent(Intent.ACTION_VIEW);
                devAccount.setData(Uri.parse("http://play.google.com/store/apps/dev?id=6031616565948906744"));
                startActivity(devAccount);
                // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }


        }


        return super.onOptionsItemSelected(item);
    }


    public void ageCalculation(View view) {
        int cur_year = Calendar.getInstance().get(Calendar.YEAR);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(yr.getWindowToken(), 0);
        DateValue = dd.getText().toString() + "/" + mm.getText().toString() + "/" + yr.getText().toString();
        Log.d("Date:", DateValue);


        if (dd.getText().toString().equals("") || mm.getText().toString().equals("") || yr.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter valid Birth Date", Toast.LENGTH_SHORT).show();

        } else {

            if (Integer.valueOf(dd.getText().toString()) > 31 || Integer.valueOf(mm.getText().toString()) > 12 || Integer.valueOf(yr.getText().toString()) > cur_year) {
                Toast.makeText(this, "Please enter valid date", Toast.LENGTH_SHORT).show();

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    birthDate = sdf.parse(DateValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Age age_value = calculateAge(birthDate);
                result_month.setText(String.valueOf(age_value.getMonths()));
                result_year.setText(String.valueOf(age_value.getYears()));
                result_days.setText(String.valueOf(age_value.getDays()));
                calculateNextBirthday();
                calculateExtra();
                if (year < 0) {
                    Toast.makeText(this, "Please enter valid date", Toast.LENGTH_SHORT).show();
                } else {
                    total_year.setText(String.valueOf(year));
                    total_month.setText(String.valueOf(totalMonth));
                    total_days.setText(String.valueOf(totalDay));
                    total_week.setText(String.valueOf(weeks));
                    total_hours.setText(String.valueOf(hours));
                    total_minutes.setText(String.valueOf(minutes));
                    total_seconds.setText(String.valueOf(seconds));
                }

            }


        }


        // Toast.makeText(getApplicationContext(), age_value.toString(),Toast.LENGTH_LONG).show();
        //System.out.println(age);

    }

    public void clear(View view) {
        result_year.setText("00");
        result_month.setText("00");
        result_days.setText("00");
        result_nextBirdate_month.setText("00");
        result_nextBirdate_days.setText("00");
        total_year.setText("00");
        total_month.setText("00");
        total_days.setText("00");
        total_week.setText("00");
        dd.setText("");
        mm.setText("");
        yr.setText("");
        total_hours.setText("00");
        total_minutes.setText("00");
        total_seconds.setText("00");
        dd.requestFocus();
        birthDate_dayofWeek.setText("");
    }

    private Age calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object


        day = days;
        month = months;
        year = years;


        return new Age(days, months, years);

    }


    private void calculateNextBirthday() {
        if (year < 0) {
            Toast.makeText(this, "Please enter valid date", Toast.LENGTH_SHORT).show();
        } else {
            int date_value = (int) (month * 30.417);
            Log.d("date_value", String.valueOf(date_value));
            float newValue = date_value + day;
            Log.d("newValue", String.valueOf(newValue));
            float remainingDays = 365 - newValue;
            Log.d("remainingDays", String.valueOf(remainingDays));

            int days_value = (int) (remainingDays % 30.417);
            int month_value = (int) (remainingDays / 30.417);

            Log.d("Remain String", String.valueOf(remainingDays));

            // nextBirthDate.setText(String.valueOf(month_value + " Months, " + days_value + " Days"));
            result_nextBirdate_month.setText(String.valueOf(month_value));
            result_nextBirdate_days.setText(String.valueOf(days_value));
        }
    }


    private void calculateExtra() {
        if (year < 0) {
            Toast.makeText(this, "Please enter valid date", Toast.LENGTH_SHORT).show();
        } else {

            totalDay = Math.round(year * 365 + month * 30.417 + day);
            weeks = Math.round(totalDay / 7);
            totalMonth = Math.round(year * 12 + month);
            hours = totalDay * 24;
            minutes = totalDay * 1440;
            seconds = totalDay * 86400;
        }
    }


    private String getDateNameOFWeek(Date inputDate) {

        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String dayOf_week = "";

        switch (dayOfWeek) {

            case 1:

                dayOf_week = "Sun";
                break;

            case 2:
                dayOf_week = "Mon";
                break;
            case 3:
                dayOf_week = "Tou";
                break;
            case 4:
                dayOf_week = "Wed";
                break;
            case 5:
                dayOf_week = "Thu";
                break;
            case 6:
                dayOf_week = "Fri";
                break;
            case 7:
                dayOf_week = "Sat";
                break;


        }
        return dayOf_week;

    }
}

