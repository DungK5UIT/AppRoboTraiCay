package com.example.approbotraicay.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.SanPham;
import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private SanPham sanPham;
    private TextView tvTen, tvGia, tvMoTa;
    private ImageView ivHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        initView();
        getIntentData();
    }

    private void initView() {
        tvTen = findViewById(R.id.tv_detail_ten);
        tvGia = findViewById(R.id.tv_detail_gia);
        tvMoTa = findViewById(R.id.tv_detail_mota);
        ivHinh = findViewById(R.id.iv_detail_hinh);
        
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getIntentData() {
        sanPham = (SanPham) getIntent().getSerializableExtra("sanpham");
        if (sanPham != null) {
            tvTen.setText(sanPham.getTenSanPham());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvGia.setText(decimalFormat.format(sanPham.getGia()) + "đ");
            tvMoTa.setText(sanPham.getMoTa());
        }
    }
}
