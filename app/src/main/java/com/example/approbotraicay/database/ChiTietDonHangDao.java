package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.ChiTietDonHang;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHangDao {
    private DatabaseHelper dbHelper;

    public ChiTietDonHangDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(ChiTietDonHang ctdh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_IDORDER, ctdh.getOrderId());
        values.put(DatabaseHelper.KEY_IDPRODUCT, ctdh.getProductId());
        values.put(DatabaseHelper.KEY_PRODUCTNAME, ctdh.getProductName());
        values.put(DatabaseHelper.KEY_QUANTITY, ctdh.getQuantity());
        values.put(DatabaseHelper.KEY_PRICE, ctdh.getPrice());

        db.insert(DatabaseHelper.TABLE_CHITIETDONHANG, null, values);
        db.close();
    }

    public List<ChiTietDonHang> getDetailsByOrderId(int orderId) {
        List<ChiTietDonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CHITIETDONHANG, null,
                DatabaseHelper.KEY_IDORDER + "=?", new String[]{String.valueOf(orderId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChiTietDonHang ctdh = new ChiTietDonHang(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_IDORDER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_IDPRODUCT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRODUCTNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PRICE))
                );
                list.add(ctdh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
