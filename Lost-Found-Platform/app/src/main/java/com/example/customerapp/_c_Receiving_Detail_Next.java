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

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class _c_Receiving_Detail_Next extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private static String TAG = "php";

    private String ask_num;
    private String ask_object;
    private String ask_object_detail;
    private String customer_name;
    private String customer_num;
    private String phone_num;
    private String date;
    private String time;
    private String receiving_date;
    private String receiving_time;

    TextView tv_ask_object;
    TextView tv_ask_object_detail;
    TextView tv_receiving_date;
    TextView tv_receiving_time;
    TextView tv_customer_name;
    TextView tv_phone_num;

    String ask_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_receiving_detail_next);

        ask_num = getIntent().getStringExtra("ask_num");
        ask_object = getIntent().getStringExtra("ask_object");
        ask_object_detail = getIntent().getStringExtra("ask_object_detail");
        customer_name = getIntent().getStringExtra("customer_name");
        customer_num = getIntent().getStringExtra("customerNum");
        phone_num = getIntent().getStringExtra("phoneNum");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        receiving_date = getIntent().getStringExtra("receiving_date");
        receiving_time = getIntent().getStringExtra("receiving_time");

        FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),_c_MenuActivity.class);

                intent.putExtra("phoneNum",phone_num);
                intent.putExtra("customerNum",customer_num);
                startActivity(intent);
            }
        });


        tv_ask_object = (TextView)findViewById(R.id.ask_object);
        tv_ask_object_detail = (TextView)findViewById(R.id.ask_object_detail);
        tv_receiving_date = (TextView)findViewById(R.id.receiving_date);
        tv_receiving_time = (TextView)findViewById(R.id.receiving_time);
        tv_customer_name = (TextView)findViewById(R.id.customer_name);
        tv_phone_num = (TextView)findViewById(R.id.phone_num);

        tv_ask_object.setText(ask_object);
        tv_ask_object_detail.setText(ask_object_detail);
        tv_receiving_date.setText(date);
        tv_receiving_time.setText(time);
        tv_customer_name.setText(customer_name);
        tv_phone_num.setText(phone_num);

        Button b=(Button)findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_state="수령예정";

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_c_update_ask_receiving_date.php", ask_num, ask_state, receiving_date, receiving_time);

                Intent intent = new Intent(getApplicationContext(),_c_MenuActivity.class); //메뉴 액티비티로 수정하기
                intent.putExtra("customerNum", customer_num);
                intent.putExtra("phoneNum", phone_num);
                startActivityForResult(intent,sub);

            }
        });

    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_c_Receiving_Detail_Next.this,
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
            String ask_num = (String)params[1];
            String ask_state = (String)params[2];
            String receiving_date = (String)params[3];
            String receiving_time = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "ask_num=" + ask_num + "&ask_state=" + ask_state + "&receiving_date=" + receiving_date + "&receiving_time=" + receiving_time;

            //System.out.println(city + "/" + branch + "/" + askObject + "/" + askObjectDetail + "/" + lostLocation + "/" + customerNum);

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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
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