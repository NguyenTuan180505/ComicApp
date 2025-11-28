// AccountActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.example.comicapp.R;
import com.example.comicapp.ui.ActivityUser.BaseNavigationActivity;

public class AccountActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        // Highlight đúng tab "Tài khoản" trên Bottom Navigation
        setupBottomNavigation(R.id.nav_account);

        // === XỬ LÝ CLICK CÁC MỤC TRONG MENU ===
        findViewById(R.id.item_update_info).setOnClickListener(v -> {
            // Chuyển sang trang cập nhật thông tin cá nhân
            startActivity(new Intent(this, UserActivity.class));
        });

        findViewById(R.id.item_logout).setOnClickListener(v -> {
            // TODO: Xóa token, SharedPreferences, FirebaseAuth.logout() ở đây nếu có
            Toast.makeText(this, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            // Quay về màn hình Login hoặc Home (tùy bạn)
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Các mục khác (có thể thêm sau)
        findViewById(R.id.item_follow_list).setOnClickListener(v ->
                Toast.makeText(this, "Danh sách theo dõi", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_nap_xu).setOnClickListener(v ->
                Toast.makeText(this, "Chức năng nạp xu", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_lich_su).setOnClickListener(v ->
                Toast.makeText(this, "Lịch sử giao dịch", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_change_password).setOnClickListener(v ->
                Toast.makeText(this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_contact_admin).setOnClickListener(v ->
                Toast.makeText(this, "Liên hệ admin", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_rate_app).setOnClickListener(v ->
                Toast.makeText(this, "Đánh giá ứng dụng", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_share_app).setOnClickListener(v ->
                Toast.makeText(this, "Chia sẻ ứng dụng", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_account;
    }
}