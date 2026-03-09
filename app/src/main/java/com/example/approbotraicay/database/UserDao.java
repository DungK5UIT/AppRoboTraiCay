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
        values.put(DatabaseHelper.KEY_USERNAME, user.getUsername());
        values.put(DatabaseHelper.KEY_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.KEY_FULLNAME, user.getFullName());
        values.put(DatabaseHelper.KEY_EMAIL, user.getEmail());
        values.put(DatabaseHelper.KEY_PHONE, user.getPhone());
        values.put(DatabaseHelper.KEY_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.KEY_ROLE, user.getRole());

        long id = db.insert(DatabaseHelper.TABLE_TAIKHOAN, null, values);
        db.close();
        return id;
    }

    public TaiKhoan getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TAIKHOAN, null,
                DatabaseHelper.KEY_USERNAME + "=?", new String[] { username },
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan user = new TaiKhoan(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ROLE)));
            cursor.close();
            return user;
        }
        if (cursor != null)
            cursor.close();
        return null;
    }

    public TaiKhoan login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TAIKHOAN, null,
                DatabaseHelper.KEY_USERNAME + "=? AND " + DatabaseHelper.KEY_PASSWORD + "=?",
                new String[] { username, password }, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan user = new TaiKhoan(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ROLE)));
            cursor.close();
            return user;
        }
        if (cursor != null)
            cursor.close();
        return null;
    }
}
