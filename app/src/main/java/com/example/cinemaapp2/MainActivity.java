package com.example.cinemaapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.cinemaapp2.databinding.ActivityMainBinding;
import com.example.cinemaapp2.ui.Maps.MapsActivity2;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Handle navigation to the home fragment
                navigateToFragment(R.id.navigation_home);
                return true;
            } else if (item.getItemId() == R.id.navigation_dashboard) {
                // Handle navigation to the dashboard fragment
                navigateToFragment(R.id.navigation_dashboard);
                return true;
            } else if(item.getItemId() == R.id.navigation_search){
                navigateToFragment(R.id.navigation_search);
                return true;
            } else if(item.getItemId() == R.id.navigation_profile){
                navigateToFragment(R.id.navigation_profile);
            }
            else if (item.getItemId() == R.id.navigation_map){
                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
                startActivity(intent);
            }
            return false;
        });



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile)
                .build();

        // Find the NavController by locating the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_view);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Set up the ActionBar with NavController
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            Log.e("MainActivity", "NavHostFragment is null");
        }

    }

    public void goHome(View v) {
        // Handle navigation to the home fragment
        navigateToFragment(R.id.navigation_home);
    }

    public void goDashboard(View v) {
        // Handle navigation to the dashboard fragment
        navigateToFragment(R.id.navigation_dashboard);
    }

    public void goProfile(View v) {
        // Handle navigation to the profile fragment
        navigateToFragment(R.id.navigation_profile);
    }

    public void goToMap(View v){
        Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
        startActivity(intent);
    }

    private void navigateToFragment(int destinationId) {
        // Find the NavController by locating the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            navController.navigate(destinationId);
        } else {
            Log.e("MainActivity", "NavHostFragment is null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }
}