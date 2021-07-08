package com.example.customerapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.ListAdapter;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class _c_search_detail extends AppCompatActivity implements IP, View.OnClickListener {

    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();
    private static String TAG = "phptest";

    //예매내역
    private Button btnViewing;
    private TextView textView_viewing;
    String myJSON;
    JSONArray history = null;
    ListAdapter adapter;

    ArrayList<HashMap<String, String>> reserveList;

    //예매내역 변수
    private static final String TAG_RESULTS = "result";
    private static final String TAG_CUSTOMER_NUM = "customer_num";
    private static final String TAG_CUSTOMER_NAME = "customer_name";
    private static final String TAG_SCREENING_DATE = "screening_date";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_FILM_NAME = "film_name";
    private static final String TAG_SCREENING_SITE = "screening_site";
    private static final String TAG_SCREENING_TIME = "screening_time";
    private static final String TAG_SCREENING_TIME2 = "screening_time2";
    private static final String TAG_VIEWING_HISTORY_NUM = "viewing_history_num";


    private TextView textView_Date;
    private TextView textView_Time;
    private DatePickerDialog.OnDateSetListener callbackMethod; //날짜
    private TimePickerDialog.OnTimeSetListener callbackMethod2; //시간
    private ListView viewingList;

    String selected_year;
    String selected_month;
    String selected_day;

    String selected_hour;
    String selected_minute;

    Editable object_detail;
    int lost_num;
    String receiving_date;
    String receiving_time;

    private String phoneNum;
    private String customerNum;

    String strName; //영화예매
    String num; //영화예매 플러그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_search_detail);

        phoneNum = getIntent().getStringExtra("phoneNum");
        customerNum = getIntent().getStringExtra("customerNum"); // intent로 값 받아오기


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

        reserveList = new ArrayList<HashMap<String, String>>();

        getData("http://" + IP_ADDRESS + "/_c_viewing_history.php?customer_num="+ customerNum); //ip주소 ///////


        textView_Date = (TextView) findViewById(R.id.textView_date);
        textView_Time = (TextView) findViewById(R.id.textView_time);
        textView_viewing = (TextView) findViewById(R.id.textView_viewing);
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button btn = (Button) findViewById(R.id.button_next);

        //예매내역 선택
        btnViewing = (Button)findViewById(R.id.viewing_button);
        btnViewing.setOnClickListener(this);


        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { //initialize listener(date)
                selected_year = String.valueOf(year);

                if (monthOfYear <= 8 && monthOfYear >= 0) {
                    selected_month = "0" + String.valueOf(monthOfYear + 1);
                } else {
                    selected_month = String.valueOf(monthOfYear + 1);
                }

                if (dayOfMonth <= 9 && dayOfMonth >= 1) {
                    selected_day = "0" + String.valueOf(dayOfMonth);
                } else {
                    selected_day = String.valueOf(dayOfMonth);
                }

                textView_Date.setText(selected_year + "년" + selected_month + "월" + selected_day + "일");
            }
        };

        callbackMethod2 = new TimePickerDialog.OnTimeSetListener() //initialize listener(time)
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay <= 9 && hourOfDay >= 0) {
                    selected_hour = "0" + String.valueOf(hourOfDay);
                } else {
                    selected_hour = String.valueOf(hourOfDay);
                }

                if (minute == 0 || minute == 5) {
                    selected_minute = "0" + String.valueOf(minute);
                } else {
                    selected_minute = String.valueOf(minute);
                }

                textView_Time.setText(selected_hour + "시" + selected_minute + "분");
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                object_detail = editText.getText();
                lost_num = Integer.parseInt(getIntent().getStringExtra("lost_num")); // intent로 값 받아오기
                receiving_date = selected_year + selected_month + selected_day;
                receiving_time = selected_hour + selected_minute + "00";

                String cinema=getIntent().getStringExtra("cinema");
                String category=getIntent().getStringExtra("category");

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/_c_update_vh_flag.php", "Y",num);

                Intent intent = new Intent(getApplicationContext(),_c_search_detail_next.class);
                intent.putExtra("cinema",cinema); //바꾸기
                intent.putExtra("category",category); //바꾸기
                intent.putExtra("detail",object_detail+" ");
                intent.putExtra("date",selected_year+"년 "+selected_month+"월 "+selected_day+"일");
                intent.putExtra("time",selected_hour+"시 "+selected_minute+"분");
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("customerNum",customerNum);
                intent.putExtra("info",strName);
                intent.putExtra("lost_num",lost_num+"");
                intent.putExtra("receiving_date",receiving_date);
                intent.putExtra("receiving_time",receiving_time);
                startActivityForResult(intent,sub);

            }
        });
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            history = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < history.length(); i++) {
                JSONObject c = history.getJSONObject(i);
                String customer_num = c.getString(TAG_CUSTOMER_NUM);
                String customer_name = c.getString(TAG_CUSTOMER_NAME);
                String screening_date = c.getString(TAG_SCREENING_DATE);
                String branch = c.getString(TAG_BRANCH);
                String film_name = c.getString(TAG_FILM_NAME);
                String screening_site = c.getString(TAG_SCREENING_SITE);
                String screening_time = c.getString(TAG_SCREENING_TIME);
                String screening_time2 = c.getString(TAG_SCREENING_TIME2);
                String viewing_history_num = c.getString(TAG_VIEWING_HISTORY_NUM);

                HashMap<String, String> reserves2 = new HashMap<String, String>();

                System.out.println(viewing_history_num+"\n\n");

                reserves2.put("v_h_n", viewing_history_num);
                reserves2.put("date", screening_date);
                reserves2.put("time", screening_time);
                reserves2.put("time2", screening_time2);
                reserves2.put("bran", branch);
                reserves2.put("site", screening_site);
                reserves2.put("name", film_name);

                reserveList.add(reserves2);


                System.out.println(film_name);
            }

            adapter = new SimpleAdapter(
                    _c_search_detail.this, reserveList, R.layout._c_dialog_viewing_item,
                    new String[]{"bran", "date", "name"},
                    new int[]{R.id.branch, R.id.screening_date, R.id.film_name}
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.viewing_button:

                final LinearLayout linear = (LinearLayout) View.inflate(_c_search_detail.this, R.layout._c_dialog_viewing, null);
                viewingList = linear.findViewById(R.id.viewing);
                viewingList.setAdapter(adapter);
                viewingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        strName = reserveList.get(position).get("date") + "  /  " + reserveList.get(position).get("time") + "-" + reserveList.get(position).get("time2")
                                + "\n" + reserveList.get(position).get("bran") + "점 " + reserveList.get(position).get("site") + "  /  " + "영화명: " + reserveList.get(position).get("name");
                        num = reserveList.get(position).get("v_h_n");

                        AlertDialog.Builder innBuilder = new AlertDialog.Builder(_c_search_detail.this, R.style.MyDialogTheme);
                        innBuilder.setMessage(strName);
                        innBuilder.setTitle("선택한 예매내역: ");
                        textView_viewing.setText(strName);
                        innBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        innBuilder.show();
                    }
                });

                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(_c_search_detail.this, R.style.MyDialogTheme);

                alertBuilder.setTitle("예매내역 중 하나를 선택하세요.");
                alertBuilder.setView(linear);

                //버튼 생성
                alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertBuilder.show();
                break;

            default:
                break;
        }
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_c_search_detail.this,
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

            String customerNum = (String) params[1];
            String lostNum = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "customer_num=" + customerNum + "&lost_num=" + lostNum;

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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
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

    //날짜선택 다이얼로그
    public void OnClickHandler(View view) //date handler
    {
        //현재 날짜 설정
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

        String year = yearFormat.format(date);
        String month = monthFormat.format(date);
        String day = dayFormat.format(date);

        int year_i = Integer.parseInt(year);
        int month_i = Integer.parseInt(month);
        int day_i = Integer.parseInt(day);

        //다이얼로그 출력
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.MyDialogTheme, callbackMethod, year_i, month_i - 1, day_i);

        dialog.show();

    }

    public void OnClickHandler2(View view) //time handler
    {
        TimePickerDialog dialog = new TimePickerDialog(this, R.style.MyDialogTheme, callbackMethod2, 0, 0, true);
        dialog.show();
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