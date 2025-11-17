package com.example.comicapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;
import com.example.comicapp.ui.Activity.AccountActivity;
import com.example.comicapp.ui.Activity.FollowActivity;
import com.example.comicapp.ui.Activity.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseNavigationActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottomNavigation(@IdRes int currentItemId) {
        bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav == null) return;

        // Đánh dấu tab hiện tại
        bottomNav.setSelectedItemId(currentItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == currentItemId) return true;

            Class<?> targetActivity = null;
            if (id == R.id.nav_home) targetActivity = HomeActivity.class;
            else if (id == R.id.nav_tasks) targetActivity = TaskActivity.class;
            else if (id == R.id.nav_fav) targetActivity = FollowActivity.class;
            else if (id == R.id.nav_account) targetActivity = AccountActivity.class;

            if (targetActivity != null) {
                Intent intent = new Intent(this, targetActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav != null) {
            // Tự động đánh dấu lại tab hiện tại
            bottomNav.setSelectedItemId(getCurrentNavItemId());
        }
    }

    // Mỗi Activity con phải override để trả về ID tab của mình
    protected abstract @IdRes int getCurrentNavItemId();
}
