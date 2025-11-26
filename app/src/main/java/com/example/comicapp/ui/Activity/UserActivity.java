package com.example.comicapp.ui.Activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import com.example.comicapp.R;

public class UserActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút lưu
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            // TODO: Gọi API cập nhật thông tin ở đây
            Toast.makeText(this, "Đã lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // (Tùy chọn) Nhấn avatar để chọn ảnh
        findViewById(R.id.imgAvatar).setOnClickListener(v -> {
            Toast.makeText(this, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_account; // Vẫn giữ highlight tab Tài khoản
    }
}