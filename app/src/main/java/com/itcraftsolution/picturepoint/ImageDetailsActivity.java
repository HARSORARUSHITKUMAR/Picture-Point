package com.itcraftsolution.picturepoint;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.itcraftsolution.picturepoint.Fragments.HomeFragment;
import com.itcraftsolution.picturepoint.databinding.ActivityImageDetailsBinding;

import java.io.File;
import java.io.IOException;

public class ImageDetailsActivity extends AppCompatActivity {

    private ActivityImageDetailsBinding binding;
    private Animation fabOpen, fabClose, rotateForward, rotatebackward;
    private boolean isOpen = false;
    private int DURATION = 300;
    private ProgressDialog dialog;
    private DownloadManager manager;
    private Uri uri;
    private WallpaperManager wallpaperManager;
    private boolean isGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();

        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        // Load Data First
        LoadData();

        wallpaperManager = WallpaperManager.getInstance(this);
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

        binding.storagePermission.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPermission();
            }
        });
        //Set the OnclickListner in Main fab

        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                DownloadImage("Download", getIntent().getStringExtra("DownloadImage"));
                DownloadImage("PicturePoint", getIntent().getStringExtra("FullImage"));

            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Bitmap bitmap = ((BitmapDrawable)binding.igDetailZoom.getDrawable()).getBitmap();
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(ImageDetailsActivity.this, "Set Wallpaper SuccessFully", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ImageDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void LoadData() {
        Glide.with(ImageDetailsActivity.this)
                .load(getIntent().getStringExtra("FullImage"))
                .error(R.drawable.error)
                .into(binding.igDetailZoom);

    }

//    private void DownloadImage(String FileName, String ImageUri) {
//        try {
//            File direct = new File(Environment.getExternalStorageDirectory() + "PicturePoint");
//            if(!direct.exists())
//            {
//                direct.mkdirs();
//            }
//            manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//            uri = Uri.parse(ImageUri);
//            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
//                    DownloadManager.Request.NETWORK_MOBILE)
//                    .setAllowedOverRoaming(false)
//                    .setTitle(FileName)
//                    .setAllowedNetworkTypes(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                    .setDestinationInExternalPublicDir("/PicturePoint",  FileName + ".jpg");
//            manager.enqueue(request);
//
//            Toast.makeText(ImageDetailsActivity.this, "Waiting for Downloading... ", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

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
            finish();
            Toast.makeText(ImageDetailsActivity.this, "Saved into Gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showPermission()
    {
        // permission for 23 to 29 SDK
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(ImageDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ImageDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }

        // permission for R or above SDK
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if(!Environment.isExternalStorageManager())
            {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", ImageDetailsActivity.this.getPackageName())));
                    startActivityIfNeeded(intent, 101);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    startActivityIfNeeded(intent, 101);
                }

            }
        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(getApplicationContext(),
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkPermission())
        {
            isGranted = true;
            binding.storagePermission.getRoot().setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer , new HomeFragment()).commit();
        }else {
            Toast.makeText(this, "Please Allow Storage Permission", Toast.LENGTH_SHORT).show();
        }
    }
}