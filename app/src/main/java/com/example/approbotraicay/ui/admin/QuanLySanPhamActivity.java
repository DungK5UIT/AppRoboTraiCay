package com.example.approbotraicay.ui.admin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.SanPhamDao;
import com.example.approbotraicay.model.SanPham;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.adapter.SanPhamAdapter;
import java.util.ArrayList;

public class QuanLySanPhamActivity extends AppCompatActivity {
    private SanPhamDao sanPhamDao;
    private RecyclerView rvAdminSp;
    private SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        sanPhamDao = new SanPhamDao(new DatabaseHelper(this));
        rvAdminSp = findViewById(R.id.rv_admin_san_pham);
        
        rvAdminSp.setLayoutManager(new LinearLayoutManager(this));
        
        List<SanPham> list = sanPhamDao.getAll();
        adapter = new SanPhamAdapter(list, sp -> {
            // Log or show detail for Admin
        });
        rvAdminSp.setAdapter(adapter);
    }
}
