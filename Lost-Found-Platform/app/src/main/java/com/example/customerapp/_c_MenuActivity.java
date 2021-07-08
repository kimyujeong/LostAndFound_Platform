package com.example.customerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class _c_MenuActivity extends AppCompatActivity implements IP {

    String myJSON;
    public static final int sub = 1001; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/

    public String getIP() {
        return IP_address;
    }

    private String IP_ADDRESS = getIP();

    private String phoneNum;
    private String customerNum;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_OBJECT_DETAIL = "object_detail";
    private static final String TAG_LOST_NUM = "lost_num";
    private static final String TAG_RECEIVING_DATE = "receiving_date";
    private static final String TAG_RECEIVING_TIME = "receiving_time";
    private static final String TAG_REGISTERED_DATE = "registered_date";
    private static final String TAG_LOST_OBJECT = "lost_object";
    private static final String TAG_FLAG = "flag";
    private static final String TAG_REJECT_DETAIL = "reject_detail";

    JSONArray approves = null;

    ArrayList<HashMap<String, String>> approveList;

    ListView list;

    int count = 0;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> array = new ArrayList<String>();

    ListAdapter adapter;

    private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._c_activity_menu);

        phoneNum = getIntent().getStringExtra("phoneNum");
        customerNum = getIntent().getStringExtra("customerNum");

        approveList = new ArrayList<HashMap<String, String>>();
        list = (ListView) findViewById(R.id.승인리스트);

        System.out.println("넘어온 phoneNum, customerNum : " + phoneNum + ", " + customerNum);

        ImageButton btnSearch = (ImageButton) findViewById(R.id.분실물조회);
        ImageButton btnAsk = (ImageButton) findViewById(R.id.분실물문의);
        ImageButton btnInquiry = (ImageButton) findViewById(R.id.나의상담내역);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _c_LostListActivity.class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("customerNum", customerNum);
                startActivity(intent);
            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _c_AskActivity.class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("customerNum", customerNum);
                startActivity(intent);
            }
        });

        btnInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), _c_consult_history.class);
                intent.putExtra("phoneNum", phoneNum);
                intent.putExtra("customerNum", customerNum);
                startActivity(intent);
            }
        });

        getData("http://" + IP_ADDRESS + "/_c_menu_approve_list.php?customer_num=" + customerNum);

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            approves = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < approves.length(); i++) {
                JSONObject c = approves.getJSONObject(i);
                String object_detail = c.getString(TAG_OBJECT_DETAIL);
                String lost_num = c.getString(TAG_LOST_NUM);
                String receiving_date = c.getString(TAG_RECEIVING_DATE);
                String receiving_time = c.getString(TAG_RECEIVING_TIME);
                String registered_date = c.getString(TAG_REGISTERED_DATE);
                String lost_object = c.getString(TAG_LOST_OBJECT);
                String flag = c.getString(TAG_FLAG);
                String reject_detail = c.getString(TAG_REJECT_DETAIL);

                HashMap<String, String> approves2 = new HashMap<String, String>();

                approves2.put(TAG_OBJECT_DETAIL, object_detail);
                approves2.put(TAG_LOST_NUM, lost_num);
                approves2.put(TAG_RECEIVING_DATE, receiving_date);
                approves2.put(TAG_RECEIVING_TIME, receiving_time);
                approves2.put(TAG_REGISTERED_DATE, registered_date);
                approves2.put(TAG_LOST_OBJECT, lost_object);
                approves2.put(TAG_FLAG, flag);
                approves2.put(TAG_REJECT_DETAIL, reject_detail);

                if(flag.equals("1")) {
                    approves2.put("text", lost_object + "에 대한 문의가 승인되었습니다.\n눌러서 수령시간을 입력해주시기 바랍니다.");
                }else if(flag.equals("2")){
                    approves2.put("text", lost_object + "에 대한 문의가 취소되었습니다.\n눌러서 취소사유를 확인해주시기 바랍니다.");
                }
                else if (flag.equals("3")) {
                    approves2.put("text", registered_date + "에 요청하신 " + lost_object + "에 대한 분실물 수령 요청이 승인되었습니다. 눌러서 수령 세부 사항을 확인해주시기 바랍니다.");
                } else if (flag.equals("4")) {
                    approves2.put("text", registered_date + "에 요청하신 " + lost_object + "에 대한 분실물 수령 요청이 취소되었습니다. 눌러서 승인 취소 사유를 확인해주시기 바랍니다.");
                }

                approveList.add(approves2);
            }

            adapter = new SimpleAdapter(
                    _c_MenuActivity.this, approveList, R.layout._c_menu_approve_list_item,
                    new String[]{"text"},
                    new int[]{R.id.text}
            );

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if(approveList.get(position).get(TAG_FLAG).equals("1")){
                        Intent intent=new Intent(getApplicationContext(), _c_Receiving_Detail.class);

                        intent.putExtra("ask_num", approveList.get(position).get("lost_num"));
                        intent.putExtra("ask_object", approveList.get(position).get("lost_object"));
                        intent.putExtra("ask_object_detail", approveList.get(position).get("object_detail"));
                        intent.putExtra("customer_name", approveList.get(position).get("receiving_date"));
                        intent.putExtra("phoneNum", phoneNum);
                        intent.putExtra("customerNum", customerNum);

                        startActivity(intent);

                    }else if(approveList.get(position).get(TAG_FLAG).equals("2")){
                        final LinearLayout linear = (LinearLayout) View.inflate(_c_MenuActivity.this, R.layout._c_dialog_reject, null);
                        TextView ask_object = (TextView)linear.findViewById(R.id.ask_object);
                        TextView ask_object_detail = (TextView)linear.findViewById(R.id.ask_object_detail);
                        TextView lost_location = (TextView)linear.findViewById(R.id.lost_location);
                        TextView reject_detail = (TextView)linear.findViewById(R.id.reject_detail);

                        ask_object.setText(approveList.get(position).get(TAG_LOST_OBJECT));
                        ask_object_detail.setText(approveList.get(position).get(TAG_OBJECT_DETAIL));
                        lost_location.setText(approveList.get(position).get(TAG_RECEIVING_TIME));
                        reject_detail.setText(approveList.get(position).get(TAG_REJECT_DETAIL));

                        new AlertDialog.Builder(_c_MenuActivity.this, R.style.MyDialogTheme)
                                .setTitle("문의 내역")
                                .setView(linear)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String ask_num = approveList.get(position).get(TAG_LOST_NUM);
                                        String ask_state = "확인완료";
                                        String customer_num="";
                                        InsertData task = new InsertData();
                                        task.execute("http://" + IP_ADDRESS + "/_c_reject_confirm.php", customer_num, ask_num, ask_state);
                                        dialog.dismiss();

                                        Intent intent = new Intent(getApplicationContext(), _c_MenuActivity.class);
                                        intent.putExtra("phoneNum", phoneNum);
                                        intent.putExtra("customerNum", customerNum);
                                        startActivityForResult(intent, sub);//액티비티 띄우기
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    else if (approveList.get(position).get("flag").equals("3")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(_c_MenuActivity.this, R.style.MyDialogTheme);
                        builder.setTitle("분실물 수령 정보");
                        builder.setMessage("\n분실물 문의 등록 날짜: " + approveList.get(position).get("registered_date") + "\n\n"
                                + "분실물 종류: " + approveList.get(position).get("lost_object") + "\n\n"
                                + "분실물 세부사항: " + approveList.get(position).get("object_detail") + "\n\n"
                                + "수령 날짜: " + approveList.get(position).get("receiving_date") + "\n\n"
                                + "수령 시간: " + approveList.get(position).get("receiving_time"));
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                    } else if (approveList.get(position).get("flag").equals("4")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(_c_MenuActivity.this, R.style.MyDialogTheme);
                        builder.setTitle("분실물 수령 승인 취소 사유");
                        builder.setMessage("\n" + approveList.get(position).get("reject_detail"));
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String state = "승인거부확인";

                                        InsertData task = new InsertData();
                                        task.execute("http://" + IP_ADDRESS + "/_s_update_approve.php", customerNum, approveList.get(position).get("lost_num"), state);

                                        Intent intent = new Intent(getApplicationContext(), _c_MenuActivity.class);
                                        intent.putExtra("phoneNum", phoneNum);
                                        intent.putExtra("customerNum", customerNum);
                                        startActivityForResult(intent, sub);//액티비티 띄우기
                                    }
                                });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(_c_MenuActivity.this,
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

            String customer_num = (String) params[1];
            String lost_num = (String) params[2];
            String inquirystate = (String) params[3];

            String serverURL = (String) params[0];
            String postParameters = "customer_num=" + customer_num + "&lost_num=" + lost_num + "&processing_state=" + inquirystate;


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
}
