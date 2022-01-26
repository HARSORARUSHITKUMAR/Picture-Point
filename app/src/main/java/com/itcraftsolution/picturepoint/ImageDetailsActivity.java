package com.itcraftsolution.picturepoint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.itcraftsolution.picturepoint.databinding.ActivityImageDetailsBinding;

public class ImageDetailsActivity extends AppCompatActivity {

    ActivityImageDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(ImageDetailsActivity.this)
                .load(getIntent().getStringExtra("FullImage"))
                .into(binding.igDetailZoom);
    }
}