package com.example.customerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class _c_Login extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public _c_Login() {
        // Required empty public constructor
    }


    public static _c_Login newInstance(String param1, String param2) {
        _c_Login fragment = new _c_Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View customerView = inflater.inflate(R.layout.fragment__c_login, container, false);

        final EditText phoneText = (EditText)customerView.findViewById(R.id.et_phoneNum);
        final EditText passwordText = (EditText)customerView.findViewById(R.id.et_pass);
        final ImageButton loginbtn = (ImageButton)customerView.findViewById(R.id.btn_login); //////////////////////////////

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone_num = phoneText.getText().toString();
                final String password = passwordText.getText().toString();

                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            //서버에서 보내준 값이 true이면?
                            if (success) {
                                String phone_num = jsonResponse.getString("phone_num");
                                String customer_num = jsonResponse.getString("customer_num");
                                //로그인에 성공했으므로 MainActivity로 넘어감
                                Intent intent = new Intent(getActivity(), _c_MenuActivity.class);
                                intent.putExtra("phoneNum", phone_num);
                                intent.putExtra("customerNum", customer_num);
                                startActivity(intent);

                            } else {//로그인 실패시
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                                builder.setMessage("Login failed")
                                        .setNegativeButton("retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                _c_LoginRequest loginRequest = new _c_LoginRequest(phone_num, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(loginRequest);
            }
        });


        return customerView;
    }
}