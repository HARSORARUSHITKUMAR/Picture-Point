package com.itcraftsolution.picturepoint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itcraftsolution.picturepoint.Adapter.HomeCategoryRecyclerAdapter;
import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tlMain);
        list = new ArrayList<>();
        categoryModels = new ArrayList<>();
        adapter = new PopularHomeRecyclerAdapter(MainActivity.this  , list);
        manager = new GridLayoutManager(MainActivity.this , 2);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        dialog.show();

        binding.rvImage.setLayoutManager(manager);
        binding.rvImage.setHasFixedSize(true);
        binding.rvImage.setAdapter(adapter);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();

        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        getData();

        categoryModels.add(new CategoryModel(R.drawable.trending, "Trending"));
        categoryModels.add(new CategoryModel(R.drawable.nature, "Nature"));
        categoryModels.add(new CategoryModel(R.drawable.animals, "Animals"));
        categoryModels.add(new CategoryModel(R.drawable.anime, "Anime"));
        categoryModels.add(new CategoryModel(R.drawable.designs, "Design"));
        categoryModels.add(new CategoryModel(R.drawable.drawing, "Drawings"));
        categoryModels.add(new CategoryModel(R.drawable.fashion, "Fashion"));
        categoryModels.add(new CategoryModel(R.drawable.funny, "Funny"));
        categoryModels.add(new CategoryModel(R.drawable.bollywood, "Bollywood"));
        categoryModels.add(new CategoryModel(R.drawable.love, "Love"));
        categoryModels.add(new CategoryModel(R.drawable.space, "Space"));
        categoryModels.add(new CategoryModel(R.drawable.sport, "Sports"));
        categoryModels.add(new CategoryModel(R.drawable.carvehical, "Bike"));
        categoryModels.add(new CategoryModel(R.drawable.sportscar, "SportsCar"));
        categoryModels.add(new CategoryModel(R.drawable.technology, "Technology"));

        catadapter = new HomeCategoryRecyclerAdapter(this, categoryModels);
        linearmanager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvcategory.setLayoutManager(linearmanager);
        binding.rvcategory.setHasFixedSize(true);
        binding.rvcategory.setAdapter(catadapter);

        binding.btnretry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                binding.btnretry.setVisibility(View.GONE);
                binding.txRetry.setVisibility(View.GONE);
            }
        });


        binding.rvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPos = manager.findFirstVisibleItemPosition();

                if(!isLoading && !isLastPage)
                {
                    if((visibleItem+firstVisibleItemPos) >= totalItem
                    && firstVisibleItemPos >= 0
                    && totalItem >= pagesize)
                    {
                        page++;
                        if(Keyword == null)
                        {
                            getData();
                        }else {
                            searchData(Keyword);
                            Keyword = null;
                            FromScroll = true;
                        }

                    }
                }

                else {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu , menu);
        MenuItem search = menu.findItem(R.id.itMenuSearch);
       SearchView SearchView = (androidx.appcompat.widget.SearchView) search.getActionView();
       SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;

           }

           @Override
           public boolean onQueryTextChange(String newText) {
                   dialog.show();
                   searchData(newText);
//                   SearchView.clearFocus();
               return true;


           }
       });
        return true;
    }

    private void searchData(String query) {
        binding.textView2.setVisibility(View.GONE);
        binding.rvcategory.setVisibility(View.GONE);
        binding.textView3.setText("Top Searches");
            dialog.dismiss();
        if (!FromScroll) {
            list.clear();
        }
            if(query.isEmpty())
            {
                binding.textView2.setVisibility(View.VISIBLE);
                binding.rvcategory.setVisibility(View.VISIBLE);
                binding.textView3.setText("Popular Searches");
                FromSearch = true;
               getData();
            }
            ApiUtilities.apiInterface().SearchImages(query, page, pagesize).enqueue(new Callback<SearchModel>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                    if(response.body() != null)
                    {

                        list.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                    }
                    Keyword = query;
                }
                @Override
                public void onFailure(Call<SearchModel> call, Throwable t) {

                }
            });


    }


    private void getData()
    {
        isLoading = true;
        ApiUtilities.apiInterface().getImages(page , 30).enqueue(new Callback<List<ImageModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                if(response.body() != null)
                {
                    if(FromSearch)
                    {
                        list.clear();
                        FromSearch = false;
                    }
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                isLoading = false;
                dialog.dismiss();

                if(list.size() > 0 )
                {
                    isLastPage = list.size()  < pagesize;
                }
                else {
                    isLastPage =true;
                }
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Internet Failed", Toast.LENGTH_SHORT).show();
                binding.txRetry.setVisibility(View.VISIBLE);
                binding.btnretry.setVisibility(View.VISIBLE);
            }
        });
    }
}