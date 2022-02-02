package com.itcraftsolution.picturepoint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.itcraftsolution.picturepoint.databinding.ActivityImageDetailsBinding;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class ImageDetailsActivity extends AppCompatActivity {

    private ActivityImageDetailsBinding binding;
    private Animation fabOpen, fabClose, rotateForward, rotatebackward;
    private boolean isOpen = false;
    private int DURATION = 300;
    private ProgressDialog dialog;
    private DownloadManager manager;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();

        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        // Load Data First
        LoadData();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Downloading Image Processing ...");
        dialog.setCancelable(false);


        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotatebackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        rotateForward.setDuration(DURATION);
        rotatebackward.setDuration(DURATION);
        fabOpen.setDuration(DURATION);
        fabClose.setDuration(DURATION);

        //Set the OnclickListner in Main fab

        binding.fabMainDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        binding.fabDetailsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "I'm using #PicturePoint, Share and Download Image there is much easier: "
                        + getIntent().getStringExtra("DownloadImage") + " #PicturePoint #Wallpaper");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, null));
            }
        });

        binding.fabDetailsDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DownloadImage("Download", getIntent().getStringExtra("DownloadImage"));

            }
        });
    }

    private void LoadData() {
        Glide.with(ImageDetailsActivity.this)
                .load(getIntent().getStringExtra("FullImage"))
                .error(R.drawable.error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.igDetailZoom);

        Glide.with(ImageDetailsActivity.this)
                .load(getIntent().getStringExtra("UserProfile"))
                .error(R.drawable.error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.igDetailsProfile);
        binding.txDetailsName.setText(getIntent().getStringExtra("UserName"));

    }

    private void DownloadImage(String FileName, String ImageUri) {
        try {
            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            uri = Uri.parse(ImageUri);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(FileName)
                    .setAllowedNetworkTypes(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + FileName + ".jpg");
            manager.enqueue(request);

            Toast.makeText(ImageDetailsActivity.this, "Save To Gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void animateFab() {
        if (isOpen) {
            binding.fabMainDetails.startAnimation(rotateForward);
            binding.fabDetailsShare.startAnimation(fabClose);
            binding.fabDetailsDownload.startAnimation(fabClose);
            binding.fabDetailsShare.setClickable(false);
            binding.fabDetailsDownload.setClickable(false);
            isOpen = false;
        } else {
            binding.fabMainDetails.startAnimation(rotatebackward);
            binding.fabDetailsShare.startAnimation(fabOpen);
            binding.fabDetailsDownload.startAnimation(fabOpen);
            binding.fabDetailsShare.setClickable(true);
            binding.fabDetailsDownload.setClickable(true);
            isOpen = true;
        }
    }
}