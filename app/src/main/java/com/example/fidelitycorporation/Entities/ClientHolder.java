package com.example.fidelitycorporation.Entities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.App.AppController;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final NetworkImageView picture;

    private final TextView name;
    private final TextView points;
    private  int id ;

    private Dialog myDialog;
    private SessionManager session;
    private TextView txtcity,txtcountry,txtRue,txtphonenumber,txtBirthdate,txtlastname_firstname;
    private Client client;
    private Context context;
    private Button btnaccept,btndecline;
    private CardView WhHBD;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public ClientHolder(Context context, View itemView) {

        super(itemView);
        // 1. Set the context
        this.context = context;
        // 2. Set up the UI widgets of the holder
        this.picture = itemView.findViewById(R.id.list_stat1_img1);
        this.name = itemView.findViewById(R.id.list_stat1_name);
        this.points = itemView.findViewById(R.id.list_stat1_points);
        // 3. Set the "onClick" listener of the holder
        itemView.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    public void bindAnnonce(Client client) {

        // 4. Bind the data to the ViewHolder
        this.client = client;
        this.name.setText(client.getFirstName()+" "+client.getLastName());
        this.points.setText(client.getPoints());
        myDialog=new Dialog(itemView.getContext());
        //  this.points.setText(Float.toString(client.getPoints()));
      //  this.birthDate.setText("222");
       // this.picture.setImageUrl("http://10.0.2.2:3000/images/"+client.getImage(), imageLoader);
        this.picture.setImageUrl(AppConfig.URL_GET_IMAGE+client.getImage(), imageLoader);
    }







    public void DialogClientDetails(){
        myDialog.setContentView(R.layout.dialog_fragment_client);

        txtcity=myDialog.findViewById(R.id.id_cityclient);
        txtcountry=myDialog.findViewById(R.id.id_countryclient);
        txtRue=myDialog.findViewById(R.id.id_address_client);
        txtphonenumber=myDialog.findViewById(R.id.id_phonenumber_client);
        txtBirthdate=myDialog.findViewById(R.id.id_birthday_client);
        WhHBD=myDialog.findViewById(R.id.id_card_view_WHBD);
        txtlastname_firstname=myDialog.findViewById(R.id.id_lastnamefirstname);
        btnaccept=myDialog.findViewById(R.id.id_accept_points);
        btndecline=myDialog.findViewById(R.id.id_decline_points);
        String datbirth=client.getBirthDate();
        String c = datbirth.substring(0,10);
        txtcity.setText(client.getCity());
        txtcountry.setText(client.getCountry());
        txtphonenumber.setText(client.getPhonenumber());
        txtRue.setText(client.getAddress());
        txtBirthdate.setText(c);
        txtlastname_firstname.setText(client.getLastName()+" "+client.getFirstName());

        //Status Of birthday
        if(client.isStatusBirthdate()==false){
            WhHBD.setVisibility(View.INVISIBLE);
        }else
        {
            WhHBD.setVisibility(View.VISIBLE);
        }
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref = itemView.getContext().getSharedPreferences(
                        PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
                int idStore =  sharedPref.getInt("idStore",0);
                Toast.makeText(itemView.getContext() ,idStore+"ClientId:"+client.getId(),Toast.LENGTH_LONG).show();
               getbirthdayPoints(idStore,client.getId());
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



        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    @Override
    public void onClick(View v) {

        Toast.makeText(itemView.getContext(),client.isStatusBirthdate()+"",Toast.LENGTH_LONG).show();
        DialogClientDetails();


    }

    public void getbirthdayPoints(int StoreId,int ClientId){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("StoreId",StoreId);
            jsonBody.put("ClientID", ClientId);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_BIRTHDATE_POINTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(itemView.getContext(),
                            "Success", Toast.LENGTH_LONG)
                            .show();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 403) {
                        Toast.makeText(itemView.getContext(),
                                "Birthday invalid ", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(itemView.getContext(),
                                "A problem has been occured , please retry later.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    session = new SessionManager(itemView.getContext());
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



