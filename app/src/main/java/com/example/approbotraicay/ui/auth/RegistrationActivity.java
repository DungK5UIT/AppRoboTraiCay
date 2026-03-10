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
import com.example.approbotraicay.database.UserDao;
import com.example.approbotraicay.model.TaiKhoan;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etFullName, etEmail, etPhone;
    private Button btnRegister;
    private TextView tvGoToLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reborn_activity_register);

        userDao = new UserDao(new DatabaseHelper(this));

        initViews();
        setupEvents();
    }

    private void initViews() {
        etUsername = findViewById(R.id.reg_et_username);
        etPassword = findViewById(R.id.reg_et_password);
        etFullName = findViewById(R.id.reg_et_fullname);
        etEmail = findViewById(R.id.reg_et_email);
        etPhone = findViewById(R.id.reg_et_phone);
        btnRegister = findViewById(R.id.reg_btn_register);
        tvGoToLogin = findViewById(R.id.reg_tv_go_to_login);
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

        long result = userDao.insert(newUser);
        if (result > 0) {
            Toast.makeText(this, "Đăng ký thành công! Mời bạn đăng nhập.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi: Tài khoản đã tồn tại hoặc dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
