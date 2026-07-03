package com.example.approbotraicay.ui.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.ChiTietDonHangAdapter;
import com.example.approbotraicay.database.ChiTietDonHangDao;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.example.approbotraicay.model.ChiTietDonHang;
import com.example.approbotraicay.model.DonHang;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import java.util.List;

public class AdminChiTietDonHangActivity extends AppCompatActivity {
    private TextView tvTen, tvSdt, tvDiaChi, tvTongTien, tvStatus, tvPhiShip;
    private RecyclerView rvChiTiet;
    private MaterialToolbar toolbar;
    private MaterialButton btnUpdateStatus;
    private DonHang dh;
    private DonHangDao donHangDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chi_tiet_don_hang);

        donHangDao = new DonHangDao(DatabaseHelper.getInstance(this));
        
        initView();
        initData();
        setupEvents();
    }

    private void initView() {
        tvTen = findViewById(R.id.tv_admin_ctdh_ten);
        tvSdt = findViewById(R.id.tv_admin_ctdh_sdt);
        tvDiaChi = findViewById(R.id.tv_admin_ctdh_diachi);
        tvTongTien = findViewById(R.id.tv_admin_ctdh_tongtien);
        tvStatus = findViewById(R.id.tv_admin_ctdh_status);
        tvPhiShip = findViewById(R.id.tv_admin_ctdh_phiship);
        btnUpdateStatus = findViewById(R.id.btn_admin_ctdh_update_status);
        rvChiTiet = findViewById(R.id.rv_admin_chi_tiet_don_hang);
        toolbar = findViewById(R.id.toolbar_admin_chi_tiet_don_hang);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        rvChiTiet.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        dh = (DonHang) getIntent().getSerializableExtra("donhang");
        if (dh != null) {
            tvTen.setText(dh.getFullName());
            tvSdt.setText(dh.getPhone());
            tvDiaChi.setText(dh.getAddress());
            
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvTongTien.setText(decimalFormat.format(dh.getTotal()) + "đ");
            tvPhiShip.setText(decimalFormat.format(dh.getShippingFee()) + "đ");

            updateStatusUI();

            ChiTietDonHangDao dao = new ChiTietDonHangDao(DatabaseHelper.getInstance(this));
            List<ChiTietDonHang> details = dao.getByOrderId(dh.getId());
            rvChiTiet.setAdapter(new ChiTietDonHangAdapter(details));
        }
    }

    private void updateStatusUI() {
        String statusText;
        int colorRes;
        
        switch (dh.getStatus()) {
            case DonHang.STATUS_PROCESSING:
                statusText = "Đang xử lý";
                colorRes = android.graphics.Color.BLUE;
                break;
            case DonHang.STATUS_SHIPPING:
                statusText = "Đang giao hàng";
                colorRes = android.graphics.Color.CYAN;
                break;
            case DonHang.STATUS_COMPLETED:
                statusText = "Đã giao thành công";
                colorRes = android.graphics.Color.GREEN;
                break;
            case DonHang.STATUS_CANCELLED:
                statusText = "Đã hủy";
                colorRes = android.graphics.Color.RED;
                break;
            default:
                statusText = "Chờ xác nhận";
                colorRes = android.graphics.Color.parseColor("#FF9800");
                break;
        }
        
        tvStatus.setText(statusText);
        
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setColor(colorRes);
        bg.setCornerRadius(40f);
        tvStatus.setBackground(bg);
        tvStatus.setTextColor(android.graphics.Color.WHITE);
        tvStatus.setPadding(
                (int)(12 * getResources().getDisplayMetrics().density), 4,
                (int)(12 * getResources().getDisplayMetrics().density), 4);
    }

    private void setupEvents() {
        btnUpdateStatus.setOnClickListener(v -> {
            String[] statuses = {"Chờ xác nhận", "Đang xử lý", "Đang giao hàng", "Đã giao thành công", "Đã hủy"};
            new AlertDialog.Builder(this)
                    .setTitle("Cập nhật trạng thái đơn hàng #" + dh.getId())
                    .setItems(statuses, (dialog, which) -> {
                        int newStatus;
                        switch (which) {
                            case 1: newStatus = DonHang.STATUS_PROCESSING; break;
                            case 2: newStatus = DonHang.STATUS_SHIPPING; break;
                            case 3: newStatus = DonHang.STATUS_COMPLETED; break;
                            case 4: newStatus = DonHang.STATUS_CANCELLED; break;
                            default: newStatus = DonHang.STATUS_PENDING; break;
                        }
                        
                        int result = donHangDao.updateStatus(dh.getId(), newStatus);
                        if (result > 0) {
                            dh.setStatus(newStatus);
                            updateStatusUI();
                            Toast.makeText(this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });
    }
}
