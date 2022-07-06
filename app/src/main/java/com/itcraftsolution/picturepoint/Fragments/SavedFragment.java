package com.itcraftsolution.picturepoint.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.itcraftsolution.picturepoint.Adapter.PopularHomeRecyclerAdapter;
import com.itcraftsolution.picturepoint.Models.ImageModel;
import com.itcraftsolution.picturepoint.Models.UrlModel;
import com.itcraftsolution.picturepoint.databinding.FragmentSavedBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class SavedFragment extends Fragment {


    public SavedFragment() {
        // Required empty public constructor
    }

    private FragmentSavedBinding binding;
    private File PICTURE_POINT_DIR =new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "/PicturePoint/");
    private ArrayList<ImageModel> list;
    private PopularHomeRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(getLayoutInflater());

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        list = new ArrayList<>();

        if(PICTURE_POINT_DIR.exists())
        {
            getData(PICTURE_POINT_DIR);
            adapter = new PopularHomeRecyclerAdapter(requireContext() , list);
            binding.rvSaved.setLayoutManager(new GridLayoutManager(requireContext() , 2));
            binding.rvSaved.setHasFixedSize(true);
            binding.rvSaved.setAdapter(adapter);
        }else
        {
            binding.savedRefershView.setRefreshing(false);
            binding.rvSaved.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Can't Download Files Find!! ", Toast.LENGTH_SHORT).show();
        }

        binding.savedRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(list.isEmpty())
                {
                    binding.savedRefershView.setRefreshing(false);
                    binding.rvSaved.setVisibility(View.GONE);
                    binding.llNotFound.setVisibility(View.VISIBLE);
                }else{
                    list.clear();
                    getData(PICTURE_POINT_DIR);
                    adapter.notifyDataSetChanged();
                    binding.savedRefershView.setRefreshing(false);
                }
            }
        });
        if(list.isEmpty())
        {
            binding.rvSaved.setVisibility(View.GONE);
            binding.llNotFound.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }
    private void getData(File file)
    {

        ImageModel model;

        File[] allFiles =file.listFiles();

        Arrays.sort(allFiles, ((o1, o2) -> {
            if (o1.lastModified() > o2.lastModified()) {
                return -1;
            } else if (o1.lastModified() < o2.lastModified()) {
                return +1;
            } else {
                return 0;
            }
        }));

        for (int i = 0; i < allFiles.length; i++) {
            File singlefile = allFiles[i];

            if (Uri.fromFile(singlefile).toString().endsWith(".png") || Uri.fromFile(singlefile).toString().endsWith(".jpg")) {
                UrlModel urlModel = new UrlModel(allFiles[i].getAbsolutePath());
//                model = new ImageModel("whats " + i, allFiles[i].getAbsolutePath(), singlefile, Uri.fromFile(singlefile));
                model = new ImageModel(urlModel , "saved");

                list.add(model);
            }

        }
    }


}