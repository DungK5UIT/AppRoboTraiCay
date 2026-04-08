package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.GioHang;
import com.example.approbotraicay.model.SanPham;
import com.example.approbotraicay.utils.Utils;
import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private SanPham sanPham;
    private TextView tvTen, tvGia, tvMoTa;
    private ImageView ivHinh;
    private Button btnAddToCart;

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
        btnAddToCart = findViewById(R.id.btn_detail_add_to_cart);
        
        btnAddToCart.setOnClickListener(v -> {
            themVaoGioHang();
        });
        
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
            Glide.with(this).load(sanPham.getHinhAnhBlob()).into(ivHinh);
        }
    }

    private void themVaoGioHang() {
        if (sanPham == null) return;
        
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new java.util.ArrayList<>();
        }

        boolean exists = false;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            if (Utils.manggiohang.get(i).getIdsp() == sanPham.getId()) {
                Utils.manggiohang.get(i).setSoluong(Utils.manggiohang.get(i).getSoluong() + 1);
                exists = true;
                break;
            }
        }

        if (!exists) {
            GioHang gioHang = new GioHang();
            gioHang.setGiasp((long) sanPham.getGia());
            gioHang.setSoluong(1);
            gioHang.setIdsp(sanPham.getId());
            gioHang.setTensp(sanPham.getTenSanPham());
            gioHang.setHinhAnhBlob(sanPham.getHinhAnhBlob());
            Utils.manggiohang.add(gioHang);
        }
        
        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GioHangActivity.class));
    }
}
