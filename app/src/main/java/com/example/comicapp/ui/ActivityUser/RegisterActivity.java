package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;
import com.example.comicapp.api.AuthApi;
import com.example.comicapp.dto.request.RegisterRequest;
import com.example.comicapp.dto.response.RegisterResponse;
import com.example.comicapp.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    MaterialButton btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(v -> performRegister());
    }

    private void performRegister() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString();
        String confirm = edtConfirmPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Không cho phép username chứa ký tự có dấu (chỉ cho phép ký tự ASCII)
        // Bạn có thể chỉnh lại regex nếu muốn giới hạn cụ thể hơn (vd: chỉ chữ, số, gạch dưới)
        if (!username.matches("\\A\\p{ASCII}*\\z")) {
            Toast.makeText(this, "Tên đăng nhập không được chứa dấu hoặc ký tự đặc biệt tiếng Việt", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthApi authApi = RetrofitClient.getInstance().create(AuthApi.class);
        RegisterRequest request = new RegisterRequest(username, email, pass);

        authApi.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("RegisterAPI", "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse res = response.body();
                    String msg = res.getMessage() != null ? res.getMessage() : "Đăng ký thành công!";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    String msg = "Đăng ký thất bại (code " + response.code() + ")";
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                    Log.e("RegisterAPI", msg);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("RegisterAPI", "Request failed", t);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
