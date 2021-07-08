package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class _s_ApplyActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    private static String TAG = "phptest";

    private Spinner mSpinLostObject;
    private Spinner mSpinImportance;
    private EditText mEtDiscoveryLocation;
    private EditText mEtLostObjectDetail;
    private EditText mEtLost_object;

    private String lost_object;
    private String importance;
    private String discovery_location;
    private String lost_object_detail;

    private String staff_num;
    private String cinema_num;

    String myJSON;
    private static final String TAG_RESULTS = "result";

    private static final int sub = 1001;

    JSONArray losts = null;

    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> importanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_activity_apply);

        Resources res = getResources();
        String[] category = res.getStringArray(R.array.카테고리);
        String[] impor = res.getStringArray(R.array.등급);

        mSpinLostObject = findViewById(R.id.spin_category);
        mSpinImportance = findViewById(R.id.spin_grade);
        mEtDiscoveryLocation = (EditText)findViewById(R.id.et_location);
        mEtLostObjectDetail = (EditText)findViewById(R.id.et_info);
        mEtLost_object = findViewById(R.id.et_category);

        staff_num =getIntent().getStringExtra("staff_num");
        cinema_num =getIntent().getStringExtra("cinema_num");

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

        categoryAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, category){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundColor(Color.parseColor("#262626"));
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
        mSpinLostObject.setAdapter(categoryAdapter);

        importanceAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, impor){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundColor(Color.parseColor("#262626"));
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
        mSpinImportance.setAdapter(importanceAdapter);

        mSpinLostObject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mEtLost_object.setText("");
                    mSpinImportance.setSelection(0);
                }
                else if(position > 0 && position <= 2) {
                    mEtLost_object.setText(mSpinLostObject.getSelectedItem().toString());
                    mEtLost_object.setSelection(mEtLost_object.length());
                    mSpinImportance.setSelection(1);
                }else if(position <=4){
                    mEtLost_object.setText(mSpinLostObject.getSelectedItem().toString());
                    mEtLost_object.setSelection(mEtLost_object.length());
                    mSpinImportance.setSelection(2);
                }else if(position <= 7){
                    mEtLost_object.setText(mSpinLostObject.getSelectedItem().toString());
                    mEtLost_object.setSelection(mEtLost_object.length());
                    mSpinImportance.setSelection(3);
                }else{
                    mEtLost_object.requestFocus();
                    mEtLost_object.setText("");
                    mSpinImportance.setSelection(0);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinImportance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mSpinImportance.setSelection(0);
                }
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buttonApply = (Button)findViewById(R.id.btn_apply);

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lost_object = mEtLost_object.getText().toString();
                importance = mSpinImportance.getSelectedItem().toString();
                discovery_location = mEtDiscoveryLocation.getText().toString();
                lost_object_detail = mEtLostObjectDetail.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_s_apply.php", lost_object, importance, discovery_location, lost_object_detail, staff_num, cinema_num);

                //String toast = lost_object + "/" + importance + "/" + discovery_location + "/" + lost_object_detail +"/"+ staff_num + "/" + cinema_num;

                //edit text 비우기
                mEtDiscoveryLocation.setText("");
                mEtLostObjectDetail.setText("");

                Intent intent = new Intent(getApplicationContext(),_s_ApplyPhoto.class);
                startActivityForResult(intent, sub);
            }
        });
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            losts = jsonObj.getJSONArray(TAG_RESULTS);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(_s_ApplyActivity.this, R.style._s_MyDialogTheme);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_s_ApplyActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String lost_object = (String)params[1];
            String importance = (String)params[2];
            String discovery_location = (String)params[3];
            String lost_object_detail = (String)params[4];
            String staff_num = (String)params[5];
            String cinema_num = (String)params[6];

            String serverURL = (String)params[0];
            String postParameters = "lost_object=" + lost_object + "&importance=" + importance + "&discovery_location=" + discovery_location
                    + "&lost_object_detail=" + lost_object_detail + "&staff_num=" + staff_num + "&cinema_num=" + cinema_num;


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
