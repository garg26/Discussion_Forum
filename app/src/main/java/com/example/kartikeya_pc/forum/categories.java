package com.example.kartikeya_pc.forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class categories extends AppCompatActivity {

    private Category_Database db;
    private EditText editText,editText1;
    private String category_name ;
    private String category_description;
    private static  String TAG = categories.class.getSimpleName();
    public String[] item;
    public AutoCompleteTextView myAutoComplete;
    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        SessionManager session = new SessionManager(this);
        if(!session.isLoggedIn())
            return;

        db = new Category_Database(this);

        editText = (EditText) findViewById(R.id.name);
        try{
             myAutoComplete = (AutoCompleteTextView)
                    findViewById(R.id.myautocomplete);

        // add the listener so it will tries to suggest while the user types
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

        // set our adapter
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
        myAutoComplete.setAdapter(myAdapter);

    } catch (NullPointerException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }


        editText1 = (EditText) findViewById(R.id.description);
        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (view.getId() == R.id.description) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_name = editText.getText().toString().trim();
                category_description = editText1.getText().toString().trim();

                if (!validateCategory() || !validateDescription())
                    return;

                Log.d(TAG, "Category :::: " + category_name + " " + category_description);
                try {
                    addCategory(category_name, category_description);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(categories.this,Questions.class);
                startActivity(i);
                Toast.makeText(categories.this, "Adding Category Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validateCategory(){
        if (!editText.getText().toString().trim().isEmpty()) {
            return true;
        }
        requestFocus(editText);
        return false;
    }
    public boolean validateDescription(){
        if(!editText1.getText().toString().trim().isEmpty()){
            return true;
        }
        requestFocus(editText1);
        return false;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
          getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    public void addCategory(final String category_name,final String category_description){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST, appConfig.url_category, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        JSONObject user = jObj.getJSONObject("user");

                        String id = ""+user.getInt("cat_id");
                        int cat_id = Integer.parseInt(id);

                        String category_name = user.getString("category_name");
                        String category_description = user.getString("category_description");
                        db.addCategory(category_name,category_description);

                        Log.d(TAG,"Categories: "+category_name);

                    }
                    else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(categories.this,
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
                Toast.makeText(categories.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
//               hideDialog();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<String, String>();
            params.put("category_name", category_name);
            params.put("category_description", category_description);

            return params;
        }

    };

    int socketTimeout = 10000;//30 seconds - change to what you want
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    strReq.setRetryPolicy(policy);
    requestQueue.add(strReq);
    }

    public String[] getItemsFromDb(String searchTerm){

        // add items on the array dynamically
        List<HashMap<String, String>> list = db.getCategory();
        int rowCount = list.size();

        item = new String[rowCount];
        int x = 0;

        for (HashMap<String, String> record : list) {


            item[x] = record.put("topic",category_name);
            x++;
        }

        return item;
    }
}
