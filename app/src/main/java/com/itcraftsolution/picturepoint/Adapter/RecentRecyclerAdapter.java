package com.itcraftsolution.picturepoint.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.itcraftsolution.picturepoint.ImageDetailsActivity;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.RvhomerecentSampleBinding;

import java.util.ArrayList;
import java.util.Objects;

public class RecentRecyclerAdapter extends RecyclerView.Adapter<RecentRecyclerAdapter.viewHolder> {

    Context context;
    ArrayList<ImageModel> list;
    private int lastposition = -1;
    public RecentRecyclerAdapter(Context context, ArrayList<ImageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rvhomerecent_sample , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ImageModel imageModel = list.get(position);
        Glide.with(context)
                .load(imageModel.getSrc().getPortrait())
                .error(R.drawable.error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.igSampleImage);

        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailsActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.binding.igSampleImage,
                        Objects.requireNonNull(ViewCompat.getTransitionName(holder.binding.igSampleImage)));
                intent.putExtra("FullImage", imageModel.getSrc().getPortrait());
                intent.putExtra("DownloadImage", imageModel.getUrl());
                context.startActivity(intent, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        RvhomerecentSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvhomerecentSampleBinding.bind(itemView);
        }
    }

    private void setAnimation(View view , int position)
    {
        if(position > lastposition)
        {
            Animation slidein = AnimationUtils.loadAnimation(context , android.R.anim.slide_in_left);
            view.setAnimation(slidein);
            lastposition = position;
        }
    }
}
