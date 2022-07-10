package com.itcraftsolution.picturepoint;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.itcraftsolution.picturepoint.Fragments.CatFragment;
import com.itcraftsolution.picturepoint.Fragments.HomeFragment;
import com.itcraftsolution.picturepoint.Fragments.SavedFragment;
import com.itcraftsolution.picturepoint.Utils.NetworkChangeListner;
import com.itcraftsolution.picturepoint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int page = 7;
    private int pagesize = 30;
    private EditText searchEditText;
    private SearchView searchView;
    private final NetworkChangeListner networkChangeListner = new NetworkChangeListner();
    private boolean isGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tlMain);

//        if(!isGranted)
//        {
//            binding.storagePermission.getRoot().setVisibility(View.VISIBLE);
//            binding.clMainView.setVisibility(View.GONE);
//        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, new HomeFragment()).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.itMenuHome:
                        temp = new HomeFragment();
                        break;

                    case R.id.itMenuCat:
                        temp = new CatFragment();
                        break;


                    case R.id.itMenuDownload:
                        temp = new SavedFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, temp).commit();
                return true;
            }
        });

        binding.storagePermission.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPermission();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.itMenuSearch);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here To Search...");
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(R.color.lightblack));
        searchEditText.setTextColor(getResources().getColor(R.color.lightblack));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String imageUrl = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?cs=srgb&dl=pexels-sohail-nachiti-807598.jpg&fm=jpg";
                Intent intent = new Intent(MainActivity.this, CatImageActivity.class);
                intent.putExtra("CategoryName", query);
                intent.putExtra("CategoryImage", imageUrl);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

//    private void showPermission()
//    {
//        // permission for 23 to 29 SDK
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},100);
//            }
//        }
//     }
        // permission for R or above SDK
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//        {
//            if(!Environment.isExternalStorageManager())
//            {
//                try {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.setData(Uri.parse(String.format("package:%s", MainActivity.this.getPackageName())));
//                    startActivityIfNeeded(intent, 101);
//                } catch (Exception e) {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    startActivityIfNeeded(intent, 101);
//                }
//
//            }
//        }
//    }

//    private boolean checkPermission() {
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            return Environment.isExternalStorageManager();
//        } else {
//            int write = ContextCompat.checkSelfPermission(getApplicationContext(),
//                    WRITE_EXTERNAL_STORAGE);
//            int read = ContextCompat.checkSelfPermission(getApplicationContext(),
//                    READ_EXTERNAL_STORAGE);
//
//            return write == PackageManager.PERMISSION_GRANTED &&
//                    read == PackageManager.PERMISSION_GRANTED;
//        }



    @Override
    public void onBackPressed() {

        if(binding.bottomNavigationView.getSelectedItemId() == R.id.itMenuHome)
        {
            super.onBackPressed();
        }else{
            binding.bottomNavigationView.setSelectedItemId(R.id.itMenuHome);
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListner, filter);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(checkPermission())
//        {
//            isGranted = true;
//            binding.storagePermission.getRoot().setVisibility(View.GONE);
//
//        }else {
//            Toast.makeText(this, "Please Allow Storage Permission", Toast.LENGTH_SHORT).show();
//        }
//        if(isGranted)
//        {
//            binding.clMainView.setVisibility(View.VISIBLE);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, new HomeFragment()).commit();
//        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListner);
        super.onStop();
    }
}