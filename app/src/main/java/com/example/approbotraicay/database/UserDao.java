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
            TaiKhoan user = mapCursorToTaiKhoan(cursor);
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public TaiKhoan getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan WHERE tendn = ?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan user = mapCursorToTaiKhoan(cursor);
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public int updateProfile(TaiKhoan user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hoten", user.getFullName());
        values.put("sdt", user.getPhone());
        values.put("email", user.getEmail());
        values.put("diachi", user.getAddress());
        int result = db.update("taikhoan", values, "tendn = ?", new String[]{user.getUsername()});
        db.close();
        return result;
    }

    public int updatePassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matkhau", newPassword);
        int result = db.update("taikhoan", values, "tendn = ?", new String[]{username});
        db.close();
        return result;
    }

    private TaiKhoan mapCursorToTaiKhoan(Cursor cursor) {
        TaiKhoan user = new TaiKhoan();
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("tendn")));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("matkhau")));
        
        int fullNameIdx = cursor.getColumnIndex("hoten");
        if (fullNameIdx != -1) user.setFullName(cursor.getString(fullNameIdx));
        
        int phoneIdx = cursor.getColumnIndex("sdt");
        if (phoneIdx != -1) user.setPhone(cursor.getString(phoneIdx));
        
        int emailIdx = cursor.getColumnIndex("email");
        if (emailIdx != -1) user.setEmail(cursor.getString(emailIdx));
        
        int addressIdx = cursor.getColumnIndex("diachi");
        if (addressIdx != -1) user.setAddress(cursor.getString(addressIdx));

        String role = cursor.getString(cursor.getColumnIndexOrThrow("quyen"));
        user.setRole(role.equalsIgnoreCase("admin") ? 1 : 0);
        return user;
    }
}
