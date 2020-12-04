package com.example.fidelitycorporation.Frags;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.fidelitycorporation.Adapter.Stat1Adapter;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.Entities.Client;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClientsFragment extends Fragment {
    public static final String TAG = "CLIENTS_FRAGMENT";
    RecyclerView listview;
    ArrayList<Client> clients = new ArrayList<Client>();
    Context context;
    Stat1Adapter stat1adapter;
    View root;
    SessionManager session;
    SharedPreferences sharedPref,sharedPref2;


    private final String PREFERENCE_FILE_KEY_STORE = "id_store";


    public ClientsFragment() {
        // Required empty public constructor

    }

    public  static void clientsinfo(Client client)
    {


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_clients, container, false);


      //  WhHBD.setVisibility(View.INVISIBLE);


        GetStoreCustomers();
/*
        if(status==false){
            WhHBD.setVisibility(View.INVISIBLE);
        }else
        {
            WhHBD.setVisibility(View.VISIBLE);
        }
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getbirthdayPoints();
                WhHBD.setVisibility(View.INVISIBLE);
            }
        });
        btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhHBD.setVisibility(View.INVISIBLE);
                WhHBD.getDisplay();
            }
        });
*/

        return root;

    }


    public void GetStoreCustomers() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            JSONObject jsonBody = new JSONObject();
            sharedPref = getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            int idStore = sharedPref.getInt("idStore", 0);

            jsonBody.put("StoreId", idStore);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_ALL_STORECOSTUMERS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("Customers", response.toString());
            //Check Clients By this Store
                    Toast.makeText(getContext(),
                            "Clients ", Toast.LENGTH_LONG)
                            .show();


                    try {

                        JSONArray tags = new JSONArray(response);
                        for (int i = 0; i < tags.length(); i++) {
                            JSONObject repObject = tags.getJSONObject(i);

                            JSONObject clientObject = repObject.getJSONObject("client");
                            String FirstName = clientObject.getString("firstName");
                            String LastName = clientObject.getString("lastName");
                            int id = clientObject.getInt("id");
                            boolean Statusbirth=repObject.getBoolean("birthdayStatus");
                            String pointsInCurrentStore=repObject.getString("pointsInCurrentStore");
                            Toast.makeText(getContext(), pointsInCurrentStore, Toast.LENGTH_LONG).show();
                            String birthday = clientObject.getString("birthDate");

                            String phoneNumber = clientObject.getString("phoneNumber");
                            String country = clientObject.getString("country");
                            String city = clientObject.getString("city");
                            String address = clientObject.getString("address");
                            String image = clientObject.getString("Image");
                            int  points = clientObject.getInt("fidelityPoints");
                            Client c=new Client();
                            c.setFirstName(FirstName);
                            c.setId(id);
                            c.setStatusBirthdate(Statusbirth);
                            c.setLastName(LastName);
                            c.setAddress(address);
                            c.setCity(city);
                            c.setCountry(country);
                            c.setBirthDate(birthday);
                            c.setImage(image);
                            c.setPoints(Integer.toString(points));
                           // c.setPoints(pointsInCurrentStore);
                            c.setPhonenumber(phoneNumber);

                            clients.add(c);
                        }

                        listview = root.findViewById(R.id.frag_clients_recycleview);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                        listview.setLayoutManager(layoutManager);
                        listview.setHasFixedSize(true);
                        stat1adapter = new Stat1Adapter(context, R.layout.list_row_topcustomerstat, clients);
                        listview.setAdapter(stat1adapter);
                        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
                        listview.setItemViewCacheSize(20);
                        listview.setDrawingCacheEnabled(true);
                        listview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                        listview.setItemAnimator(itemAnimator);
                        // 9. Set the ItemAnimator
                        stat1adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                      if(error.networkResponse.statusCode == 401){
                        Toast.makeText(getContext(),
                                "StoreID doesnt exist .", Toast.LENGTH_LONG)
                                .show();
                    }else if(error.networkResponse.statusCode == 404){
                        Toast.makeText(getContext(),
                                "clients not found", Toast.LENGTH_LONG)
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


    public void getbirthdayPoints(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("StoreId",1);
            jsonBody.put("ClientID", 1);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_BIRTHDATE_POINTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(getContext(),
                            "Success", Toast.LENGTH_LONG)
                            .show();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 403) {
                        Toast.makeText(getContext(),
                                "Birthday invalid ", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(getContext(),
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
                            responseString = new String(response.data,"UTF-8");
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
