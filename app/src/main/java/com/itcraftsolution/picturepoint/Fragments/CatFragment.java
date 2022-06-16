package com.itcraftsolution.picturepoint.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.FragmentCatBinding;


public class CatFragment extends Fragment {

    public CatFragment() {
        // Required empty public constructor
    }


    private FragmentCatBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }
}