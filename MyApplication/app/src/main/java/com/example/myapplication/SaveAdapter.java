package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String item_name, item_code, item_qty;


    public SaveAdapter(String item_name,String item_code, String item_qty){

        this.item_name = item_name;
        this.item_code = item_code;
        this.item_qty = item_qty;

    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView name, qty, code;
        LinearLayout lay;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            qty = itemView.findViewById(R.id.qty);
            code = itemView.findViewById(R.id.code);
            lay = itemView.findViewById(R.id.lay);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.save_layout,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
