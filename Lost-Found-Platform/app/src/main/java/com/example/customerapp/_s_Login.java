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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link _s_Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class _s_Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public _s_Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment _s_login.
     */
    // TODO: Rename and change types and number of parameters
    public static _s_Login newInstance(String param1, String param2) {
        _s_Login fragment = new _s_Login();
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

        // Inflate the layout for this fragment
        View staffView = inflater.inflate(R.layout.fragment__s_login, container, false);

        final EditText idText = (EditText)staffView.findViewById(R.id.et_id);
        final EditText passwordText = (EditText)staffView.findViewById(R.id.et_pass);
        final ImageButton loginbtn = (ImageButton)staffView.findViewById(R.id.btn_login); //////////////////////////////

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String staff_num = idText.getText().toString();
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
                                String staff_num = jsonResponse.getString("staff_num");
                                String cinema_num = jsonResponse.getString("cinema_num");
                                //로그인에 성공했으므로 MainActivity로 넘어감
                                Intent intent = new Intent(getActivity(), _s_MenuActivity.class);
                                intent.putExtra("staff_num", staff_num);
                                intent.putExtra("cinema_num", cinema_num);
                                startActivity(intent);

                            } else {//로그인 실패시
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style._s_MyDialogTheme);
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

                _s_LoginRequest loginRequest = new _s_LoginRequest(staff_num, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(loginRequest);
            }
        });


        return staffView;
    }
}