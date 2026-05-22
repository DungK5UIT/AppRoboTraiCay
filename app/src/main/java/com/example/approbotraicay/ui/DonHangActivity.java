package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.DonHangAdapter;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.example.approbotraicay.model.DonHang;
import com.example.approbotraicay.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class DonHangActivity extends AppCompatActivity {
    private RecyclerView rvDonHang;
    private LinearLayout llEmpty;
    private MaterialToolbar toolbar;
    private DonHangDao donHangDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);

        initView();
        initData();
    }

    private void initView() {
        rvDonHang = findViewById(R.id.rv_don_hang);
        llEmpty = findViewById(R.id.ll_empty_don_hang);
        toolbar = findViewById(R.id.toolbar_don_hang);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvDonHang.setLayoutManager(new LinearLayoutManager(this));
        rvDonHang.setAdapter(new DonHangAdapter(new java.util.ArrayList<>(), null));
    }

    private void initData() {
        sessionManager = new SessionManager(this);
        donHangDao = new DonHangDao(DatabaseHelper.getInstance(this));

        List<DonHang> list = donHangDao.getDonHangByUser(sessionManager.getUserName());
        if (list.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            DonHangAdapter adapter = new DonHangAdapter(list, dh -> {
                Intent intent = new Intent(DonHangActivity.this, ChiTietDonHangActivity.class);
                intent.putExtra("donhang", dh);
                startActivity(intent);
            });
            rvDonHang.setAdapter(adapter);
        }
    }
}
