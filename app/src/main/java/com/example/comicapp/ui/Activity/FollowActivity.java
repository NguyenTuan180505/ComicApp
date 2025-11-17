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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.followLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // CHỈ CẦN 1 DÒNG NÀY → Bottom Nav tự xử lý
        setupBottomNavigation(R.id.nav_fav);
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_fav; // ĐÚNG!
    }
}