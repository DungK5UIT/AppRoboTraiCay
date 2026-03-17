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

    public long insert(SanPham sp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_TENSANPHAM, sp.getTenSanPham());
        values.put(DatabaseHelper.KEY_GIA, sp.getGia());
        values.put(DatabaseHelper.KEY_HINHSANPHAM, sp.getHinhAnh());
        values.put(DatabaseHelper.KEY_MOTA, sp.getMoTa());
        values.put(DatabaseHelper.KEY_IDNHOM, sp.getIdNhom());
        long id = db.insert(DatabaseHelper.TABLE_SANPHAM, null, values);
        db.close();
        return id;
    }

    public List<SanPham> getSanPhamByNhom(int idNhom) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SANPHAM, null,
                DatabaseHelper.KEY_IDNHOM + "=?", new String[]{String.valueOf(idNhom)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_TENSANPHAM)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_GIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_HINHSANPHAM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MOTA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_IDNHOM))
                );
                list.add(sp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SANPHAM, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_TENSANPHAM)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_GIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_HINHSANPHAM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_MOTA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_IDNHOM))
                );
                list.add(sp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public int update(SanPham sp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_TENSANPHAM, sp.getTenSanPham());
        values.put(DatabaseHelper.KEY_GIA, sp.getGia());
        values.put(DatabaseHelper.KEY_HINHSANPHAM, sp.getHinhAnh());
        values.put(DatabaseHelper.KEY_MOTA, sp.getMoTa());
        values.put(DatabaseHelper.KEY_IDNHOM, sp.getIdNhom());
        return db.update(DatabaseHelper.TABLE_SANPHAM, values, DatabaseHelper.KEY_ID + "=?", new String[]{String.valueOf(sp.getId())});
    }

    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_SANPHAM, DatabaseHelper.KEY_ID + "=?", new String[]{String.valueOf(id)});
    }
}
