package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class _s_AskLostListDetail extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private String ask_num;
    private String ask_object;
    private String ask_object_detail;
    private String lost_location;
    private String customer_name;
    private String phone_num;
    private String registered_date;

    private String ask_state;
    private String flag;
    private String reject_detail;

    EditText etRejectDetail;

    private static String TAG = "phptest";

    String cinema_num;
    String staff_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_ask_lost_list_detail);

        ask_num =getIntent().getStringExtra("ask_num"); //넘기기
        ask_object =getIntent().getStringExtra("ask_object");
        ask_object_detail =getIntent().getStringExtra("ask_object_detail");
        lost_location = getIntent().getStringExtra("lost_location");
        customer_name =getIntent().getStringExtra("customer_name");
        phone_num =getIntent().getStringExtra("phone_num"); //넘기기
        registered_date =getIntent().getStringExtra("registered_date");

        staff_num=getIntent().getStringExtra("staff_num");
        cinema_num =getIntent().getStringExtra("cinema_num");

        TextView tv_customer_name = (TextView)findViewById(R.id.customer_name);
        TextView tv_phone_num = (TextView)findViewById(R.id.phone_num);
        TextView tv_ask_object = (TextView)findViewById(R.id.ask_object);
        TextView tv_ask_object_detail = (TextView)findViewById(R.id.ask_object_detail);
        TextView tv_lost_location = (TextView)findViewById(R.id.lost_location);
        TextView tv_registered_date = (TextView)findViewById(R.id.registered_date);

        Button completed=(Button)findViewById(R.id.btn_ok);
        Button cancel=(Button)findViewById(R.id.btn_cancel);

        tv_customer_name.setText(customer_name);
        tv_phone_num.setText(phone_num);
        tv_ask_object.setText(ask_object);
        tv_ask_object_detail.setText(ask_object_detail);
        tv_lost_location.setText(lost_location);
        tv_registered_date.setText(registered_date);

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask_state="답변진행중";
                flag = "1";
                reject_detail = "";
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_s_update_ask_lost_list.php", ask_num, staff_num, ask_state, reject_detail, flag);

                Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                intent.putExtra("staff_num",staff_num);
                intent.putExtra("cinema_num", cinema_num);
                startActivityForResult(intent, sub);//액티비티 띄우기
                Toast.makeText(getApplicationContext(), "문의 승인이 완료되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linear = (LinearLayout) View.inflate(_s_AskLostListDetail.this, R.layout._s_dialog_reject_detail_ask_lost, null);

                new AlertDialog.Builder(_s_AskLostListDetail.this, R.style._s_MyDialogTheme)
                        .setTitle("거절 사유 입력")
                        .setView(linear)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                ask_state="답변완료";
                                flag = "2";

                                etRejectDetail = (EditText) linear.findViewById(R.id.et_reject_ask_lost);
                                reject_detail = etRejectDetail.getText().toString();

                                System.out.println(ask_num + ", " + staff_num + ", " + ask_state + ", " + reject_detail + ", " + flag);


                                InsertData task = new InsertData();
                                task.execute("http://" + IP_ADDRESS + "/_s_update_ask_lost_list2.php", ask_num, staff_num, ask_state, reject_detail, flag);

                                Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                                intent.putExtra("staff_num", staff_num);
                                intent.putExtra("cinema_num", cinema_num);
                                startActivityForResult(intent, sub);//액티비티 띄우기
                                Toast.makeText(getApplicationContext(), "문의가 거절 되었습니다.", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_s_AskLostListDetail.this,
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
            String staff_num = (String)params[2];
            String ask_state = (String)params[3];
            String reject_detail = (String)params[4];
            String flag = (String)params[5];

            String serverURL = (String)params[0];
            String postParameters = "ask_num=" + ask_num + "&staff_num=" + staff_num + "&ask_state=" + ask_state + "&reject_detail=" + reject_detail + "&flag=" + flag;

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