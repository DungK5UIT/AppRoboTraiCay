package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.database.ChiTietDonHangDao;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.example.approbotraicay.model.ChiTietDonHang;
import com.example.approbotraicay.model.DonHang;
import com.example.approbotraicay.model.GioHang;
import com.example.approbotraicay.utils.SessionManager;
import com.example.approbotraicay.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThanhToanActivity extends AppCompatActivity {
    private TextInputEditText etTen, etSdt, etDiaChi;
    private android.widget.TextView tvTamTinh, tvTongTien;
    private MaterialButton btnXacNhan;
    private MaterialToolbar toolbar;
    private long tongTienCalculated = 0;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        initView();
        initData();
        setupEvents();
    }

    private void initView() {
        etTen = findViewById(R.id.et_thanh_toan_ten);
        etSdt = findViewById(R.id.et_thanh_toan_sdt);
        etDiaChi = findViewById(R.id.et_thanh_toan_diachi);
        tvTamTinh = findViewById(R.id.tv_thanh_toan_tamtinh);
        tvTongTien = findViewById(R.id.tv_thanh_toan_tongtien);
        btnXacNhan = findViewById(R.id.btn_thanh_toan_xacnhan);
        toolbar = findViewById(R.id.toolbar_thanh_toan);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private long phiShip = 30000; // Phí ship cố định - Dev B UI Logic

    private void initData() {
        sessionManager = new SessionManager(this);
        dbHelper = DatabaseHelper.getInstance(this);

        for (GioHang gh : Utils.manggiohang) {
            tongTienCalculated += (gh.getGiasp() * gh.getSoluong());
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTamTinh.setText(decimalFormat.format(tongTienCalculated) + "đ");
        
        long tongCuoi = tongTienCalculated + phiShip;
        tvTongTien.setText(decimalFormat.format(tongCuoi) + "đ");
        
        // Hiển thị phí ship trong UI (nếu có TextView tương ứng)
        android.widget.TextView tvPhiShip = findViewById(R.id.tv_thanh_toan_phiship);
        if (tvPhiShip != null) tvPhiShip.setText(decimalFormat.format(phiShip) + "đ");
    }

    private void setupEvents() {
        toolbar.setNavigationOnClickListener(v -> finish());

        btnXacNhan.setOnClickListener(v -> {
            String ten = etTen.getText().toString().trim();
            String sdt = etSdt.getText().toString().trim();
            String diaChi = etDiaChi.getText().toString().trim();

            if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            DonHang dh = new DonHang();
            dh.setUsername(sessionManager.getUserName());
            dh.setFullName(ten);
            dh.setPhone(sdt);
            dh.setAddress(diaChi);
            dh.setShippingFee(phiShip);
            dh.setTotal(tongTienCalculated + phiShip);
            dh.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
            dh.setStatus(DonHang.STATUS_PENDING);

            // Dev B: Chuyển toàn bộ danh sách chi tiết sang ChiTietDonHang list
            java.util.List<ChiTietDonHang> details = new java.util.ArrayList<>();
            for (GioHang gh : Utils.manggiohang) {
                ChiTietDonHang ctdh = new ChiTietDonHang();
                ctdh.setProductId(gh.getIdsp());
                ctdh.setProductName(gh.getTensp());
                ctdh.setQuantity(gh.getSoluong());
                ctdh.setPrice(gh.getGiasp());
                details.add(ctdh);
            }

            // Gọi logic transactional của Dev A
            boolean success = new DonHangDao(dbHelper).createOrderTransactionally(dh, details);
            
            if (success) {
                Utils.manggiohang.clear();
                showSuccessDialog();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi tạo đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đặt hàng thành công")
                .setMessage("Cảm ơn bạn đã mua hàng tại Robot Trai Cây!")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    Intent intent = new Intent(this, TrangChuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }
}
