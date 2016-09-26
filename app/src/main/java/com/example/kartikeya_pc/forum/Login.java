package com.example.kartikeya_pc.forum;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
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
import com.dd.processbutton.iml.ActionProcessButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements ProgressGenerator.OnCompleteListener{
    private EditText editText, editText2, editText3,editText4,editText5;
    private String name, email, password;
    private SessionManager session;
    private database db;
    private static String TAG = "garg";
    private ProgressGenerator progressGenerator;
    private ActionProcessButton btnSignUp, btnSignIn;
    private MaterialDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);
        db = new database(this);

        Button signin = (Button) findViewById(R.id.button);
        Button signup = (Button) findViewById(R.id.button1);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new MaterialDialog.Builder(Login.this)
                        .title(R.string.signin)
                        .customView(R.layout.signin, true)
                        .build();
                editText4 = (EditText) dialog.getCustomView().findViewById(R.id.email1);
                editText5 = (EditText) dialog.getCustomView().findViewById(R.id.pass1);

                progressGenerator = new ProgressGenerator(Login.this);
                btnSignIn = (ActionProcessButton) dialog.getCustomView().findViewById(R.id.btnSignIn);
                btnSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        email = editText4.getText().toString().trim();
                        password = editText5.getText().toString().trim();
                        if (!validateEmail1() || !validatePassword1() ) {
                            btnSignIn.setProgress(-1);
                            progressGenerator.start(btnSignIn);
                            return;
                        }
                        try {
                            new Register(email, password);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Login.this,"User Successfully login",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Login.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });


                CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword);
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editText5.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                        editText5.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
                    }
                });
                int widgetColor = ThemeSingleton.get().widgetColor;
                MDTintHelper.setTint(checkbox,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                MDTintHelper.setTint(editText4,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                MDTintHelper.setTint(editText5,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                dialog.show();

            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog = new MaterialDialog.Builder(Login.this)
                        .title(R.string.signup)
                        .customView(R.layout.signup, true)
                         .build();


                editText = (EditText) dialog.getCustomView().findViewById(R.id.name);
                editText2 = (EditText) dialog.getCustomView().findViewById(R.id.email);
                editText3 = (EditText) dialog.getCustomView().findViewById(R.id.pass);


                progressGenerator = new ProgressGenerator(Login.this);
                btnSignUp = (ActionProcessButton) dialog.getCustomView().findViewById(R.id.btnSignUp);
                Log.e(TAG, "start");
                        btnSignUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                name = editText.getText().toString().trim();
                                email = editText2.getText().toString().trim();
                                password = editText3.getText().toString().trim();


                                try{
                                    if (!validateName() || !validateEmail() || !validatePassword() ) {
                                        btnSignIn.setProgress(-1);
                                        progressGenerator.start(btnSignUp);
                                        return;
                                    }
                                    new Register(name,email,password);

                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                                Toast.makeText(Login.this,"User Successfully added",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });


                CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.showPassword);
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editText3.setInputType(!isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                        editText3.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : null);
                    }
                });
                int widgetColor = ThemeSingleton.get().widgetColor;
                MDTintHelper.setTint(checkbox,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                MDTintHelper.setTint(editText,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                MDTintHelper.setTint(editText2,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                MDTintHelper.setTint(editText3,
                        widgetColor == 0 ? ContextCompat.getColor(Login.this, R.color.colorAccent) : widgetColor);

                dialog.show();

            }
        });
    }

    public boolean validateName(){
        if(editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,"Name is Empty",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public boolean validateEmail(){
        String email1 = editText2.getText().toString().trim();
            if (email1.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                Toast.makeText(this, "Email Id is not correct", Toast.LENGTH_LONG).show();
                return false;
            }


        return true;
    }
    public boolean validateEmail1(){
        String email1 = editText4.getText().toString().trim();
            if(email1.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                Toast.makeText(this,"Email Id is not correct",Toast.LENGTH_LONG).show();
                return false;
            }
        return true;
    }
    public boolean validatePassword(){
        if (editText3.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Password is Empty", Toast.LENGTH_LONG).show();
                return false;
            }

        return true;
    }
    public boolean validatePassword1(){

            if (editText5.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Password is Empty", Toast.LENGTH_LONG).show();
                return false;
            }

        return true;
    }


    public class Register{
        Register(final String name, final String email, final String password) {
            btnSignUp.setProgress(50);
            progressGenerator.start(btnSignUp);

            RequestQueue requestQueue = Volley.newRequestQueue(Login.this.getApplicationContext());
            btnSignUp.setProgress(100);
            progressGenerator.start(btnSignUp);
            StringRequest strReq = new StringRequest(Request.Method.POST, appConfig.url_sign, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    Log.v("Tab2", "start");
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        Log.v("Tab2", "start2");

                        if (!error) {
                            Log.d(TAG, email + " " + password + " " + name);
                            Log.v("Tab2", "start3");
                            JSONObject user = jObj.getJSONObject("user");

                            session.setLogin(true);


                            String name = user.getString("name");
                            String email = user.getString("email");
                            String password = user.getString("password");
                            String id = "" + user.getInt("id");
                            int user_id = Integer.parseInt(id);

                            db.addUser(name,email,password);

                            Log.d(TAG, "TAB: " + user_id);
                            Log.v("Tab2", "start4");


                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(Login.this,
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
                        Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                    }
                    if (error instanceof TimeoutError) {
                        Log.e("Volley", "TimeoutError");
                    } else if (error instanceof NoConnectionError) {
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
                    Toast.makeText(Login.this,
                            error.getMessage(), Toast.LENGTH_LONG).show();

                    btnSignUp.setProgress(-1);
                    progressGenerator.start(btnSignUp);

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Log.v("Tab2", "end");
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
        public Register(final String email,final String password){

            btnSignIn.setProgress(50);
            progressGenerator.start(btnSignIn);

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            btnSignIn.setProgress(100);
            progressGenerator.start(btnSignIn);
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



                        }
                        else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(Login.this,
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
                    Toast.makeText(Login.this,
                            error.getMessage(), Toast.LENGTH_LONG).show();

                    btnSignIn.setProgress(-1);
                    progressGenerator.start(btnSignIn);
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

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}

