package com.itcraftsolution.picturepoint;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.itcraftsolution.picturepoint.Adapter.HomeCategoryRecyclerAdapter;
import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
import com.itcraftsolution.picturepoint.CustomDialog.permission_Dailog;
import com.itcraftsolution.picturepoint.Fragments.CatFragment;
import com.itcraftsolution.picturepoint.Fragments.HomeFragment;
import com.itcraftsolution.picturepoint.Fragments.SavedFragment;
import com.itcraftsolution.picturepoint.Models.CategoryModel;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;

import com.itcraftsolution.picturepoint.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<ImageModel> list;
    private GridLayoutManager manager;
    private LinearLayoutManager linearmanager;
    private PopularHomeRecyclerAdapter adapter;
    private HomeCategoryRecyclerAdapter catadapter;
    private int page = 7;
    private int pagesize = 30;
    private String Keyword = null;
    private boolean isLoading, isLastPage;
    private boolean FromSearch = false;
    private boolean FromScroll = false;
    private SearchView searchView;
    private boolean isGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tlMain);

//        permission_Dailog dialog = new permission_Dailog(MainActivity.this);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
//        dialog.setCancelable(false);
//        dialog.show();
//            if(isGranted)
//            {
//                binding.storagePermission.getRoot().setVisibility(View.GONE);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.frMainContainer , new HomeFragment());
//                fragmentTransaction.commit();
//            }
            //        list = new ArrayList<>();
//        categoryModels = new ArrayList<>();
//        adapter = new PopularHomeRecyclerAdapter(MainActivity.this, list);
//        manager = new GridLayoutManager(MainActivity.this, 2);
//        dialog = new ProgressDialog(MainActivity.this);
//        dialog.setMessage("Loading....");
//        dialog.setCancelable(false);
//        dialog.show();



//        Fade fade = new Fade();
//        View decor = getWindow().getDecorView();
//
//        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
//        fade.excludeTarget(android.R.id.statusBarBackground, true);
//        fade.excludeTarget(android.R.id.navigationBarBackground, true);
//
//        getWindow().setEnterTransition(fade);
//        getWindow().setExitTransition(fade);

//        binding.storagePermission.btnPermission.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////            showPermission();
//            }
//        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, new HomeFragment()).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                    switch (item.getItemId())
                    {
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.topmenu , menu);
        MenuItem menuItem = menu.findItem(R.id.itMenuSearch);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here To Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String imageUrl = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?cs=srgb&dl=pexels-sohail-nachiti-807598.jpg&fm=jpg";
                Intent intent = new Intent(MainActivity.this , CatImageActivity.class);
                intent.putExtra("CategoryName" , query);
                intent.putExtra("CategoryImage" , imageUrl);
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

    private void showPermission()
    {
        // permission for 23 to 29 SDK
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
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
                    intent.setData(Uri.parse(String.format("package:%s", MainActivity.this.getPackageName())));
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(checkPermission())
//        {
//            isGranted = true;
//            binding.storagePermission.getRoot().setVisibility(View.GONE);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer , new HomeFragment()).commit();
//        }else {
//            Toast.makeText(this, "Please Allow Storage Permission", Toast.LENGTH_SHORT).show();
//        }
//    }
}