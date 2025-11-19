// FollowActivity.java
package com.example.comicapp.ui.Activity;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.comicapp.R;
import com.example.comicapp.ui.Activity.BaseNavigationActivity;

public class FollowActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow);

        // KHÔNG SET padding bottom nữa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.followLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupBottomNavigation(R.id.nav_fav);
    }


    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_fav; // ĐÚNG!
    }
}