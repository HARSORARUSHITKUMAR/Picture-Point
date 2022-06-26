package com.itcraftsolution.picturepoint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Adapter.RecentRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;
import com.itcraftsolution.picturepoint.Utils.NetworkChangeListner;
import com.itcraftsolution.picturepoint.databinding.ActivityCatImageBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatImageActivity extends AppCompatActivity {

    private ActivityCatImageBinding binding;
    private ArrayList<ImageModel> list;
    private RecentRecyclerAdapter adapter;
    private GridLayoutManager manager;
    private String catName, catImage;
    private ProgressDialog dialog;
    private final NetworkChangeListner networkChangeListner = new NetworkChangeListner();
    private int page = 1;
    private int pageSize = 80;
    private boolean isLoading ;
    private boolean isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        dialog = new ProgressDialog(CatImageActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();


        loadData();
        //call SearchData
        searchData(catName);
        list = new ArrayList<>();
        adapter = new RecentRecyclerAdapter(this, list);
        manager = new GridLayoutManager(this, 2);
        binding.rvCatImage.setLayoutManager(manager);
        binding.rvCatImage.setHasFixedSize(true);
        binding.rvCatImage.setAdapter(adapter);


        binding.rvCatImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if((visibleItem + firstVisibleItemPos >= totalItem) && firstVisibleItemPos >= 0 && totalItem >= pageSize)
                    {
                        page++;
                       searchData(catName);
                    }
                }
            }
        });
    }

    private void loadData()
    {
        catName = getIntent().getStringExtra("CategoryName");
        catImage = getIntent().getStringExtra("CategoryImage");
        Glide.with(CatImageActivity.this).load(catImage).into(binding.igCatImage);
        binding.txCatName.setText(catName);
    }

    private void searchData(String query) {
        isLoading = true;
        ApiUtilities.apiInterface().SearchImages(query, page, pageSize).enqueue(new Callback<SearchModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if (response.body() != null) {
                    list.addAll(response.body().getPhotos());
                    adapter.notifyDataSetChanged();
                }
                isLoading = false;
                dialog.dismiss();

                if(list.size() > 0)
                {
                    isLastPage = list.size() < pageSize;
                }else{
                    isLastPage = true;
                }
                if(list.isEmpty())
                {
                    binding.rvCatImage.setVisibility(View.GONE);
                    binding.llNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CatImageActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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