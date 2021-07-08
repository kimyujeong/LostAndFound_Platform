package com.example.customerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
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

public class _s_ApproveActivity extends AppCompatActivity implements IP{

    String myJSON;

    public String getIP(){
        return IP_address;
    }

    private static final String TAG_RESULTS = "result";
    private static final String TAG_LOST_OBJECT_DETAIL = "lost_object_detail";
    private static final String TAG_OBJECT_DETAIL = "object_detail";
    private static final String TAG_LOST_NUM = "lost_num";
    private static final String TAG_RECEIVING_DATE = "receiving_date";
    private static final String TAG_RECEIVING_TIME = "receiving_time";
    private static final String TAG_REGISTERED_DATE = "registered_date";
    private static final String TAG_LOST_OBJECT = "lost_object";
    private static final String TAG_CUSTOMER_NUM = "customer_num";

    JSONArray approves = null;

    ArrayList<HashMap<String, String>> approveList;

    ListView list;

    int count=0;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> array=new ArrayList<String>();

    ListAdapter adapter;

    private String cinema_num;//////////////////////////
    private String staff_num;//////////////////////////

    private String IP_ADDRESS = getIP();

    _s_TalkAdapter_approve talkAdapter;
    ListView listView;
    ArrayList<_s_TalkItem_lostlist> talkItems = new ArrayList<>();

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_approve_list);
        list = (ListView) findViewById(R.id.listView);
        approveList = new ArrayList<HashMap<String, String>>();

        cinema_num =getIntent().getStringExtra("cinema_num");
        staff_num =getIntent().getStringExtra("staff_num");

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

        getData("http://"+IP_ADDRESS+"/_s_approve.php?cinema_num="+ cinema_num);

    }

    void loadDB() {

        new Thread() {
            @Override
            public void run() {

                String serverUri = "http://"+IP_ADDRESS+"/_s_loadDB_approve.php";

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
            approves = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < approves.length(); i++) {
                JSONObject c = approves.getJSONObject(i);
                String lost_object_detail = c.getString(TAG_LOST_OBJECT_DETAIL);
                String object_detail = c.getString(TAG_OBJECT_DETAIL);
                String lost_num = c.getString(TAG_LOST_NUM);
                String receiving_date = c.getString(TAG_RECEIVING_DATE);
                String receiving_time = c.getString(TAG_RECEIVING_TIME);
                String registered_date = c.getString(TAG_REGISTERED_DATE);
                String lost_object = c.getString(TAG_LOST_OBJECT);
                String customer_num = c.getString(TAG_CUSTOMER_NUM);

                HashMap<String, String> approves2 = new HashMap<String, String>();

                approves2.put(TAG_LOST_OBJECT_DETAIL, lost_object_detail);
                approves2.put(TAG_OBJECT_DETAIL, object_detail);
                approves2.put(TAG_LOST_NUM, lost_num);
                approves2.put(TAG_RECEIVING_DATE, receiving_date);
                approves2.put(TAG_RECEIVING_TIME, receiving_time);
                approves2.put(TAG_REGISTERED_DATE, registered_date);
                approves2.put(TAG_LOST_OBJECT, lost_object);
                approves2.put(TAG_CUSTOMER_NUM, customer_num);

                approveList.add(approves2);
            }

            /*adapter = new SimpleAdapter(
                    _s_ApproveActivity.this, approveList, R.layout._s_approve_list_item,
                    new String[]{TAG_LOST_NUM, TAG_LOST_OBJECT, TAG_REGISTERED_DATE},
                    new int[]{R.id.lost_num, R.id.lost_object, R.id.registered_date}
            );

            list.setAdapter(adapter);*/

            loadDB();

            talkAdapter = new _s_TalkAdapter_approve(getLayoutInflater(), talkItems, approveList);
            list.setAdapter(talkAdapter);
            list.setEmptyView(findViewById(R.id.emptyElement));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(),_s_ApproveActivity_detail.class);

                    /*_s_TalkItem_lostlist sTalkItemLostlist = talkItems.get(position);
                    //네트워크에 있는 이미지 읽어오기.
                    Glide.with(view).load(sTalkItemLostlist.getImgPath()).into(lost_photo);*/
                    String path=talkItems.get(position).getImgPath();
                    //Glide.with(view).load(path).into(lost_photo);

                    intent.putExtra("lost_object_detail",approveList.get(position).get("lost_object_detail"));
                    intent.putExtra("object_detail",approveList.get(position).get("object_detail"));
                    intent.putExtra("lost_num",approveList.get(position).get("lost_num"));
                    intent.putExtra("receiving_date",approveList.get(position).get("receiving_date"));
                    intent.putExtra("receiving_time",approveList.get(position).get("receiving_time"));
                    intent.putExtra("registered_date",approveList.get(position).get("registered_date"));
                    intent.putExtra("lost_object",approveList.get(position).get("lost_object"));
                    intent.putExtra("customer_num",approveList.get(position).get("customer_num"));
                    intent.putExtra("cinema_num",cinema_num);
                    intent.putExtra("staff_num",staff_num);
                    intent.putExtra("image",path);

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


