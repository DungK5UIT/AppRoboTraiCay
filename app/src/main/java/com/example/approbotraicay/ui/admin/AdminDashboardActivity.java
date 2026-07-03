package com.example.approbotraicay.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.example.approbotraicay.utils.SessionManager;
import com.example.approbotraicay.ui.auth.LoginActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    private TextView tvTotalRevenue, tvTodayRevenue;
    private MaterialCardView cardProducts, cardOrders, cardUsers, cardSettings;
    private DonHangDao donHangDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        donHangDao = new DonHangDao(DatabaseHelper.getInstance(this));
        sessionManager = new SessionManager(this);
        
        initView();
        setupEvents();
        loadStatistics();
    }

    private void initView() {
        tvTotalRevenue = findViewById(R.id.tv_admin_total_revenue);
        tvTodayRevenue = findViewById(R.id.tv_admin_today_revenue);
        cardProducts = findViewById(R.id.card_admin_products);
        cardOrders = findViewById(R.id.card_admin_orders);
        cardUsers = findViewById(R.id.card_admin_users);
        cardSettings = findViewById(R.id.card_admin_settings);
        
        MaterialToolbar toolbar = findViewById(R.id.toolbar_admin_dashboard);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupEvents() {
        cardProducts.setOnClickListener(v -> {
            startActivity(new Intent(this, QuanLySanPhamActivity.class));
        });

        cardOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDonHangActivity.class));
        });

        // Other cards can show a "Coming Soon" or handle other modules
        cardUsers.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Tính năng Quản lý người dùng đang phát triển", android.widget.Toast.LENGTH_SHORT).show();
        });

        cardSettings.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Đăng xuất Admin")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản quản trị?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        sessionManager.logout();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        findViewById(R.id.btn_admin_view_stats).setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Xem báo cáo chi tiết", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void loadStatistics() {
        double total = donHangDao.getTotalRevenue();
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        double daily = donHangDao.getDailyRevenue(today);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTotalRevenue.setText(decimalFormat.format(total) + "đ");
        tvTodayRevenue.setText(decimalFormat.format(daily) + "đ");
        
        // Dev B: Add a simple fade-in animation for numbers
        tvTotalRevenue.setAlpha(0f);
        tvTotalRevenue.animate().alpha(1f).setDuration(1000).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics(); // Refresh data when returning
    }
}
