// AccountActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.example.comicapp.R;
import com.example.comicapp.api.UserApi;
import com.example.comicapp.data.model.User;
import com.example.comicapp.dto.response.RegisterResponse;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.service.AuthService;
import com.example.comicapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.comicapp.ui.ActivityUser.BaseNavigationActivity;

public class AccountActivity extends BaseNavigationActivity {

    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        
        // Load user data
        loadUserData();

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
        findViewById(R.id.item_follow_list).setOnClickListener(v -> {
            // Mở trang danh sách theo dõi (yêu thích)
            startActivity(new Intent(this, FollowActivity.class));
        });

        findViewById(R.id.item_nap_xu).setOnClickListener(v ->
                Toast.makeText(this, "Chức năng nạp xu", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_lich_su).setOnClickListener(v ->
                Toast.makeText(this, "Lịch sử giao dịch", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_change_password).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.item_contact_admin).setOnClickListener(v ->
                Toast.makeText(this, "Liên hệ admin", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_rate_app).setOnClickListener(v ->
                Toast.makeText(this, "Đánh giá ứng dụng", Toast.LENGTH_SHORT).show());

        findViewById(R.id.item_share_app).setOnClickListener(v ->
                Toast.makeText(this, "Chia sẻ ứng dụng", Toast.LENGTH_SHORT).show());
    }
    private void handleRegister(String username, String email, String password) {
        AuthService authService = new AuthService(this);
        authService.register(username, email, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(RegisterResponse response) {
                runOnUiThread(() -> {
                    Toast.makeText(AccountActivity.this,
                            "Đăng ký thành công: " + response.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    // Chuyển đến màn hình đăng nhập
                });
            }
            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(AccountActivity.this, message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_account;
    }

    private void loadUserData() {
        String rawToken = SessionManager.getToken(this);
        
        if (rawToken == null) {
            return; // No token available
        }

        // Ensure token has "Bearer " prefix
        String authToken = rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;
        
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);

        userApi.getCurrentUser(authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // Update UI on main thread
                    runOnUiThread(() -> {
                        tvName.setText(user.getUsername());
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("AccountActivity", "Failed to load user data", t);
            }
        });
    }
}