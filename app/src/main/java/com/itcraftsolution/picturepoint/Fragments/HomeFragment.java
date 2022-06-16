package com.itcraftsolution.picturepoint.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Adapter.RecentRecyclerAdapter;
import com.itcraftsolution.picturepoint.Api.ApiUtilities;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.SearchModel;
import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.FragmentHomeBinding;

import java.util.ArrayList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        list = new ArrayList<>();
        adapter = new PopularHomeRecyclerAdapter(requireContext() , list);
        binding.rvMostPopular.setLayoutManager(new LinearLayoutManager(requireContext()  , RecyclerView.HORIZONTAL , false));
        binding.rvMostPopular.setHasFixedSize(true);
        binding.rvMostPopular.setAdapter(adapter);
        getSearchImage("most popular");

        resentList = new ArrayList<>();
        recentRecyclerAdapter = new RecentRecyclerAdapter(requireContext() , resentList);
        binding.rvRecent.setLayoutManager(new GridLayoutManager(requireContext() , 2));
        binding.rvRecent.setHasFixedSize(true);
        binding.rvRecent.setAdapter(recentRecyclerAdapter);
        findPhotos();

        return binding.getRoot();
    }

    private void getSearchImage(String query)
    {
        ApiUtilities.apiInterface().SearchImages(query, 1, 80).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                list.clear();
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    list.addAll(response.body().getPhotos());
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(requireContext(), "Not able to Find Images !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

            }
        });

    }

    private void findPhotos()
    {
        ApiUtilities.apiInterface().getImages(1, 80).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if(response.isSuccessful())
                {
                    assert response.body() != null;
                    resentList.addAll(response.body().getPhotos());
                    recentRecyclerAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(requireContext(), "Not able to Find Images !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

            }
        });
    }
}