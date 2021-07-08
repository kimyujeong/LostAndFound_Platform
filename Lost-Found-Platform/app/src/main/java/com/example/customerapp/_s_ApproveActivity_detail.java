package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class _s_ApproveActivity_detail extends AppCompatActivity implements IP {

    public String getIP() {
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_SCREENING_DATE = "screening_date";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_FILM_NAME = "film_name";
    private static final String TAG_SCREENING_SITE = "screening_site";
    private static final String TAG_SCREENING_TIME1 = "screening_time1";
    private static final String TAG_SCREENING_TIME2 = "screening_time2";
    private static final String TAG_NUM = "num";

    JSONArray applies = null;

    ArrayList<String> applyList;

    private String reserve_text=null;
    int vh_num=0;

    private String lost_num;
    private String registered_date;
    private String object_detail;
    private String lost_object;
    private String lost_object_detail;
    private String receiving_date;
    private String receiving_time;
    private String customer_num;

    private String image;

    private String inquirystate;
    private String reject_detail;

    private static String TAG = "phptest";

    String cinema_num;
    String staff_num;

    TextView tv_reserve_detail;

    HashMap<String, String> l;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_approve_list_clicked);

        lost_num = getIntent().getStringExtra("lost_num"); //넘기기
        registered_date = getIntent().getStringExtra("registered_date");
        object_detail = getIntent().getStringExtra("object_detail"); //넘기기
        lost_object = getIntent().getStringExtra("lost_object");
        lost_object_detail = getIntent().getStringExtra("lost_object_detail");
        receiving_date = getIntent().getStringExtra("receiving_date");
        receiving_time = getIntent().getStringExtra("receiving_time");
        customer_num = getIntent().getStringExtra("customer_num");

        image = getIntent().getStringExtra("image");

        cinema_num = getIntent().getStringExtra("cinema_num");
        staff_num = getIntent().getStringExtra("staff_num");

        applyList = new ArrayList<String>();

        getData("http://"+IP_ADDRESS+"/_s_approve_detail_reserve_history.php?customer_num="+ customer_num);


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

        TextView tv_lost_num = (TextView) findViewById(R.id.lost_num);
        TextView tv_lost_object = (TextView) findViewById(R.id.lost_object);
        TextView tv_date = (TextView) findViewById(R.id.date);
        TextView tv_time = (TextView) findViewById(R.id.time);
        TextView tv_registered_date = (TextView) findViewById(R.id.registered_date);
        TextView tv_inquiry_detail = (TextView) findViewById(R.id.inquiry_detail);
        TextView tv_lost_detail = (TextView) findViewById(R.id.lost_detail);
        tv_reserve_detail = (TextView) findViewById(R.id.reserve);

        Button completed = (Button) findViewById(R.id.b1);
        Button cancel = (Button) findViewById(R.id.b2);

        tv_registered_date.setText(registered_date);
        tv_inquiry_detail.setText(object_detail);
        tv_lost_object.setText(lost_object);
        tv_lost_detail.setText(lost_object_detail);
        tv_date.setText(receiving_date);
        tv_time.setText(receiving_time);
        tv_lost_num.setText(lost_num);

        //final RelativeLayout linear = (RelativeLayout) View.inflate(_s_ApproveActivity_detail.this, R.layout._s_approve_list_clicked, null);
        ImageView lost_photo = findViewById(R.id.lost_photo);
        Glide.with(this).load(image).into(lost_photo);
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++"+image);


        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                inquirystate = "수령예정";

                String flag = "";
                String reject_detail = "";

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_s_update_approve.php", customer_num, lost_num, inquirystate, flag, reject_detail);
                //System.out.println("++++++++++++++++++++++++++++++++++++++++++++"+customer_num+" / "+lost_num);
                Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                intent.putExtra("staff_num", staff_num);
                intent.putExtra("cinema_num", cinema_num);
                startActivityForResult(intent, sub);//액티비티 띄우기
                Toast.makeText(getApplicationContext(), "수령 요청이 승인되었습니다", Toast.LENGTH_LONG).show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inquirystate = "승인거부";
                final InsertData task = new InsertData();

                final LinearLayout linear = (LinearLayout) View.inflate(_s_ApproveActivity_detail.this, R.layout._s_dialog_approve_reject, null);

                new AlertDialog.Builder(_s_ApproveActivity_detail.this, R.style._s_MyDialogTheme)
                        .setTitle("승인 요청 취소 사유")
                        .setView(linear)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                EditText reject = (EditText) linear.findViewById(R.id.detail);
                                reject_detail = reject.getText().toString();
                                String flag = "4";

                                task.execute("http://" + IP_ADDRESS + "/_s_update_approve_reject.php", customer_num, lost_num, inquirystate, flag, reject_detail);

                                dialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), _s_MenuActivity.class);
                                intent.putExtra("staff_num", staff_num);
                                intent.putExtra("cinema_num", cinema_num);
                                startActivityForResult(intent, sub);//액티비티 띄우기
                                Toast.makeText(getApplicationContext(), "수령 요청 승인이 취소되었습니다.", Toast.LENGTH_LONG).show();

                            }
                        })
                        /*.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })*/
                        .show();
            }
        });


    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            applies = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < applies.length(); i++) {
                JSONObject c = applies.getJSONObject(i);
                String screening_date = c.getString(TAG_SCREENING_DATE);
                String branch = c.getString(TAG_BRANCH);
                String film_name = c.getString(TAG_FILM_NAME);
                String screening_site = c.getString(TAG_SCREENING_SITE);
                String screening_time1 = c.getString(TAG_SCREENING_TIME1);
                String screening_time2 = c.getString(TAG_SCREENING_TIME2);
                vh_num = Integer.parseInt(c.getString(TAG_NUM));

                reserve_text=screening_date+"  /  "+screening_time1+"-"+screening_time2+"\n"+branch+" "+screening_site+"\n"+film_name;
                tv_reserve_detail.setText(reserve_text);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_s_ApproveActivity_detail.this,
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

            String customer_num = (String) params[1];
            String lost_num = (String) params[2];
            String inquirystate = (String) params[3];

            String flag = (String) params[4];
            String reject_detail = (String) params[5];

            String serverURL = (String) params[0];
            String postParameters = "customer_num=" + customer_num + "&lost_num=" + lost_num + "&processing_state=" + inquirystate + "&flag=" + flag + "&reject_detail=" + reject_detail;

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



    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }


}
