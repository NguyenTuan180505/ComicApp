// AccountActivity.java
package com.example.comicapp.ui.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import com.example.comicapp.R;
import com.example.comicapp.ui.Activity.BaseNavigationActivity;

public class AccountActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        // Nút lưu
        findViewById(R.id.btnSave).setOnClickListener(v ->
                Toast.makeText(this, "Đã lưu thông tin!", Toast.LENGTH_SHORT).show()
        );

        // Nút quay lại
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Bottom Nav
        setupBottomNavigation(R.id.nav_account);
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_account;
    }
}