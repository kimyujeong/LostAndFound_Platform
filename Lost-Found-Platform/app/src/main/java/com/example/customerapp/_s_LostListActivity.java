package com.example.customerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

public class _s_LostListActivity extends AppCompatActivity implements IP {

    public String getIP() {
        return IP_address;
    }

    private String IP_ADDRESS = getIP();


    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_IMPORTANCE = "importance";
    private static final String TAG_LOST_OBJECT = "lost_object";
    private static final String TAG_LOST_OBJECT_DETAIL = "lost_object_detail";
    private static final String TAG_PROCESSING_STATE = "processing_state";
    private static final String TAG_REGISTERED_TIME = "registered_time";
    private static final String TAG_EXPIRED_DATE = "expired_date";
    private static final String TAG_STAFF_NUM = "staff_name";

    JSONArray losts = null;

    ArrayList<HashMap<String, String>> lostList;

    ListView list;
    int count = 0;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> array = new ArrayList<String>();

    ListAdapter adapter;
    private String cinema_num;
    private String staff_num;

    //이미지
    _s_TalkAdapter_lostlist talkAdapter;
    ListView listView;
    ArrayList<_s_TalkItem_lostlist> talkItems = new ArrayList<>();
    ArrayList<_s_TalkItem_lostlist> talkItems2 = new ArrayList<>();

    String imgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_lost_list);
        spinner = (Spinner) findViewById(R.id.spinner);
        list = (ListView) findViewById(R.id.listView);
        lostList = new ArrayList<HashMap<String, String>>();
        cinema_num = getIntent().getStringExtra("cinema_num");
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
        getData("http://" + IP_ADDRESS + "/_s_cinelf_lostlist.php?cinema_num=" + cinema_num);

    }

    //이미지
    void loadDB() {

        new Thread() {
            @Override
            public void run() {

                String serverUri = "http://"+IP_ADDRESS+"/loadDB.php";

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
                    talkItems.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            talkAdapter.notifyDataSetChanged();
                        }
                    });
                    for (String row : rows) {
                        String[] datas = row.split("&");
                        if (datas.length != 3) continue;

                        String num = datas[1];
                        if (!num.equals(cinema_num)) continue;
                        imgPath = "http://"+IP_ADDRESS+"/" + datas[0];   //이미지는 상대경로라서 앞에 서버 주소를 써야한다.
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

    void loadDB2(final String date_p) {

        new Thread() {
            @Override
            public void run() {

                String serverUri = "http://"+IP_ADDRESS+"/loadDB.php";

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

                    String date_load=date_p;
                    //읽어온 문자열에서 row(레코드)별로 분리하여 배열로 리턴하기
                    String[] rows = buffer.toString().split(";");

                    System.out.println("++++++++++++++" + rows[0]);

                    //대량의 데이터 초기화
                    talkItems.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            talkAdapter.notifyDataSetChanged();
                        }
                    });
                    for (String row : rows) {
                        String[] datas = row.split("&");
                        if (datas.length != 3) continue;

                        String num = datas[1];
                        if (!num.equals(cinema_num)) continue;
                        if (!datas[2].equals(date_load)) continue;
                        imgPath = "http://"+IP_ADDRESS+"/" + datas[0];   //이미지는 상대경로라서 앞에 서버 주소를 써야한다.
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
                String importance = c.getString(TAG_IMPORTANCE);
                String lost_object = c.getString(TAG_LOST_OBJECT);
                String lost_object_detail = c.getString(TAG_LOST_OBJECT_DETAIL);
                String processing_state = c.getString(TAG_PROCESSING_STATE);
                String registered_time = c.getString(TAG_REGISTERED_TIME);
                String expired_date = c.getString(TAG_EXPIRED_DATE);
                String staff_num = c.getString(TAG_STAFF_NUM);

                //registertime을 spinner에 넣기 위한 배열 정의
                if (i == 0) {
                    array.add("전체");
                    array.add(registered_time.substring(0, 10));
                    count += 2;
                } else {
                    for (int j = 0; j < count; j++) {
                        if (array.get(j).equals(registered_time.substring(0, 10))) {
                            break;
                        }
                        if (j == count - 1) {
                            array.add(registered_time.substring(0, 10));
                            count++;
                        }
                    }
                }

                HashMap<String, String> losts2 = new HashMap<String, String>();

                losts2.put(TAG_BRANCH, branch);
                losts2.put(TAG_IMPORTANCE, importance);
                losts2.put(TAG_LOST_OBJECT, lost_object);
                losts2.put(TAG_LOST_OBJECT_DETAIL, lost_object_detail);
                losts2.put(TAG_PROCESSING_STATE, processing_state);
                losts2.put(TAG_REGISTERED_TIME, registered_time);
                losts2.put(TAG_EXPIRED_DATE, expired_date);
                losts2.put(TAG_STAFF_NUM, staff_num);

                lostList.add(losts2);

                loadDB();

                talkAdapter = new _s_TalkAdapter_lostlist(getLayoutInflater(), talkItems, lostList);
                list.setAdapter(talkAdapter);
                list.setEmptyView(findViewById(R.id.emptyElement));
            }

            //이미지 확대
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final RelativeLayout linear = (RelativeLayout) View.inflate(_s_LostListActivity.this, R.layout._s_dialog_image, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder( _s_LostListActivity.this, R.style._s_MyDialogTheme);

                    ImageView image = new ImageView(_s_LostListActivity.this);
                    String path= talkItems.get(position).getImgPath();

                    builder.setTitle("분실물 사진");
                    builder.setView(linear);
                            //.setMessage(lostList.get(position).get(TAG_LOST_OBJECT_DETAIL)).setView(linear);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ImageView lost_photo = linear.findViewById(R.id.image);
                        Glide.with(_s_LostListActivity.this).load(path).into(lost_photo);
                    }

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            //Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            //spinner구현
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, array) {
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    view.setBackgroundColor(Color.parseColor("#262626"));
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.WHITE);
                    }
                    return view;
                }
            };
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        loadDB();

                        talkAdapter = new _s_TalkAdapter_lostlist(getLayoutInflater(), talkItems, lostList);
                        list.setAdapter(talkAdapter);
                        list.setEmptyView(findViewById(R.id.emptyElement));
                    } else if (i > 0) {
                        String a = array.get(i);
                        ArrayList<HashMap<String, String>> lostList2 = new ArrayList<HashMap<String, String>>();
                        for (int k = 0; k < lostList.size(); k++) {
                            //date=
                            if (lostList.get(k).get("registered_time").substring(0, 10).equals(a)) {
                                lostList2.add(lostList.get(k));
                            }
                        }
                        loadDB2(a);
                        talkAdapter = new _s_TalkAdapter_lostlist(getLayoutInflater(), talkItems, lostList2);
                        list.setAdapter(talkAdapter);
                        list.setEmptyView(findViewById(R.id.emptyElement));
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


