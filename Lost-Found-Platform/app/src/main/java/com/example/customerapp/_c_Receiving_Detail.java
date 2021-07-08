package com.example.customerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class _c_Receiving_Detail extends AppCompatActivity implements IP {

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public String getIP() {
        return IP_address;
    }

    private String IP_ADDRESS = getIP();
    private static String TAG = "phptest";

    private TextView textView_Date;
    private TextView textView_Time;
    private DatePickerDialog.OnDateSetListener callbackMethod; //날짜
    private TimePickerDialog.OnTimeSetListener callbackMethod2; //시간

    String selected_year;
    String selected_month;
    String selected_day;

    String selected_hour;
    String selected_minute;

    Editable object_detail;
    int lost_num;
    String customer_num;
    String receiving_date;
    String receiving_time;

    private String ask_num;
    private String ask_object;
    private String ask_object_detail;
    private String customer_name;
    private String phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_receiving_detail);

        textView_Date = (TextView) findViewById(R.id.textView_date);
        textView_Time = (TextView) findViewById(R.id.textView_time);
        Button btnNext = (Button) findViewById(R.id.button_next);

        final String phoneNum = getIntent().getStringExtra("phoneNum");
        final String customerNum = getIntent().getStringExtra("customerNum");

        FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),_c_MenuActivity.class);

                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("customerNum",customerNum);
                startActivity(intent);
            }
        });

        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { //initialize listener(date)
                selected_year = String.valueOf(year);

                if (monthOfYear <= 8 && monthOfYear >= 0) {
                    selected_month = "0" + String.valueOf(monthOfYear + 1);
                } else {
                    selected_month = String.valueOf(monthOfYear + 1);
                }

                if (dayOfMonth <= 9 && dayOfMonth >= 1) {
                    selected_day = "0" + String.valueOf(dayOfMonth);
                } else {
                    selected_day = String.valueOf(dayOfMonth);
                }

                textView_Date.setText(selected_year + "년" + selected_month + "월" + selected_day + "일");
            }
        };

        callbackMethod2 = new TimePickerDialog.OnTimeSetListener() //initialize listener(time)
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay <= 9 && hourOfDay >= 0) {
                    selected_hour = "0" + String.valueOf(hourOfDay);
                } else {
                    selected_hour = String.valueOf(hourOfDay);
                }

                if (minute == 0 || minute == 5) {
                    selected_minute = "0" + String.valueOf(minute);
                } else {
                    selected_minute = String.valueOf(minute);
                }

                textView_Time.setText(selected_hour + "시" + selected_minute + "분");
            }
        };

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ask_num = getIntent().getStringExtra("ask_num");
                ask_object = getIntent().getStringExtra("ask_object");
                ask_object_detail = getIntent().getStringExtra("ask_object_detail");
                customer_name = getIntent().getStringExtra("customer_name");
                customer_num = getIntent().getStringExtra("customerNum"); // intent로 값 받아오기
                phone_num = getIntent().getStringExtra("phoneNum");
                receiving_date = selected_year + selected_month + selected_day;
                receiving_time = selected_hour + selected_minute + "00";

                Intent intent = new Intent(getApplicationContext(), _c_Receiving_Detail_Next.class);
                intent.putExtra("ask_num", ask_num);
                intent.putExtra("ask_object", ask_object);
                intent.putExtra("ask_object_detail", ask_object_detail);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("customerNum", customer_num);
                intent.putExtra("phoneNum", phone_num);
                intent.putExtra("date",selected_year+"년 "+selected_month+"월 "+selected_day+"일");
                intent.putExtra("time",selected_hour+"시 "+selected_minute+"분");
                intent.putExtra("receiving_date", receiving_date);
                intent.putExtra("receiving_time", receiving_time);

                startActivityForResult(intent,sub);

            }
        });
    }

    //날짜선택 다이얼로그
    public void OnClickHandler(View view) //date handler
    {
        //현재 날짜 설정
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

        String year = yearFormat.format(date);
        String month = monthFormat.format(date);
        String day = dayFormat.format(date);

        int year_i = Integer.parseInt(year);
        int month_i = Integer.parseInt(month);
        int day_i = Integer.parseInt(day);

        //다이얼로그 출력
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.MyDialogTheme, callbackMethod, year_i, month_i - 1, day_i);
        dialog.show();

    }

    public void OnClickHandler2(View view) //time handler
    {
        TimePickerDialog dialog = new TimePickerDialog(this, R.style.MyDialogTheme, callbackMethod2, 0, 0, true);
        dialog.show();
    }
}