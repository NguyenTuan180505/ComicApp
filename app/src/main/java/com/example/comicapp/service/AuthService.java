package com.example.comicapp.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.comicapp.api.AuthApi;
import com.example.comicapp.dto.request.RegisterRequest;
import com.example.comicapp.dto.response.RegisterResponse;
import com.example.comicapp.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private final AuthApi authApi;
    private final Context context;

    public AuthService(Context context) {
        this.context = context;
        this.authApi = RetrofitClient.getInstance().create(AuthApi.class);
    }

    public void register(String username, String email, String password, final AuthCallback callback) {
        RegisterRequest request = new RegisterRequest(username, email, password);

        authApi.register(request).enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("RegisterError", "Code: " + response.code() + ", Error: " + errorBody);
                    callback.onError("Lỗi: " + response.code() + " - " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public interface AuthCallback {
        void onSuccess(RegisterResponse response);
        void onError(String message);
    }
}