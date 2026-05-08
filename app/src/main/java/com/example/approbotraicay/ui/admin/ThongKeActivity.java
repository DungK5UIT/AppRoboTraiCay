package com.example.approbotraicay.ui.admin;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.SanPhamAdapter;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.example.approbotraicay.model.SanPham;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {
    private RecyclerView rvTopSelling;
    private DonHangDao donHangDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        donHangDao = new DonHangDao(new DatabaseHelper(this));
        
        initView();
        loadTopProducts();
    }

    private void initView() {
        rvTopSelling = findViewById(R.id.rv_top_selling);
        rvTopSelling.setLayoutManager(new LinearLayoutManager(this));
        
        MaterialToolbar toolbar = findViewById(R.id.toolbar_thong_ke);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadTopProducts() {
        // Use Dev A's logic to get data, but Dev B handles the display
        List<SanPham> topProducts = donHangDao.getTopSellingProducts(5);
        SanPhamAdapter adapter = new SanPhamAdapter(topProducts, sp -> {
            // Dev B: Optional interaction
        });
        rvTopSelling.setAdapter(adapter);
        
        // Dev B: Add layout animation to the list
        rvTopSelling.scheduleLayoutAnimation();
    }
}
