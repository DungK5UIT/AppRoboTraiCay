package com.example.approbotraicay.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * ProfileActivity - Created by Dev C (UI/Layout)
 * This activity handles the user profile interface.
 * Functionality (Update, Logout, Firebase sync) will be implemented by Dev A/B.
 */
public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvProfileUsername;
    private ImageView ivAvatar;
    private TextInputEditText etFullName, etPhone, etEmail, etAddress;
    private MaterialButton btnUpdate, btnChangePass, btnLogout;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_profile);
        tvProfileUsername = findViewById(R.id.tv_profile_username);
        ivAvatar = findViewById(R.id.iv_profile_avatar);
        etFullName = findViewById(R.id.et_profile_fullname);
        etPhone = findViewById(R.id.et_profile_phone);
        etEmail = findViewById(R.id.et_profile_email);
        etAddress = findViewById(R.id.et_profile_address);
        btnUpdate = findViewById(R.id.btn_profile_update);
        btnChangePass = findViewById(R.id.btn_profile_change_pass);
        btnLogout = findViewById(R.id.btn_profile_logout);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI Interactions (Placeholders for Dev A/B)
        btnUpdate.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng Cập nhật (Hành động của Dev A)", Toast.LENGTH_SHORT).show();
        });
        
        btnChangePass.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng Đổi mật khẩu (Hành động của Dev A)", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng Đăng xuất (Hành động của Dev B)", Toast.LENGTH_SHORT).show();
        });
    }
}
