package com.example.approbotraicay.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.UserDao;
import com.example.approbotraicay.model.TaiKhoan;
import com.example.approbotraicay.utils.SessionManager;
import com.example.approbotraicay.api.ApiService;
import com.example.approbotraicay.api.RetrofitClient;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvGoToRegister;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userDao = new UserDao(dbHelper);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Chào mừng quay trở lại, " + sessionManager.getUserName(), Toast.LENGTH_SHORT).show();
            finish();
        }

        initViews();
        setupEvents();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvGoToRegister = findViewById(R.id.tv_go_to_register);
    }

    private void setupEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getUsers().enqueue(new Callback<Map<String, TaiKhoan>>() {
            @Override
            public void onResponse(Call<Map<String, TaiKhoan>> call, Response<Map<String, TaiKhoan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean found = false;
                    for (Map.Entry<String, TaiKhoan> entry : response.body().entrySet()) {
                        TaiKhoan u = entry.getValue();
                        if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                            sessionManager.createLoginSession(u.getId(), u.getFullName(), u.getRole());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi truy xuất dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, TaiKhoan>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
