package com.example.kartikeya_pc.forum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;



public class Tab2 extends Fragment {

  /* private static String TAG = "garg";
    private EditText editText1, editText2, editText3;
    private Button button;
    private String name, email, password;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private database db;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab2,container,false);

        // Session manager
        session = new SessionManager(getActivity());

        // SQLite database handler
        db = new database(getActivity());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }


        editText1 = (EditText)view.findViewById(R.id.name);
        editText2 = (EditText)view.findViewById(R.id.email);
        editText3 = (EditText)view.findViewById(R.id.password);

        editText3.setTransformationMethod(new PasswordTransformationMethod());

        inputLayoutName = (TextInputLayout)view. findViewById(R.id.view1);
        inputLayoutEmail = (TextInputLayout)view. findViewById(R.id.view2);
        inputLayoutPassword = (TextInputLayout)view. findViewById(R.id.view3);

        button = (Button)view.findViewById(R.id.signup);



            button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                name = editText1.getText().toString().trim();
                email = editText2.getText().toString().trim();
                password = editText3.getText().toString().trim();

                if (!validateName() || !validateEmail() || !validatePassword() ) {
                    return;
                }

               try{
                                Log.d("Tab","Kartikeya");
                                   registerUser(name,email,password);

               }catch (NullPointerException e){
                   e.printStackTrace();
               }

                Toast.makeText(getActivity(), "Account Created Successfully!", Toast.LENGTH_SHORT).show();

            }



        });

        editText1.addTextChangedListener(new MyTextWatcher(editText1));
        editText2.addTextChangedListener(new MyTextWatcher(editText2));
        editText3.addTextChangedListener(new MyTextWatcher(editText3));


        return view;
    }

    public boolean validateName(){
        if(editText1.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.error_name1));
            requestFocus(editText1);
            return false;
        }
        else if(editText1.getText().toString().trim().length()<3){
            inputLayoutName.setError(getString(R.string.error_name2));
            requestFocus(editText1);
            return false;
        }
        else
            inputLayoutName.setErrorEnabled(false);
        return true;
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
    private void requestFocus(View view) {
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
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.name:
                    validateName();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;

            }
        }
    }


    public void registerUser(final String name, final String email, final String password){

        Log.v("Tab2","garg");
       // pDialog.setMessage("Registering ...");
       // showDialog();


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, appConfig.url_sign, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                Log.v("Tab2","start");
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.v("Tab2","start2");

                    if (!error) {
                        Log.d(TAG,email + " " + password + " " + name );
                        Log.v("Tab2","start3");
                        JSONObject user = jObj.getJSONObject("user");

                        String id = ""+user.getInt("id");
                        int user_id = Integer.parseInt(id);

                        Log.d(TAG,"TAB: "+user_id);
                        Log.v("Tab2","start4");

Preference.hffh=5;

                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);





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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
              // hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Log.v("Tab2","end");
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        requestQueue.add(strReq);
    }

     /*   private void showDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

}
