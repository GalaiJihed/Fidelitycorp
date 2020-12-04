package com.example.fidelitycorporation.Acts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.fidelitycorporation.Adapter.ProductListAdapter;
import com.example.fidelitycorporation.Adapter.ProductListReceiptAdapter;
import com.example.fidelitycorporation.Adapter.ProductListReceiptSelectedAdapter;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.Entities.Client;
import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.RecyclerItemClickListener;
import com.example.fidelitycorporation.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptActivity extends AppCompatActivity  {
    RecyclerView listview,listviewselected;
    public static List<Product> products,selected;
    SessionManager session;
    float price;
    SharedPreferences sharedPref;
    Button confirmbtn;
    SharedPreferences.Editor editor;
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    TextView total;
    Context context;
    ProductListReceiptAdapter productListAdapter ;
    ProductListReceiptSelectedAdapter productListReceiptSelectedAdapter;
    View root;
    Button refresh;
    EditText editText ;
    private Dialog myDialog,myDialog2;
    private TextView clientName,clientFP;
    private int clientId;
    private Activity currentAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_receipt);
        Intent i = getIntent();
        Client client = new Client();
        client = (Client)i.getSerializableExtra("Client");
        clientId=client.getId();
        clientName = findViewById(R.id.act_reciept_name_client);
        clientFP = findViewById(R.id.act_reciept_fidelitypoints_client);


        if (client.getFirstName()==null)
        {
            clientName.setText(client.getPhonenumber());
            clientFP.setText("ACCOUNT NOT VERRFIED");
        }
        else
        {
            clientName.setText(client.getFirstName()+" "+client.getLastName()+Integer.toString(clientId));
            clientFP.setText(client.getPoints()+" FP");
        }















        price = 0;
        confirmbtn = findViewById(R.id.confirmbtn);
        myDialog = new Dialog(ReceiptActivity.this);
        myDialog2 = new Dialog(ReceiptActivity.this);

        selected = new ArrayList<>();
        products=new ArrayList<>();
        listview = findViewById(R.id.my_product_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listview.setLayoutManager(layoutManager);
        listview.setHasFixedSize(true);


        listviewselected = findViewById(R.id.selected_products);
        RecyclerView.LayoutManager layoutManagerSelected = new LinearLayoutManager(getApplicationContext());
        listviewselected.setLayoutManager(layoutManagerSelected);
        listviewselected.setHasFixedSize(true);
       getList();
      // getSelectedList();
       editText = findViewById(R.id.search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //user define method
                filter(editable.toString());

            }
        });

        listview.addOnItemTouchListener(
                new RecyclerItemClickListener(context, listview ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        //ADD item to selected products
                        selected.add(products.get(position));
                        productListReceiptSelectedAdapter.notifyDataSetChanged();
                        //price+= products.get(position).getPrice() * products.get(position).getQuantity() ;
                        //remove
                        products.remove(position);
                        productListAdapter.notifyItemRemoved(position);
                        productListAdapter.notifyItemRangeChanged(position,products.size());


                        //total.setText(String.valueOf(price));

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        listviewselected.addOnItemTouchListener(
                new RecyclerItemClickListener(context, listviewselected ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                            //ADD item to selected products
                            products.add(selected.get(position));
                            productListAdapter.notifyDataSetChanged();
                            //price-= selected.get(position).getPrice() * products.get(position).getQuantity() ;
                            //remove
                            selected.remove(position);
                            productListReceiptSelectedAdapter.notifyItemRemoved(position);
                            productListReceiptSelectedAdapter.notifyItemRangeChanged(position,selected.size());
                        // total.setText(String.valueOf(price));

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.size() !=0)
                {

                    for ( Product p : selected) {
                        price += p.getPrice() * p.getQuantity();
                    }
                   // Log.e("Price Total", String.valueOf(price));
                    ValidationReceiptDialog(String.valueOf(price));
                }

            }
        });

    }

    private void finishAct()
    {
        this.finish();
    }
    public void ValidationReceiptDialog(String totalprice)
    {
        myDialog.setContentView(R.layout.client_receipt_popup);
        TextView total = myDialog.findViewById(R.id.totalprice);
        EditText fidelityPointsUsed = myDialog.findViewById(R.id.client_reciept_fidelitypoints);
        Button confirm = myDialog.findViewById(R.id.client_reciept_confirm);
        total.setText(totalprice+" DT");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                price = 0;
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOrder("25800441",selected,Integer.parseInt(fidelityPointsUsed.getText().toString()),price,clientId);
                myDialog.dismiss();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void ReceiptDetailsDialog(double totalPrice, boolean FPUsed, double newTotalPrice, int fidelityPointsEarned, int fidelityPointsUsed)
    {

        Log.e("POPUP RECIEPALY DIALOG", "Entred");
        TextView price_label,price;
        TextView fp_label,fp;
        Button confirm;
        myDialog2.setContentView(R.layout.reciept_details);




         price_label = myDialog2.findViewById(R.id.reciept_details_label_price);
         fp_label = myDialog2.findViewById(R.id.reciept_details_label_fp);
         price = myDialog2.findViewById(R.id.reciept_details_price);
         fp = myDialog2.findViewById(R.id.reciept_details_fp);
         confirm = myDialog2.findViewById(R.id.details_reciept_confirm);
        Toast.makeText(getApplicationContext(),
                "FPUSED "+FPUsed, Toast.LENGTH_LONG)
                .show();

        if (FPUsed)
        {
            price_label.setText("Amount to be paid");
            fp_label.setText("Fidelity Points used");
            price.setText(Double.toString(newTotalPrice)+" DT");
            fp.setText(Integer.toString(fidelityPointsUsed)+"Points");
        }
        else
        {
            price_label.setText("Amount to be paid");
            fp_label.setText("Fidelity earned");
            price.setText(Double.toString(totalPrice)+" DT");
            fp.setText(Integer.toString(fidelityPointsEarned)+"Points");
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
                finishAct();

            }
        });
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }


    public void getList()  {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            sharedPref =  getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            int idStore =  sharedPref.getInt("idStore",0);

            jsonBody.put("StoreId", idStore);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_PRODUCTS_BYSTORE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());
                    // Check the JWT Token
                    Toast.makeText(getApplicationContext(),
                            "Products ", Toast.LENGTH_LONG)
                            .show();


                    try {

                        JSONArray tags = new JSONArray(response);
                        for (int i = 0; i < tags.length(); i++) {
                            JSONObject productObject = tags.getJSONObject(i);

                            int id = productObject.getInt("id");

                            String Reference = productObject.getString("Reference");
                            String productName = productObject.getString("ProductName");
                            float Price = productObject.getLong("Price");
                            String picture = productObject.getString("Image");
                            float PromoPrice = productObject.getLong("PromoPrice");
                            int ReductionPerc = productObject.getInt("ReductionPerc");
                            float FP = productObject.getLong("FP");

                            Product p = new Product();
                            p.setId(id);
                            p.setReference(Reference);
                            p.setProductName(productName);
                            p.setPrice(Price);
                            p.setPromoPrice(PromoPrice);
                            p.setReductionPerc(ReductionPerc);
                            p.setImage(picture);
                            p.setFP(FP);
                            p.setQuantity(1);
                            products.add(p);
                        }


                        listview = findViewById(R.id.my_product_list);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        listview.setLayoutManager(layoutManager);
                        listview.setHasFixedSize(true);

                        productListAdapter = new ProductListReceiptAdapter(getApplicationContext(), R.layout.list_row_product, products);
                        listview.setAdapter(productListAdapter);
                        productListAdapter.notifyDataSetChanged();



                        listviewselected = findViewById(R.id.selected_products);
                        RecyclerView.LayoutManager layoutManagerSelected = new LinearLayoutManager(getApplicationContext());
                        listviewselected.setLayoutManager(layoutManagerSelected);
                        listviewselected.setHasFixedSize(true);

                        productListReceiptSelectedAdapter = new ProductListReceiptSelectedAdapter(getApplicationContext(), R.layout.list_row_product_selected, selected);
                        listviewselected.setAdapter(productListReceiptSelectedAdapter);
                        productListReceiptSelectedAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    Toast.makeText(getApplicationContext(),
                            "A problem has been occured , please retry later.", Toast.LENGTH_LONG)
                            .show();

                }
            }) {

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

    public void addNewOrder(String phoneNumber,List<Product> products,int fidelityPointsUsed,float totalprice,int clientID)  {

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            sharedPref =  getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            int idStore =  sharedPref.getInt("idStore",0);

            jsonBody.put("StoreId", idStore);
            jsonBody.put("ClientId", clientID);
            jsonBody.put("Totalprice", totalprice);
            if (fidelityPointsUsed!=0)  {jsonBody.put("Fidelitypointsused", fidelityPointsUsed);}
          //  jsonBody.put("FidelityPointsUsed", fidelityPointsUsed);
            JSONArray prodcutsJsonArray=new JSONArray();
            for (Product produit :products

            ) {
                JSONObject item=new JSONObject();
                item.put("ProductId",produit.getId());
                item.put("quantity",produit.getQuantity());
                item.put("FP",produit.getFP());
                prodcutsJsonArray.put(item);


            }


            jsonBody.put("Products", prodcutsJsonArray);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_NEW_ORDER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("RecieptAcitiity", response.toString());
                    double newTotalPrice=0; // OK
                    int fidelityPointsEarned=0; // OK
                    double totalPrice=0; // OK
                    boolean FPUsed=false; // OK


                    try {
                        JSONObject  respObj = new JSONObject(response);
                        totalPrice = respObj.getDouble("totalprice");
                        FPUsed = respObj.getBoolean("FPused");

                        Log.e("RecieptAcitiity", "FPUSED"+FPUsed);
                        if (FPUsed)
                        {
                            newTotalPrice = respObj.getDouble("newTotalPrice");
                            //fidelityPointsUsed;
                        }
                        else
                        {
                            fidelityPointsEarned = respObj.getInt("fidelityPointsEarned");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }











                    ReceiptDetailsDialog(totalPrice,FPUsed,newTotalPrice,fidelityPointsEarned,fidelityPointsUsed);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getApplicationContext(),
                                "A problem has been occured", Toast.LENGTH_LONG)
                                .show();


                    } else if(error.networkResponse.statusCode == 402){
                        Toast.makeText(getApplicationContext(),
                                "Client dont have enough Points to use.", Toast.LENGTH_LONG)
                                .show();
                    }
                    else if(error.networkResponse.statusCode == 403){
                        Toast.makeText(getApplicationContext(),
                                "Account not verfied you cannot use your FP please proceed to register.", Toast.LENGTH_LONG)
                                .show();
                    }
                    else if(error.networkResponse.statusCode == 401){
                        Toast.makeText(getApplicationContext(),
                                "Please re login ", Toast.LENGTH_LONG)
                                .show();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "A problem has been occured.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }) {

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
    private void filter(String text){
        ArrayList<Product> filteredList=new ArrayList<>();

        for(Product item :products){
            if(item.getProductName().toLowerCase().contains(text.toLowerCase()) || item.getReference().contains(text.toLowerCase()) || (String.valueOf(item.getPrice()).contains(text))){
                filteredList.add(item);
            }
        }

        productListAdapter.filterList(filteredList);

    }



}
