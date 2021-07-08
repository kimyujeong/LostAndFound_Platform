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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class _s_ExpectedActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_LOST_NUM = "lost_num";
    private static final String TAG_LOST_OBJECT = "lost_object";
    private static final String TAG_CUSTOMER_NAME = "customer_name";
    private static final String TAG_RECEIVING_DATE = "receiving_date";
    private static final String TAG_RECEIVING_TIME = "receiving_time";
    private static final String TAG_MARK = "mark";

    //다음 액티비티 정보
    private static final String TAG_LOST_OBJECT_DETAIL = "lost_object_detail";
    private static final String TAG_PHONE_NUM = "phone_num";



    JSONArray losts = null;

    ArrayList<HashMap<String, String>> lostList;
    ArrayList<HashMap<String, String>> lostList2;

    ListView list;
    int count=0;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> array=new ArrayList<String>();

    ListAdapter adapter;
    String cinema_num; ///////////////////////////////5
    String staff_num; ///////////////////////////////5

    _s_TalkAdapter_expected talkAdapter;
    ListView listView;
    ArrayList<_s_TalkItem_lostlist> talkItems = new ArrayList<>();

    String imgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_completed_list);
        spinner=(Spinner)findViewById(R.id.spinner);
        list = (ListView) findViewById(R.id.listView);
        lostList = new ArrayList<HashMap<String, String>>();
        cinema_num =getIntent().getStringExtra("cinema_num");////////////////////////////////////////////
        staff_num=getIntent().getStringExtra("staff_num");

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

        getData("http://"+IP_ADDRESS+"/_s_cinelf_expectedlist.php?cinema_num="+ cinema_num); //ip주소 ////////////////////////

    }

    void loadDB() {

        new Thread() {
            @Override
            public void run() {

                String serverUri = "http://"+IP_ADDRESS+"/_s_loadDB_expected.php";

                try {
                    URL url = new URL(serverUri);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);

                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    final StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }


                    //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
                    String[] rows = buffer.toString().split(";");

                    System.out.println("++++++++++++++" + rows[0]);

                    //대량의 데이터 초기화
                    //talkItems.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            talkAdapter.notifyDataSetChanged();
                        }
                    });
                    for (String row : rows) {
                        String[] datas = row.split("&");
                        if (datas.length != 3) continue;

                        String num = datas[2];
                        if (!num.equals(cinema_num)) continue;
                        imgPath = "http://"+IP_ADDRESS+"/" + datas[1];
                        talkItems.add(new _s_TalkItem_lostlist(imgPath));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                talkAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            losts = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < losts.length(); i++) {
                JSONObject c = losts.getJSONObject(i);

                String lost_num=c.getString(TAG_LOST_NUM);
                String customer_name=c.getString(TAG_CUSTOMER_NAME);
                String phone_num=c.getString(TAG_PHONE_NUM);
                String receiving_date=c.getString(TAG_RECEIVING_DATE);
                String receiving_time=c.getString(TAG_RECEIVING_TIME);
                String lost_object = c.getString(TAG_LOST_OBJECT);
                String lost_object_detail = c.getString(TAG_LOST_OBJECT_DETAIL);
                String mark = c.getString(TAG_MARK);

                //registertime을 spinner에 넣기 위한 배열 정의
                /*if(i==0){
                    array.add("전체");
                    array.add(receiving_date);
                    count+=2;
                }else {
                    for (int j = 0; j < count; j++) {
                        if (array.get(j).equals(receiving_date)) {
                            break;
                        }
                        if (j == count - 1) {
                            array.add(receiving_date);
                            count++;
                        }
                    }
                }*/

                HashMap<String, String> losts2 = new HashMap<String, String>();

                losts2.put(TAG_LOST_NUM, lost_num);
                losts2.put(TAG_LOST_OBJECT, lost_object);
                losts2.put(TAG_LOST_OBJECT_DETAIL, lost_object_detail);
                losts2.put(TAG_CUSTOMER_NAME, customer_name);
                losts2.put(TAG_PHONE_NUM, phone_num);
                losts2.put(TAG_RECEIVING_DATE, receiving_date);
                losts2.put(TAG_RECEIVING_TIME, receiving_time);
                losts2.put(TAG_MARK, mark);

                lostList.add(losts2);
            }

            /*adapter = new SimpleAdapter(
                    _s_ExpectedActivity.this, lostList, R.layout._s_expected_list_item,
                    new String[]{TAG_LOST_NUM,TAG_MARK, TAG_CUSTOMER_NAME, TAG_PHONE_NUM, TAG_LOST_OBJECT, TAG_LOST_OBJECT_DETAIL, TAG_RECEIVING_DATE, TAG_RECEIVING_TIME
                    },
                    new int[]{R.id.lost_num,R.id.mark, R.id.customer_name, R.id.phone_num, R.id.lost_object, R.id.lost_object_detail, R.id.receiving_date, R.id.receiving_time}
            );
            list.setAdapter(adapter);
            list.setEmptyView(findViewById(R.id.emptyElement));*/

            loadDB();

            talkAdapter = new _s_TalkAdapter_expected(getLayoutInflater(), talkItems, lostList);
            list.setAdapter(talkAdapter);
            list.setEmptyView(findViewById(R.id.emptyElement));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(lostList.get(position).get("mark").equals("L")) {
                        Intent intent = new Intent(getApplicationContext(), _s_ExpectedList_detail.class);
                        String path = talkItems.get(position).getImgPath();

                        intent.putExtra("lost_num", lostList.get(position).get("lost_num"));
                        intent.putExtra("customer_name", lostList.get(position).get("customer_name"));
                        intent.putExtra("phone_num", lostList.get(position).get("phone_num"));
                        intent.putExtra("lost_object", lostList.get(position).get("lost_object"));
                        intent.putExtra("lost_object_detail", lostList.get(position).get("lost_object_detail"));
                        intent.putExtra("receiving_date", lostList.get(position).get("receiving_date"));
                        intent.putExtra("receiving_time", lostList.get(position).get("receiving_time"));
                        intent.putExtra("staff_num", staff_num);
                        intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////
                        intent.putExtra("mark", lostList.get(position).get("mark"));
                        intent.putExtra("image", path);
                        startActivity(intent);
                    }else if(lostList.get(position).get("mark").equals("A")) {
                        Intent intent = new Intent(getApplicationContext(), _s_ExpectedList_detail.class);
                        intent.putExtra("lost_num", lostList.get(position).get("lost_num"));
                        intent.putExtra("customer_name", lostList.get(position).get("customer_name"));
                        intent.putExtra("phone_num", lostList.get(position).get("phone_num"));
                        intent.putExtra("lost_object", lostList.get(position).get("lost_object"));
                        intent.putExtra("lost_object_detail", lostList.get(position).get("lost_object_detail"));
                        intent.putExtra("receiving_date", lostList.get(position).get("receiving_date"));
                        intent.putExtra("receiving_time", lostList.get(position).get("receiving_time"));
                        intent.putExtra("staff_num", staff_num);
                        intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////
                        intent.putExtra("mark", lostList.get(position).get("mark"));
                        startActivity(intent);
                    }
                }
            });

            //spinner구현
            /*arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,array){
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    view.setBackgroundColor(Color.parseColor("#262626"));
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
                                _s_ExpectedActivity.this, lostList, R.layout._s_expected_list_item,
                                new String[]{TAG_LOST_NUM, TAG_MARK, TAG_CUSTOMER_NAME, TAG_PHONE_NUM, TAG_LOST_OBJECT, TAG_LOST_OBJECT_DETAIL, TAG_RECEIVING_DATE, TAG_RECEIVING_TIME},
                                new int[]{R.id.lost_num, R.id.mark,R.id.customer_name, R.id.phone_num, R.id.lost_object, R.id.lost_object_detail, R.id.receiving_date, R.id.receiving_time}
                        );

                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(), _s_ExpectedList_detail.class);

                                intent.putExtra("lost_num",lostList.get(position).get("lost_num"));
                                intent.putExtra("customer_name",lostList.get(position).get("customer_name"));
                                intent.putExtra("phone_num",lostList.get(position).get("phone_num"));
                                intent.putExtra("lost_object",lostList.get(position).get("lost_object"));
                                intent.putExtra("lost_object_detail",lostList.get(position).get("lost_object_detail"));
                                intent.putExtra("receiving_date",lostList.get(position).get("receiving_date"));
                                intent.putExtra("receiving_time",lostList.get(position).get("receiving_time"));
                                intent.putExtra("staff_num",staff_num);
                                intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////
                                intent.putExtra("mark",lostList.get(position).get("mark"));
                                startActivity(intent);
                            }
                        });
                    }else if(i>0){
                        String a = array.get(i);lostList2 = new ArrayList<HashMap<String, String>>();
                        for (int k = 0; k < lostList.size(); k++) {
                            if (lostList.get(k).get("receiving_date").equals(a)) {
                                lostList2.add(lostList.get(k));
                            }
                        }
                        ListAdapter adapter2 = new SimpleAdapter(
                                _s_ExpectedActivity.this, lostList2, R.layout._s_expected_list_item,
                                new String[]{TAG_LOST_NUM, TAG_MARK, TAG_CUSTOMER_NAME, TAG_PHONE_NUM, TAG_LOST_OBJECT, TAG_LOST_OBJECT_DETAIL, TAG_RECEIVING_DATE, TAG_RECEIVING_TIME},
                                new int[]{R.id.lost_num,R.id.mark, R.id.customer_name, R.id.phone_num, R.id.lost_object, R.id.lost_object_detail, R.id.receiving_date, R.id.receiving_time}
                        );

                        list.setAdapter(adapter2);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(), _s_ExpectedList_detail.class);

                                intent.putExtra("lost_num",lostList2.get(position).get("lost_num"));
                                intent.putExtra("customer_name",lostList2.get(position).get("customer_name"));
                                intent.putExtra("phone_num",lostList2.get(position).get("phone_num"));
                                intent.putExtra("lost_object",lostList2.get(position).get("lost_object"));
                                intent.putExtra("lost_object_detail",lostList2.get(position).get("lost_object_detail"));
                                intent.putExtra("receiving_date",lostList2.get(position).get("receiving_date"));
                                intent.putExtra("receiving_time",lostList2.get(position).get("receiving_time"));
                                intent.putExtra("mark",lostList2.get(position).get("mark"));
                                intent.putExtra("staff_num",staff_num);
                                intent.putExtra("cinema_num", cinema_num);//////////////////////////////////////////
                                startActivity(intent);



                            }
                        });
                    }
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });*/



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

