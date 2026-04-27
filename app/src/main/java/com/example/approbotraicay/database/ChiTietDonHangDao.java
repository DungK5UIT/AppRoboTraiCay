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
        
        // Join with sanpham to get product name and image path/blob
        String sql = "SELECT ct.*, sp.tensp, sp.anh FROM Chitietdonhang ct " +
                     "INNER JOIN sanpham sp ON ct.masp = sp.masp " +
                     "WHERE ct.id_dathang = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(orderId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChiTietDonHang ctdh = new ChiTietDonHang();
                ctdh.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_chitiet")));
                ctdh.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow("id_dathang")));
                ctdh.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("masp")));
                ctdh.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("tensp")));
                ctdh.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("soluong")));
                ctdh.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("dongia")));
                ctdh.setImageBlob(cursor.getBlob(cursor.getColumnIndexOrThrow("anh")));
                list.add(ctdh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
