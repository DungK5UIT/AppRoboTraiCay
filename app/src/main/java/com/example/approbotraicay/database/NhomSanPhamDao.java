package com.example.approbotraicay.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.NhomSanPham;
import java.util.ArrayList;
import java.util.List;

public class NhomSanPhamDao {
    private DatabaseHelper dbHelper;

    public NhomSanPhamDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<NhomSanPham> getAll() {
        List<NhomSanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nhomsanpham", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                NhomSanPham nhom = new NhomSanPham(
                        cursor.getInt(cursor.getColumnIndexOrThrow("maso")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tennsp")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("anh"))
                );
                list.add(nhom);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
