package com.example.approbotraicay.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.ChiTietDonHangAdapter;
import com.example.approbotraicay.database.ChiTietDonHangDao;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.model.ChiTietDonHang;
import com.example.approbotraicay.model.DonHang;
import com.google.android.material.appbar.MaterialToolbar;
import java.text.DecimalFormat;
import java.util.List;

public class ChiTietDonHangActivity extends AppCompatActivity {
    private TextView tvTen, tvSdt, tvDiaChi, tvTongTien, tvStatus, tvPhiShip;
    private RecyclerView rvChiTiet;
    private MaterialToolbar toolbar;
    private com.google.android.material.button.MaterialButton btnCancel;
    private DonHang dh;
    private com.example.approbotraicay.database.DonHangDao donHangDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);

        donHangDao = new com.example.approbotraicay.database.DonHangDao(new DatabaseHelper(this));
        
        initView();
        initData();
        setupEvents();
    }

    private void initView() {
        tvTen = findViewById(R.id.tv_ctdh_ten);
        tvSdt = findViewById(R.id.tv_ctdh_sdt);
        tvDiaChi = findViewById(R.id.tv_ctdh_diachi);
        tvTongTien = findViewById(R.id.tv_ctdh_tongtien);
        tvStatus = findViewById(R.id.tv_ctdh_status);
        tvPhiShip = findViewById(R.id.tv_ctdh_phiship);
        btnCancel = findViewById(R.id.btn_ctdh_cancel);
        rvChiTiet = findViewById(R.id.rv_chi_tiet_don_hang);
        toolbar = findViewById(R.id.toolbar_chi_tiet_don_hang);

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

            ChiTietDonHangDao dao = new ChiTietDonHangDao(new DatabaseHelper(this));
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
                btnCancel.setVisibility(android.view.View.GONE);
                break;
            case DonHang.STATUS_SHIPPING:
                statusText = "Đang giao hàng";
                colorRes = android.graphics.Color.CYAN;
                btnCancel.setVisibility(android.view.View.GONE);
                break;
            case DonHang.STATUS_COMPLETED:
                statusText = "Đã giao thành công";
                colorRes = android.graphics.Color.GREEN;
                btnCancel.setVisibility(android.view.View.GONE);
                break;
            case DonHang.STATUS_CANCELLED:
                statusText = "Đã hủy";
                colorRes = android.graphics.Color.RED;
                btnCancel.setVisibility(android.view.View.GONE);
                break;
            default:
                statusText = "Chờ xác nhận";
                colorRes = android.graphics.Color.parseColor("#FF9800");
                btnCancel.setVisibility(android.view.View.VISIBLE);
                break;
        }
        
        tvStatus.setText(statusText);
        tvStatus.getBackground().setTint(colorRes);
    }

    private void setupEvents() {
        btnCancel.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    int result = donHangDao.updateStatus(dh.getId(), DonHang.STATUS_CANCELLED);
                    if (result > 0) {
                        dh.setStatus(DonHang.STATUS_CANCELLED);
                        updateStatusUI();
                        android.widget.Toast.makeText(this, "Đã hủy đơn hàng", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Quay lại", null)
                .show();
        });
    }
}
