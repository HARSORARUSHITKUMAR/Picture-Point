package com.itcraftsolution.picturepoint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private ProgressDialog dialog;
    private PopularHomeRecyclerAdapter adapter;
    private HomeCategoryRecyclerAdapter catadapter;
    private int page = 7;
    private int pagesize = 30;
    private String Keyword = null;
    private boolean isLoading, isLastPage;
    private boolean FromSearch = false;
    private boolean FromScroll = false;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tlMain);

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
//
//        categoryModels.add(new CategoryModel(R.drawable.trending, "Trending"));
//        categoryModels.add(new CategoryModel(R.drawable.nature, "Nature"));
//        categoryModels.add(new CategoryModel(R.drawable.animals, "Animals"));
//        categoryModels.add(new CategoryModel(R.drawable.anime, "Anime"));
//        categoryModels.add(new CategoryModel(R.drawable.designs, "Design"));
//        categoryModels.add(new CategoryModel(R.drawable.drawing, "Drawings"));
//        categoryModels.add(new CategoryModel(R.drawable.fashion, "Fashion"));
//        categoryModels.add(new CategoryModel(R.drawable.funny, "Funny"));
//        categoryModels.add(new CategoryModel(R.drawable.bollywood, "Bollywood"));
//        categoryModels.add(new CategoryModel(R.drawable.love, "Love"));
//        categoryModels.add(new CategoryModel(R.drawable.space, "Space"));
//        categoryModels.add(new CategoryModel(R.drawable.sport, "Sports"));
//        categoryModels.add(new CategoryModel(R.drawable.carvehical, "Bike"));
//        categoryModels.add(new CategoryModel(R.drawable.sportscar, "SportsCar"));
//        categoryModels.add(new CategoryModel(R.drawable.technology, "Technology"));
//
//        catadapter = new HomeCategoryRecyclerAdapter(this, categoryModels);
//        linearmanager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

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
}