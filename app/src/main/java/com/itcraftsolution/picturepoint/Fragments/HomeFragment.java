package com.itcraftsolution.picturepoint.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Adapter.RecentRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;
import com.itcraftsolution.picturepoint.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;
    private ArrayList<ImageModel> list;
    private PopularHomeRecyclerAdapter adapter;
    private RecentRecyclerAdapter recentRecyclerAdapter;
    private ArrayList<ImageModel> resentList;
    private GridLayoutManager manager;
    private int page = 1;
    private final int pageSize = 80;
    private boolean isLoading ;
    private boolean isLastPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
//        list = new ArrayList<>();



//        adapter = new PopularHomeRecyclerAdapter(requireContext() , list);
//        binding.rvMostPopular.setLayoutManager(new LinearLayoutManager(requireContext()  , RecyclerView.HORIZONTAL , false));
//        binding.rvMostPopular.setHasFixedSize(true);
//        binding.rvMostPopular.setAdapter(adapter);
//        getSearchImage("most popular");

        resentList = new ArrayList<>();
        binding.smLayout.startShimmer();
        recentRecyclerAdapter = new RecentRecyclerAdapter(requireContext() , resentList);
        manager = new GridLayoutManager(requireContext() , 2);
        binding.rvRecent.setLayoutManager(manager);
        binding.rvRecent.setHasFixedSize(true);
        binding.rvRecent.setAdapter(recentRecyclerAdapter);
//        findPhotos();

        binding.rvRecent.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        findPhotos();
                    }
                }
            }
        });
        return binding.getRoot();
    }

    private void getSearchImage(String query)
    {
        ApiUtilities.apiInterface().SearchImages(query, page, pageSize).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                list.clear();
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    list.addAll(response.body().getPhotos());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

                Toast.makeText(requireContext(), "Not able to Find Images !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void findPhotos()
    {
        isLoading = true;
        ApiUtilities.apiInterface().getImages(page, pageSize).enqueue(new Callback<SearchModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    binding.smLayout.setVisibility(View.GONE);
                    binding.smLayout.stopShimmer();
                    binding.rvRecent.setVisibility(View.VISIBLE);
                    resentList.addAll(response.body().getPhotos());
                    recentRecyclerAdapter.notifyDataSetChanged();
                }
                isLoading = false;


                // last page check
                if(resentList.size() > 0)
                {
                    // if less than 80 images to page is last page
                    isLastPage = resentList.size() < pageSize;
                }else{
                    isLastPage = true;
                }
                if(resentList.isEmpty())
                {
                    binding.rvRecent.setVisibility(View.GONE);
                    binding.llNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

                Toast.makeText(requireContext(), "Not able to Find Images !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(resentList.isEmpty())
        {
            findPhotos();
        }
    }
}