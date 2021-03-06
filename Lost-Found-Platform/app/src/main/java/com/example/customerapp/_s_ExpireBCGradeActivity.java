package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class _s_ExpireBCGradeActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();
    private static String TAG = "phptest";

    String stringlno = "";
    String myJSON;
    String state;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_LNO = "lost_num";
    private static final String TAG_SITE = "cinema_num";
    private static final String TAG_OBJECT = "lost_object";
    private static final String TAG_DETAIL = "lost_object_detail";
    private static final String TAG_STATE="processing_state";
    private static final String TAG_EXPIRED = "expired_date";
    private static final String TAG_STAFFNO = "staff_num";

    JSONArray expires = null;

    ArrayList<HashMap<String, String>> expireList;

    ListView list;

    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> array = new ArrayList<String>();

    ListAdapter adapter;


    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");

    Date today;

    String expiredTime;

    private String cinema_num;
    private String staff_num;

    _s_TalkAdapter_expire talkAdapter;
    ListView listView;
    ArrayList<_s_TalkItem_lostlist> talkItems = new ArrayList<>();

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_expire_list_bc);

        list = (ListView) findViewById(R.id.expiredListViewBC);
        expireList = new ArrayList<HashMap<String, String>>();
        cinema_num =getIntent().getStringExtra("cinema_num");////////////////////////////////////////////
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

        getData("http://" + IP_ADDRESS + "/_s_expirelistBC.php?cinema_num="+ cinema_num); //ip??????
        today = new Date();
        expiredTime = "2020-08-17";
        //// ?????? ?????? ????????? ?????? ???... ?????? ????????? ?????? ??????
        //expiredTime = format1.format(today);
    }

    void loadDB() {

        new Thread() {
            @Override
            public void run() {

                String serverUri = "http://"+IP_ADDRESS+"/_s_loadDB_expireBC.php";

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


                    //????????? ??????????????? row(?????????)?????? ???????????? ????????? ????????????
                    String[] rows = buffer.toString().split(";");

                    System.out.println("++++++++++++++" + rows[0]);

                    //????????? ????????? ?????????
                    //talkItems.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            talkAdapter.notifyDataSetChanged();
                        }
                    });
                    for (String row : rows) {
                        String[] datas = row.split("&");
                        if (datas.length != 4) continue;

                        String num = datas[2];
                        if (!num.equals(cinema_num)) continue;
                        String date=datas[3];
                        if (!date.equals(expiredTime)) continue;
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
            expires = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < expires.length(); i++) {
                JSONObject c = expires.getJSONObject(i);
                String lost_num = c.getString(TAG_LNO);
                String lost_object = c.getString(TAG_OBJECT);
                String cinema_num = c.getString(TAG_SITE);
                String processing_state = c.getString(TAG_STATE);
                String staff_num = c.getString(TAG_STAFFNO);
                String lost_object_detail = c.getString(TAG_DETAIL);
                String expired_date = c.getString(TAG_EXPIRED);

                HashMap<String, String> expires2 = new HashMap<String, String>();

                expires2.put(TAG_LNO, lost_num);
                expires2.put(TAG_OBJECT, lost_object);
                expires2.put(TAG_SITE, cinema_num);
                expires2.put(TAG_STATE, processing_state);
                expires2.put(TAG_STAFFNO, staff_num);
                expires2.put(TAG_DETAIL, lost_object_detail);
                expires2.put(TAG_EXPIRED, expired_date);

                expireList.add(expires2);
            }

            final ArrayList<HashMap<String, String>> expireList2 = new ArrayList<HashMap<String, String>>();

            if(expireList.size() != 0){

                for (int k = 0; k < expireList.size(); k++) {
                    if (expireList.get(k).get("expired_date").substring(0,10).equals(expiredTime)) {
                        expireList2.add(expireList.get(k));
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), expiredTime + "??? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
            }

            /*adapter = new SimpleAdapter(
                    _s_ExpireBCGradeActivity.this, expireList2, R.layout._s_expire_list_item,
                    new String[]{TAG_LNO, TAG_OBJECT, TAG_SITE, TAG_STATE, TAG_STAFFNO, TAG_DETAIL,TAG_EXPIRED},
                    new int[]{R.id.lno, R.id.object, R.id.site, R.id.state, R.id.staffno, R.id.detail,R.id.expired}
            );

            list.setAdapter(adapter);
            list.setEmptyView(findViewById(R.id.emptyElement));*/

            loadDB();

            talkAdapter = new _s_TalkAdapter_expire(getLayoutInflater(), talkItems, expireList2);
            list.setAdapter(talkAdapter);
            list.setEmptyView(findViewById(R.id.emptyElement));

            ImageButton selectAllBtn = (ImageButton)findViewById(R.id.selectAllBtn);
            selectAllBtn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int count = 0 ;
                    count = talkAdapter.getCount() ;

                    for (int i=0; i<count; i++) {
                        list.setItemChecked(i, true) ;
                    }
                }
            }) ;

            Button deleteBtn = (Button) findViewById(R.id.deleteBtn) ;

            deleteBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    SparseBooleanArray checkItems = list.getCheckedItemPositions();
                    int count = talkAdapter.getCount();

                    for(int i = count-1; i >= 0; i--){
                        if(checkItems.get(i)){
                            stringlno += expireList2.get(i).get(TAG_LNO) + ", ";
                        }
                    }

                    state = "????????????";
                    InsertData task = new InsertData();

                    task.execute("http://" + IP_ADDRESS + "/_s_deleteBC.php", stringlno, state);
                }
            }) ;

            //????????? ??????
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    final RelativeLayout linear = (RelativeLayout) View.inflate(_s_ExpireBCGradeActivity.this, R.layout._s_dialog_image, null);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder( _s_ExpireBCGradeActivity.this, R.style._s_MyDialogTheme);

                    String path=talkItems.get(position).getImgPath();

                    builder.setTitle("????????? ??????").setMessage(expireList2.get(position).get(TAG_DETAIL)).setView(linear);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ImageView lost_photo = linear.findViewById(R.id.image);
                        Glide.with(_s_ExpireBCGradeActivity.this).load(path).into(lost_photo);
                    }

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            //Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        }
                    });
                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
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

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(_s_ExpireBCGradeActivity.this, R.style._s_MyDialogTheme);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_s_ExpireBCGradeActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String lno = (String)params[1];
            String state = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "lno=" + lno + "&state=" + state;


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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
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

}


