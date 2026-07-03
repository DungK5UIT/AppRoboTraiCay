package com.example.approbotraicay.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.SanPhamAdapter;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.SanPhamDao;
import com.example.approbotraicay.model.SanPham;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity {
    private SanPhamDao sanPhamDao;
    private RecyclerView rvAdminSp;
    private SanPhamAdapter adapter;
    private ExtendedFloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        sanPhamDao = new SanPhamDao(DatabaseHelper.getInstance(this));
        
        initView();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Auto-refresh list when returning from Add/Edit screens
        loadData();
    }

    private void initView() {
        rvAdminSp = findViewById(R.id.rv_admin_san_pham);
        android.widget.Button btnToOrders = findViewById(R.id.btn_admin_to_orders);
        fabAdd = findViewById(R.id.fab_admin_add_sp);

        rvAdminSp.setLayoutManager(new LinearLayoutManager(this));

        btnToOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDonHangActivity.class));
        });

        // Dev B: Connect FAB to ThemSanPhamActivity
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, ThemSanPhamActivity.class));
        });
    }

    private void loadData() {
        List<SanPham> list = sanPhamDao.getAll();
        adapter = new SanPhamAdapter(list, sp -> {
            showOptionsDialog(sp);
        });
        rvAdminSp.setAdapter(adapter);
    }

    private void showOptionsDialog(SanPham sp) {
        String[] options = {"✏️ Chỉnh sửa", "🗑️ Xóa sản phẩm"};
        new AlertDialog.Builder(this)
                .setTitle(sp.getTenSanPham())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Dev B: Open SuaSanPhamActivity with selected product
                        Intent intent = new Intent(this, SuaSanPhamActivity.class);
                        intent.putExtra(SuaSanPhamActivity.EXTRA_SAN_PHAM, sp);
                        startActivity(intent);
                    } else if (which == 1) {
                        confirmDelete(sp);
                    }
                })
                .show();
    }

    private void confirmDelete(SanPham sp) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa \"" + sp.getTenSanPham() + "\"?")
                .setPositiveButton("🗑️ Xóa", (dialog, which) -> {
                    int result = sanPhamDao.deleteSanPham(sp.getId());
                    if (result > 0) {
                        Toast.makeText(this, "✅ Đã xóa \"" + sp.getTenSanPham() + "\"", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
