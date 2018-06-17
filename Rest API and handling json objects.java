Json Objets

[{"result":"success","fname":"Harshal","lname":"Baraskar","email":"baraskarl@gmail.com","address":[{"address1":"Green park CHS, Flat No 302, Ghansoli, Navi Mumbai."},{"address1":"Address 2, Green park CHS, Flat No 302, Ghansoli, Navi Mumbai."}]},{"result":"success","fname":"Pavan","lname":"Kaware","email":"pavan@gmail.com","address":"Shivaji Nagar, Pune"}]

--------------------------------------------------
package com.apps.rdjsmartapps.androidrestapi;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String fname;
    Button fetchInfo;
    EditText first_name;
    TextView user_name;
    TextView user_email;
    TextView user_address;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize view objects
        fetchInfo = (Button) findViewById(R.id.fetchData);
        first_name = (EditText) findViewById(R.id.fname);
        user_name = (TextView) findViewById(R.id.userName);
        user_email = (TextView) findViewById(R.id.userEmail);
        user_address = (TextView) findViewById(R.id.userAddress);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");


        fetchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make api call
                fname = first_name.getText().toString();

                if(fname != " "){
                    Log.d("MainActivity", "Inside If");
                    // Starting new thread
                    progressDialog.show();

                    // make string request
                    //makeStringRequest();
                    makeJsonArrayRequest();

                }
                else{
                    Toast.makeText(MainActivity.this, "Please enter first name!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    } // End of OnCreate

    // Make string request

    void makeStringRequest(){

        String url = "https://www.tutorerp.com/rest_api_test/user_info.php";

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Result handling
                        //System.out.println(response.substring(0,100));
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            if(result.equals("success")){
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                String email = jsonObject.getString("email");
                                String address = jsonObject.getString("address");

                                user_name.setText("Name: "+fname+" "+lname);
                                user_email.setText("Email: "+email);
                                user_address.setText("Address: "+address);

                                progressDialog.cancel();
                            }
                            else{
                                user_name.setText("Name: "+result);
                                user_email.setText("Email: ");
                                user_address.setText("Address: ");
                                progressDialog.cancel();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                user_name.setText("Name: Something went wrong!");
                user_email.setText("Email: ");
                user_address.setText("Address: ");
                error.printStackTrace();

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("fname", fname);
                return params;
            }
        };


        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }// End of MakeStringRequest


    private void makeJsonArrayRequest() {

        String url = "https://www.tutorerp.com/rest_api_test/json_array.php";

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Result handling
                        //System.out.println(response.substring(0,100));
                        try {
                            // First convert json response strring to json arrayobject and then parse it.
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i); // Gets first json object from json array.
                                String result = jsonObject.getString("fname");
                                if (result.equals(fname)) {
                                    String fname = jsonObject.getString("fname");
                                    String lname = jsonObject.getString("lname");
                                    String email = jsonObject.getString("email");
                                    String address = jsonObject.getString("address");

                                    // Parsing address object
                                    JSONArray addressArray = new JSONArray(address);
                                    JSONObject addressObject1 = addressArray.getJSONObject(0);
                                    JSONObject addressObject2 = addressArray.getJSONObject(1);

                                    user_name.setText("Name: " + fname + " " + lname);
                                    user_email.setText("Email: " + email);
                                    user_address.setText("Address: " + addressObject1.getString("address1") + addressObject2.getString("address1"));

                                    progressDialog.cancel();
                                }
                            }

                            progressDialog.cancel();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.cancel();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                user_name.setText("Name: Something went wrong!");
                user_email.setText("Email: ");
                user_address.setText("Address: ");
                error.printStackTrace();
                progressDialog.cancel();

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("fname", "Harshal");
                return params;
            }
        };


        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }



}// End of Class
