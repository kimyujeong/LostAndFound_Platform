package com.example.customerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class _s_MenuActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    String myJSON;


    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    private String staffNum;
    private String cinemaNum;

    JSONArray counts = null;

    ArrayList<String> countList;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_COUNT = "count";

    TextView a_approve;
    TextView a_ask;
    TextView a_expected;
    TextView a_expired;

    String expiredTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_activity_menu);



        staffNum = getIntent().getStringExtra("staff_num");
        cinemaNum = getIntent().getStringExtra("cinema_num");
        System.out.println("넘어온 staffNum, cinemaNum: " + staffNum + ", " + cinemaNum);


        expiredTime = "2020-08-17";

        getData("http://"+IP_ADDRESS+"/_s_count.php?cinema_num="+ cinemaNum+"&expired_date="+expiredTime);
        countList = new ArrayList<String>();

        ImageButton lost_list_button = (ImageButton)findViewById(R.id.분실물목록); //분실물 목록
        ImageButton completed_button = (ImageButton)findViewById(R.id.수령완료목록); //분실물 목록
        ImageButton apply_button = (ImageButton)findViewById(R.id.등록); //분실물 목록
        ImageButton expected_button = (ImageButton)findViewById(R.id.수령예정분실물); //분실물 목록
        ImageButton expire_button = (ImageButton)findViewById(R.id.보관기간만료분실물);

        ImageButton ask_lost_button = (ImageButton)findViewById(R.id.문의);
        ImageButton inquiry_button = (ImageButton)findViewById(R.id.승인);

        a_approve =(TextView)findViewById(R.id.tv_approve);
        a_ask = (TextView)findViewById(R.id.tv_ask);
        a_expected = (TextView)findViewById(R.id.tv_expected);
        a_expired = (TextView)findViewById(R.id.tv_expired);

        ask_lost_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _s_AskLostActivity.class);
                intent.putExtra("cinema_num", cinemaNum);
                intent.putExtra("staff_num", staffNum);
                startActivityForResult(intent, sub);
            }
        });

        inquiry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _s_ApproveActivity.class);
                intent.putExtra("cinema_num", cinemaNum);
                intent.putExtra("staff_num", staffNum);
                startActivityForResult(intent, sub);
            }
        });
        /////////////////////////////////////

        expire_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),_s_ExpireActivity.class);
                intent.putExtra("cinema_num", cinemaNum);
                intent.putExtra("staff_num", staffNum);
                startActivityForResult(intent,sub);//액티비티 띄우기
            }
        });

        lost_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),_s_LostListActivity.class);
                intent.putExtra("cinema_num", cinemaNum);
                intent.putExtra("staff_num", staffNum);
                startActivityForResult(intent,sub);
            }
        });
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),_s_ApplyActivity.class);
                intent.putExtra("staff_num",staffNum);
                intent.putExtra("cinema_num", cinemaNum);
                startActivityForResult(intent,sub);//액티비티 띄우기
            }
        });
        completed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),_s_CompletedActivity.class);
                intent.putExtra("cinema_num", cinemaNum);
                intent.putExtra("staff_num", staffNum);
                startActivityForResult(intent,sub);//액티비티 띄우기
            }
        });

        expected_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _s_ExpectedActivity.class);
                intent.putExtra("staff_num",staffNum);
                intent.putExtra("cinema_num", cinemaNum);
                startActivityForResult(intent, sub);//액티비티 띄우기
            }
        });
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            counts = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < counts.length(); i++) {
                JSONObject c = counts.getJSONObject(i);
                String count = c.getString(TAG_COUNT);

                countList.add(count);
            }

            int totalElements = countList.size();
            for (int index = 0; index < totalElements; index++) {
                System.out.println(countList.get(index));
            }

            String approve = countList.get(0);
            String ask = countList.get(1);
            String expected = countList.get(2);
            String expired = countList.get(3);

            System.out.println(approve + " " + ask + " " + expected + " " + expired);

            if(approve.equals("0")){
                a_approve.setVisibility(View.GONE);
            }else{
                a_approve.setVisibility(View.VISIBLE);
                a_approve.setText(approve);
            }

            if(ask.equals("0")){
                a_ask.setVisibility(View.GONE);
            }else{
                a_ask.setVisibility(View.VISIBLE);
                a_ask.setText(ask);
            }

            if(expected.equals("0")){
                a_expected.setVisibility(View.GONE);
            }else{
                a_expected.setVisibility(View.VISIBLE);
                a_expected.setText(expected);
            }

            if(expired.equals("0")){
                a_expired.setVisibility(View.GONE);
            }else{
                a_expired.setVisibility(View.VISIBLE);
                a_expired.setText(expired);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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


