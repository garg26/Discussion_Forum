package com.example.kartikeya_pc.forum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

public class Questions extends Activity {

    private QuestionStore db;
    private EditText editText;
    public final static String TAG = Questions.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        SessionManager session = new SessionManager(this);
        if(!session.isLoggedIn())
            return;

        db = new QuestionStore(this);

        editText = (EditText) findViewById(R.id.question);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(view.getId() == R.id.question){
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText == null) {
                    Toast.makeText(Questions.this, "Question is Empty", Toast.LENGTH_LONG).show();
                    return;
                }

               String string = editText.getText().toString().trim();
                try {
                    Log.v(TAG,"start");
                    addQuestion(string);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Log.v(TAG,"end");
                Toast.makeText(Questions.this, "Adding Question Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addQuestion(final String question){

        Log.v(TAG,"start in add");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST, appConfig.url_question, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.v(TAG,"response");
                Log.d(TAG, "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        JSONObject user = jObj.getJSONObject("user");


                        String question_id = ""+user.getInt("question_id");
                        int questions_id = Integer.parseInt(question_id);

                        String question = user.getString("questions");
                        db.addQuestion(user.getString("questions"));

                        Intent i = new Intent(Questions.this,Account.class);
                        i.putExtra("question",question);
                        startActivity(i);
                        finish();
                        Log.d(TAG,"Question: "+questions_id);


                    }
                    else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(Questions.this,
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
                Toast.makeText(Questions.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Log.v(TAG,"map");
                Map<String, String> params = new HashMap<String, String>();
                params.put("question", question);


                return params;
            }

        };

        int socketTimeout = 10000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        requestQueue.add(strReq);
    }


}

