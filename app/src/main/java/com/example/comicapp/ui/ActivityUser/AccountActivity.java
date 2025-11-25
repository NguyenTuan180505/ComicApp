// AccountActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import com.example.comicapp.R;

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

        // Đăng xuất
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            getSharedPreferences("auth", MODE_PRIVATE)
                    .edit()
                    .putBoolean("logged_in", false)
                    .remove("role")
                    .apply();
            Intent intent = new Intent(AccountActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
        });

        // Bottom Nav
        setupBottomNavigation(R.id.nav_account);
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_account;
    }
}
