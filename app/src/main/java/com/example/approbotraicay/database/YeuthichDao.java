package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.SanPham;
import java.util.ArrayList;
import java.util.List;

public class YeuthichDao {
    private DatabaseHelper dbHelper;
    private SanPhamDao sanPhamDao;

    public YeuthichDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.sanPhamDao = new SanPhamDao(dbHelper);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS yeuthich (masp INTEGER PRIMARY KEY)");
    }

    public void addFavorite(int masp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("masp", masp);
        db.insertWithOnConflict("yeuthich", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeFavorite(int masp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("yeuthich", "masp = ?", new String[]{String.valueOf(masp)});
    }

    public boolean isFavorite(int masp) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM yeuthich WHERE masp = ?", new String[]{String.valueOf(masp)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<SanPham> getAllFavorites() {
        List<SanPham> favoriteProducts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT masp FROM yeuthich", null);
        
        List<SanPham> allProducts = sanPhamDao.getAll();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int masp = cursor.getInt(0);
                for (SanPham sp : allProducts) {
                    if (sp.getId() == masp) {
                        favoriteProducts.add(sp);
                        break;
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoriteProducts;
    }
}
