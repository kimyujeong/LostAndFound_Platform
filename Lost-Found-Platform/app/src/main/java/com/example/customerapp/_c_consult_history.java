package com.example.customerapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class _c_consult_history extends AppCompatActivity implements IP{

    String myJSON;

    public String getIP(){
        return IP_address;
    }



    private static final String TAG_RESULTS = "result";
    private static final String TAG_REGISTEREDDATE = "registered_date";
    private static final String TAG_ASKOBJECT = "ask_object";
    private static final String TAG_ASKSTATE = "ask_state";
    private static final String TAG_MARK = "mark";
    private static final String TAG_NUM = "num";

    JSONArray consults = null;

    ArrayList<HashMap<String, String>> consultList;
    ArrayList<HashMap<String, String>> consultList2;

    ListView list;

    int count=0;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> array=new ArrayList<String>();

    ListAdapter adapter;

    private String customerNum;//////////////////////////
    private String phoneNum;

    private String IP_ADDRESS = getIP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_consult_list);
        spinner=(Spinner)findViewById(R.id.spinner);
        list = (ListView) findViewById(R.id.listView);
        consultList = new ArrayList<HashMap<String, String>>();

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


        //getData("http://192.168.0.129/cinelf_lostlist.php?site="+staff_site); ///////////////////////////////

        getData("http://"+IP_ADDRESS+"/_c_consult_list.php?customerNum="+customerNum);

    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            consults = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < consults.length(); i++) {
                JSONObject c = consults.getJSONObject(i);
                String registered_date = c.getString(TAG_REGISTEREDDATE);
                String ask_object = c.getString(TAG_ASKOBJECT);
                String ask_state = c.getString(TAG_ASKSTATE);
                String mark = c.getString(TAG_MARK);
                String num = c.getString(TAG_NUM);

                //registertime을 spinner에 넣기 위한 배열 정의
                if(i==0){
                    array.add("전체");
                    array.add(registered_date.substring(0, 10));
                    count+=2;
                }else {
                    for (int j = 0; j < count; j++) {
                        if (array.get(j).equals(registered_date.substring(0, 10))) {
                            break;
                        }
                        if (j == count - 1) {
                            array.add(registered_date.substring(0, 10));
                            count++;
                        }
                    }
                }

                HashMap<String, String> consults2 = new HashMap<String, String>();

                consults2.put(TAG_REGISTEREDDATE, registered_date);
                consults2.put(TAG_ASKOBJECT, ask_object);
                consults2.put(TAG_ASKSTATE, ask_state);
                consults2.put(TAG_MARK, mark);
                consults2.put(TAG_NUM, num);

                consultList.add(consults2);
            }

            adapter = new SimpleAdapter(
                    _c_consult_history.this, consultList, R.layout._c_consult_list_item,
                    new String[]{TAG_REGISTEREDDATE,TAG_ASKOBJECT,TAG_ASKSTATE},
                    new int[]{R.id.registered_date, R.id.ask_object, R.id.ask_state}
            );

            list.setAdapter(adapter);

            //spinner구현
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array){
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    view.setBackgroundColor(Color.parseColor("#2C2C2C"));
                    TextView tv = (TextView) view;
                    if(position == 0){
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.WHITE);
                    }
                    return view;
                }
            };
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i==0){
                        adapter = new SimpleAdapter(
                                _c_consult_history.this, consultList, R.layout._c_consult_list_item,
                                new String[]{TAG_REGISTEREDDATE,TAG_ASKOBJECT,TAG_ASKSTATE},
                                new int[]{R.id.registered_date, R.id.ask_object, R.id.ask_state}
                        );

                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(),_c_consult_history_detail.class);

                                intent.putExtra("date",consultList.get(position).get("registered_date"));
                                intent.putExtra("category",consultList.get(position).get("ask_object"));
                                intent.putExtra("state",consultList.get(position).get("ask_state"));
                                intent.putExtra("mark",consultList.get(position).get("mark"));
                                intent.putExtra("num",consultList.get(position).get("num"));
                                intent.putExtra("customerNum",customerNum);
                                intent.putExtra("phoneNum",phoneNum);

                                startActivity(intent);
                            }
                        });

                    }else if(i>0){
                        String a = array.get(i);
                        consultList2 = new ArrayList<HashMap<String, String>>();
                        for (int k = 0; k < consultList.size(); k++) {
                            if (consultList.get(k).get("registered_date").substring(0,10).equals(a)) {
                                consultList2.add(consultList.get(k));
                            }
                        }
                        ListAdapter adapter2 = new SimpleAdapter(
                                _c_consult_history.this, consultList2, R.layout._c_consult_list_item,
                                new String[]{TAG_REGISTEREDDATE,TAG_ASKOBJECT,TAG_ASKSTATE},
                                new int[]{R.id.registered_date, R.id.ask_object, R.id.ask_state}
                        );

                        list.setAdapter(adapter2);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent=new Intent(getApplicationContext(),_c_consult_history_detail.class);

                                intent.putExtra("date",consultList2.get(position).get("registered_date"));
                                intent.putExtra("category",consultList2.get(position).get("ask_object"));
                                intent.putExtra("state",consultList2.get(position).get("ask_state"));
                                intent.putExtra("mark",consultList2.get(position).get("mark"));
                                intent.putExtra("num",consultList2.get(position).get("num"));
                                intent.putExtra("customerNum",customerNum);
                                intent.putExtra("phoneNum",phoneNum);

                                startActivity(intent);
                            }
                        });
                    }
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
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


