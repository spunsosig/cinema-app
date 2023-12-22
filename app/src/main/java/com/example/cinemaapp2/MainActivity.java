package com.example.cinemaapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cinemaapp2.api.ApiClient;
import com.example.cinemaapp2.api.ApiService;
import com.example.cinemaapp2.api.RequestMovie;
import com.example.cinemaapp2.api.RequestTVShow;
import com.example.cinemaapp2.databinding.ActivityMainBinding;
import com.example.cinemaapp2.models.Movie;
import com.example.cinemaapp2.models.MovieAdapter;
import com.example.cinemaapp2.models.MovieResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Query;


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
            } else if (item.getItemId() == R.id.navigation_notifications) {
                // Handle navigation to the notifications fragment
                navigateToFragment(R.id.navigation_notifications);
                return true;
            } else if(item.getItemId() == R.id.navigation_search){
                navigateToFragment(R.id.navigation_search);
                return true;
            }
            return false;
        });



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_notifications)
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

    public void goNotifications(View v) {
        // Handle navigation to the notifications fragment
        navigateToFragment(R.id.navigation_notifications);
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




}