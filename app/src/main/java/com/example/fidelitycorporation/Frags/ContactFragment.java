package com.example.fidelitycorporation.Frags;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {
private Dialog myDialog;
View root;
    private ImageView btnClose;
    private Button btnYes,btnNo,Send;
    private EditText sendMessage;
    SessionManager session;
    SharedPreferences sharedPref;

    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_contact, container, false);
        myDialog = new Dialog(root.getContext());
        Send= root.findViewById(R.id.btnTransform_send_message);;
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSendMessage(getView());
            }
        });
        return root;
    }
    public void DialogSendMessage(View v){


        myDialog.setContentView(R.layout.popupconfirmsend);
        btnClose=myDialog.findViewById(R.id.btn_close_popup_send);
        btnNo=myDialog.findViewById(R.id.id_alert_no_send);
        btnYes=myDialog.findViewById(R.id.id_alert_yes_send);
        sendMessage=root.findViewById(R.id.id_send_message_text);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref =  getActivity().getSharedPreferences(
                        PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
                int idStore =  sharedPref.getInt("idStore",0);
                //Toast.makeText(getContext(),idStore+"",Toast.LENGTH_LONG).show();
                String Message=sendMessage.getText().toString();
                sendMessageToUs(Message,idStore);
                sendMessage.getText().clear();


                myDialog.cancel();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                //   myDialog.getWindow();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void sendMessageToUs(final String Message,final int StoreId ){
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("Message", Message);
                jsonBody.put("StoreId", StoreId);


                final String requestBody = jsonBody.toString();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CONTACT_SEND_MESSAGE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("VOLLEY", response.toString());
                        // Check the JWT Token
                        Toast.makeText(getContext(),
                                 "Messsage added", Toast.LENGTH_LONG)
                                .show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("VOLLEY", error.toString());
                        if (error.networkResponse.statusCode == 401) {
                            Toast.makeText(getContext(),
                                    "Store  does not exist", Toast.LENGTH_LONG)
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
            } catch (
                    JSONException e) {
                e.printStackTrace();
            }

        }

    }

