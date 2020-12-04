package com.example.fidelitycorporation.Acts;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.App.AppController;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.ConnectionChecker;
import com.example.fidelitycorporation.helper.SessionManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private Switch Switchxml;
    private SessionManager session;
    private TextView ManagerName,Storename;
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor2;
    private NetworkImageView storeimg;
    private Button btnretry;
    private Dialog myDialog;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);




        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        ManagerName=findViewById(R.id.id_manager_name_home);
        Storename=findViewById(R.id.id_name_store);
        storeimg=findViewById(R.id.storeimage);
        session=new SessionManager(getApplicationContext());
          Switchxml = (Switch)findViewById(R.id.switch1);

        myDialog = new Dialog(HomeActivity.this);




        if (!ConnectionChecker.internetIsConnected())
        {
            Toast.makeText(getApplicationContext(),"Please verify your connection",Toast.LENGTH_LONG).show();
            Log.e("CNXCHECK", "NO CNX");
            Poppupdialog();
        }
        else
        {
            Log.e("CNXCHECK", " CNX");
        }
        Switchxml.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                session.logoutUser();

                }else{
                    Toast.makeText(getApplicationContext(),"Switch is On",Toast.LENGTH_LONG).show();
                }
            }
        });
        getCurrentUser();
        NavigationView navView = findViewById(R.id.nav_view);
        //NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavController navController = Navigation.findNavController(findViewById(R.id.fragment));
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                navController.getGraph())
                .build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }





    public void Poppupdialog(){


        myDialog.setContentView(R.layout.check_connexion);
        btnretry=myDialog.findViewById(R.id.id_alert_retry_connexion);


        btnretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!ConnectionChecker.internetIsConnected())
                {
                    Toast.makeText(getApplicationContext(),"Please verify your connection",Toast.LENGTH_LONG).show();
                    Log.e("CNXCHECK", "NO CNX");
                    Poppupdialog();
                }
                else
                {
                    Log.e("CNXCHECK", " CNX");

                    myDialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }








   public void   getCurrentUser(){

               RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
               StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_CURRENT_MANAGER, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {

                       try
                       {
                           JSONObject rep=new JSONObject(response);
                           SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
                           SharedPreferences sharedPref2 = getApplicationContext().getSharedPreferences("ManagerInformations", Context.MODE_PRIVATE);
                           //Log.e("VOLLEY","ID  Store :" +  rep.getJSONObject("store").getInt("id"));
                            String StoreName= rep.getJSONObject("store").getString("StoreName");
                            Storename.setText(StoreName);
                            ManagerName.setText(rep.getString("firstName"));
                            storeimg.setImageUrl(AppConfig.URL_GET_IMAGE+rep.getJSONObject("store").getString("Image"), imageLoader);
                           Log.e("VOLLEY","SubscriptionType" +  rep.getString("SubscriptionType")+"Number OF SMS "+ rep.getInt("NumberOfSMS"));
                            int idStore= rep.getJSONObject("store").getInt("id");
                              editor = sharedPref.edit();
                              editor2 = sharedPref2.edit();
                              editor2.putString("SubscriptionType",rep.getString("SubscriptionType"));
                              editor2.putInt("NumberOfNotification",rep.getInt("NumberOfNotification"));
                              editor2.putInt("NumberOfSMS",rep.getInt("NumberOfSMS"));
                              editor2.putString("SubscriptionExpire",rep.getString("SubscriptionEndDate"));
                              editor2.commit();

                            editor.putInt("idStore",idStore);
                            editor.apply();
                            editor.commit();
                       }
                       catch (JSONException e) {
                           // JSON error
                           e.printStackTrace();
                       }

                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.e("VOLLEY", error.toString());

                       if (error.networkResponse.statusCode == 404) {
                           Toast.makeText(getApplicationContext(),
                                   "Manager not found,", Toast.LENGTH_LONG)
                                   .show();

                       } else if (error.networkResponse.statusCode == 401) {
                           Toast.makeText(getApplicationContext(),
                                   "Probleme with token.", Toast.LENGTH_LONG)
                                   .show();

                       }
                   }
               }) {
                   //                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
                   //This is for Headers If You Needed
                   @Override
                   public Map<String, String> getHeaders() throws AuthFailureError {
                       Map<String, String> params = new HashMap<String, String>();
                       params.put("Content-Type", "application/json; charset=UTF-8");
                       session = new SessionManager(getApplicationContext());
                       params.put("auth", session.getUser());
                       return params;
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


       }

    }

