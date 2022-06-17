package com.itcraftsolution.picturepoint.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.picturepoint.Adapter.HomeCategoryRecyclerAdapter;
import com.itcraftsolution.picturepoint.Models.CategoryModel;
import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.FragmentCatBinding;

import java.util.ArrayList;


public class CatFragment extends Fragment {

    public CatFragment() {
        // Required empty public constructor
    }


    private FragmentCatBinding binding;
    private HomeCategoryRecyclerAdapter adapter;
    private ArrayList<CategoryModel> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatBinding.inflate(getLayoutInflater());

        list = new ArrayList<>();

        list.add(new CategoryModel("https://images.pexels.com/photos/5011647/pexels-photo-5011647.jpeg?cs=srgb&dl=pexels-uzunov-rostislav-5011647.jpg&fm=jpg" , "3D"));
        list.add(new CategoryModel("https://images.pexels.com/photos/1910225/pexels-photo-1910225.jpeg?cs=srgb&dl=pexels-bruno-thethe-1910225.jpg&fm=jpg" , "Abstract"));
        list.add(new CategoryModel("https://images.pexels.com/photos/792381/pexels-photo-792381.jpeg?cs=srgb&dl=pexels-george-desipris-792381.jpg&fm=jpg" , "Animal"));
        list.add(new CategoryModel("https://images.pexels.com/photos/1266808/pexels-photo-1266808.jpeg?cs=srgb&dl=pexels-steve-johnson-1266808.jpg&fm=jpg" , "Artistic"));
        list.add(new CategoryModel("https://images.pexels.com/photos/349758/hummingbird-bird-birds-349758.jpeg?cs=srgb&dl=pexels-pixabay-349758.jpg&fm=jpg" , "Bird"));
        list.add(new CategoryModel("https://images.pexels.com/photos/374132/pexels-photo-374132.jpeg?cs=srgb&dl=pexels-burst-374132.jpg&fm=jpg" , "City & Building"));
        list.add(new CategoryModel("https://images.pexels.com/photos/191240/pexels-photo-191240.jpeg?cs=srgb&dl=pexels-fernando-arcos-191240.jpg&fm=jpg" , "Classic"));
        list.add(new CategoryModel("https://images.pexels.com/photos/50996/colour-pencils-color-paint-draw-50996.jpeg?cs=srgb&dl=pexels-pixabay-50996.jpg&fm=jpg" , "ColourFul"));
        list.add(new CategoryModel("https://images.pexels.com/photos/4406662/pexels-photo-4406662.jpeg?cs=srgb&dl=pexels-eberhard-grossgasteiger-4406662.jpg&fm=jpg" , "Fantasy"));
        list.add(new CategoryModel("https://images.pexels.com/photos/954126/pexels-photo-954126.jpeg?cs=srgb&dl=pexels-hi%E1%BA%BFu-ho%C3%A0ng-954126.jpg&fm=jpg" , "Flower"));
        list.add(new CategoryModel("https://images.pexels.com/photos/4226728/pexels-photo-4226728.jpeg?cs=srgb&dl=pexels-karolina-grabowska-4226728.jpg&fm=jpg" , "Love & Hearts"));
        list.add(new CategoryModel("https://images.pexels.com/photos/1172207/pexels-photo-1172207.jpeg?cs=srgb&dl=pexels-oleksandr-pidvalnyi-1172207.jpg&fm=jpg" , "Men"));
        list.add(new CategoryModel("https://images.pexels.com/photos/9962458/pexels-photo-9962458.jpeg?cs=srgb&dl=pexels-%D0%B5%D0%BA%D0%B0%D1%82%D0%B5%D1%80%D0%B8%D0%BD%D0%B0-%D0%B8%D0%B2%D0%B0%D0%BD%D0%BE%D0%B2%D0%B0-9962458.jpg&fm=jpg" , "Nature"));
        list.add(new CategoryModel("https://images.pexels.com/photos/1888883/pexels-photo-1888883.jpeg?cs=srgb&dl=pexels-zachary-debottis-1888883.jpg&fm=jpg" , "Patterns & Textures"));
        list.add(new CategoryModel("https://images.pexels.com/photos/3309775/pexels-photo-3309775.jpeg?cs=srgb&dl=pexels-el%C4%ABna-ar%C4%81ja-3309775.jpg&fm=jpg" , "Quotes"));
        list.add(new CategoryModel("https://images.pexels.com/photos/1142950/pexels-photo-1142950.jpeg?cs=srgb&dl=pexels-jacub-gomez-1142950.jpg&fm=jpg" , "Space"));
        list.add(new CategoryModel("https://images.pexels.com/photos/2582937/pexels-photo-2582937.jpeg?cs=srgb&dl=pexels-athena-2582937.jpg&fm=jpg" , "Technology"));
        list.add(new CategoryModel("https://images.pexels.com/photos/4275885/pexels-photo-4275885.jpeg?cs=srgb&dl=pexels-mateusz-sa%C5%82aciak-4275885.jpg&fm=jpg" , "Travel"));
        list.add(new CategoryModel("https://images.pexels.com/photos/1038001/pexels-photo-1038001.jpeg?cs=srgb&dl=pexels-moose-photos-1038001.jpg&fm=jpg" , "Vector"));
        list.add(new CategoryModel("https://images.pexels.com/photos/167699/pexels-photo-167699.jpeg?cs=srgb&dl=pexels-lumn-167699.jpg&fm=jpg" , "Winter"));


        adapter = new HomeCategoryRecyclerAdapter(requireContext() , list);
        binding.rvCat.setAdapter(adapter);
        binding.rvCat.setHasFixedSize(true);
        binding.rvCat.setLayoutManager(new LinearLayoutManager(requireContext()));

        return binding.getRoot();
    }
}