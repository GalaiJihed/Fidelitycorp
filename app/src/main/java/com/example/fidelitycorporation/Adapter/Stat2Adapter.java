package com.example.fidelitycorporation.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fidelitycorporation.Entities.Client;
import com.example.fidelitycorporation.Entities.ClientHolder;

import java.util.List;


public class Stat2Adapter extends RecyclerView.Adapter<ClientHolder> {

    private final List<Client> clients;
    private Context context;
    private int itemResource;

    public Stat2Adapter(Context context, int itemResource, List<Client> clients) {

        // 1. Initialize our adapter
        this.clients = clients;
        this.context = context;
        this.itemResource = itemResource;
    }

    // 2. Override the onCreateViewHolder method
    @Override
    public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new ClientHolder(this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(ClientHolder holder, int position) {
        // 5. Use position to access the correct Bakery object
        Client client = this.clients.get(position);

        // 6. Bind the bakery object to the holder
        holder.bindAnnonce(client);
    }

    @Override
    public int getItemCount() {
        return this.clients.size();
    }
    // Clean all elements of the recycler
    public void clear() {
        clients.clear();
        notifyDataSetChanged();
    }
    // Add a list of items -- change to type used
    public void addAll(List<Client> list) {
        clients.addAll(list);
        notifyDataSetChanged();
    }
}


