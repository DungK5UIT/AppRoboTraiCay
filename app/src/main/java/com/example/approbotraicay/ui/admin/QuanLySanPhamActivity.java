package com.example.approbotraicay.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity {
    private SanPhamDao sanPhamDao;
    private RecyclerView rvAdminSp;
    private SanPhamAdapter adapter;
    private Button btnToOrders;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        sanPhamDao = new SanPhamDao(DatabaseHelper.getInstance(this));
        
        initView();
        loadData();
    }

    private void initView() {
        rvAdminSp = findViewById(R.id.rv_admin_san_pham);
        btnToOrders = findViewById(R.id.btn_admin_to_orders);
        fabAdd = findViewById(R.id.fab_admin_add_sp);

        rvAdminSp.setLayoutManager(new LinearLayoutManager(this));

        btnToOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDonHangActivity.class));
        });

        fabAdd.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng Thêm sản phẩm đang phát triển (Cần Dev B hỗ trợ upload ảnh)", Toast.LENGTH_SHORT).show();
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
        String[] options = {"Chỉnh sửa (Update)", "Xóa sản phẩm (Delete)"};
        new AlertDialog.Builder(this)
                .setTitle(sp.getTenSanPham())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Toast.makeText(this, "Chỉnh sửa: " + sp.getTenSanPham(), Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        confirmDelete(sp);
                    }
                })
                .show();
    }

    private void confirmDelete(SanPham sp) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa " + sp.getTenSanPham() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = sanPhamDao.deleteSanPham(sp.getId());
                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
