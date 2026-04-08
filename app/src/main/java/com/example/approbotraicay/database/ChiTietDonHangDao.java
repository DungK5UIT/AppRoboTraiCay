package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.ChiTietDonHang;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHangDao {
    private DatabaseHelper dbHelper;

    public ChiTietDonHangDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(ChiTietDonHang ctdh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_dathang", ctdh.getOrderId());
        values.put("masp", ctdh.getProductId());
        values.put("soluong", ctdh.getQuantity());
        values.put("dongia", ctdh.getPrice());
        // 'anh' can be null or handled if needed
        
        db.insert("Chitietdonhang", null, values);
        db.close();
    }

    public List<ChiTietDonHang> getByOrderId(int orderId) {
        List<ChiTietDonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Join with sanpham to get product name if needed in the UI, or just mapping
        String sql = "SELECT * FROM Chitietdonhang WHERE id_dathang = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(orderId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChiTietDonHang ctdh = new ChiTietDonHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_chitiet")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_dathang")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("masp")),
                        "Sản phẩm #" + cursor.getInt(cursor.getColumnIndexOrThrow("masp")), // Temp name
                        cursor.getInt(cursor.getColumnIndexOrThrow("soluong")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("dongia"))
                );
                list.add(ctdh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
