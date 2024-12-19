package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Model> list;

    public SaveAdapter(List<Model> list){

        this.list = list;

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

        viewHolder holder1 = (viewHolder) holder;
        //filter.addAll(list);
        Model model = list.get(position);
        holder1.lay.setVisibility(View.VISIBLE);

        holder1.name.setText(model.getName());
        holder1.code.setText(model.getItem_code());
        holder1.qty.setText(model.getQty());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
