package com.itcraftsolution.picturepoint;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.itcraftsolution.picturepoint.Utils.NetworkChangeListner;
import com.itcraftsolution.picturepoint.databinding.ActivityImageDetailsBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class ImageDetailsActivity extends AppCompatActivity {

    private ActivityImageDetailsBinding binding;
    private WallpaperManager wallpaperManager;
    private static final String APP_DIR = Environment.DIRECTORY_PICTURES +
            File.separator + "PicturePoint";
    private File PICTURE_POINT_DIR = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "/PicturePoint/");
    private OutputStream outputStream;
    private static final String CHANNEL_NAME = "IT_CRAFT_SOLUTION";
    private final NetworkChangeListner networkChangeListner = new NetworkChangeListner();

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

        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkPermission())
                {
                    showPermission();
                }else{
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.igDetailZoom.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    saveImgIntoGallery(bitmap);
                }

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

        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = Uri.parse(getIntent().getStringExtra("FullImage"));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm using @PicturePoint app: https://play.google.com/store/apps/details?id=com.itcraftsolution.picturepoint . #Wallpaper #PicturePoint");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getIntent().getStringExtra("FullImage"));
                File fdelete = new File(uri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Toast.makeText(ImageDetailsActivity.this, "Image Deleted Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ImageDetailsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(ImageDetailsActivity.this, "file not Deleted :" + uri.getPath(), Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(fdelete));
                    sendBroadcast(intent);
                }
//                    DeleteAndScanFile(getIntent().getStringExtra("FullImage"), fdelete);

            }
        });
    }

    private void LoadData() {
        Glide.with(ImageDetailsActivity.this)
                .load(getIntent().getStringExtra("FullImage"))
                .error(R.drawable.error)
                .into(binding.igDetailZoom);
        if(getIntent().getStringExtra("DownloadImage").equals("saved"))
        {
            binding.btnShare.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.btnClose.setVisibility(View.GONE);
            binding.btnDownload.setVisibility(View.GONE);
        }
    }

    private void DownloadImage()
    {
//        File file = new File(APP_DIR);
//        if (!file.exists()) {
//            if (!file.mkdirs()) {
//                Toast.makeText(ImageDetailsActivity.this, "Somthing went wrong !!", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        String fileName;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
//        String currentDateTime = sdf.format(new Date());
//
//            fileName = "IMG_" + currentDateTime + ".jpg";
//            File mediaFile = new File(file + File.separator + fileName);
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.igDetailZoom.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//
//            try {
//                    outputStream = new FileOutputStream(mediaFile);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , outputStream);
//            showNotification(ImageDetailsActivity.this , mediaFile);
//        try {
//            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        intent.setData(Uri.fromFile(mediaFile));
//        sendBroadcast(intent);
//        finish();


    }

    private void saveImgIntoGallery(Bitmap bitmap)
    {
        FileOutputStream fos;
        try {
            String fileName;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateTime = sdf.format(new Date());
            fileName = "IMG_" + currentDateTime + ".jpg";
            File mediaFile = new File(PICTURE_POINT_DIR + File.separator + fileName);

                if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R || Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)
                {
                    ContentResolver contentResolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, APP_DIR);
                    Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    fos = (FileOutputStream) contentResolver.openOutputStream(imageUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Objects.requireNonNull(fos);
                }else{

                    if (!PICTURE_POINT_DIR.exists()) {
                        if (!PICTURE_POINT_DIR.mkdirs()) {
                            Toast.makeText(ImageDetailsActivity.this, "Somthing went wrong !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    fos = new FileOutputStream(mediaFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(mediaFile));
                    sendBroadcast(intent);

                }
            showNotification(ImageDetailsActivity.this, mediaFile);
            finish();
        }catch (Exception e)
        {
            Toast.makeText(this, "Not Save to Gallery" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                .setContentText("File Saved to " + APP_DIR)
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

    private void DeleteAndScanFile( String path,
                                   final File fi) {

//        String fpath = path.substring(path.lastIndexOf("/") + 1);
//        Log.i("fpath", fpath);
//        try {
//            MediaScannerConnection.scanFile(ImageDetailsActivity.this, new String[] { Environment
//                            .getExternalStorageDirectory().toString()
//                            + "/PicturePoint/"
//                            + fpath }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Toast.makeText(ImageDetailsActivity.this, "path : "+path + " uri : "+uri, Toast.LENGTH_LONG).show();
//                            if (uri != null) {
//                                getContentResolver().delete(uri, null,
//                                        null);
//
//                                Toast.makeText(ImageDetailsActivity.this, "Image Deleted Successfully", Toast.LENGTH_LONG).show();
//
//                            }else
//                            {
//                                if(fi.exists()) {
//                                    fi.delete();
//                                    Toast.makeText(ImageDetailsActivity.this, "path file :" + fi.getPath(), Toast.LENGTH_SHORT).show();
//                                }
//                                Toast.makeText(ImageDetailsActivity.this, "Image Deleted Successfully null", Toast.LENGTH_LONG).show();
//
//                            }
//                            Intent intent = new Intent(ImageDetailsActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finishAffinity();
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        ContentResolver resolver = this.getContentResolver();
        Uri uri = Uri.fromFile(fi);
        int dlt = resolver.delete(uri,null,null);
        Toast.makeText(this, ""+dlt, Toast.LENGTH_SHORT).show();

    }

    private void showPermission()
    {
        // permission for 23 to 29 SDK
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(ImageDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ImageDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ImageDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }

    private boolean checkPermission() {

            int write = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListner, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListner);
        super.onStop();
    }
}