package com.example.customerapp;

//import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;
import com.android.volley.error.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class _c_LoginRequest extends StringRequest{

    private static String IP_ADDRESS = "172.20.10.3";
    final static private String URL = "http://" + IP_ADDRESS + "/_c_login.php";

    private Map<String, String> parameters;

    //생성자
    public _c_LoginRequest(String phone_num, String password, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("phone_num", phone_num);
        parameters.put("password", password);
    }



    //추후 사용을 위한 부분

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}