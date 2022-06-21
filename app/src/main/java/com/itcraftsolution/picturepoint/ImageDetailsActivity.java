package com.itcraftsolution.picturepoint;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.itcraftsolution.picturepoint.databinding.ActivityImageDetailsBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ImageDetailsActivity extends AppCompatActivity {

    private ActivityImageDetailsBinding binding;
    private Animation fabOpen, fabClose, rotateForward, rotatebackward;
    private boolean isOpen = false;
    private int DURATION = 300;
    private ProgressDialog dialog;
    private DownloadManager manager;
    private Uri uri;
    private WallpaperManager wallpaperManager;
    private static String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "PicturePoint";
    private OutputStream outputStream;
    private static final String CHANNEL_NAME = "IT_CRAFT_SOLUTION";


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

    private void DownloadImage(String FileName , String ImageUri)
    {
        File file = new File(APP_DIR);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(ImageDetailsActivity.this, "Somthing went wrong !!", Toast.LENGTH_SHORT).show();
            }
        }

        String fileName;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

            fileName = "IMG_" + currentDateTime + ".jpg";
            File mediaFile = new File(file + File.separator + fileName);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.igDetailZoom.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            try {
                    outputStream = new FileOutputStream(mediaFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , outputStream);
            showNotification(ImageDetailsActivity.this , mediaFile);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(mediaFile));
        sendBroadcast(intent);

    }


    private static void showNotification(Context context,  File destFile) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context);
        }

        Uri data = FileProvider.getUriForFile(context, "com.itcraftsolution.picturepoint" + ".provider", new File(destFile.getAbsolutePath()));
        Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setDataAndType(data, "image/*");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_NAME);

        notification.setSmallIcon(R.drawable.logo256)
                .setContentTitle(destFile.getName())
                .setContentText("File Saved to" + APP_DIR)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notification.build());

        Toast.makeText( context,"Saved to Gallery", Toast.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void makeNotificationChannel(Context context) {

        NotificationChannel channel = new NotificationChannel(CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }
}