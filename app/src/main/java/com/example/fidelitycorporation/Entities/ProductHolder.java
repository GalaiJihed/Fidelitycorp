package com.example.fidelitycorporation.Entities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.fidelitycorporation.Acts.HomeActivity;
import com.example.fidelitycorporation.Acts.LoginActivity;
import com.example.fidelitycorporation.Adapter.ProductListAdapter;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.App.AppController;
import com.example.fidelitycorporation.Frags.ClientsFragment;
import com.example.fidelitycorporation.Frags.ListProductFragment;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


  //  private final NetworkImageView picture;
    private Dialog myDialog,myDialogEdit,MyDialogReduction;
    SessionManager session;
    private NetworkImageView imgproduct;
    private ImageView btnClose,btnClose_edit,btnClose_reduction;
    private EditText productnameedit,priceedit;
    private Button btnYes,btnNo,btnyesreduction,btnnoreduction;
    private Button editProd;
    private final String PREFERENCE_FILE_KEY_REDUCTION = "reductionPedit";
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    SharedPreferences sharedPref,sharedPref2;
    SharedPreferences.Editor editor;
    private final TextView referance;
    private final TextView productname;
    private final TextView price;
    private final TextView promoprice;
    private final TextView delete;
    private final  TextView reduction;
    private final TextView edit;
  //  private final TextView reductionPerc;
    private final TextView FP;
    private  int id ;
    private Product product;
    private Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductHolder(Context context, View itemView) {

        super(itemView);
        // 1. Set the context
        this.context = context;

        // 2. Set up the UI widgets of the holder
       // this.picture = itemView.findViewById(R.id.list_stat1_img);
        this.referance = itemView.findViewById(R.id.id_reference_product);
        this.productname = itemView.findViewById(R.id.id_name_product);
        this.price = itemView.findViewById(R.id.id_price_product);
        this.promoprice=itemView.findViewById(R.id.id_promoprice_product);
    //    this.reductionPerc=itemView.findViewById(R.id.FiveText);
        this.reduction=itemView.findViewById(R.id.id_btnreduction);
        this.FP=itemView.findViewById(R.id.id_FP_product);
        this.delete=itemView.findViewById(R.id.id_delete_prod);
        this.edit=itemView.findViewById(R.id.id_edit_product);
        this.imgproduct=itemView.findViewById(R.id.id_image_product);

        // 3. Set the "onClick" listener of the holder
        itemView.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    public void bindProduct(final Product product) {

        // 4. Bind the data to the ViewHolder
        this.product = product;
       // this.name.setText(client.getFirstName()+" "+client.getLastName());
        this.productname.setText(product.getProductName());
        this.referance.setText(product.getReference());
        myDialog = new Dialog(itemView.getContext());
        myDialogEdit=new Dialog(itemView.getContext());
        MyDialogReduction=new Dialog(itemView.getContext());
        session=new SessionManager(itemView.getContext());
        this.price.setText(Float.toString(product.getPrice())+"DT");
        this.promoprice.setText(Float.toString(product.getPromoPrice())+"DT");
      //  this.reductionPerc.setText(Float.toString(product.getReductionPerc()));
        this.FP.setText(Float.toString(product.getFP()));
       // URI imageUri = URI.create(AppConfig.URL_GET_IMAGE+product.getImage());
        //this.imgproduct.setImageURI(Uri.parse(String.valueOf(imageUri)));
        //*this.birthDate.setText(client.getBirthDate().toString());
        //this.picture.setImageUrl(annonce.getPicture(), imageLoader);
        this.imgproduct.setImageUrl(AppConfig.URL_GET_IMAGE+product.getImage(), imageLoader);
        Log.e("Binding", AppConfig.URL_GET_IMAGE+product.getImage());

    }
public void deleteProduct(int id)
{
    RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
    JSONObject jsonBody = new JSONObject();


    final String requestBody = jsonBody.toString();

    StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AppConfig.URL_DELETE_PRODUCT+id, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.e("VOLLEY", response.toString());
            // Check the JWT Token
            Toast.makeText(itemView.getContext(),
                    "Product Deleted ", Toast.LENGTH_LONG)
                    .show();




        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("VOLLEY", error.toString());

            Toast.makeText(itemView.getContext(),
                    "A problem has been occured , please retry later.", Toast.LENGTH_LONG)
                    .show();

        }
    }) {

        //This is for Headers If You Needed
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            session = new SessionManager(itemView.getContext());
            params.put("auth", session.getUser());
            params.put("Content-Type", "application/json; charset=UTF-8");
            session = new SessionManager(itemView.getContext());
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

}
    public void EditProduct(final int id,final String ProductName,final double Price,final double PromoPrice){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id",id);
            jsonBody.put("ProductName",ProductName);
            jsonBody.put("Price",Price);
            jsonBody.put("PromoPrice",PromoPrice);
            jsonBody.put("ReductionPerc",0);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_EDIT_PRODUCT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(itemView.getContext(),
                            response, Toast.LENGTH_LONG)
                            .show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 404) {
                        Toast.makeText(itemView.getContext(),
                                "  Product not found", Toast.LENGTH_LONG)
                                .show();
                    } else if(error.networkResponse.statusCode == 400){
                        Toast.makeText(itemView.getContext(),
                                "   Erreur", Toast.LENGTH_LONG)
                                .show();
                    }
                    else if(error.networkResponse.statusCode == 409) {
                        Toast.makeText(itemView.getContext(),
                                "   Error has been occured try again !", Toast.LENGTH_LONG)
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

public void popupreduction(){

    MyDialogReduction.setContentView(R.layout.popup_reduction_product);

    Spinner spinner = MyDialogReduction.findViewById(R.id.myreduction_edit);

    ArrayList<String> arrayList1 = new ArrayList<String>();
    arrayList1.add("0");
    arrayList1.add("10");
    arrayList1.add("20");
    arrayList1.add("30");
    arrayList1.add("40");
    arrayList1.add("50");
    arrayList1.add("60");
    arrayList1.add("70");
    arrayList1.add("80");
    arrayList1.add("90");
    ArrayAdapter<String> adp = new ArrayAdapter<String>(myDialogEdit.getContext(),android.R.layout.simple_spinner_dropdown_item,arrayList1);
    spinner.setAdapter(adp);


    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3)
        {

            String v=  parent.getItemAtPosition(position).toString();


            float PriceConf=Float.valueOf(product.getPrice());
            int redConv=Integer.valueOf(v);
            float  SPromopr=(PriceConf/100)*redConv;
            float valafterpro=PriceConf-SPromopr;
            Toast.makeText(parent.getContext(), valafterpro+"s", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPref = itemView.getContext().getSharedPreferences(PREFERENCE_FILE_KEY_REDUCTION, Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            editor.putFloat("reduction_edit",valafterpro);
            editor.apply();
            editor.commit();

               /* String city = "The file type is " + parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();*/
        }

        public void onNothingSelected(AdapterView<?> arg0)
        {
            // TODO Auto-generated method stub
        }
    });
    btnyesreduction=MyDialogReduction.findViewById(R.id.id_alert_yes_reductionproduct);
    btnnoreduction=MyDialogReduction.findViewById(R.id.id_alert_no_reductionproduct);
    btnClose_reduction=MyDialogReduction.findViewById(R.id.btn_close_popup_reductionproduct);
    btnClose_reduction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyDialogReduction.dismiss();
        }
    });
    btnnoreduction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyDialogReduction.dismiss();
        }
    });
    btnyesreduction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Service Web
        //    Promotion();
            refreshFrag();
        }
    });
    MyDialogReduction.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    MyDialogReduction.show();
}
/*
public void Promotion(){
    try {


        sharedPref =  itemView.getContext().getSharedPreferences(
                PREFERENCE_FILE_KEY_REDUCTION, Context.MODE_PRIVATE);
        float promo =sharedPref.getFloat("reduction_edit",0);
        RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("ProductId", product.getId());
        jsonBody.put("storeID",1);
        jsonBody.put("Points",promo);

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADD_REDUCTION_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("VOLLEY", response.toString());
                // Check the JWT Token
                Toast.makeText(itemView.getContext(),
                        requestBody + " response :" + response, Toast.LENGTH_LONG)
                        .show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("VOLLEY", error.toString());
                if (error.networkResponse.statusCode == 401) {
                    Toast.makeText(itemView.getContext(),
                            "StoreID doesnt exist", Toast.LENGTH_LONG)
                            .show();

                } else{
                    Toast.makeText(itemView.getContext(),
                            "Product Not found", Toast.LENGTH_LONG)
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
                session = new SessionManager(itemView.getContext());
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
*/
    public void popupEditDialog(View v){
    myDialogEdit.setContentView(R.layout.edit_product);
    productnameedit=myDialogEdit.findViewById(R.id.edit_prod_name);
    priceedit=myDialogEdit.findViewById(R.id.id_price_product_edit);
    editProd=myDialogEdit.findViewById(R.id.id_edit_product);
    btnClose_edit=myDialogEdit.findViewById(R.id.id_close_edit_popup);
    btnClose_edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDialogEdit.cancel();
        }
    });
   editProd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String SProductNameEdit=productnameedit.getText().toString().trim();
            int id =product.getId();
            String SPriceEdit=priceedit.getText().toString().trim();

            double PriceConf=Double.valueOf(SPriceEdit);

            EditProduct(id,SProductNameEdit,PriceConf,PriceConf);

            Toast.makeText(itemView.getContext(),"Product Edited",Toast.LENGTH_LONG).show();
                refreshFrag();
            myDialogEdit.cancel();
        }
    });


    myDialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialogEdit.show();
}

public void Poppupdialog(View v){


    myDialog.setContentView(R.layout.popop_confirm_delete_prod);
    btnClose=myDialog.findViewById(R.id.btn_close_popup_product);
    btnNo=myDialog.findViewById(R.id.id_alert_no_product);
    btnYes=myDialog.findViewById(R.id.id_alert_yes_product);
    btnClose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDialog.cancel();
        }
    });
    btnYes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteProduct(product.getId());
            ArrayList<Product> ps =new ArrayList<>();
            Toast.makeText(itemView.getContext(),"Product deleted",Toast.LENGTH_LONG).show();

            refreshFrag();
            myDialog.cancel();
        }
    });
    btnNo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDialog.cancel();
         //   myDialog.getWindow();
        }
    });

    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();
}
    @Override
    public void onClick(View v) {

        this.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Poppupdialog(v);
            }
        });
        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupEditDialog(v);
            }
        });
        this.reduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupreduction();
            }
            });
    }
        public void refreshFrag(){
            String fragmentTag = ListProductFragment.class.getSimpleName();
            ListProductFragment fragment = new ListProductFragment();
            FragmentTransaction localFragmentTransaction = ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.ididid, fragment, fragmentTag).addToBackStack(fragmentTag).commit();
        }
}
