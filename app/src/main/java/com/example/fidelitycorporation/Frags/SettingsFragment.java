package com.example.fidelitycorporation.Frags;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {
    private Dialog myDialog;
    private View root;
    private SessionManager session;
    private SharedPreferences sharedPref;
    private TextView firstname_txt, lastname_txt, phonenumber_txt, email_txt, address_txt, nameLastName_txt,settings_storerating;
    private TextView editProfile;
    private Button update_profile;
    private EditText lastname_edit_profile, firstname_edit_profile, email_edit_profile, address_edit_profile, phonenumber_edit_profile;
    private final String PREFERENCE_FILE_KEY_STORE_Manager_Id = "id_manager";
    private SharedPreferences sharedPrefmanagerid;
    private SharedPreferences.Editor editor;

    private final String PREFERENCE_FILE_KEY_STORE = "phonenumber";


    private String EVENT_DATE_TIME = "2019-12-31 10:30:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        session = new SessionManager(this.getContext());
        myDialog = new Dialog(root.getContext());
        editProfile = root.findViewById(R.id.id_edit_profile);
        firstname_txt = root.findViewById(R.id.firstnamePorfile);
        lastname_txt = root.findViewById(R.id.lastnamePorfile);
        address_txt = root.findViewById(R.id.id_address_profile_manager);
        email_txt = root.findViewById(R.id.id_email_profile_manager);
        phonenumber_txt = root.findViewById(R.id.id_phone_number_profile_manager);
        nameLastName_txt = root.findViewById(R.id.settings_namelastname);
        settings_storerating=root.findViewById(R.id.settings_storerating);


        editProfile.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                ShowPopupEdit(getView());
            }
        });

        getProfile();
        //Setting Tabs
        TabHost tabs = (TabHost) root.findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("General");
        spec.setContent(R.id.tab1);
        spec.setIndicator("General");
        tabs.addTab(spec);
        TabHost.TabSpec spec1 = tabs.newTabSpec("Advanced");
        spec1.setContent(R.id.tab2);
        spec1.setIndicator("Advanced");
        tabs.addTab(spec1);
        tabs.setCurrentTab(0);


        return root;
    }


    public void ShowPopupEdit(View v) {
        myDialog.setContentView(R.layout.edit_profile);
        firstname_edit_profile = myDialog.findViewById(R.id.firstname_editprofile);
        lastname_edit_profile = myDialog.findViewById(R.id.lastname_editprofile);
        email_edit_profile = myDialog.findViewById(R.id.email_editprofile);
        address_edit_profile = myDialog.findViewById(R.id.id_address_profile);
        //phonenumber_edit_profile=myDialog.findViewById(R.id.id_edit_phone_number);

        update_profile = myDialog.findViewById(R.id.id_Update_profile);


        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref = getActivity().getSharedPreferences(
                        PREFERENCE_FILE_KEY_STORE_Manager_Id, Context.MODE_PRIVATE);
                int idmanager = sharedPrefmanagerid.getInt("idmanager", 0);
                //   Toast.makeText(getContext(),idmanager+"",Toast.LENGTH_LONG).show();
                final String Sfirstname = firstname_edit_profile.getText().toString().trim();
                final String Slastname = lastname_edit_profile.getText().toString().trim();
                final String Semail = email_edit_profile.getText().toString().trim();
                final String Saddress = address_edit_profile.getText().toString().trim();

                //   String Sphonenumber=phonenumber_edit_profile.getText().toString().trim();
                // final int IphoneNumber=Integer.valueOf(Sphonenumber);

                EditProfile(idmanager, Sfirstname, Slastname, Semail, Saddress);
                Toast.makeText(getContext(), "Sucess ", Toast.LENGTH_LONG).show();

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    public void EditProfile(final int id, final String firstname, final String lastName, final String email, final String address) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("firstName", firstname);
            jsonBody.put("lastName", lastName);
            jsonBody.put("email", email);
            jsonBody.put("address", address);
            //  jsonBody.put("phoneNumber",phoneNumber);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_EDIT_PROFILE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(getContext(),
                            response, Toast.LENGTH_LONG)
                            .show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 404) {
                        Toast.makeText(getContext(),
                                "   Manager not found", Toast.LENGTH_LONG)
                                .show();
                    } else if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getContext(),
                                "   Erreur", Toast.LENGTH_LONG)
                                .show();
                    } else if (error.networkResponse.statusCode == 409) {
                        Toast.makeText(getContext(),
                                "   Phone number already exist", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    session = new SessionManager(getContext());
                    params.put("auth", session.getUser());
                    return params;
                }

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

    private void getProfile() {
        try {


            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            JSONObject jsonBody = new JSONObject();
            sharedPref = getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            long phoneNumber = sharedPref.getLong("phoneNumber", 0);

            jsonBody.put("phoneNumber", phoneNumber);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_MANAGER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());
                    // Check the JWT Token
                    Toast.makeText(getContext(),
                            "Manager Profile ", Toast.LENGTH_LONG)
                            .show();


                    try {


                        JSONObject jObj = new JSONObject(response);
                        // Store  user in RoomDatabase

                        int idManager = jObj.getInt("id");
                        sharedPrefmanagerid = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY_STORE_Manager_Id, Context.MODE_PRIVATE);

                        editor = sharedPrefmanagerid.edit();
                        editor.putInt("idmanager", idManager);
                        editor.apply();
                        editor.commit();


                        firstname_txt.setText("First name :"+ jObj.getString("firstName"));
                        lastname_txt.setText("Last name :"+ jObj.getString("lastName"));
                        email_txt.setText("Email :"+jObj.getString("email"));
                        address_txt.setText("Address :"+jObj.getString("address"));
                        phonenumber_txt.setText("Phone Number :"+jObj.getString("phoneNumber"));
                        nameLastName_txt.setText(jObj.getString("firstName" )+ " " + jObj.getString("lastName"));
                        double storeRating = jObj.getJSONObject("store").getDouble("StoreNotes") ;
                        settings_storerating.setText(Double.toString(storeRating));


                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 404) {
                        Toast.makeText(getContext(),
                                "manager not found", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    session = new SessionManager(getContext());
                    params.put("auth", session.getUser());
                    return params;
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


