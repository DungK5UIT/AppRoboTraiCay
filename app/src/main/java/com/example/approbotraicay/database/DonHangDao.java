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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenkh", dh.getFullName()); // Sử dụng fullName hoặc username làm tên khách hàng
        values.put("diachi", dh.getAddress());
        values.put("sdt", dh.getPhone());
        values.put("tongthanhtoan", dh.getTotal());
        values.put("ngaydathang", dh.getDate());
        
        long id = db.insert("Dathang", null, values);
        db.close();
        return id;
    }

    public List<DonHang> getDonHangByUser(String username) {
        ensureStatusColumnExists();
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Dathang WHERE tenkh = ? ORDER BY id_dathang DESC", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToDonHang(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<DonHang> getAllOrders() {
        ensureStatusColumnExists();
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Dathang ORDER BY id_dathang DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToDonHang(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public int updateStatus(int orderId, int status) {
        ensureStatusColumnExists();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", status);
        return db.update("Dathang", values, "id_dathang = ?", new String[]{String.valueOf(orderId)});
    }

    public double getTotalRevenue() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(tongthanhtoan) FROM Dathang WHERE trangthai = 3", null); // 3: Đã giao/Hoàn thành
        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }

    public double getDailyRevenue(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(tongthanhtoan) FROM Dathang WHERE ngaydathang LIKE ? AND trangthai = 3", new String[]{date + "%"});
        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }

    private void ensureStatusColumnExists() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("ALTER TABLE Dathang ADD COLUMN trangthai INTEGER DEFAULT 0");
        } catch (Exception e) {
            // Column already exists
        }
    }

    private DonHang mapCursorToDonHang(Cursor cursor) {
        return new DonHang(
                cursor.getInt(cursor.getColumnIndexOrThrow("id_dathang")),
                cursor.getString(cursor.getColumnIndexOrThrow("tenkh")),
                cursor.getString(cursor.getColumnIndexOrThrow("tenkh")),
                cursor.getString(cursor.getColumnIndexOrThrow("sdt")),
                cursor.getString(cursor.getColumnIndexOrThrow("diachi")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("tongthanhtoan")),
                cursor.getString(cursor.getColumnIndexOrThrow("ngaydathang")),
                cursor.getInt(cursor.getColumnIndexOrThrow("trangthai"))
        );
    }
}
