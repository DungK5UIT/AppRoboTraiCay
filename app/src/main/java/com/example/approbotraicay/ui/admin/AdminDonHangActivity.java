package com.example.approbotraicay.ui.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.DonHangAdapter;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.example.approbotraicay.model.DonHang;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class AdminDonHangActivity extends AppCompatActivity {
    private RecyclerView rvOrders;
    private DonHangDao donHangDao;
    private DonHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_don_hang);

        donHangDao = new DonHangDao(DatabaseHelper.getInstance(this));
        
        initView();
        loadOrders();
    }

    private void initView() {
        rvOrders = findViewById(R.id.rv_admin_orders);
        MaterialToolbar toolbar = findViewById(R.id.toolbar_admin_orders);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrders() {
        List<DonHang> orders = donHangDao.getAllOrders();
        adapter = new DonHangAdapter(orders, dh -> {
            android.content.Intent intent = new android.content.Intent(AdminDonHangActivity.this, AdminChiTietDonHangActivity.class);
            intent.putExtra("donhang", dh);
            startActivity(intent);
        });
        rvOrders.setAdapter(adapter);
    }

    private void showStatusDialog(DonHang order) {
        String[] statuses = {"Chờ xác nhận", "Đã xác nhận", "Đang giao", "Đã giao", "Đã hủy"};
        new AlertDialog.Builder(this)
                .setTitle("Cập nhật trạng thái đơn hàng #" + order.getId())
                .setItems(statuses, (dialog, which) -> {
                    int result = donHangDao.updateStatus(order.getId(), which);
                    if (result > 0) {
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadOrders();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
