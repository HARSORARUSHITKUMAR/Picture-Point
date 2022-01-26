package com.itcraftsolution.picturepoint.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.itcraftsolution.picturepoint.ImageDetailsActivity;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.RvhomepopularSampleBinding;

import java.util.ArrayList;

public class PopularHomeRecyclerAdapter extends RecyclerView.Adapter<PopularHomeRecyclerAdapter.viewHolder> {

    Context context;
    ArrayList<ImageModel> list;

    public PopularHomeRecyclerAdapter(Context context, ArrayList<ImageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rvhomepopular_sample , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ImageModel imageModel = list.get(position);
        Glide.with(context)
                .load(imageModel.getUrls().getRegular())
                .error(R.drawable.error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.igSampleImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , ImageDetailsActivity.class);
                intent.putExtra("FullImage" , imageModel.getUrls().getRegular());
                intent.putExtra("UserName" , imageModel.getUser().getUsername());
                intent.putExtra("UserProfile" , imageModel.getUser().getProfile_image().getMedium());
                intent.putExtra("DownloadImage" , imageModel.getLinks().getDownload());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        RvhomepopularSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvhomepopularSampleBinding.bind(itemView);
        }
    }
}
