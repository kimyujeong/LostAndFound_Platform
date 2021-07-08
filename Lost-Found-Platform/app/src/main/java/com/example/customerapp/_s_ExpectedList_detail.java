package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class _s_ExpectedList_detail extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    private String lost_num;
    private String customer_name;
    private String phone_num;
    private String lost_object;
    private String lost_object_detail;
    private String receiving_date;
    private String receiving_time;
    private String mark;

    private String image;

    private String inquirystate;
    private String loststate;
    private String askstate;

    private static String TAG = "phptest";

    String cinema_num;
    String staff_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mark=getIntent().getStringExtra("mark");
        if(mark.equals("L")) {
            setContentView(R.layout._s_expected_list_clicked);
            image = getIntent().getStringExtra("image");
            ImageView lost_photo = findViewById(R.id.lost_photo);
            Glide.with(this).load(image).into(lost_photo);
        }
        else if(mark.equals("A")){
            setContentView(R.layout._s_expected_list_clicked_a);
        }

        lost_num =getIntent().getStringExtra("lost_num"); //넘기기
        customer_name =getIntent().getStringExtra("customer_name");
        phone_num =getIntent().getStringExtra("phone_num"); //넘기기
        lost_object =getIntent().getStringExtra("lost_object");
        lost_object_detail =getIntent().getStringExtra("lost_object_detail");
        receiving_date =getIntent().getStringExtra("receiving_date");
        receiving_time =getIntent().getStringExtra("receiving_time");


        cinema_num =getIntent().getStringExtra("cinema_num");
        staff_num=getIntent().getStringExtra("staff_num");



        TextView tv_customer_name = (TextView)findViewById(R.id.name);
        TextView tv_phone_num = (TextView)findViewById(R.id.phone);
        TextView tv_lost_object = (TextView)findViewById(R.id.lost_object);
        TextView tv_lost_object_detail = (TextView)findViewById(R.id.lost_object_detail);
        TextView tv_receiving_date = (TextView)findViewById(R.id.date);
        TextView tv_receiving_time = (TextView)findViewById(R.id.time);
        //TextView tv_mark = (TextView)findViewById(R.id.mark);

        Button completed=(Button)findViewById(R.id.btn_completed);
        Button cancel=(Button)findViewById(R.id.btn_cancel);

        tv_customer_name.setText(customer_name);
        tv_phone_num.setText(phone_num);
        tv_lost_object.setText(lost_object);
        tv_lost_object_detail.setText(lost_object_detail);
        tv_receiving_date.setText(receiving_date);
        tv_receiving_time.setText(receiving_time);

        FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),_s_MenuActivity.class);

                intent.putExtra("staff_num",staff_num);
                intent.putExtra("cinema_num",cinema_num);
                startActivity(intent);
            }
        });



        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mark.equals("L")) {

                    //Toast.makeText(getApplicationContext(),lost_num,Toast.LENGTH_LONG).show();

                    inquirystate = "수령완료";
                    loststate = "수령완료";
                    askstate="";
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/_s_update_expectedlist.php", lost_num, inquirystate, loststate, askstate);

                    Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                    intent.putExtra("staff_num",staff_num);
                    intent.putExtra("cinema_num", cinema_num);
                    startActivityForResult(intent, sub);//액티비티 띄우기
                    Toast.makeText(getApplicationContext(), "수령이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }
                else if(mark.equals("A")){

                    //Toast.makeText(getApplicationContext(),lost_num,Toast.LENGTH_LONG).show();

                    askstate="수령완료";
                    inquirystate="";
                    loststate="";
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/_s_update_expectedlist2.php", lost_num, inquirystate, loststate, askstate);

                    Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                    intent.putExtra("staff_num",staff_num);
                    intent.putExtra("cinema_num", cinema_num);
                    startActivityForResult(intent, sub);//액티비티 띄우기
                    Toast.makeText(getApplicationContext(), "수령이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mark.equals("L")) {
                    inquirystate = "수령취소";
                    loststate = "wait";
                    askstate="";
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/_s_update_expectedlist.php", lost_num, inquirystate, loststate, askstate);

                    Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                    intent.putExtra("staff_num",staff_num);
                    intent.putExtra("cinema_num", cinema_num);
                    startActivityForResult(intent, sub);//액티비티 띄우기
                    Toast.makeText(getApplicationContext(), "수령이 취소되었습니다.", Toast.LENGTH_LONG).show();
                }
                else if(mark.equals("A")){
                    askstate="수령취소";
                    inquirystate="";
                    loststate="";
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/_s_update_expectedlist2.php", lost_num, inquirystate, loststate, askstate);

                    Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                    intent.putExtra("staff_num",staff_num);
                    intent.putExtra("cinema_num", cinema_num);
                    startActivityForResult(intent, sub);//액티비티 띄우기
                    Toast.makeText(getApplicationContext(), "수령이 취소되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(_s_ExpectedList_detail.this, R.style._s_MyDialogTheme);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_s_ExpectedList_detail.this,
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

            String lost_num = (String)params[1];
            //String phone_num = (String)params[2];
            String inquirystate = (String)params[2];
            String loststate = (String)params[3];
            String askstate = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "lost_num=" + lost_num + "&inquirystate=" + inquirystate + "&loststate=" + loststate + "&askstate=" + askstate;

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
