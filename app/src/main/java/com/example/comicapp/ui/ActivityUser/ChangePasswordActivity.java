package com.example.comicapp.ui.ActivityUser;


import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;
import com.example.comicapp.api.UserApi;
import com.example.comicapp.data.model.User;
import com.example.comicapp.dto.request.ChangePasswordRequest;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmPassword;
    MaterialButton btnChangePassword;
    ImageButton btnBack;

    ImageView ivCheck1, ivCheck2, ivCheck3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
        setupListener();
    }

    private void initView() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        tilCurrentPassword = findViewById(R.id.tilCurrentPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);

        ivCheck1 = findViewById(R.id.ivCheck1);
        ivCheck2 = findViewById(R.id.ivCheck2);
        ivCheck3 = findViewById(R.id.ivCheck3);
    }

    private void setupListener() {

        btnBack.setOnClickListener(v -> finish());

        btnChangePassword.setOnClickListener(v -> validateAndCallApi());
    }

    private void validateAndCallApi() {
        String oldPass = etCurrentPassword.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        clearError();

        if (oldPass.isEmpty()) {
            tilCurrentPassword.setError("Vui lòng nhập mật khẩu cũ");
            return;
        }

        if (newPass.length() < 8) {
            tilNewPassword.setError("Mật khẩu tối thiểu 8 ký tự");
            return;
        }

        if (!newPass.matches(".*\\d.*")) {
            tilNewPassword.setError("Mật khẩu phải chứa ít nhất 1 số");
            return;
        }

        if (!newPass.matches(".*[@#$%!^&*()].*")) {
            tilNewPassword.setError("Mật khẩu phải chứa ký tự đặc biệt");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        callChangePasswordApi(oldPass, newPass);
    }

    private void callChangePasswordApi(String oldPass, String newPass) {

        String token = "Bearer " + SessionManager.getToken(this);

        UserApi api = RetrofitClient
                .getInstance()
                .create(UserApi.class);

        ChangePasswordRequest request =
                new ChangePasswordRequest(oldPass, newPass);

        btnChangePassword.setEnabled(false);

        api.changePassword(token, request)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        btnChangePassword.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(
                                    ChangePasswordActivity.this,
                                    "Đổi mật khẩu thành công",
                                    Toast.LENGTH_LONG
                            ).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        btnChangePassword.setEnabled(true);
                        Toast.makeText(
                                ChangePasswordActivity.this,
                                "Lỗi server",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

    }

    private void clearError() {
        tilCurrentPassword.setError(null);
        tilNewPassword.setError(null);
        tilConfirmPassword.setError(null);
    }
}
