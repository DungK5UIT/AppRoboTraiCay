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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class DonHangActivity extends AppCompatActivity {
    private RecyclerView rvDonHang;
    private LinearLayout llEmpty;
    private MaterialToolbar toolbar;
    private ChipGroup chipGroupFilter;
    private DonHangDao donHangDao;
    private SessionManager sessionManager;
    private List<DonHang> allOrders = new ArrayList<>();
    private DonHangAdapter adapter;

    // Trạng thái filter hiện tại: -1 = Tất cả
    private int currentFilter = -1;

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
        chipGroupFilter = findViewById(R.id.chip_group_filter);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        rvDonHang.setLayoutManager(new LinearLayoutManager(this));

        // Chip filter events
        if (chipGroupFilter != null) {
            chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.isEmpty()) {
                    currentFilter = -1;
                } else {
                    int checkedId = checkedIds.get(0);
                    if (checkedId == R.id.chip_all) currentFilter = -1;
                    else if (checkedId == R.id.chip_pending) currentFilter = DonHang.STATUS_PENDING;
                    else if (checkedId == R.id.chip_processing) currentFilter = DonHang.STATUS_PROCESSING;
                    else if (checkedId == R.id.chip_shipping) currentFilter = DonHang.STATUS_SHIPPING;
                    else if (checkedId == R.id.chip_completed) currentFilter = DonHang.STATUS_COMPLETED;
                    else if (checkedId == R.id.chip_cancelled) currentFilter = DonHang.STATUS_CANCELLED;
                }
                applyFilter();
            });
        }
    }

    private void initData() {
        sessionManager = new SessionManager(this);
        donHangDao = new DonHangDao(DatabaseHelper.getInstance(this));
        loadOrders();
    }

    private void loadOrders() {
        allOrders = donHangDao.getDonHangByUser(sessionManager.getUserName());
        applyFilter();
    }

    private void applyFilter() {
        List<DonHang> filtered = new ArrayList<>();
        if (currentFilter == -1) {
            filtered.addAll(allOrders);
        } else {
            for (DonHang dh : allOrders) {
                if (dh.getStatus() == currentFilter) {
                    filtered.add(dh);
                }
            }
        }

        if (filtered.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            adapter = new DonHangAdapter(filtered, dh -> {
                Intent intent = new Intent(DonHangActivity.this, ChiTietDonHangActivity.class);
                intent.putExtra("donhang", dh);
                startActivity(intent);
            });
            rvDonHang.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload khi quay lại từ chi tiết đơn (sau khi hủy hoặc đánh giá)
        loadOrders();
    }
}
