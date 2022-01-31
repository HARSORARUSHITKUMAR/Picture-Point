package com.itcraftsolution.picturepoint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;
import com.itcraftsolution.picturepoint.databinding.ActivityCatImageBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatImageActivity extends AppCompatActivity {

    private ActivityCatImageBinding binding;
    private ArrayList<ImageModel> list;
    private PopularHomeRecyclerAdapter adapter;
    private GridLayoutManager manager;
    private int page = 4;
    private int pagesize = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchData(getIntent().getStringExtra("CategoryName"));
        list = new ArrayList<>();
        adapter = new PopularHomeRecyclerAdapter(this  , list);
        manager = new GridLayoutManager(this , 2);
        binding.rvCatImage.setLayoutManager(manager);
        binding.rvCatImage.setHasFixedSize(true);
        binding.rvCatImage.setAdapter(adapter);
    }

    private void searchData(String query) {
        ApiUtilities.apiInterface().SearchImages(query, page, pagesize).enqueue(new Callback<SearchModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if(response.body() != null)
                {
                    list.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(CatImageActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Toast.makeText(CatImageActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}