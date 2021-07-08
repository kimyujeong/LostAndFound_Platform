package com.example.customerapp;


import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class _s_LoginRequest extends StringRequest {

    private static String IP_ADDRESS = "172.20.10.3";
    final private static String URL = "http://" + IP_ADDRESS + "/_s_login.php";

    private Map<String, String> parameters;

    //생성자

    public _s_LoginRequest(String staff_num, String password, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("staff_num", staff_num);
        parameters.put("password", password);
        //parameters.put("site",site);
    }



    //추후 사용을 위한 부분

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}