package com.example.approbotraicay.database;

import android.content.ContentValues;
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

    public long insert(NhomSanPham nhom) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_TENNHOM, nhom.getTenNhom());
        values.put(DatabaseHelper.KEY_HINHNHOM, nhom.getHinhAnh());
        long id = db.insert(DatabaseHelper.TABLE_NHOMSANPHAM, null, values);
        db.close();
        return id;
    }

    public List<NhomSanPham> getAll() {
        List<NhomSanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NHOMSANPHAM, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                NhomSanPham nhom = new NhomSanPham(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_TENNHOM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_HINHNHOM))
                );
                list.add(nhom);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public int update(NhomSanPham nhom) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_TENNHOM, nhom.getTenNhom());
        values.put(DatabaseHelper.KEY_HINHNHOM, nhom.getHinhAnh());
        return db.update(DatabaseHelper.TABLE_NHOMSANPHAM, values, DatabaseHelper.KEY_ID + "=?", new String[]{String.valueOf(nhom.getId())});
    }

    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_NHOMSANPHAM, DatabaseHelper.KEY_ID + "=?", new String[]{String.valueOf(id)});
    }
}
