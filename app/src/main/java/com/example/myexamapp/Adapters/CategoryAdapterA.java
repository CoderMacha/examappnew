package com.example.myexamapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexamapp.Models.CategoryModelA;

import com.example.myexamapp.R;
import com.example.myexamapp.SetsActivityA;

import com.example.myexamapp.databinding.ItemCategoryABinding;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapterA extends RecyclerView.Adapter<CategoryAdapterA.viewHolder>{

    Context context;
    ArrayList<CategoryModelA>list;

    public CategoryAdapterA(Context context, ArrayList<CategoryModelA> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category_a,parent,false);

        return new viewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CategoryModelA model = list.get(position);

        holder.binding.categoryName.setText(model.getCategoryName());

        Picasso.get()
                .load(model.getCategoryName())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.categoryImages);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SetsActivityA.class);
                intent.putExtra("category",model.getCategoryName());
                intent.putExtra("sets",model.getSetNum());
                intent.putExtra("key",model.getKey());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {

        ItemCategoryABinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=ItemCategoryABinding.bind(itemView);


        }
    }
}
