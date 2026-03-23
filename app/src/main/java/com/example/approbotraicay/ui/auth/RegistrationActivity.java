package com.example.approbotraicay.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.model.TaiKhoan;
import com.example.approbotraicay.api.ApiService;
import com.example.approbotraicay.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etFullName, etEmail, etPhone;
    private Button btnRegister;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupEvents();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etFullName = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnRegister = findViewById(R.id.btn_register);
        tvGoToLogin = findViewById(R.id.tv_go_to_login);
    }

    private void setupEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tên đăng nhập và mật khẩu là bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        TaiKhoan newUser = new TaiKhoan();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setRole(0);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.postUser(newUser).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Đăng ký thành công! Mời bạn đăng nhập.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
