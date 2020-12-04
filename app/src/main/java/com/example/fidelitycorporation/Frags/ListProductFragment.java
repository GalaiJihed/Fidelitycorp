package com.example.fidelitycorporation.Frags;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListProductFragment extends Fragment {


    RecyclerView listview;
    List<Product> products;
    SessionManager session;
    SharedPreferences sharedPref;

    SharedPreferences.Editor editor;
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";

    Context context;
    ProductListAdapter productListAdapter;
    View root;
    Button refresh;
    EditText editText;

    public ListProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_list_product, container, false);
        products = new ArrayList<>();
        listview = root.findViewById(R.id.frag_product);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        listview.setLayoutManager(layoutManager);
        listview.setHasFixedSize(true);


        getList();

        editText = (EditText) root.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //user define method


            }
        });
        return root;
    }

    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();

        for (Product item : products) {
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        productListAdapter.filterList(filteredList);

    }


    public void getList() {

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            JSONObject jsonBody = new JSONObject();
            sharedPref = getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            int idStore = sharedPref.getInt("idStore", 0);

            jsonBody.put("StoreId", idStore);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_PRODUCTS_BYSTORE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());
                    // Check the JWT Token
//                    Toast.makeText(getContext(),
//                            "Products ", Toast.LENGTH_LONG)
//                            .show();


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


                            products.add(p);
                        }


                        listview = root.findViewById(R.id.frag_product);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                        listview.setLayoutManager(layoutManager);
                        listview.setHasFixedSize(true);

                        productListAdapter = new ProductListAdapter(context, R.layout.list_product_card_view, products);
                        listview.setAdapter(productListAdapter);
                        //DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
                        //listview.setItemViewCacheSize(20);
                        //listview.setDrawingCacheEnabled(true);
                        //listview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                        // 9. Set the ItemAnimator
                        //listview.setItemAnimator(itemAnimator);
                        productListAdapter.notifyDataSetChanged();

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

                    Toast.makeText(getContext(),
                            "A problem has been occured , please retry later.", Toast.LENGTH_LONG)
                            .show();

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



