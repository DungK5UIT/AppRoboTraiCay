package com.example.approbotraicay.ui;

import android.content.Intent;
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
        initData();
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
            if (Utils.manggiohang != null && Utils.manggiohang.size() > 0) {
                Intent intent = new Intent(this, ThanhToanActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        rvGioHang.setLayoutManager(new LinearLayoutManager(this));
        if (Utils.manggiohang == null || Utils.manggiohang.isEmpty()) {
            showEmptyCart();
        } else {
            llEmpty.setVisibility(View.GONE);
            rvGioHang.setVisibility(View.VISIBLE);
            
            adapter = new GioHangAdapter(this, Utils.manggiohang, new GioHangAdapter.CartUpdateListener() {
                @Override
                public void onCartUpdated() {
                    calculateTotal();
                }
            });
            rvGioHang.setAdapter(adapter);
            calculateTotal();
        }
    }

    private void calculateTotal() {
        long tongTien = 0;
        if (Utils.manggiohang != null) {
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                tongTien += (Utils.manggiohang.get(i).getGiasp() * Utils.manggiohang.get(i).getSoluong());
            }
        }
        
        if (tongTien == 0) {
            showEmptyCart();
        } else {
            llEmpty.setVisibility(View.GONE);
            rvGioHang.setVisibility(View.VISIBLE);
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvTongTien.setText(decimalFormat.format(tongTien) + "đ");
        }
    }

    private void showEmptyCart() {
        llEmpty.setVisibility(View.VISIBLE);
        rvGioHang.setVisibility(View.GONE);
        tvTongTien.setText("0đ");
    }
}
