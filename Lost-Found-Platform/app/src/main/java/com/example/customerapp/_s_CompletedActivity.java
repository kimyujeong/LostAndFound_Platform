package com.example.customerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.RelativeLayout;
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

public class _s_CompletedActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();
    String myJSON;


    private static final String TAG_RESULTS = "result";
    private static final String TAG_BRANCH = "branch";
    //private static final String TAG_IMPORTANCE = "importance";
    private static final String TAG_LOST_OBJECT = "lost_object";
    private static final String TAG_LOST_OBJECT_DETAIL = "lost_object_detail";
    private static final String TAG_PROCESSING_STATE = "processing_state";
    private static final String TAG_REGISTERED_TIME = "registered_time";
    private static final String TAG_STAFF_NUM = "staff_num";
    private static final String TAG_MARK = "mark";

    private static final String TAG_PHONE_NUM = "phone_num";
    private static final String TAG_CUSTOMER_NAME = "customer_name";
    private static final String TAG_RECEIVING_DATE = "receiving_date";


    JSONArray losts = null;

    ArrayList<HashMap<String, String>> lostList;

    ListView list;

    ListAdapter adapter;

    String cinema_num; /////////////////////////////////////
    String staff_num_2;

    _s_TalkAdapter_completed talkAdapter;
    ListView listView;
    ArrayList<_s_TalkItem_lostlist> talkItems = new ArrayList<>();

    String imgPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_completed_list);
        list = (ListView) findViewById(R.id.listView);
        lostList = new ArrayList<HashMap<String, String>>();
        cinema_num =getIntent().getStringExtra("cinema_num");//////////////////////////////////
        staff_num_2 =getIntent().getStringExtra("staff_num");

        FloatingActionButton fb=(FloatingActionButton)findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),_s_MenuActivity.class);

                intent.putExtra("staff_num",staff_num_2);
                intent.putExtra("cinema_num",cinema_num);
                startActivity(intent);
            }
        });

        getData("http://" + IP_ADDRESS + "/_s_completedlist.php?cinema_num="+ cinema_num); //ip주소 ///////
    }

    void loadDB() {

        new Thread() {
            @Override
            public void run() {

                String serverUri = "http://"+IP_ADDRESS+"/_s_loadDB_completed.php";

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
                String branch = c.getString(TAG_BRANCH);
                //String importance = c.getString(TAG_IMPORTANCE);
                String lost_object = c.getString(TAG_LOST_OBJECT);
                String lost_object_detail = c.getString(TAG_LOST_OBJECT_DETAIL);
                String processing_state = c.getString(TAG_PROCESSING_STATE);
                String registered_time = c.getString(TAG_REGISTERED_TIME);
                String staff_num = c.getString(TAG_STAFF_NUM);
                String mark = c.getString(TAG_MARK);

                String phone_num = c.getString(TAG_PHONE_NUM);
                String customer_name = c.getString(TAG_CUSTOMER_NAME);
                String receiving_date = c.getString(TAG_RECEIVING_DATE);


                HashMap<String, String> losts2 = new HashMap<String, String>();

                losts2.put(TAG_BRANCH, branch);
                losts2.put(TAG_LOST_OBJECT, lost_object);
                losts2.put(TAG_LOST_OBJECT_DETAIL, lost_object_detail);
                losts2.put(TAG_PROCESSING_STATE, processing_state);
                losts2.put(TAG_REGISTERED_TIME, registered_time);
                losts2.put(TAG_STAFF_NUM, staff_num);
                losts2.put(TAG_PHONE_NUM, phone_num);
                losts2.put(TAG_CUSTOMER_NAME, customer_name);
                losts2.put(TAG_RECEIVING_DATE, receiving_date);
                //
                losts2.put(TAG_MARK,mark);

                lostList.add(losts2);
            }

            /*adapter = new SimpleAdapter(
                    _s_CompletedActivity.this, lostList, R.layout._s_completed_list_item,
                    new String[]{TAG_BRANCH, TAG_LOST_OBJECT, TAG_LOST_OBJECT_DETAIL, TAG_PROCESSING_STATE, TAG_REGISTERED_TIME, TAG_STAFF_NUM},
                    new int[]{R.id.city, R.id.object, R.id.detail, R.id.state, R.id.registertime, R.id.staffno}
            );

            list.setAdapter(adapter);
            list.setEmptyView(findViewById(R.id.emptyElement));*/

            loadDB();

            talkAdapter = new _s_TalkAdapter_completed(getLayoutInflater(), talkItems, lostList);
            list.setAdapter(talkAdapter);
            list.setEmptyView(findViewById(R.id.emptyElement));

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final RelativeLayout linear = (RelativeLayout) View.inflate(_s_CompletedActivity.this, R.layout._s_dialog_image, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(_s_CompletedActivity.this, R.style._s_MyDialogTheme);

                    String a = "\n이름: " + lostList.get(position).get("customer_name") + "\n\n" + "전화번호: " + lostList.get(position).get("phone_num") + "\n\n" + "수령날짜: " + lostList.get(position).get("receiving_date") + "\n";

                    builder.setTitle("인계 정보").setMessage(a).setView(linear);
                    ImageView lost_photo = linear.findViewById(R.id.image);

                    if (lostList.get(position).get("mark").equals("L")) {
                        String path = talkItems.get(position).getImgPath();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Glide.with(_s_CompletedActivity.this).load(path).into(lost_photo);
                        }
                    }
                    else{
                        lost_photo.setVisibility(View.GONE);
                    }

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
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



