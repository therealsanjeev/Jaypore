package com.groupsale.Ecomm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.groupsale.Ecomm.databinding.SingleCategoryNameItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    SingleCategoryNameItemBinding binding;
    List<String> categoryList;
    Context context;


    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cateogryName;

        public CategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cateogryName = binding.categoryName;

            cateogryName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, "Yet to be implemented", Toast.LENGTH_SHORT).show();

        }
    }

    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        binding = SingleCategoryNameItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new CategoryViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {

        holder.cateogryName.setText(categoryList.get(position));

    }


    public void SetData(List<String> list) {
        this.categoryList = list;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


}
