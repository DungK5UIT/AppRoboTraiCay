package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.SanPhamAdapter;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.YeuthichDao;
import com.example.approbotraicay.model.SanPham;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class YeuThichActivity extends AppCompatActivity {
    private RecyclerView rvFavorite;
    private LinearLayout llEmpty;
    private YeuthichDao yeuthichDao;
    private SanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeu_thich);

        yeuthichDao = new YeuthichDao(DatabaseHelper.getInstance(this));
        
        initView();
        loadFavorites();
    }

    private void initView() {
        rvFavorite = findViewById(R.id.rv_favorite);
        llEmpty = findViewById(R.id.ll_empty_favorite);
        MaterialToolbar toolbar = findViewById(R.id.toolbar_favorite);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        
        rvFavorite.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadFavorites() {
        List<SanPham> favList = yeuthichDao.getAllFavorites();
        if (favList.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            rvFavorite.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            rvFavorite.setVisibility(View.VISIBLE);
            adapter = new SanPhamAdapter(favList, sp -> {
                Intent intent = new Intent(YeuThichActivity.this, ChiTietSanPhamActivity.class);
                intent.putExtra("sanpham", sp);
                startActivity(intent);
            });
            rvFavorite.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(); // Refresh list when returning from detail
    }
}
