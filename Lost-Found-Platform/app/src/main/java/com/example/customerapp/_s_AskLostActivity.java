package com.example.customerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class _s_AskLostActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();
    String myJSON;


    private static final String TAG_RESULTS = "result";

    //select 해서 가져올 것
    private static final String TAG_ASK_NUM = "ask_num";
    private static final String TAG_ASK_OBJECT = "ask_object";
    private static final String TAG_ASK_OBJECT_DETAIL = "ask_object_detail";
    private static final String TAG_LOST_LOCATION = "lost_location";
    private static final String TAG_ASK_STATE = "ask_state";
    private static final String TAG_REGISTERED_DATE = "registered_date";
    private static final String TAG_PHONE_NUM = "phone_num";
    private static final String TAG_CUSTOMER_NAME = "customer_name";

    //private static final String TAG_STAFF_NUM = "staff_num";//나중에 넣을것

    JSONArray askJSON = null;

    ArrayList<HashMap<String, String>> inquiryList;

    ListView list;

    ListAdapter adapter;

    String cinema_num;/////////////////////////////////////
    String staff_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_activity_ask_lost);
        list = (ListView) findViewById(R.id.listView);
        inquiryList = new ArrayList<HashMap<String, String>>();
        cinema_num =getIntent().getStringExtra("cinema_num");//////////////////////////////////
        staff_num = getIntent().getStringExtra("staff_num");

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
        getData("http://" + IP_ADDRESS + "/_s_ask_lost_list.php?cinema_num="+ cinema_num); //ip주소 ///////
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            askJSON = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < askJSON.length(); i++) {
                JSONObject c = askJSON.getJSONObject(i);
                String ask_num = c.getString(TAG_ASK_NUM);
                String ask_object = c.getString(TAG_ASK_OBJECT);
                String ask_object_detail = c.getString(TAG_ASK_OBJECT_DETAIL);
                String lost_location = c.getString(TAG_LOST_LOCATION);
                String registered_time = c.getString(TAG_REGISTERED_DATE);
                String customer_name = c.getString(TAG_CUSTOMER_NAME);
                String phone_num = c.getString(TAG_PHONE_NUM);


                HashMap<String, String> inquiry = new HashMap<String, String>();

                inquiry.put(TAG_ASK_NUM, ask_num);
                inquiry.put(TAG_ASK_OBJECT, ask_object);
                inquiry.put(TAG_ASK_OBJECT_DETAIL, ask_object_detail);
                inquiry.put(TAG_LOST_LOCATION, lost_location);
                inquiry.put(TAG_REGISTERED_DATE, registered_time);
                inquiry.put(TAG_CUSTOMER_NAME, customer_name);
                inquiry.put(TAG_PHONE_NUM, phone_num);

                inquiryList.add(inquiry);
            }

            adapter = new SimpleAdapter(
                    _s_AskLostActivity.this, inquiryList, R.layout._s_ask_lost_item,
                    new String[]{TAG_ASK_NUM, TAG_ASK_OBJECT, TAG_ASK_OBJECT_DETAIL, TAG_LOST_LOCATION},
                    new int[]{R.id.ask_num, R.id.ask_object, R.id.ask_object_detail, R.id.lost_location}
            );

            list.setAdapter(adapter);
            //리스트가 비었을 때 하이하이
            list.setEmptyView(findViewById(R.id.emptyElement));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(), _s_AskLostListDetail.class);

                    intent.putExtra("ask_num", inquiryList.get(position).get("ask_num"));
                    intent.putExtra("ask_object", inquiryList.get(position).get("ask_object"));
                    intent.putExtra("ask_object_detail", inquiryList.get(position).get("ask_object_detail"));
                    intent.putExtra("lost_location", inquiryList.get(position).get("lost_location"));
                    intent.putExtra("registered_date", inquiryList.get(position).get("registered_date"));
                    intent.putExtra("customer_name", inquiryList.get(position).get("customer_name"));
                    intent.putExtra("phone_num",inquiryList.get(position).get("phone_num"));

                    intent.putExtra("staff_num",staff_num);
                    intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////

                    startActivity(intent);
                }
            });



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

