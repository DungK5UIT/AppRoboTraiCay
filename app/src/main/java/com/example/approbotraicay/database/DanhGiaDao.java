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

    public long insert(DanhGia dg) {
        ensureTableExists();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("masp", dg.getProductId());
        values.put("tendn", dg.getUsername());
        values.put("rating", dg.getRating());
        values.put("noidung", dg.getComment());
        values.put("ngaydg", dg.getDate());
        
        long id = db.insert("Danhgia", null, values);
        db.close();
        return id;
    }

    public List<DanhGia> getReviewsByProduct(int productId) {
        ensureTableExists();
        List<DanhGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Danhgia WHERE masp = ? ORDER BY id_dg DESC", new String[]{String.valueOf(productId)});
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                DanhGia dg = new DanhGia();
                dg.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_dg")));
                dg.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("masp")));
                dg.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("tendn")));
                dg.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow("rating")));
                dg.setComment(cursor.getString(cursor.getColumnIndexOrThrow("noidung")));
                dg.setDate(cursor.getString(cursor.getColumnIndexOrThrow("ngaydg")));
                list.add(dg);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    private void ensureTableExists() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS Danhgia (" +
                     "id_dg INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "masp INTEGER, " +
                     "tendn TEXT, " +
                     "rating REAL, " +
                     "noidung TEXT, " +
                     "ngaydg TEXT)";
        db.execSQL(sql);
    }
}
