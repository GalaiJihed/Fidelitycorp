package com.example.fidelitycorporation.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ProductListReceiptSelectedAdapter extends RecyclerView.Adapter<ProductListReceiptSelectedAdapter.ProductViewSelectedHolder> {

    private  List<Product> products;
    private Context context;
    private int itemResource;

    public ProductListReceiptSelectedAdapter(List<Product> products){

        this.products = products;
    }
    public ProductListReceiptSelectedAdapter(Context context, int itemResource, List<Product> products) {

        // 1. Initialize our adapter
        this.products = products;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public ProductViewSelectedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);

        return new ProductViewSelectedHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewSelectedHolder holder, int position) {
        Product product = this.products.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindProduct(product);

    }


    @Override
    public int getItemCount() {
        return this.products.size();
    }
    // Clean all elements of the recycler
    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }
    // Add a list of items -- change to type used
    public void addAll(List<Product> list) {
        products.addAll(list);
        notifyDataSetChanged();
    }
    /**
     * for text change listener FIlter List
     * @param filteredList
     * pass new array list and update d sam to existing arraylist
     */
    public  void filterList(ArrayList<Product> filteredList){

        products=filteredList;
        notifyDataSetChanged();
    }

    public static class ProductViewSelectedHolder extends RecyclerView.ViewHolder  {


        //  private final NetworkImageView picture;
        SessionManager session;

        private ImageButton add;
        private final String PREFERENCE_FILE_KEY_REDUCTION = "reductionPedit";
        private final String PREFERENCE_FILE_KEY_STORE = "id_store";
        SharedPreferences sharedPref,sharedPref2;
        SharedPreferences.Editor editor;
        private final TextView referance;
        private EditText quantity;
        private SeekBar seekBar;
        private final TextView productname;
        private final TextView price;
        private  int id ;
        private Product product;
        private Context context;

        public ProductViewSelectedHolder(Context context, View itemView) {

            super(itemView);
            // 1. Set the context
            this.context = context;

            // 2. Set up the UI widgets of the holder
            // this.picture = itemView.findViewById(R.id.list_stat1_img);
            this.referance = itemView.findViewById(R.id.product_ref_selected);
            this.productname = itemView.findViewById(R.id.product_name_selected);
            this.price = itemView.findViewById(R.id.product_price_selected);
            seekBar = itemView.findViewById(R.id.seekBar_selected);
            quantity = itemView.findViewById(R.id.quantity_selected);


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    quantity.setText(String.valueOf(progress));
                    product.setQuantity(progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });




        }

        @SuppressLint("SetTextI18n")
        public void bindProduct(final Product product) {

            this.product = product;
            this.productname.setText(product.getProductName());
            this.referance.setText(product.getReference());
            session=new SessionManager(itemView.getContext());
            this.price.setText(Float.toString(product.getPrice())+" DT");

        }



    }
}
