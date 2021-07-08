package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class _c_search_detail_next extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    private static String TAG = "phptest";
    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private String detail;
    private String date;
    private String category;
    private String time;
    private String cinema;
    private String info;

    private String lost_num;
    private String receiving_date;
    private String receiving_time;

    TextView tv_date;
    TextView tv_cinema;
    TextView tv_category;
    TextView tv_time;
    TextView tv_detail;
    TextView tv_info;

    private String phoneNum;
    private String customerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_search_detail_next);

        cinema = getIntent().getStringExtra("cinema");
        date = getIntent().getStringExtra("date");
        category = getIntent().getStringExtra("category");
        time = getIntent().getStringExtra("time");
        detail = getIntent().getStringExtra("detail");
        info = getIntent().getStringExtra("info");

        phoneNum = getIntent().getStringExtra("phoneNum");
        customerNum = getIntent().getStringExtra("customerNum");

        lost_num=getIntent().getStringExtra("lost_num");
        receiving_date=getIntent().getStringExtra("receiving_date");
        receiving_time=getIntent().getStringExtra("receiving_time");


        tv_date = (TextView)findViewById(R.id.date);
        tv_cinema = (TextView)findViewById(R.id.cinema);
        tv_category = (TextView)findViewById(R.id.category);
        tv_detail = (TextView)findViewById(R.id.detail);
        tv_time = (TextView)findViewById(R.id.time);
        tv_info = (TextView)findViewById(R.id.info);

        tv_date.setText(date);
        tv_category.setText(category);
        tv_cinema.setText(cinema);
        tv_detail.setText(detail);
        tv_time.setText(time);
        tv_info.setText(info);



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

        Button b=(Button)findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_c_search_detail.php", customerNum,lost_num,detail,receiving_date,receiving_time);

                Intent intent = new Intent(getApplicationContext(),_c_MenuActivity.class); //메뉴 액티비티로 수정하기
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("customerNum",customerNum);
                startActivityForResult(intent,sub);
            }
        });

    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(_c_search_detail_next.this, R.style.MyDialogTheme);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_c_search_detail_next.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String customerNum = (String) params[1];
            String lostNum = (String) params[2];
            String detail = (String) params[3];
            String date = (String) params[4];
            String time = (String) params[5];

            String serverURL = (String) params[0];
            String postParameters = "customerNum=" + customerNum + "&lostNum=" + lostNum + "&detail=" + detail + "&date=" + date + "&time=" + time;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }


}