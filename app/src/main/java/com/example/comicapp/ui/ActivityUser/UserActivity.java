package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import com.example.comicapp.R;
import com.example.comicapp.api.UserApi;
import com.example.comicapp.data.model.User;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.ui.ActivityUser.LoginActivity;
import com.example.comicapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends BaseNavigationActivity {
    private EditText tvUsername;
    private EditText tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Khai báo views
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);

        // Nút quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // (Tùy chọn) Nhấn avatar để chọn ảnh
        findViewById(R.id.imgAvatar).setOnClickListener(v -> {
            Toast.makeText(this, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
        });

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        String rawToken = SessionManager.getToken(this);
        Log.d("UserAPI", "Raw token from SessionManager: " + (rawToken != null ? (rawToken.length() > 20 ? rawToken.substring(0, 20) + "..." : rawToken) : "NULL"));
        
        if (rawToken == null) {
            // Chưa đăng nhập, chuyển về màn hình đăng nhập
            Log.e("UserAPI", "Token is NULL - redirecting to LoginActivity");
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Đảm bảo token có prefix "Bearer "
        String authToken = rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;
        Log.d("UserAPI", "Calling endpoint: api/user/me");
        Log.d("UserAPI", "Auth token: " + (authToken.length() > 30 ? authToken.substring(0, 30) + "..." : authToken));
        
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);

        userApi.getCurrentUser(authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("UserAPI", "Response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Log.d("UserAPI", "User data received: username=" + user.getUsername() + 
                            ", email=" + user.getEmail() + 
                            ", id=" + user.getId());
                    
                    // Cập nhật giao diện với dữ liệu người dùng
                    if (tvUsername != null) {
                        tvUsername.setText(user.getUsername() != null ? user.getUsername() : "Chưa cập nhật");
                    }
                    if (tvEmail != null) {
                        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "Chưa cập nhật");
                    }
                } else {
                    // Xử lý lỗi
                    String errorMsg = "Không thể tải thông tin người dùng (code " + response.code() + ")";
                    
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("UserAPI", "Error body: " + errorBody);
                            errorMsg += "\n" + errorBody;
                        } catch (Exception e) {
                            Log.e("UserAPI", "Error reading error body", e);
                        }
                    }
                    
                    if (response.code() == 403) {
                        // Token hết hạn hoặc không hợp lệ, hoặc endpoint sai
                        Log.e("UserAPI", "403 Forbidden - có thể do token hết hạn hoặc endpoint sai");
                        Toast.makeText(UserActivity.this, "Không thể tải thông tin (403). Vui lòng kiểm tra lại token hoặc endpoint", Toast.LENGTH_LONG).show();
                        // Tạm thời KHÔNG tự động logout để debug
                        // SessionManager.logout(UserActivity.this);
                        // startActivity(new Intent(UserActivity.this, LoginActivity.class));
                        // finish();
                    } else {
                        Toast.makeText(UserActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserAPI", "Request failed", t);
                Toast.makeText(UserActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_account; // Vẫn giữ highlight tab Tài khoản
    }
}