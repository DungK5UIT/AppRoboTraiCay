package com.example.approbotraicay.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        userDao = new UserDao(DatabaseHelper.getInstance(this));
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

        btnUpdate.setOnClickListener(v -> updateProfile());
        btnLogout.setOnClickListener(v -> performLogout());
        btnChangePass.setOnClickListener(v -> showChangePasswordDialog());
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
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                ivAvatar.setImageBitmap(bitmap);
                Toast.makeText(this, "Đã chọn ảnh đại diện!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                ivAvatar.setImageURI(imageUri);
            }
        }
    }

    private void showChangePasswordDialog() {
        // FIX: kiểm tra currentUser trước khi mở dialog
        if (currentUser == null) {
            Toast.makeText(this, "Không thể tải thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        TextInputEditText etOldPass = view.findViewById(R.id.et_old_password);
        TextInputEditText etNewPass = view.findViewById(R.id.et_new_password);
        TextInputEditText etConfirmPass = view.findViewById(R.id.et_confirm_password);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String oldPass = etOldPass.getText() != null ? etOldPass.getText().toString() : "";
                    String newPass = etNewPass.getText() != null ? etNewPass.getText().toString() : "";
                    String confirmPass = etConfirmPass.getText() != null ? etConfirmPass.getText().toString() : "";

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
            etFullName.setText(currentUser.getFullName() != null ? currentUser.getFullName() : "");
            etPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            etEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");
            etAddress.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        } else {
            // Hiển thị username từ session, cho phép user điền thông tin
            tvProfileUsername.setText(username);
            Toast.makeText(this, "Hãy điền đầy đủ thông tin cá nhân!", Toast.LENGTH_LONG).show();
            // Tạo user stub để tránh crash khi update
            currentUser = new TaiKhoan();
            currentUser.setUsername(username);
        }
    }

    private void updateProfile() {
        // FIX: kiểm tra currentUser không null
        if (currentUser == null) {
            Toast.makeText(this, "Không thể cập nhật: tài khoản chưa tải được!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String address = etAddress.getText() != null ? etAddress.getText().toString().trim() : "";

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
            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            // Sync cloud trong nền, thất bại cũng không hiện lỗi gây rối
            syncProfileToCloudSilently();
        } else {
            Toast.makeText(this, "Cập nhật thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    /** Sync profile lên cloud mà không hiện lỗi gây phiền — FIX Bug F */
    private void syncProfileToCloudSilently() {
        try {
            com.example.approbotraicay.api.ApiService apiService =
                    com.example.approbotraicay.api.RetrofitClient.getClient()
                            .create(com.example.approbotraicay.api.ApiService.class);
            apiService.updateUser(currentUser.getUsername(), currentUser)
                    .enqueue(new retrofit2.Callback<TaiKhoan>() {
                        @Override
                        public void onResponse(retrofit2.Call<TaiKhoan> call,
                                               retrofit2.Response<TaiKhoan> response) {
                            // Success — không cần thông báo thêm
                        }
                        @Override
                        public void onFailure(retrofit2.Call<TaiKhoan> call, Throwable t) {
                            // Fail silently — không làm phiền user với lỗi cloud
                        }
                    });
        } catch (Exception e) {
            // Ignore — offline mode OK
        }
    }

    private void performLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    sessionManager.logout();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
