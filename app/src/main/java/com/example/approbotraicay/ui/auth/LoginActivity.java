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

        TaiKhoan user = userDao.login(username, password);
        if (user != null) {
            sessionManager.createLoginSession(user.getId(), user.getFullName(), user.getRole());
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }
}
