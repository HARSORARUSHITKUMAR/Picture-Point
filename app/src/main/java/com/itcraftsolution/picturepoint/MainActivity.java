package com.itcraftsolution.picturepoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiInterface;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;
import com.itcraftsolution.picturepoint.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<ImageModel> list;
    private GridLayoutManager manager;
    private ProgressDialog dialog;
    private Toolbar toolbar;
    private PopularHomeRecyclerAdapter adapter;
    private int page = 1;
    private int pagesize = 30;
    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tlMain);
        list = new ArrayList<>();
        adapter = new PopularHomeRecyclerAdapter(MainActivity.this  , list);
        manager = new GridLayoutManager(MainActivity.this , 2);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        dialog.show();

        binding.rvImage.setLayoutManager(manager);
        binding.rvImage.setHasFixedSize(true);
        binding.rvImage.setAdapter(adapter);

        getData();

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
                        getData();
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
               dialog.show();
               searchData(query);
               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
        return true;
    }

    private void searchData(String query) {
        dialog.dismiss();
        ApiUtilities.apiInterface().SearchImages(query).enqueue(new Callback<SearchModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                list.clear();
                list.addAll(response.body().getResults());

                adapter.notifyDataSetChanged();
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
                Toast.makeText(MainActivity.this, "Something went wrong!! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}