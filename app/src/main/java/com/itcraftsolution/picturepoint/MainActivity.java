package com.itcraftsolution.picturepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.itcraftsolution.picturepoint.Fragments.CatFragment;
import com.itcraftsolution.picturepoint.Fragments.HomeFragment;
import com.itcraftsolution.picturepoint.Fragments.SavedFragment;
import com.itcraftsolution.picturepoint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int page = 7;
    private int pagesize = 30;
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tlMain);

        getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, new HomeFragment()).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                    switch (item.getItemId())
                    {
                        case R.id.itMenuHome:
                                temp = new HomeFragment();
                                break;

                        case R.id.itMenuCat:
                            temp = new CatFragment();
                            break;


                        case R.id.itMenuDownload:
                           temp = new SavedFragment();
                           break;


                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frMainContainer, temp).commit();
                 return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.topmenu , menu);
        MenuItem menuItem = menu.findItem(R.id.itMenuSearch);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here To Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String imageUrl = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?cs=srgb&dl=pexels-sohail-nachiti-807598.jpg&fm=jpg";
                Intent intent = new Intent(MainActivity.this , CatImageActivity.class);
                intent.putExtra("CategoryName" , query);
                intent.putExtra("CategoryImage" , imageUrl);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


}