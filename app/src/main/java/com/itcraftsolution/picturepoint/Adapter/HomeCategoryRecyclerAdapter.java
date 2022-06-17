package com.itcraftsolution.picturepoint.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.picturepoint.CatImageActivity;
import com.itcraftsolution.picturepoint.Models.CategoryModel;
import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.CategorySampleBinding;

import java.util.ArrayList;

public class HomeCategoryRecyclerAdapter extends RecyclerView.Adapter<HomeCategoryRecyclerAdapter.viewHolder> {

    Context context;
    ArrayList<CategoryModel> list;

    public HomeCategoryRecyclerAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CategoryModel model = list.get(position);
        Glide.with(context).load(model.getImgUri()).error(R.drawable.error).into(holder.binding.igSampleImage);
        holder.binding.txSampleName.setText(model.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CatImageActivity.class);
                intent.putExtra("CategoryName", model.getCategoryName());
                intent.putExtra("CategoryImage", model.getImgUri());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        CategorySampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CategorySampleBinding.bind(itemView);
        }
    }
}
