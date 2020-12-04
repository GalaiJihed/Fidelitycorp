package com.example.fidelitycorporation.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fidelitycorporation.Acts.ReceiptActivity;
import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ProductListReceiptAdapter extends RecyclerView.Adapter<ProductListReceiptAdapter.ProductViewHolder> {


    private  List<Product> products;
    private Context context;
    private int itemResource;




    public ProductListReceiptAdapter(List<Product> products){

        this.products = products;
    }



    public ProductListReceiptAdapter(Context context, int itemResource, List<Product> products) {

        // 1. Initialize our adapter
        this.products = products;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);

        return new ProductViewHolder(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // 5. Use position to access the correct Bakery object
        Product product = this.products.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindProduct(product);
        /*holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,products.size());

            }
        });*/

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
    public void addItem( Product p)
    {
        products.add(p);
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

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        SessionManager session;

        private Button add;
        private final String PREFERENCE_FILE_KEY_REDUCTION = "reductionPedit";
        private final String PREFERENCE_FILE_KEY_STORE = "id_store";
        SharedPreferences sharedPref,sharedPref2;
        SharedPreferences.Editor editor;
        private final TextView referance;
        private final TextView productname;
        private final TextView price;
        private  int id ;
        private Product product;
        private Context context;

        public ProductViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            this.context = context;

            // 2. Set up the UI widgets of the holder
            // this.picture = itemView.findViewById(R.id.list_stat1_img);
            this.referance = itemView.findViewById(R.id.product_ref);
            this.productname = itemView.findViewById(R.id.product_name);
            this.price = itemView.findViewById(R.id.product_price);




        }
        public void bindProduct(final Product product) {

            // 4. Bind the data to the ViewHolder
            this.product = product;
            // this.name.setText(client.getFirstName()+" "+client.getLastName());
            this.productname.setText(product.getProductName());
            this.referance.setText(product.getReference());
            session=new SessionManager(itemView.getContext());
            this.price.setText(Float.toString(product.getPrice())+" DT");
            //*this.birthDate.setText(client.getBirthDate().toString());
            //this.picture.setImageUrl(annonce.getPicture(), imageLoader);
        }
    }
}
