package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.NhomSanPhamAdapter;
import com.example.approbotraicay.adapter.SanPhamAdapter;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.NhomSanPhamDao;
import com.example.approbotraicay.database.SanPhamDao;
import com.example.approbotraicay.model.NhomSanPham;
import com.example.approbotraicay.model.SanPham;
import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {
    private RecyclerView rvNhom, rvSp;
    private SanPhamAdapter spAdapter;
    private NhomSanPhamAdapter nhomAdapter;
    private SanPhamDao spDao;
    private NhomSanPhamDao nhomDao;
    private List<SanPham> allProducts = new ArrayList<>();
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        spDao = new SanPhamDao(dbHelper);
        nhomDao = new NhomSanPhamDao(dbHelper);
    }

    private void initView() {
        rvNhom = findViewById(R.id.rv_nhom_san_pham);
        rvSp = findViewById(R.id.rv_san_pham_moi);
        etSearch = findViewById(R.id.et_search);

        rvNhom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSp.setLayoutManager(new GridLayoutManager(this, 2));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadData() {
        List<NhomSanPham> categories = nhomDao.getAll();
        nhomAdapter = new NhomSanPhamAdapter(categories, nhom -> {
            List<SanPham> filtered = spDao.getSanPhamByNhom(nhom.getId());
            spAdapter.updateList(filtered);
        });
        rvNhom.setAdapter(nhomAdapter);

        allProducts = spDao.getAll();
        spAdapter = new SanPhamAdapter(allProducts, sp -> {
            Intent intent = new Intent(TrangChuActivity.this, ChiTietSanPhamActivity.class);
            intent.putExtra("sanpham", sp);
            startActivity(intent);
        });
        rvSp.setAdapter(spAdapter);
    }

    private void filter(String text) {
        List<SanPham> filteredList = new ArrayList<>();
        for (SanPham item : allProducts) {
            if (item.getTenSanPham().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        spAdapter.updateList(filteredList);
    }
}
