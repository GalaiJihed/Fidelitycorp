package com.example.fidelitycorporation.Acts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {
    private final String PREFERENCE_FILE_KEY_STORE = "phonenumber";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    SessionManager session;
    private EditText EphoneNumber;
    private EditText Epassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        EphoneNumber = findViewById(R.id.id_phone_number_login);
        Epassword = findViewById(R.id.id_password_login);
        btnLogin = findViewById(R.id.login_btn);
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"Missing Test ",Toast.LENGTH_LONG).show();*
                String SPhoneNumber = EphoneNumber.getText().toString();
                String Spassword = Epassword.getText().toString().trim();
                if (SPhoneNumber.isEmpty() && Spassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), " all the fields is empty", Toast.LENGTH_LONG).show();

                } else if (SPhoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), " the field phone number is empty", Toast.LENGTH_LONG).show();

                } else if (Spassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), " the field password is empty", Toast.LENGTH_LONG).show();

                } else if (SPhoneNumber.length() > 8) {
                    Toast.makeText(getApplicationContext(), "the phone number is over 8 number ", Toast.LENGTH_LONG).show();

                } else if (SPhoneNumber.length() < 8) {
                    Toast.makeText(getApplicationContext(), "the phone number is under 8 number ", Toast.LENGTH_LONG).show();

                } else {
                    login(SPhoneNumber, Spassword);
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
                    long PhoneNumber = Long.valueOf(SPhoneNumber);
                    editor = sharedPref.edit();
                    editor.putLong("phoneNumber", PhoneNumber);
                    editor.apply();
                    editor.commit();
                }

            }
        });
    }

    public void login(String phoneNumber, String password) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("phoneNumber", phoneNumber);
            jsonBody.put("password", password);
            jsonBody.put("typeAccount", "MANAGER");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        String token = obj.getString("token");
                        Log.e("VOLLEY", token);
                        session.setLogin(true, token);
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "welcome ,you are connected", Toast.LENGTH_LONG).show();


                    // Launch Home Acitivty
                    Intent intent = new Intent(
                            LoginActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getApplicationContext(),
                                "Please verify your credentials", Toast.LENGTH_LONG)
                                .show();
                    }
                    else if ( error.networkResponse.statusCode == 403){
                        Toast.makeText(getApplicationContext(),
                                "Subscription ended please contact Fidelity administration", Toast.LENGTH_LONG)
                                .show();
                    }

                    else {
                        Toast.makeText(getApplicationContext(),
                                "A problem has been occured , please retry later.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.toString());
                        try {
                            responseString = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
