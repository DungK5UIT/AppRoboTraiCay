package com.example.approbotraicay.ui.admin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.approbotraicay.R;
import com.example.approbotraicay.database.DatabaseHelper;
import com.example.approbotraicay.database.NhomSanPhamDao;
import com.example.approbotraicay.database.SanPhamDao;
import com.example.approbotraicay.model.NhomSanPham;
import com.example.approbotraicay.model.SanPham;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * SuaSanPhamActivity - Chỉnh sửa thông tin sản phẩm đã có
 * Dev B: Giao diện form chỉnh sửa, hiển thị ảnh cũ, cho phép thay ảnh mới
 * Dev A: Tích hợp SanPhamDao.updateSanPham() để lưu vào SQLite
 */
public class SuaSanPhamActivity extends AppCompatActivity {

    public static final String EXTRA_SAN_PHAM = "san_pham";

    private ImageView ivPreview;
    private TextInputEditText etTenSp, etGia, etMoTa;
    private Spinner spnNhomSp;
    private MaterialButton btnChonAnh, btnSubmit;

    private SanPhamDao sanPhamDao;
    private NhomSanPhamDao nhomSanPhamDao;
    private List<NhomSanPham> nhomList = new ArrayList<>();
    private SanPham currentSanPham;
    private byte[] selectedImageBytes = null; // null = giữ ảnh cũ

    private static final String[] IMAGE_NAMES = {
        "cam1", "cam2", "dau1", "dau2", "dua1", "dua2",
        "duahau1", "duahau2", "le1", "le2", "luu1", "luu2",
        "nho1", "nho2", "xoai1", "xoai2"
    };
    private static final String[] IMAGE_DISPLAY_NAMES = {
        "🍊 Cam (1)", "🍊 Cam (2)", "🍓 Dâu (1)", "🍓 Dâu (2)",
        "🍈 Dưa (1)", "🍈 Dưa (2)", "🍉 Dưa hấu (1)", "🍉 Dưa hấu (2)",
        "🍐 Lê (1)", "🍐 Lê (2)", "🌹 Lựu (1)", "🌹 Lựu (2)",
        "🍇 Nho (1)", "🍇 Nho (2)", "🥭 Xoài (1)", "🥭 Xoài (2)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_san_pham);

        sanPhamDao = new SanPhamDao(DatabaseHelper.getInstance(this));
        nhomSanPhamDao = new NhomSanPhamDao(DatabaseHelper.getInstance(this));

        // Nhận SanPham object từ Intent
        currentSanPham = (SanPham) getIntent().getSerializableExtra(EXTRA_SAN_PHAM);
        if (currentSanPham == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initView();
        loadNhomSanPham();
        fillCurrentData();
    }

    private void initView() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_sua_sp);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        ivPreview = findViewById(R.id.iv_preview_sua_sp);
        etTenSp = findViewById(R.id.et_ten_sp_sua);
        etGia = findViewById(R.id.et_gia_sua);
        etMoTa = findViewById(R.id.et_mota_sua);
        spnNhomSp = findViewById(R.id.spn_nhom_sp_sua);
        btnChonAnh = findViewById(R.id.btn_chon_anh_sua);
        btnSubmit = findViewById(R.id.btn_sua_san_pham_submit);

        btnChonAnh.setOnClickListener(v -> showImagePickerDialog());
        btnSubmit.setOnClickListener(v -> capNhatSanPham());
    }

    private void loadNhomSanPham() {
        nhomList = nhomSanPhamDao.getAll();
        List<String> tenNhomList = new ArrayList<>();
        for (NhomSanPham n : nhomList) {
            tenNhomList.add(n.getTenNhom());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tenNhomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNhomSp.setAdapter(adapter);
    }

    private void fillCurrentData() {
        // Fill form with existing product data
        etTenSp.setText(currentSanPham.getTenSanPham());
        etGia.setText(String.valueOf((long) currentSanPham.getGia()));
        etMoTa.setText(currentSanPham.getMoTa());

        // Show existing image
        if (currentSanPham.getHinhAnhBlob() != null && currentSanPham.getHinhAnhBlob().length > 0) {
            byte[] imgBytes = currentSanPham.getHinhAnhBlob();
            Bitmap bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            if (bmp != null) {
                ivPreview.setImageBitmap(bmp);
            }
        }

        // Set spinner to current category
        for (int i = 0; i < nhomList.size(); i++) {
            if (nhomList.get(i).getId() == currentSanPham.getIdNhom()) {
                spnNhomSp.setSelection(i);
                break;
            }
        }
    }

    private void showImagePickerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("🔄 Chọn ảnh mới")
                .setItems(IMAGE_DISPLAY_NAMES, (dialog, which) -> {
                    String imageName = IMAGE_NAMES[which];
                    int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                    if (resId != 0) {
                        ivPreview.setImageResource(resId);
                        selectedImageBytes = drawableToBytes(resId);
                        Toast.makeText(this, "Đã chọn: " + IMAGE_DISPLAY_NAMES[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Giữ ảnh cũ", null)
                .show();
    }

    private byte[] drawableToBytes(int resId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            int maxSize = 400;
            int w = bitmap.getWidth(), h = bitmap.getHeight();
            if (w > maxSize || h > maxSize) {
                float scale = Math.min((float) maxSize / w, (float) maxSize / h);
                bitmap = Bitmap.createScaledBitmap(bitmap, (int)(w * scale), (int)(h * scale), true);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void capNhatSanPham() {
        String tenSp = etTenSp.getText() != null ? etTenSp.getText().toString().trim() : "";
        String giaStr = etGia.getText() != null ? etGia.getText().toString().trim() : "";
        String moTa = etMoTa.getText() != null ? etMoTa.getText().toString().trim() : "";

        if (TextUtils.isEmpty(tenSp)) {
            etTenSp.setError("Vui lòng nhập tên sản phẩm!");
            etTenSp.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(giaStr)) {
            etGia.setError("Vui lòng nhập đơn giá!");
            etGia.requestFocus();
            return;
        }

        double gia;
        try {
            gia = Double.parseDouble(giaStr);
        } catch (NumberFormatException e) {
            etGia.setError("Đơn giá không hợp lệ!");
            return;
        }

        int idNhom = nhomList.isEmpty() ? currentSanPham.getIdNhom()
                : nhomList.get(spnNhomSp.getSelectedItemPosition()).getId();

        // Update fields
        currentSanPham.setTenSanPham(tenSp);
        currentSanPham.setGia(gia);
        currentSanPham.setMoTa(moTa);
        currentSanPham.setIdNhom(idNhom);
        if (selectedImageBytes != null) {
            currentSanPham.setHinhAnhBlob(selectedImageBytes); // Đổi ảnh mới
        }
        // Nếu selectedImageBytes == null, giữ nguyên ảnh cũ

        int result = sanPhamDao.updateSanPham(currentSanPham);
        if (result > 0) {
            Toast.makeText(this, "✅ Cập nhật \"" + tenSp + "\" thành công!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "❌ Cập nhật thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}
