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
        values.put(DatabaseHelper.KEY_USERNAME, dh.getUsername());
        values.put(DatabaseHelper.KEY_FULLNAME, dh.getFullName());
        values.put(DatabaseHelper.KEY_PHONE, dh.getPhone());
        values.put(DatabaseHelper.KEY_ADDRESS, dh.getAddress());
        values.put(DatabaseHelper.KEY_TOTAL, dh.getTotal());
        values.put(DatabaseHelper.KEY_DATE, dh.getDate());
        values.put(DatabaseHelper.KEY_STATUS, dh.getStatus());

        long id = db.insert(DatabaseHelper.TABLE_DONHANG, null, values);
        db.close();
        return id;
    }

    public List<DonHang> getOrdersByUsername(String username) {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DONHANG, null,
                DatabaseHelper.KEY_USERNAME + "=?", new String[]{username},
                null, null, DatabaseHelper.KEY_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DonHang dh = new DonHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_FULLNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ADDRESS)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_TOTAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_STATUS))
                );
                list.add(dh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
