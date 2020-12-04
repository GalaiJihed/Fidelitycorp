package com.example.fidelitycorporation.Frags;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.fidelitycorporation.Acts.ReceiptActivity;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.Entities.Client;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewcomerFragment extends Fragment {
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    Dialog myDialog;
    Dialog myDialog2;
    SessionManager session;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public NewcomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newcomer, container, false);
        final TextView number = view.findViewById(R.id.newcomer_number);

        Button send = view.findViewById(R.id.btnnotify);
        myDialog = new Dialog(view.getContext());
        myDialog2 = new Dialog(view.getContext());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView number = getActivity().findViewById(R.id.newcomer_number);
                if(number.length()<=0)
                {
                    Toast.makeText(getContext(),"put your number phone the field is empty",Toast.LENGTH_LONG).show();
                }else
          // ShowPopup(getView());
              CheckLoginClient();

            }
        });

        Button btn0 = view.findViewById(R.id.btn0);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "0");
                }
            }
        });

        Button btn1 = view.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "1");
                }
            }
        });
        Button btn2 = view.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "2");
                }
            }
        });
        Button btn3 = view.findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "3");
                }
            }
        });
        Button btn4 = view.findViewById(R.id.btn4);
        Button btn5 = view.findViewById(R.id.btn5);
        Button btn6 = view.findViewById(R.id.btn6);
        Button btn7 = view.findViewById(R.id.btn7);
        Button btn8 = view.findViewById(R.id.btn8);
        Button btn9 = view.findViewById(R.id.btn9);
        Button btnrem = view.findViewById(R.id.btnrem);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "4");
                }
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "5");
                }
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "6");
                }
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "7");
                }
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "8");
                }
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() < 8) {
                    number.setText(number.getText() + "9");
                }

            }
        });
        btnrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() > 0) {
                    number.setText(number.getText().subSequence(0, number.getText().length() - 1));
                }

            }
        });


        return view;

    }


    public void ShowPopup(View v) {
        myDialog.setContentView(R.layout.client_validation_popup);
        Button verify = myDialog.findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText code1=myDialog.findViewById(R.id.code);
                int getCode=Integer.valueOf(code1.getText().toString());
                checkRequstCode(getCode);
                myDialog.dismiss();


            }
        });


        ImageView close = myDialog.findViewById(R.id.closebtn);

        TextView number2 = myDialog.findViewById(R.id.number);
        TextView number = getActivity().findViewById(R.id.newcomer_number);
        String number1 = number.getText().toString();
        number2.setText(number1);

      //  CheckLoginClient(phoneNumber,idStore);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopupProceed(View v ,int phoneNumber) {
        myDialog2.setContentView(R.layout.proceed_popup);
        ImageView close = myDialog2.findViewById(R.id.btn_close_popup_proceed);

        Button Yes = myDialog2.findViewById(R.id.btn_Yes_popup_proceed);
        Button No = myDialog2.findViewById(R.id.btn_no_popup_proceed);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 newClient(phoneNumber);
                myDialog2.dismiss();
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }



    public void  CheckLoginClient() {
        try {

            TextView number = getActivity().findViewById(R.id.newcomer_number);
            int number1 =Integer.valueOf(number.getText().toString()) ;




            sharedPref =  getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            final int idStore =  sharedPref.getInt("idStore",0);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("phoneNumber", number1);
            jsonBody.put("StoreId", idStore);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CHECK_LOGIN_COSTUMER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Client client=new Client();
                    Intent n = new Intent(getContext(), ReceiptActivity.class);

                    Toast.makeText(getContext(),
                            "Already Associated with the store"+response, Toast.LENGTH_LONG)
                            .show();

                    try {
                        JSONObject clientObject = new  JSONObject(response);

                        client.setFirstName(clientObject.getString("firstName"));
                        client.setLastName(clientObject.getString("lastName"));
                        client.setPoints(clientObject.getString("fidelityPoints"));
                        client.setId(clientObject.getInt("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    n.putExtra("Client", client);
                    startActivity(n);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e("VOLLEY", error.toString());
                  if (error.networkResponse.statusCode == 403){
                      Toast.makeText(getContext(),
                              "Enter Verify code.", Toast.LENGTH_LONG)
                              .show();
                      ShowPopup(getView());

                      // AFFICHAGE NOUVELLE INTERFACE

                    } if(error.networkResponse.statusCode == 409 || error.networkResponse.statusCode == 400){
                    Toast.makeText(getContext(),
                            "A problem has been occured", Toast.LENGTH_LONG)
                            .show();

                }
                    if(error.networkResponse.statusCode == 406 ){
                        Toast.makeText(getContext(),
                                "Phone number doesnt Exist ! ", Toast.LENGTH_LONG)
                                .show();
                        /// NEW CLIENT
                        ShowPopupProceed(getView(),number1 );

                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");

                    sharedPref =  getActivity().getSharedPreferences(
                            PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
                    int idStore =  sharedPref.getInt("idStore",0);
                    session = new SessionManager(getContext());
                    params.put("auth", session.getUser());
                    //params.put("StoreId",String.valueOf(idStore));
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
    public void checkRequstCode(int Code){

        try {

            TextView number = getActivity().findViewById(R.id.newcomer_number);
            int number1 =Integer.valueOf(number.getText().toString()) ;




            sharedPref =  getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            int idStore =  sharedPref.getInt("idStore",0);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("phoneNumber", number1);
            jsonBody.put("StoreId", idStore);
            jsonBody.put("requestCode", Code);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CHECK_REQUEST_CODE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());

                    Client client=new Client();
                    Intent n = new Intent(getContext(), ReceiptActivity.class);

                    Toast.makeText(getContext(),
                            "Pairring succes with the store"+response, Toast.LENGTH_LONG)
                            .show();

                    try {
                        JSONObject clientObject = new  JSONObject(response);

                        client.setFirstName(clientObject.getString("firstName"));
                        client.setLastName(clientObject.getString("lastName"));
                        client.setPoints(clientObject.getString("fidelityPoints"));
                        client.setId(clientObject.getInt("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    n.putExtra("Client", client);
                    startActivity(n);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                 if(error.networkResponse.statusCode == 403){
                        Toast.makeText(getContext(),
                                "Please verify the code .", Toast.LENGTH_LONG)
                                .show();
                    }else if(error.networkResponse.statusCode == 402){
                        Toast.makeText(getContext(),
                                "Phone Number does not exist.", Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Toast.makeText(getContext(),
                                "A problem has been occured try later", Toast.LENGTH_LONG)
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

    public void newClient(int phoneNumber){
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            final JSONObject jsonBody = new JSONObject();
            sharedPref =  getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            final int idStore =  sharedPref.getInt("idStore",0);
            jsonBody.put("phoneNumber", phoneNumber);
            jsonBody.put("StoreId", idStore);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADD_NEW_Client, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());
                    // Check the JWT Token
                    Toast.makeText(getContext(),
                            requestBody + " response :" + response, Toast.LENGTH_LONG)
                            .show();
                    Client client= new Client();

                    try {
                        JSONObject clientObject = new  JSONObject(response);

                        client.setPhonenumber(Integer.toString(phoneNumber));
                        client.setPoints(clientObject.getString("fidelityPoints"));
                        client.setId(clientObject.getInt("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Intent n = new Intent(getContext(), ReceiptActivity.class);
                    n.putExtra("Client", client);
                    startActivity(n);



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getContext(),
                                "Errors", Toast.LENGTH_LONG)
                                .show();


                    } else if(error.networkResponse.statusCode == 409){
                        Toast.makeText(getContext(),
                                "Phone Number already in use .", Toast.LENGTH_LONG)
                                .show();
                    }else {
                        Toast.makeText(getContext(),
                                "New client saved.", Toast.LENGTH_LONG)
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
}