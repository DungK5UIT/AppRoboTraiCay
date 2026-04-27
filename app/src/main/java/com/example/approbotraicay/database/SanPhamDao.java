package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.SanPham;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDao {
    private DatabaseHelper dbHelper;

    public SanPhamDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToSanPham(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<SanPham> getSanPhamByNhom(int idNhom) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Cột mã nhóm trong DB banhang.db là 'maso'
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE maso = ?", new String[]{String.valueOf(idNhom)});
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToSanPham(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<SanPham> search(String query) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE tensp LIKE ?", new String[]{"%" + query + "%"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToSanPham(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // CRUD Methods for Admin
    public int deleteSanPham(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("sanpham", "masp = ?", new String[]{String.valueOf(id)});
    }

    public int updateSanPham(SanPham sp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tensp", sp.getTenSanPham());
        values.put("dongia", sp.getGia());
        values.put("mota", sp.getMoTa());
        values.put("maso", sp.getIdNhom());
        if (sp.getHinhAnhBlob() != null) {
            values.put("anh", sp.getHinhAnhBlob());
        }
        return db.update("sanpham", values, "masp = ?", new String[]{String.valueOf(sp.getId())});
    }

    public long insertSanPham(SanPham sp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tensp", sp.getTenSanPham());
        values.put("dongia", sp.getGia());
        values.put("mota", sp.getMoTa());
        values.put("maso", sp.getIdNhom());
        values.put("anh", sp.getHinhAnhBlob());
        return db.insert("sanpham", null, values);
    }

    private SanPham mapCursorToSanPham(Cursor cursor) {
        return new SanPham(
                cursor.getInt(cursor.getColumnIndexOrThrow("masp")),
                cursor.getString(cursor.getColumnIndexOrThrow("tensp")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("dongia")),
                cursor.getString(cursor.getColumnIndexOrThrow("mota")),
                cursor.getInt(cursor.getColumnIndexOrThrow("maso")),
                cursor.getBlob(cursor.getColumnIndexOrThrow("anh"))
        );
    }
}
