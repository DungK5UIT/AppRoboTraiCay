package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.ui.auth.LoginActivity;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.UserDao;
import com.example.approbotraicay.model.TaiKhoan;
import com.example.approbotraicay.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvProfileUsername;
    private ImageView ivAvatar;
    private TextInputEditText etFullName, etPhone, etEmail, etAddress;
    private MaterialButton btnUpdate, btnChangePass, btnLogout;
    private MaterialToolbar toolbar;
    
    private SessionManager sessionManager;
    private UserDao userDao;
    private TaiKhoan currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initData();
        initView();
        loadUserProfile();
    }

    private void initData() {
        sessionManager = new SessionManager(this);
        userDao = new UserDao(new DatabaseHelper(this));
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

        // Implement Logic - Dev A
        btnUpdate.setOnClickListener(v -> updateProfile());
        
        btnLogout.setOnClickListener(v -> performLogout());
        
        btnChangePass.setOnClickListener(v -> showChangePasswordDialog());

        // Implement Logic - Dev B (Cloud Sync & Image)
        ivAvatar.setOnClickListener(v -> pickImage());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            android.net.Uri imageUri = data.getData();
            ivAvatar.setImageURI(imageUri);
            Toast.makeText(this, "Đã chọn ảnh đại diện!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChangePasswordDialog() {
        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        TextInputEditText etOldPass = view.findViewById(R.id.et_old_password);
        TextInputEditText etNewPass = view.findViewById(R.id.et_new_password);
        TextInputEditText etConfirmPass = view.findViewById(R.id.et_confirm_password);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String oldPass = etOldPass.getText().toString();
                    String newPass = etNewPass.getText().toString();
                    String confirmPass = etConfirmPass.getText().toString();

                    if (!oldPass.equals(currentUser.getPassword())) {
                        Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPass.length() < 6) {
                        Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPass.equals(confirmPass)) {
                        Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int result = userDao.updatePassword(currentUser.getUsername(), newPass);
                    if (result > 0) {
                        currentUser.setPassword(newPass);
                        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadUserProfile() {
        String username = sessionManager.getUserName();
        currentUser = userDao.getUserByUsername(username);

        if (currentUser != null) {
            tvProfileUsername.setText(currentUser.getUsername());
            etFullName.setText(currentUser.getFullName());
            etPhone.setText(currentUser.getPhone());
            etEmail.setText(currentUser.getEmail());
            etAddress.setText(currentUser.getAddress());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateProfile() {
        String name = etFullName.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setFullName(name);
        currentUser.setPhone(phone);
        currentUser.setEmail(email);
        currentUser.setAddress(address);

        int result = userDao.updateProfile(currentUser);
        if (result > 0) {
            Toast.makeText(this, "Cập nhật local thành công! Đang đồng bộ...", Toast.LENGTH_SHORT).show();
            syncProfileToCloud();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void syncProfileToCloud() {
        com.example.approbotraicay.api.ApiService apiService = com.example.approbotraicay.api.RetrofitClient.getClient().create(com.example.approbotraicay.api.ApiService.class);
        apiService.updateUser(currentUser.getUsername(), currentUser).enqueue(new retrofit2.Callback<TaiKhoan>() {
            @Override
            public void onResponse(retrofit2.Call<TaiKhoan> call, retrofit2.Response<TaiKhoan> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Đồng bộ Cloud thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Đồng bộ Cloud thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối Cloud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogout() {
        sessionManager.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
