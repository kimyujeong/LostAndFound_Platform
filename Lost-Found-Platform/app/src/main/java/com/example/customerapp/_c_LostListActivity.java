package com.example.customerapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class _c_LostListActivity extends AppCompatActivity implements IP{

    public String getIP(){
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_LOST_OBJECT = "lost_object";
    private static final String TAG_PROCESSING_STATE = "processing_state";
    private static final String TAG_REGISTERED_TIME = "registered_time";
    private static final String TAG_EXPIRED_DATE = "expired_date";
    private static final String TAG_CITY = "city";
    private static final String TAG_LOST_NUM = "lost_num";

    private DatePickerDialog.OnDateSetListener callbackMethod;

    JSONArray losts = null;

    ArrayList<HashMap<String, String>> lostList;
    ArrayList<HashMap<String, String>> lostList4;
    ListView list;
    int count = 0;
    Spinner spinner1;
    Spinner spinner2;
    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;

    //spinner1 -> city
    ArrayList<String> array1 = new ArrayList<String>();
    //spinner2 -> branch
    ArrayList<String> array2 = new ArrayList<String>();

    ListAdapter adapter;
    // private String staff_site;//////////////////////////

    //선택한 날짜 비교 위한 전역
    String selected_year = "";
    String selected_month = "";
    String selected_day = "";
    ArrayList<HashMap<String, String>> lostList3_copy = new ArrayList<HashMap<String, String>>();

    private String phoneNum;
    private String customerNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_lost_list);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        list = (ListView) findViewById(R.id.listView);
        lostList = new ArrayList<HashMap<String, String>>();

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

        getData("http://"+IP_ADDRESS+"/_c_cinelf_lostlist.php");

    }

    //날짜선택 다이얼로그
    public void OnClickHandler(View view) {
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

    public void SearchListener(View view){
        adapter = new SimpleAdapter(
                _c_LostListActivity.this, lostList4, R.layout._c_lost_list_item,
                new String[]{TAG_BRANCH, TAG_LOST_OBJECT, TAG_PROCESSING_STATE, TAG_REGISTERED_TIME, TAG_EXPIRED_DATE},
                new int[]{R.id.branch, R.id.lost_object, R.id.processing_state, R.id.registered_time, R.id.expired_date}
        );
        list.setAdapter(adapter);

        //_c_search_detail
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),_c_search_detail.class);

                intent.putExtra("cinema",lostList4.get(position).get("branch")+" / "+lostList4.get(position).get("city"));
                intent.putExtra("category",lostList4.get(position).get("lost_object"));
                intent.putExtra("lost_num",lostList4.get(position).get("lost_num"));
                //intent.putExtra("city",lostList.get(position).get("city"));
                //intent.putExtra("processing_state",lostList.get(position).get("processing_state"));
                //intent.putExtra("registered_time",lostList.get(position).get("registered_time"));
                //intent.putExtra("expired_date",lostList.get(position).get("expired_date"));
                intent.putExtra("phoneNum",phoneNum);
                intent.putExtra("customerNum",customerNum);
                startActivity(intent);
            }
        });
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            losts = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < losts.length(); i++) {
                JSONObject c = losts.getJSONObject(i);
                String branch = c.getString(TAG_BRANCH);
                String lost_object = c.getString(TAG_LOST_OBJECT);
                String processing_state = c.getString(TAG_PROCESSING_STATE);
                String registered_time = c.getString(TAG_REGISTERED_TIME);
                String expired_date = c.getString(TAG_EXPIRED_DATE);
                String city = c.getString(TAG_CITY);
                String lost_num = c.getString(TAG_LOST_NUM);


                //city를 spinner1에 넣기 위한 배열 정의
                if (i == 0) {
                    array1.add("전체");
                    array1.add(city);
                    array2.add("전체");
                    count += 2;
                } else {
                    for (int j = 0; j < count; j++) {
                        if (array1.get(j).equals(city)) {
                            break;
                        }
                        if (j == count - 1) {
                            array1.add(city);
                            //array2.add(branch);
                            count++;
                        }
                    }
                }

                HashMap<String, String> losts2 = new HashMap<String, String>();

                losts2.put(TAG_BRANCH, branch);
                losts2.put(TAG_LOST_OBJECT, lost_object);
                losts2.put(TAG_PROCESSING_STATE, processing_state);
                losts2.put(TAG_REGISTERED_TIME, registered_time);
                losts2.put(TAG_EXPIRED_DATE, expired_date);
                losts2.put(TAG_CITY, city);
                losts2.put(TAG_LOST_NUM, lost_num);

                lostList.add(losts2);
            }


            //spinner1 구현_city
            arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, array1) {
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    view.setBackgroundColor(Color.parseColor("#2C2C2C"));
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.WHITE);
                    }
                    return view;
                }
            };

            //spinner2 구현///////////////////////////////////////////////////////////////////
            arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, array2) {
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    view.setBackgroundColor(Color.parseColor("#2C2C2C"));
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.WHITE);
                    }
                    return view;
                }
            };
            /////////////////////////////////////////////////////////////////////////////////

            spinner1.setAdapter(arrayAdapter1);
            spinner2.setAdapter(arrayAdapter2);

            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                    if (i == 0) {

                    } else if (i > 0) {
                        final String a = array1.get(i);
                        //디버그 용
                        //Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                        final ArrayList<HashMap<String, String>> lostList2 = new ArrayList<HashMap<String, String>>();
                        for (int k = 0; k < lostList.size(); k++) {
                            if (lostList.get(k).get("city").equals(a)) {
                                lostList2.add(lostList.get(k));
                            }
                        }

                        ///////spinner2에서 지역입력////////////////////
                        if (a.equals("서울")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("강변");
                            array2.add("상봉");
                            array2.add("왕십리");
                            array2.add("용산");
                        }
                        if (a.equals("경기/인천")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("구리");
                            array2.add("수원");
                            array2.add("파주");
                            array2.add("계양");
                            array2.add("부평");
                        }

                        if (a.equals("강원")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("강릉");
                            array2.add("원주");
                            array2.add("춘천");
                        }

                        if (a.equals("대전/충청")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("대전");
                            array2.add("천안");
                            array2.add("청주");
                        }

                        if (a.equals("부산/울산")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("서면");
                            array2.add("울산");
                            array2.add("해운대");
                        }

                        if (a.equals("경상/대구")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("대구");
                            array2.add("대구현대");
                            array2.add("김해");
                            array2.add("통영");
                            array2.add("포항");
                        }
                        if (a.equals("광주/전라")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("광주터미널");
                            array2.add("여수");
                            array2.add("전주");
                        }
                        if (a.equals("제주")) {
                            array2.clear();
                            array2.add("전체");
                            array2.add("제주노형");
                        }

                        /////////////////////branch 스피너 구현///////////////////////////////////////////////////////////////////////////////////
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (i == 0) {
                                }
                                if (i > 0) {
                                    String b = array2.get(i);
                                    ArrayList<HashMap<String, String>> lostList3 = new ArrayList<HashMap<String, String>>();
                                    for (int k = 0; k < lostList2.size(); k++) {
                                        if (lostList2.get(k).get("branch").equals(b)) {
                                            lostList3.add(lostList2.get(k));
                                            lostList3_copy.add(lostList2.get(k));
                                        }
                                    }

                                }
                                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                    ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            //////////////////날짜비교2/////////////////////////////
            callbackMethod = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    selected_year = String.valueOf(year);

                    //Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
                    if (!selected_year.equals("")) {
                        //디버그 용
                        //Toast.makeText(getApplicationContext(), "들어왔다!", Toast.LENGTH_SHORT).show();

                        if (monthOfYear <= 8 ) {
                            selected_month = "0" + String.valueOf(monthOfYear+1);
                        } else {
                            selected_month = String.valueOf(monthOfYear+1);
                        }

                        if (dayOfMonth <=9) {
                            selected_day = "0" + String.valueOf(dayOfMonth);
                        } else {
                            selected_day = String.valueOf(dayOfMonth);
                        }

                        String date = selected_year + "-" + selected_month + "-" + selected_day;
                        //Toast.makeText(getApplicationContext(),date, Toast.LENGTH_SHORT).show();

                        lostList4 = new ArrayList<HashMap<String, String>>();
                        for (int p = 0; p < lostList3_copy.size(); p++) {
                            if (lostList3_copy.get(p).get("registered_time").equals(date)) {
                                lostList4.add(lostList3_copy.get(p));
                            }
                        }

                    }
                }
            };
            //////////////////////////////////////////////////////////////////

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

