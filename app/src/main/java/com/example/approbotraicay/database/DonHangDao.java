package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.DonHang;
import java.util.ArrayList;
import java.util.List;

public class DonHangDao {
    private DatabaseHelper dbHelper;

    public DonHangDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(DonHang dh) {
        ensureRequiredColumnsExist();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tendangnhap", dh.getUsername());   // FIX: lưu username riêng
        values.put("tenkh", dh.getFullName());
        values.put("diachi", dh.getAddress());
        values.put("sdt", dh.getPhone());
        values.put("tongthanhtoan", dh.getTotal());
        values.put("phi_ship", dh.getShippingFee());
        values.put("ngaydathang", dh.getDate());
        values.put("trangthai", dh.getStatus());

        long id = db.insert("Dathang", null, values);
        db.close();
        return id;
    }

    /**
     * Transactional order creation.
     * Inserts both order and its details in a single transaction.
     */
    public boolean createOrderTransactionally(DonHang dh, List<com.example.approbotraicay.model.ChiTietDonHang> details) {
        ensureRequiredColumnsExist();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put("tendangnhap", dh.getUsername()); // FIX: lưu username riêng
            orderValues.put("tenkh", dh.getFullName());
            orderValues.put("diachi", dh.getAddress());
            orderValues.put("sdt", dh.getPhone());
            orderValues.put("tongthanhtoan", dh.getTotal());
            orderValues.put("phi_ship", dh.getShippingFee());
            orderValues.put("ngaydathang", dh.getDate());
            orderValues.put("trangthai", dh.getStatus());

            long orderId = db.insert("Dathang", null, orderValues);
            if (orderId == -1) return false;

            for (com.example.approbotraicay.model.ChiTietDonHang ctdh : details) {
                ContentValues detailValues = new ContentValues();
                detailValues.put("id_dathang", orderId);
                detailValues.put("masp", ctdh.getProductId());
                detailValues.put("soluong", ctdh.getQuantity());
                detailValues.put("dongia", ctdh.getPrice());

                if (db.insert("Chitietdonhang", null, detailValues) == -1) {
                    return false;
                }
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * FIX: Query theo cột tendangnhap (username) thay vì tenkh (họ tên)
     */
    public List<DonHang> getDonHangByUser(String username) {
        ensureRequiredColumnsExist();
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // FIX: dùng cột tendangnhap thay vì tenkh
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Dathang WHERE tendangnhap = ? ORDER BY id_dathang DESC",
                new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToDonHang(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public List<DonHang> getAllOrders() {
        ensureRequiredColumnsExist();
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Dathang ORDER BY id_dathang DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToDonHang(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public int updateStatus(int orderId, int status) {
        ensureRequiredColumnsExist();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", status);
        int result = db.update("Dathang", values, "id_dathang = ?", new String[]{String.valueOf(orderId)});
        db.close();
        return result;
    }

    public double getTotalRevenue() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(tongthanhtoan) FROM Dathang WHERE trangthai = ?",
                new String[]{String.valueOf(DonHang.STATUS_COMPLETED)});
        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    public double getDailyRevenue(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(tongthanhtoan) FROM Dathang WHERE ngaydathang LIKE ? AND trangthai = ?",
                new String[]{date + "%", String.valueOf(DonHang.STATUS_COMPLETED)});
        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    /**
     * Advanced Reporting: Returns top selling products with total sold quantity.
     */
    public java.util.List<com.example.approbotraicay.model.SanPham> getTopSellingProducts(int limit) {
        java.util.List<com.example.approbotraicay.model.SanPham> list = new java.util.ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT sp.*, SUM(ct.soluong) as total_sold " +
                     "FROM sanpham sp " +
                     "JOIN Chitietdonhang ct ON sp.masp = ct.masp " +
                     "GROUP BY sp.masp " +
                     "ORDER BY total_sold DESC " +
                     "LIMIT ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(limit)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                com.example.approbotraicay.model.SanPham sp = new com.example.approbotraicay.model.SanPham(
                        cursor.getInt(cursor.getColumnIndexOrThrow("masp")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tensp")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("dongia")),
                        cursor.getString(cursor.getColumnIndexOrThrow("mota")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("maso")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("anh"))
                );
                list.add(sp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public java.util.Map<String, Double> getMonthlyRevenue() {
        java.util.Map<String, Double> map = new java.util.LinkedHashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT SUBSTR(ngaydathang, 4, 7) as month_year, SUM(tongthanhtoan) as total " +
                     "FROM Dathang " +
                     "WHERE trangthai = ? " +
                     "GROUP BY month_year " +
                     "ORDER BY month_year DESC LIMIT 12";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(DonHang.STATUS_COMPLETED)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                map.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return map;
    }

    public java.util.Map<String, Double> getRevenueByCategory() {
        java.util.Map<String, Double> map = new java.util.LinkedHashMap<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT nsp.tenloai, SUM(ct.soluong * ct.dongia) as total " +
                     "FROM Chitietdonhang ct " +
                     "JOIN sanpham sp ON ct.masp = sp.masp " +
                     "JOIN loaisp nsp ON sp.maso = nsp.maloai " +
                     "JOIN Dathang dh ON ct.id_dathang = dh.id_dathang " +
                     "WHERE dh.trangthai = ? " +
                     "GROUP BY nsp.tenloai";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(DonHang.STATUS_COMPLETED)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                map.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return map;
    }

    /**
     * Đảm bảo các cột cần thiết tồn tại trong bảng Dathang.
     * Dùng try-catch vì ALTER TABLE sẽ fail nếu cột đã tồn tại.
     */
    public void ensureRequiredColumnsExist() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try { db.execSQL("ALTER TABLE Dathang ADD COLUMN trangthai INTEGER DEFAULT 0"); } catch (Exception e) {}
        try { db.execSQL("ALTER TABLE Dathang ADD COLUMN phi_ship REAL DEFAULT 0"); } catch (Exception e) {}
        try { db.execSQL("ALTER TABLE Dathang ADD COLUMN tendangnhap TEXT DEFAULT ''"); } catch (Exception e) {}
    }

    private DonHang mapCursorToDonHang(Cursor cursor) {
        DonHang dh = new DonHang();
        dh.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_dathang")));
        dh.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("tenkh")));
        dh.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("sdt")));
        dh.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("diachi")));
        dh.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("tongthanhtoan")));
        dh.setDate(cursor.getString(cursor.getColumnIndexOrThrow("ngaydathang")));

        // FIX: đọc username từ cột tendangnhap
        int usernameIdx = cursor.getColumnIndex("tendangnhap");
        if (usernameIdx != -1) dh.setUsername(cursor.getString(usernameIdx));

        int statusIdx = cursor.getColumnIndex("trangthai");
        if (statusIdx != -1) dh.setStatus(cursor.getInt(statusIdx));

        int shipIdx = cursor.getColumnIndex("phi_ship");
        if (shipIdx != -1) dh.setShippingFee(cursor.getDouble(shipIdx));

        return dh;
    }
}
