package com.example.customerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class _s_ExpireActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    String myJSON;

    JSONArray counts = null;

    ArrayList<String> countList;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_COUNT = "count";

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    private String cinema_num;
    private String staff_num;

    String expiredTime;

    TextView a_agrade;
    TextView a_bcgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_activity_expire);

        ImageButton btnA = (ImageButton)findViewById(R.id.grade_A);
        ImageButton btnBC = (ImageButton)findViewById(R.id.grade_BC); /*페이지 전환버튼*/
        cinema_num =getIntent().getStringExtra("cinema_num");
        staff_num =getIntent().getStringExtra("staff_num");
        //Toast.makeText(getApplicationContext(), cinema_num,Toast.LENGTH_LONG).show();

        a_agrade =(TextView)findViewById(R.id.tv_a);
        a_bcgrade = (TextView)findViewById(R.id.tv_bc);

        expiredTime = "2020-08-17";

        getData("http://"+IP_ADDRESS+"/_s_count_expire.php?cinema_num="+ cinema_num+"&expired_date="+expiredTime);
        countList = new ArrayList<String>();

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

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _s_ExpireAGradeActivity.class);
                intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////
                intent.putExtra("staff_num", staff_num);
                startActivityForResult(intent, sub);
            }
        });

        btnBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _s_ExpireBCGradeActivity.class);
                intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////
                intent.putExtra("staff_num", staff_num);
                startActivityForResult(intent,sub);//액티비티 띄우기
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

            System.out.println(approve + " " + ask + " " );

            if(approve.equals("0")){
                a_agrade.setVisibility(View.GONE);
            }else{
                a_agrade.setVisibility(View.VISIBLE);
                a_agrade.setText(approve);
            }

            if(ask.equals("0")){
                a_bcgrade.setVisibility(View.GONE);
            }else{
                a_bcgrade.setVisibility(View.VISIBLE);
                a_bcgrade.setText(ask);
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
