package com.example.approbotraicay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.GioHang;
import com.example.approbotraicay.model.SanPham;
import com.example.approbotraicay.utils.Utils;
import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private SanPham sanPham;
    private TextView tvTen, tvGia, tvMoTa;
    private ImageView ivHinh;
    private Button btnAddToCart;
    private com.example.approbotraicay.database.YeuthichDao yeuthichDao;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabFavorite;
    private com.example.approbotraicay.database.DanhGiaDao danhGiaDao;
    private androidx.recyclerview.widget.RecyclerView rvReviews;
    private com.example.approbotraicay.adapter.DanhGiaAdapter reviewAdapter;
    private java.util.List<com.example.approbotraicay.model.DanhGia> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        yeuthichDao = new com.example.approbotraicay.database.YeuthichDao(com.example.approbotraicay.database.DatabaseHelper.getInstance(this));
        
        initView();
        getIntentData();
        updateFavoriteIcon();
    }

    private void initView() {
        tvTen = findViewById(R.id.tv_detail_ten);
        tvGia = findViewById(R.id.tv_detail_gia);
        tvMoTa = findViewById(R.id.tv_detail_mota);
        ivHinh = findViewById(R.id.iv_detail_hinh);
        btnAddToCart = findViewById(R.id.btn_detail_add_to_cart);
        fabFavorite = findViewById(R.id.fab_favorite);
        rvReviews = findViewById(R.id.rv_reviews);
        
        rvReviews.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        danhGiaDao = new com.example.approbotraicay.database.DanhGiaDao(com.example.approbotraicay.database.DatabaseHelper.getInstance(this));
        
        btnAddToCart.setOnClickListener(v -> {
            themVaoGioHang();
        });

        findViewById(R.id.btn_write_review).setOnClickListener(v -> {
            showReviewDialog();
        });

        if (fabFavorite != null) {
            fabFavorite.setOnClickListener(v -> {
                if (sanPham == null) return;
                if (yeuthichDao.isFavorite(sanPham.getId())) {
                    yeuthichDao.removeFavorite(sanPham.getId());
                    Toast.makeText(this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    yeuthichDao.addFavorite(sanPham.getId());
                    Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                }
                updateFavoriteIcon();
            });
        }
        
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void updateFavoriteIcon() {
        if (fabFavorite != null && sanPham != null && yeuthichDao.isFavorite(sanPham.getId())) {
            fabFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else if (fabFavorite != null) {
            fabFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void getIntentData() {
        sanPham = (SanPham) getIntent().getSerializableExtra("sanpham");
        if (sanPham != null) {
            tvTen.setText(sanPham.getTenSanPham());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvGia.setText(decimalFormat.format(sanPham.getGia()) + "đ");
            tvMoTa.setText(sanPham.getMoTa());
            Glide.with(this).load(sanPham.getHinhAnhBlob()).into(ivHinh);
            loadReviews();
        }
    }

    private void loadReviews() {
        if (sanPham != null) {
            reviewList = danhGiaDao.getReviewsByProduct(sanPham.getId());
            reviewAdapter = new com.example.approbotraicay.adapter.DanhGiaAdapter(reviewList);
            rvReviews.setAdapter(reviewAdapter);
        }
    }

    private void showReviewDialog() {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_danh_gia, null);
        android.widget.RatingBar rb = dialogView.findViewById(R.id.rb_review);
        android.widget.EditText et = dialogView.findViewById(R.id.et_review_content);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    String content = et.getText().toString().trim();
                    float rating = rb.getRating();
                    if (content.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    com.example.approbotraicay.model.DanhGia dg = new com.example.approbotraicay.model.DanhGia();
                    dg.setProductId(sanPham.getId());
                    dg.setRating(rating);
                    dg.setComment(content);
                    com.example.approbotraicay.utils.SessionManager sessionManager = new com.example.approbotraicay.utils.SessionManager(this);
                    dg.setUsername(sessionManager.isLoggedIn() ? sessionManager.getUserName() : "Khách");
                    dg.setDate(new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date()));
                    
                    danhGiaDao.insert(dg);
                    Toast.makeText(this, "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
                    loadReviews();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void themVaoGioHang() {
        if (sanPham == null) return;
        
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new java.util.ArrayList<>();
        }

        boolean exists = false;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            if (Utils.manggiohang.get(i).getIdsp() == sanPham.getId()) {
                Utils.manggiohang.get(i).setSoluong(Utils.manggiohang.get(i).getSoluong() + 1);
                exists = true;
                break;
            }
        }

        if (!exists) {
            GioHang gioHang = new GioHang();
            gioHang.setGiasp((long) sanPham.getGia());
            gioHang.setSoluong(1);
            gioHang.setIdsp(sanPham.getId());
            gioHang.setTensp(sanPham.getTenSanPham());
            gioHang.setHinhAnhBlob(sanPham.getHinhAnhBlob());
            Utils.manggiohang.add(gioHang);
        }
        
        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GioHangActivity.class));
    }
}
