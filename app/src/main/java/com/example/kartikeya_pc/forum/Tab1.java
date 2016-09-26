package com.example.kartikeya_pc.forum;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Tab1 extends Fragment {

   /* private EditText editText2,editText3;
    private Button button;
    private String name,email,password;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private ProgressDialog pDialog;
    private database db;
    private TextView textView;
    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       / View view = inflater.inflate(R.layout.tab1,container,false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new database(getActivity());

        // Session Manager
        session = new SessionManager(getActivity());


        editText2 = (EditText)view.findViewById(R.id.email);
        editText3 = (EditText)view.findViewById(R.id.password);
        inputLayoutEmail = (TextInputLayout)view. findViewById(R.id.view1);
        inputLayoutPassword = (TextInputLayout)view. findViewById(R.id.view3);

      //  Toast.makeText(getActivity(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        button = (Button)view.findViewById(R.id.signin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editText2.getText().toString();
                password = editText3.getText().toString();

                if (!validateEmail() || !validatePassword() ) {
                    return;
                }
                /*else{
                    alert.showAlertDialog(getActivity(), "Login failed..", "Username/Password is incorrect", false);
                }*/
               /* try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkLogin(email, password);
                                }
                            });


                }catch (NullPointerException e){
                    e.printStackTrace();
                }


            }
        });
        editText2.addTextChangedListener(new MyTextWatcher(editText2));
        editText3.addTextChangedListener(new MyTextWatcher(editText3));

       /* textView = (TextView)view.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getActivity(),
                        Tab2.class);
                startActivity(i);
            }
        });*/

       /* if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        }
        return view;
    }


    public boolean validateEmail(){
        String email1 = editText2.getText().toString().trim();
        if(email1.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            inputLayoutEmail.setError(getString(R.string.error_email));
            requestFocus(editText2);
            return false;
        }
        else
            inputLayoutEmail.setErrorEnabled(false);
        return true;
    }
    public boolean validatePassword(){

        if(editText3.getText().toString().trim().isEmpty()){
            inputLayoutPassword.setError(getString(R.string.error_password1));
            requestFocus(editText3);
            return false;
        }
        else if(editText3.getText().toString().trim().length()<4){
            inputLayoutPassword.setError(getString(R.string.error_password2));
            requestFocus(editText3);
            return false;
        }
        else
            inputLayoutPassword.setErrorEnabled(false);
        return true;
    }


  /*  private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        Toast.makeText(getActivity(),"No network connection", Toast.LENGTH_LONG).show();
        return false;
    }*/
 /* private void requestFocus(View view) {
      if (view.requestFocus()) {
          getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
      }
  }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;

            }
        }
    }
    public void checkLogin( final String email, final String password){
        String req = "req_request";
        pDialog.setMessage("Logging in ...");
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, appConfig.url_login, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject   jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        session.setLogin(true);

                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        db.addUser(name, email, uid, created_at);
                        Intent intent = new Intent(getActivity(),
                                MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(getActivity(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                    }
                    else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                }




            } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {

                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        requestQueue.add(strReq);
    }
    private void showDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

}




