package com.itcraftsolution.picturepoint.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.picturepoint.R;
import com.itcraftsolution.picturepoint.databinding.FragmentSavedBinding;


public class SavedFragment extends Fragment {


    public SavedFragment() {
        // Required empty public constructor
    }

    private FragmentSavedBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}