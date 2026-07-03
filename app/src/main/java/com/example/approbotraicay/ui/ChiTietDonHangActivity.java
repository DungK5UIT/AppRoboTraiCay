package com.example.approbotraicay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.ChiTietDonHangAdapter;
import com.example.approbotraicay.database.ChiTietDonHangDao;
import com.example.approbotraicay.database.DanhGiaDao;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.DonHangDao;
import com.example.approbotraicay.model.ChiTietDonHang;
import com.example.approbotraicay.model.DanhGia;
import com.example.approbotraicay.model.DonHang;
import com.example.approbotraicay.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChiTietDonHangActivity extends AppCompatActivity {
    private TextView tvTen, tvSdt, tvDiaChi, tvTongTien, tvStatus, tvPhiShip;
    private RecyclerView rvChiTiet;
    private MaterialToolbar toolbar;
    private MaterialButton btnCancel, btnReview;
    private DonHang dh;
    private DonHangDao donHangDao;
    private DanhGiaDao danhGiaDao;
    private SessionManager sessionManager;
    private List<ChiTietDonHang> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);

        donHangDao = new DonHangDao(DatabaseHelper.getInstance(this));
        danhGiaDao = new DanhGiaDao(DatabaseHelper.getInstance(this));
        sessionManager = new SessionManager(this);

        initView();
        initData();
        setupEvents();
    }

    private void initView() {
        tvTen = findViewById(R.id.tv_ctdh_ten);
        tvSdt = findViewById(R.id.tv_ctdh_sdt);
        tvDiaChi = findViewById(R.id.tv_ctdh_diachi);
        tvTongTien = findViewById(R.id.tv_ctdh_tongtien);
        tvStatus = findViewById(R.id.tv_ctdh_status);
        tvPhiShip = findViewById(R.id.tv_ctdh_phiship);
        btnCancel = findViewById(R.id.btn_ctdh_cancel);
        btnReview = findViewById(R.id.btn_ctdh_review);
        rvChiTiet = findViewById(R.id.rv_chi_tiet_don_hang);
        toolbar = findViewById(R.id.toolbar_chi_tiet_don_hang);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        rvChiTiet.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        dh = (DonHang) getIntent().getSerializableExtra("donhang");
        if (dh != null) {
            tvTen.setText(dh.getFullName());
            tvSdt.setText(dh.getPhone() != null ? dh.getPhone() : "—");
            tvDiaChi.setText(dh.getAddress() != null ? dh.getAddress() : "—");

            DecimalFormat df = new DecimalFormat("###,###,###");
            tvTongTien.setText(df.format(dh.getTotal()) + "đ");
            tvPhiShip.setText(df.format(dh.getShippingFee()) + "đ");

            updateStatusUI();

            ChiTietDonHangDao dao = new ChiTietDonHangDao(DatabaseHelper.getInstance(this));
            detailList = dao.getByOrderId(dh.getId());
            rvChiTiet.setAdapter(new ChiTietDonHangAdapter(detailList));
        }
    }

    private void updateStatusUI() {
        String statusText;
        int bgColor;

        switch (dh.getStatus()) {
            case DonHang.STATUS_PROCESSING:
                statusText = "Đang xử lý";
                bgColor = android.graphics.Color.parseColor("#FF9800");
                btnCancel.setVisibility(View.GONE);
                btnReview.setVisibility(View.GONE);
                break;
            case DonHang.STATUS_SHIPPING:
                statusText = "🚚 Đang giao hàng";
                bgColor = android.graphics.Color.parseColor("#2196F3");
                btnCancel.setVisibility(View.GONE);
                btnReview.setVisibility(View.GONE);
                break;
            case DonHang.STATUS_COMPLETED:
                statusText = "✅ Đã giao thành công";
                bgColor = android.graphics.Color.parseColor("#4CAF50");
                btnCancel.setVisibility(View.GONE);
                // Hiện nút đánh giá cho đơn đã giao
                btnReview.setVisibility(View.VISIBLE);
                break;
            case DonHang.STATUS_CANCELLED:
                statusText = "❌ Đã hủy";
                bgColor = android.graphics.Color.parseColor("#F44336");
                btnCancel.setVisibility(View.GONE);
                btnReview.setVisibility(View.GONE);
                break;
            default: // STATUS_PENDING
                statusText = "⏳ Chờ xác nhận";
                bgColor = android.graphics.Color.parseColor("#9C27B0");
                btnCancel.setVisibility(View.VISIBLE);
                btnReview.setVisibility(View.GONE);
                break;
        }

        tvStatus.setText(statusText);

        // FIX Bug B: Dùng GradientDrawable thay vì setTint() trên background có thể null
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setColor(bgColor);
        bg.setCornerRadius(40f);
        tvStatus.setBackground(bg);
        tvStatus.setTextColor(android.graphics.Color.WHITE);
        tvStatus.setPadding(
                (int)(12 * getResources().getDisplayMetrics().density), 4,
                (int)(12 * getResources().getDisplayMetrics().density), 4);
    }

    private void setupEvents() {
        // Nút hủy đơn
        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    int result = donHangDao.updateStatus(dh.getId(), DonHang.STATUS_CANCELLED);
                    if (result > 0) {
                        dh.setStatus(DonHang.STATUS_CANCELLED);
                        updateStatusUI();
                        Toast.makeText(this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Quay lại", null)
                .show();
        });

        // Nút đánh giá sản phẩm
        btnReview.setOnClickListener(v -> showReviewProductPicker());
    }

    /**
     * Hiện dialog chọn sản phẩm để đánh giá.
     * Nếu chỉ có 1 sản phẩm thì mở thẳng dialog đánh giá.
     */
    private void showReviewProductPicker() {
        if (detailList == null || detailList.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm để đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (detailList.size() == 1) {
            // Chỉ 1 sản phẩm → mở thẳng
            showReviewDialog(detailList.get(0));
            return;
        }

        // Nhiều sản phẩm → cho user chọn
        String[] names = new String[detailList.size()];
        String username = sessionManager.getUserName();
        for (int i = 0; i < detailList.size(); i++) {
            ChiTietDonHang ct = detailList.get(i);
            boolean reviewed = danhGiaDao.isDaDanhGia(username, ct.getProductId(), dh.getId());
            names[i] = ct.getProductName() + (reviewed ? " ✅ (Đã đánh giá)" : "");
        }

        new AlertDialog.Builder(this)
                .setTitle("Chọn sản phẩm muốn đánh giá")
                .setItems(names, (dialog, which) -> {
                    ChiTietDonHang selected = detailList.get(which);
                    String username2 = sessionManager.getUserName();
                    if (danhGiaDao.isDaDanhGia(username2, selected.getProductId(), dh.getId())) {
                        Toast.makeText(this, "Bạn đã đánh giá sản phẩm này rồi!", Toast.LENGTH_SHORT).show();
                    } else {
                        showReviewDialog(selected);
                    }
                })
                .show();
    }

    /**
     * Hiện dialog đánh giá cho 1 sản phẩm cụ thể.
     * Có RatingBar động + kiểm tra đánh giá trùng.
     */
    private void showReviewDialog(ChiTietDonHang chiTiet) {
        String username = sessionManager.getUserName();

        // Kiểm tra đã đánh giá chưa
        if (danhGiaDao.isDaDanhGia(username, chiTiet.getProductId(), dh.getId())) {
            Toast.makeText(this, "Bạn đã đánh giá \"" + chiTiet.getProductName() + "\" rồi!", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_danh_gia, null);

        TextView tvProductName = dialogView.findViewById(R.id.tv_review_product_name);
        RatingBar ratingBar = dialogView.findViewById(R.id.rb_review);
        TextView tvRatingLabel = dialogView.findViewById(R.id.tv_rating_label);
        TextInputEditText etContent = dialogView.findViewById(R.id.et_review_content);

        tvProductName.setText(chiTiet.getProductName());

        // Cập nhật label khi user thay đổi số sao
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            String label;
            if (rating <= 1) label = "Rất tệ 😞";
            else if (rating <= 2) label = "Tệ 😕";
            else if (rating <= 3) label = "Bình thường 😐";
            else if (rating <= 4) label = "Tốt 😊";
            else label = "Xuất sắc 😍";
            tvRatingLabel.setText(label);
        });

        new AlertDialog.Builder(this)
                .setTitle("Đánh giá sản phẩm")
                .setView(dialogView)
                .setPositiveButton("Gửi đánh giá", (dialog, which) -> {
                    String content = etContent.getText() != null
                            ? etContent.getText().toString().trim()
                            : "";

                    if (content.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập nội dung đánh giá!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ratingBar.getRating() == 0) {
                        Toast.makeText(this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DanhGia dg = new DanhGia();
                    dg.setProductId(chiTiet.getProductId());
                    dg.setOrderId(dh.getId());
                    dg.setUsername(username);
                    dg.setRating(ratingBar.getRating());
                    dg.setComment(content);
                    dg.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

                    long result = danhGiaDao.insert(dg);
                    if (result > 0) {
                        Toast.makeText(this,
                                "✅ Cảm ơn bạn đã đánh giá \"" + chiTiet.getProductName() + "\"!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Gửi đánh giá thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
