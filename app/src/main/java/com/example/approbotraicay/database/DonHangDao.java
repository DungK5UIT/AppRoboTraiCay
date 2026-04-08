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
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Giả sử tenkh lưu username hoặc tên đầy đủ. Ở đây ta tìm theo tenkh
        Cursor cursor = db.rawQuery("SELECT * FROM Dathang WHERE tenkh = ? ORDER BY id_dathang DESC", new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DonHang dh = new DonHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_dathang")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenkh")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenkh")),
                        cursor.getString(cursor.getColumnIndexOrThrow("sdt")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diachi")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("tongthanhtoan")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ngaydathang")),
                        0 // Mặc định trạng thái là 0 vì DB gốc không có cột status
                );
                list.add(dh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
