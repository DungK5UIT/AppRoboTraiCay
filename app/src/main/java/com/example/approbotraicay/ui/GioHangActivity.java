package com.example.approbotraicay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.GioHangAdapter;
import com.example.approbotraicay.utils.Utils;
import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    private RecyclerView rvGioHang;
    private TextView tvTongTien;
    private LinearLayout llEmpty;
    private GioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        initView();
        setupCart();
        calculateTotal();
    }

    private void initView() {
        rvGioHang = findViewById(R.id.rv_gio_hang);
        tvTongTien = findViewById(R.id.tv_cart_tongtien);
        llEmpty = findViewById(R.id.ll_empty_cart);
        
        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        findViewById(R.id.btn_cart_checkout).setOnClickListener(v -> {
            if (Utils.manggiohang.size() > 0) {
                Toast.makeText(this, "Tính năng Thanh toán đang phát triển!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCart() {
        rvGioHang.setLayoutManager(new LinearLayoutManager(this));
        if (Utils.manggiohang.size() == 0) {
            llEmpty.setVisibility(View.VISIBLE);
        } else {
            llEmpty.setVisibility(View.GONE);
            adapter = new GioHangAdapter(this, Utils.manggiohang, new GioHangAdapter.CartUpdateListener() {
                @Override
                public void onCartUpdated() {
                    calculateTotal();
                    if (Utils.manggiohang.size() == 0) {
                        llEmpty.setVisibility(View.VISIBLE);
                    }
                }
            });
            rvGioHang.setAdapter(adapter);
        }
    }

    private void calculateTotal() {
        long tongTien = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            tongTien += (Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongTien.setText(decimalFormat.format(tongTien) + "đ");
    }
}
