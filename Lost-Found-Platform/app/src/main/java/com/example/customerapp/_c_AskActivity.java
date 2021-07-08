package com.example.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class _c_AskActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    private String IP_ADDRESS = getIP();
    private static String TAG = "phptest";

    private String phoneNum;
    private String customerNum;

    private Spinner spinCity;
    private Spinner spinBranch;
    private EditText etObject;
    private EditText etObjectDetail;
    private EditText etLostLocation;
    private Button btnAsk;

    private String city;
    private String branch;
    private String object;
    private String objectDetail;
    private String lostLocation;



    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_BRANCH = "branch";


    JSONArray branches = null;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_activity_ask);

        phoneNum = getIntent().getStringExtra("phoneNum");
        customerNum = getIntent().getStringExtra("customerNum");

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

        System.out.println("넘어온 phoneNum, customerNum : " + phoneNum + ", " + customerNum);
        //Toast.makeText(getApplicationContext(), phoneNum + ", " + customerNum, Toast.LENGTH_SHORT).show();

        spinCity = findViewById(R.id.spin_city);
        spinBranch = findViewById(R.id.spin_branch);
        etObject = findViewById(R.id.et_object);
        etObjectDetail = findViewById(R.id.et_object_detail);
        etLostLocation = findViewById(R.id.et_lost_location);
        btnAsk = findViewById(R.id.btn_ask);

        Resources res = getResources();
        String[] cityarray = res.getStringArray(R.array.지역);

        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cityarray){
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

        spinCity.setAdapter(cityAdapter);

        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    city = spinCity.getSelectedItem().toString();
                    getData("http://" + IP_ADDRESS + "/_c_ask_branch.php?city=" + city);
                }
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    branch = spinBranch.getSelectedItem().toString();
                }
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object = etObject.getText().toString();
                objectDetail = etObjectDetail.getText().toString();
                lostLocation = etLostLocation.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_c_ask.php", city, branch, object, objectDetail,
                        lostLocation, customerNum);

                etObject.setText("");
                etObjectDetail.setText("");
                etLostLocation.setText("");

                Intent intent = new Intent(getApplicationContext(),_c_MenuActivity.class);
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("customerNum",customerNum);
                Toast.makeText(getApplication(),"문의 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                startActivityForResult(intent,sub);
            }
        });

    }

    protected void showList() {
        ArrayList<String> branchArray=new ArrayList<String>();

        branchArray.add("지점을 선택하세요");

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, branchArray);
        spinBranch.setAdapter(arrayAdapter);

        try {
            System.out.println("1 - myJSON: " + myJSON);
            //System.out.println("1 - jsonObj: " + jsonObj);

            JSONObject jsonObj = new JSONObject(myJSON);
            branches = jsonObj.getJSONArray(TAG_RESULTS);

            System.out.println("2 - myJSON: " + myJSON);
            System.out.println("2 - jsonObj: " + jsonObj);
            System.out.println("2 - branches: " + branches);

            //branchArray.add("지점을 선택하세요");

            for (int i = 0; i < branches.length(); i++) {
                JSONObject c = branches.getJSONObject(i);
                String jsonBranch = c.getString(TAG_BRANCH);
                branchArray.add(jsonBranch);
            }

            System.out.println("3 - branchArray: " + branchArray);

            //지점 spinner구현
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, branchArray){
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

            spinBranch.setAdapter(arrayAdapter);

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
        ProgressDialog progressDialog = new ProgressDialog(_c_AskActivity.this, R.style.MyDialogTheme);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_c_AskActivity.this,
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
            String city = (String)params[1];
            String branch = (String)params[2];
            String askObject = (String)params[3];
            String askObjectDetail = (String)params[4];
            String lostLocation = (String)params[5];
            String customerNum = (String)params[6];

            String serverURL = (String)params[0];
            String postParameters = "city=" + city + "&branch=" + branch + "&ask_object=" + askObject + "&ask_object_detail=" + askObjectDetail +
                    "&lost_location=" + lostLocation + "&customer_num=" + customerNum;

            System.out.println(city + "/" + branch + "/" + askObject + "/" + askObjectDetail + "/" + lostLocation + "/" + customerNum);

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