package com.example.customerapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class _c_consult_history_detail extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    String myJSON;

    private String mark;
    private String date;
    private String category;
    private String state;
    private int num;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_CITY = "city";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_OBJECT_DETAIL = "object_detail";

    JSONArray details = null;

    TextView tv_date;
    TextView tv_cinema;
    TextView tv_category;
    TextView tv_detail;
    TextView tv_state;

    private String customerNum;//////////////////////////
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_consult_list_clicked);

        mark = getIntent().getStringExtra("mark");
        date = getIntent().getStringExtra("date");
        category = getIntent().getStringExtra("category");
        state = getIntent().getStringExtra("state");
        num = Integer.parseInt(getIntent().getStringExtra("num"));
        customerNum=getIntent().getStringExtra("customerNum");
        phoneNum = getIntent().getStringExtra("phoneNum");

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

        tv_date = (TextView)findViewById(R.id.date);
        tv_cinema = (TextView)findViewById(R.id.cinema);
        tv_category = (TextView)findViewById(R.id.category);
        tv_detail = (TextView)findViewById(R.id.detail);
        tv_state = (TextView)findViewById(R.id.state);

        tv_date.setText(date);
        tv_category.setText(category);
        tv_state.setText(state);

        if(mark.equals("A")){
            getData("http://"+IP_ADDRESS+"/_c_consult_ask_detail.php?num="+num); ///////////////////////////////
        }
        else if(mark.equals("L")){
            getData("http://"+IP_ADDRESS+"/_c_consult_lost_detail.php?num="+num); ///////////////////////////////
        }

        Button b=(Button)findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),_c_MenuActivity.class);
                intent.putExtra("customerNum",customerNum);
                intent.putExtra("phoneNum",phoneNum);
                startActivityForResult(intent,sub);
            }
        });

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            details = jsonObj.getJSONArray(TAG_RESULTS);

            JSONObject c = details.getJSONObject(0);
            String city = c.getString(TAG_CITY);
            String branch = c.getString(TAG_BRANCH);
            String detail = c.getString(TAG_OBJECT_DETAIL);

            tv_cinema.setText(city+" / "+branch);
            tv_detail.setText(detail);

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
