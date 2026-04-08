package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.TaiKhoan;

public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(TaiKhoan user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tendn", user.getUsername());
        values.put("matkhau", user.getPassword());
        values.put("quyen", user.getRole() == 1 ? "admin" : "user");
        // Add other fields if present in your specific banhang.db version
        long id = db.insert("taikhoan", null, values);
        db.close();
        return id;
    }

    public TaiKhoan login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan WHERE tendn = ? AND matkhau = ?", new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan user = new TaiKhoan();
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("tendn")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("matkhau")));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("quyen"));
            user.setRole(role.equalsIgnoreCase("admin") ? 1 : 0);
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }
}
