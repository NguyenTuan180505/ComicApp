package com.example.comicapp.ui.follow;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.example.comicapp.R;
import com.example.comicapp.ui.account.AccountActivity;

public class FollowActivity extends AppCompatActivity {
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

        // Bấm vào nút "Tài khoản" ở thanh dưới -> chuyển sang AccountActivity
        LinearLayout navAccount = findViewById(R.id.nav_account);
        navAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FollowActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

    }
}