package com.example.approbotraicay.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.approbotraicay.model.TaiKhoan;

public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        ensureAdminAccountExists();
    }

    public long insert(TaiKhoan user) {
        ensureProfileColumnsExist();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tendn", user.getUsername());
        values.put("matkhau", user.getPassword());
        values.put("quyen", user.getRole() == 1 ? "admin" : "user");
        if (user.getFullName() != null) values.put("hoten", user.getFullName());
        if (user.getPhone() != null) values.put("sdt", user.getPhone());
        if (user.getEmail() != null) values.put("email", user.getEmail());
        if (user.getAddress() != null) values.put("diachi", user.getAddress());
        long id = db.insert("taikhoan", null, values);
        db.close();
        return id;
    }

    public TaiKhoan login(String username, String password) {
        ensureProfileColumnsExist();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM taikhoan WHERE tendn = ? AND matkhau = ?",
                new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan user = mapCursorToTaiKhoan(cursor);
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public TaiKhoan getUserByUsername(String username) {
        ensureProfileColumnsExist();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM taikhoan WHERE tendn = ?",
                new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            TaiKhoan user = mapCursorToTaiKhoan(cursor);
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public int updateProfile(TaiKhoan user) {
        ensureProfileColumnsExist();
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

    public java.util.List<TaiKhoan> getAllUsers() {
        java.util.List<TaiKhoan> list = new java.util.ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToTaiKhoan(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    public int deleteUser(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("taikhoan", "tendn = ?", new String[]{username});
        db.close();
        return result;
    }

    public int updateRole(String username, int role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quyen", role == 1 ? "admin" : "user");
        int result = db.update("taikhoan", values, "tendn = ?", new String[]{username});
        db.close();
        return result;
    }

    public int updateAccountStatus(String username, int status) {
        ensureStatusColumnExists();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", status); // 0: Active, 1: Locked
        int result = db.update("taikhoan", values, "tendn = ?", new String[]{username});
        db.close();
        return result;
    }

    /**
     * FIX: Đảm bảo các cột profile tồn tại trong bảng taikhoan.
     * DB gốc banhang.db có thể chỉ có tendn, matkhau, quyen.
     */
    private void ensureProfileColumnsExist() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try { db.execSQL("ALTER TABLE taikhoan ADD COLUMN hoten TEXT DEFAULT ''"); } catch (Exception e) {}
        try { db.execSQL("ALTER TABLE taikhoan ADD COLUMN sdt TEXT DEFAULT ''"); } catch (Exception e) {}
        try { db.execSQL("ALTER TABLE taikhoan ADD COLUMN email TEXT DEFAULT ''"); } catch (Exception e) {}
        try { db.execSQL("ALTER TABLE taikhoan ADD COLUMN diachi TEXT DEFAULT ''"); } catch (Exception e) {}
    }

    private void ensureStatusColumnExists() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try { db.execSQL("ALTER TABLE taikhoan ADD COLUMN trangthai INTEGER DEFAULT 0"); } catch (Exception e) {}
    }

    /**
     * Tự động tạo tài khoản admin mặc định (admin/admin) nếu trong DB chưa có
     */
    private void ensureAdminAccountExists() {
        TaiKhoan admin = getUserByUsername("admin");
        if (admin == null) {
            admin = new TaiKhoan();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setRole(1); // 1 = admin
            admin.setFullName("Quản trị viên");
            insert(admin);
        }
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
        user.setRole(role != null && role.equalsIgnoreCase("admin") ? 1 : 0);

        int statusIdx = cursor.getColumnIndex("trangthai");
        if (statusIdx != -1) user.setStatus(cursor.getInt(statusIdx));

        return user;
    }
}
