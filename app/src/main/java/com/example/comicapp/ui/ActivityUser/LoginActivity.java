package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;
import com.example.comicapp.api.AuthApi;
import com.example.comicapp.dto.request.LoginRequest;
import com.example.comicapp.dto.response.LoginResponse;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.ui.ActivityAdmin.ManageComicsActivity;
import com.example.comicapp.ui.ActivityUser.HomeActivity;
import com.example.comicapp.utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnSignIn;
    TextView txtForgotPassword, txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtRegister);

        btnSignIn.setOnClickListener(v -> login());

        txtForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng đang cập nhật", Toast.LENGTH_SHORT).show()
        );

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthApi authApi = RetrofitClient.getInstance().create(AuthApi.class);
        LoginRequest request = new LoginRequest(username, password);

        authApi.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getToken();
                    SessionManager.saveToken(LoginActivity.this, token);

                    String role = getRoleFromToken(token);

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    if ("ADMIN".equals(role)) {
                        startActivity(new Intent(LoginActivity.this, ManageComicsActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }

                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Không kết nối được server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔐 Decode role từ JWT
    private String getRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            JSONObject json = new JSONObject(payload);
            return json.getString("role");
        } catch (Exception e) {
            return "USER";
        }
    }
}
