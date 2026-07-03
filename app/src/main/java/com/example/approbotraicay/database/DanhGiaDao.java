package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.DanhGia;
import java.util.ArrayList;
import java.util.List;

public class DanhGiaDao {
    private DatabaseHelper dbHelper;

    public DanhGiaDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /** Insert đánh giá mới, trả về id hoặc -1 nếu thất bại */
    public long insert(DanhGia dg) {
        ensureTableExists();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("masp", dg.getProductId());
        values.put("tendn", dg.getUsername());
        values.put("rating", dg.getRating());
        values.put("noidung", dg.getComment());
        values.put("ngaydg", dg.getDate());
        // Lưu id_dathang để kiểm tra đã đánh giá chưa (chống đánh giá trùng)
        values.put("id_dathang", dg.getOrderId());

        long id = db.insert("Danhgia", null, values);
        db.close();
        return id;
    }

    /**
     * Kiểm tra user đã đánh giá sản phẩm trong đơn hàng này chưa.
     * Business rule: mỗi user chỉ được đánh giá 1 lần / sản phẩm / đơn hàng.
     */
    public boolean isDaDanhGia(String username, int productId, int orderId) {
        ensureTableExists();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Danhgia WHERE tendn = ? AND masp = ? AND id_dathang = ?",
                new String[]{username, String.valueOf(productId), String.valueOf(orderId)});
        boolean result = false;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getInt(0) > 0;
            cursor.close();
        }
        db.close();
        return result;
    }

    /** Lấy danh sách đánh giá theo sản phẩm, mới nhất trước */
    public List<DanhGia> getReviewsByProduct(int productId) {
        ensureTableExists();
        List<DanhGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Danhgia WHERE masp = ? ORDER BY id_dg DESC",
                new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DanhGia dg = mapCursorToDanhGia(cursor);
                list.add(dg);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    /** Tính điểm trung bình của sản phẩm */
    public float getAverageRating(int productId) {
        ensureTableExists();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT AVG(rating) FROM Danhgia WHERE masp = ?",
                new String[]{String.valueOf(productId)});
        float avg = 0f;
        if (cursor != null && cursor.moveToFirst()) {
            avg = cursor.getFloat(0);
            cursor.close();
        }
        db.close();
        return avg;
    }

    /** Đếm số lượng đánh giá của sản phẩm */
    public int countReviews(int productId) {
        ensureTableExists();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Danhgia WHERE masp = ?",
                new String[]{String.valueOf(productId)});
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    private DanhGia mapCursorToDanhGia(Cursor cursor) {
        DanhGia dg = new DanhGia();
        dg.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_dg")));
        dg.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("masp")));
        dg.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("tendn")));
        dg.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("rating")));
        dg.setComment(cursor.getString(cursor.getColumnIndexOrThrow("noidung")));
        dg.setDate(cursor.getString(cursor.getColumnIndexOrThrow("ngaydg")));

        int orderIdx = cursor.getColumnIndex("id_dathang");
        if (orderIdx != -1) dg.setOrderId(cursor.getInt(orderIdx));
        return dg;
    }

    private void ensureTableExists() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Danhgia (" +
                "id_dg INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "masp INTEGER, " +
                "tendn TEXT, " +
                "rating REAL DEFAULT 5, " +
                "noidung TEXT, " +
                "ngaydg TEXT, " +
                "id_dathang INTEGER DEFAULT 0)");
        // Thêm cột id_dathang nếu DB cũ đã có bảng nhưng thiếu cột này
        try { db.execSQL("ALTER TABLE Danhgia ADD COLUMN id_dathang INTEGER DEFAULT 0"); } catch (Exception e) {}
    }
}
