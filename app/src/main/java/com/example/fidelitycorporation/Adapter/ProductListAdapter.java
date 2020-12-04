package com.example.fidelitycorporation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;



import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.Entities.ProductHolder;
import com.example.fidelitycorporation.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductHolder> {

    private  List<Product> products;
    private Context context;
    private int itemResource;

    public ProductListAdapter(List<Product> products){

        this.products = products;
    }
    public ProductListAdapter(Context context, int itemResource, List<Product> products) {

        // 1. Initialize our adapter
        this.products = products;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);

        return new ProductHolder(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        // 5. Use position to access the correct Bakery object
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
    public void NotifiDataSetChanged(){
        notifyDataSetChanged();
    }
}
