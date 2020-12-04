package com.example.fidelitycorporation.Frags;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.example.fidelitycorporation.helper.FilePath;
import com.example.fidelitycorporation.helper.SessionManager;
import net.gotev.uploadservice.MultipartUploadRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {
    private final String PREFERENCE_FILE_KEY = "reductionP";
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    SessionManager session;
    View root;
    SharedPreferences sharedPref, sharedPref2;
    SharedPreferences.Editor editor;
    private Uri stafffilePath;
    private EditText EProductName;
    private EditText EReference;
    private EditText EPrice;
    private EditText EReduction;
    private ImageView StaffUpload;
    private Button btnaddproduct;


    public AddProductFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_product, container, false);
        //  Get session With Token
        session = new SessionManager(getContext());
        Log.d("token", session.getUser());
        EProductName = root.findViewById(R.id.id_name_prod);
        EReference = root.findViewById(R.id.id_ref_prod);
        EPrice = root.findViewById(R.id.id_price_prod);
        StaffUpload = root.findViewById(R.id.id_img_prod);
        btnaddproduct = root.findViewById(R.id.id_add_product);
        StaffUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDocument();
            }
        });
        sharedPref2 = getActivity().getSharedPreferences(
                PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
        int idStore = sharedPref2.getInt("idStore", 0);
        // Toast.makeText(getContext(),idStore+"",Toast.LENGTH_LONG).show();
        // Spinner Setup
        Log.e("Store", "getIdStore  : " + idStore + "");


        btnaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref = getActivity().getSharedPreferences(
                        PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                sharedPref2 = getActivity().getSharedPreferences(
                        PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
                int idStore = sharedPref2.getInt("idStore", 0);

                String red = sharedPref.getString("reduction", null);
                String SProductName = EProductName.getText().toString().trim();
                String SPrice = EPrice.getText().toString().trim();
                String SReference = EReference.getText().toString().trim();

                if (SPrice.isEmpty() && SProductName.isEmpty() && stafffilePath == null && SReference.isEmpty()) {
                    Toast.makeText(getContext(), "all fields are empty", Toast.LENGTH_LONG).show();

                } else if (SReference.isEmpty()) {
                    Toast.makeText(getContext(), "the reference product  is empty", Toast.LENGTH_LONG).show();

                } else if (SPrice.isEmpty()) {
                    Toast.makeText(getContext(), "the field price is empty", Toast.LENGTH_LONG).show();

                } else if (SProductName.isEmpty()) {
                    Toast.makeText(getContext(), "the field product name is empty", Toast.LENGTH_LONG).show();
                } else if (stafffilePath == null) {
                    Toast.makeText(getContext(), "the field image product  is empty", Toast.LENGTH_LONG).show();
                } else {
                    double PriceConf = Double.valueOf(SPrice);
//                    int redConv=Integer.valueOf(red);
                    //    double  SPromopr=(PriceConf/100)*redConv;
                    //       double valafterpro=PriceConf-SPromopr;
                    AddProduct(SProductName, SReference, PriceConf, PriceConf, idStore);
                    uploadFile(stafffilePath, SReference);
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_LONG).show();
                    clear();
                }
            }
        });

        return root;
    }

    public void clear() {
        EPrice.getText().clear();
        EReference.getText().clear();
        EProductName.getText().clear();
    }

    private void AddProduct(final String ProductName, final String Reference, final double Price, final double PromoPrice, final int StoreId) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ProductName", ProductName);
            jsonBody.put("Reference", Reference);
            jsonBody.put("Price", Price);
            jsonBody.put("PromoPrice", PromoPrice);
            jsonBody.put("ReductionPerc", 0);
            jsonBody.put("Image", "Image");
            jsonBody.put("FP", 10);
            jsonBody.put("storeID", StoreId);

            final String requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADD_PRODUCT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());
                    // Check the JWT Token
//                    Toast.makeText(getContext(),
//                            requestBody + " response :" + response, Toast.LENGTH_LONG)
//                            .show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getContext(),
                                "Please Verify storeID", Toast.LENGTH_LONG)
                                .show();

                    } else if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getContext(),
                                "validate the product parametre.", Toast.LENGTH_LONG)
                                .show();
                    } else if (error.networkResponse.statusCode == 409) {
                        Toast.makeText(getContext(),
                                "Error has been occured try again .", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(getContext(),
                                "Product Added .", Toast.LENGTH_LONG)
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

    public void chooseDocument() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchIntent();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Allow the permission in order to upload the document")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConfig.STORAGE_PERMISSION_CODE);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConfig.STORAGE_PERMISSION_CODE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConfig.STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void launchIntent() {
        Intent chooseintent = new Intent();
        //If you want to upload an image specify as "image/*"
        chooseintent.setType("image/*");
        chooseintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(chooseintent, "Select file"), AppConfig.CHOOSE_FILE_STAFF);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConfig.CHOOSE_FILE_STAFF && resultCode == RESULT_OK && data != null && data.getData() != null) {
            stafffilePath = data.getData();
            StaffUpload.setImageURI(stafffilePath);
            if (requestCode == AppConfig.CHOOSE_FILE_STAFF && resultCode == RESULT_OK && data != null && data.getData() != null) {
                stafffilePath = data.getData();
                EReference = root.findViewById(R.id.id_ref_prod);
                String SReference = EReference.getText().toString().trim();
                //Toast.makeText(getContext(),SReference+"Path"+stafffilePath.toString(),Toast.LENGTH_LONG).show();
                //    uploadFile(stafffilePath,SReference);

            }
        }
    }


    // i am not not sure about this method (To verify)

    public void uploadFile(Uri path, String reference) {
        String fullpath = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            fullpath = FilePath.getPath(getContext(), path);
        }


        if (fullpath == null) {

            // Toast.makeText(getContext(), "Please move your  file in the internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();
                //      singleUploadBroadcastReceiver.setDelegate(Register2Activity.this);
                //    singleUploadBroadcastReceiver.setUploadID(uploadId);

// here I am getting the filename from the path in order to check the file uploading

                String fpath[] = fullpath.split("/");
                String exactfilename = fpath[fpath.length - 1];
                String efilename[] = exactfilename.split("\\.");
                String ext = efilename[efilename.length - 1];

//Here I am trying to upload the excel file so iam checking whether the file is excel or not


                if (ext.equals("jpg") || ext.equals("png") || ext.equals("jpeg")) {

                    //Creating a multi-part request
                    EReference = root.findViewById(R.id.id_ref_prod);
                    String SReference = EReference.getText().toString().trim();
                    new MultipartUploadRequest(getContext(), uploadId, AppConfig.URL_UPLOAD_FILE_PRODUCT)
                            .addFileToUpload(fullpath, "filedata") //Adding file
                            .addParameter("name", reference)
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please upload a Excel file").setPositiveButton("ok", null).create().show();

                }

            } catch (Exception exc) {
                // Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}





