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
    private TextView tvTen, tvSdt, tvDiaChi, tvTongTien;
    private RecyclerView rvChiTiet;
    private MaterialToolbar toolbar;
    private DonHang dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);

        initView();
        initData();
    }

    private void initView() {
        tvTen = findViewById(R.id.tv_ctdh_ten);
        tvSdt = findViewById(R.id.tv_ctdh_sdt);
        tvDiaChi = findViewById(R.id.tv_ctdh_diachi);
        tvTongTien = findViewById(R.id.tv_ctdh_tongtien);
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

            ChiTietDonHangDao dao = new ChiTietDonHangDao(new DatabaseHelper(this));
            List<ChiTietDonHang> details = dao.getDetailsByOrderId(dh.getId());
            rvChiTiet.setAdapter(new ChiTietDonHangAdapter(details));
        }
    }
}
