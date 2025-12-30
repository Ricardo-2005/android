package com.example.foodcalu.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodcalu.R;
import com.example.foodcalu.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.isLoggedIn(this)) {
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            switchFragment(item.getItemId());
            return true;
        });

        if (savedInstanceState == null) {
            switchFragment(R.id.menu_home);
            bottomNav.setSelectedItemId(R.id.menu_home);
        }
    }

    private void switchFragment(int itemId) {
        Fragment fragment;
        if (itemId == R.id.menu_food) {
            fragment = new FoodFragment();
        } else if (itemId == R.id.menu_workout) {
            fragment = new WorkoutFragment();
        } else if (itemId == R.id.menu_profile) {
            fragment = new ProfileFragment();
        } else {
            fragment = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
