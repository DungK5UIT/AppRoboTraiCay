package com.example.approbotraicay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "banhang.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;
    private final Context context;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabase();
    }

    private void copyDatabase() {
        try {
            File dbPath = context.getDatabasePath(DATABASE_NAME);
            if (!dbPath.exists()) {
                File dbDir = dbPath.getParentFile();
                if (dbDir != null && !dbDir.exists()) {
                    dbDir.mkdirs(); // Tạo thư mục databases nếu chưa tồn tại (giống Appbanhang)
                }
                
                Log.d("DatabaseHelper", "Bắt đầu copy database từ assets...");
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(dbPath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                Log.d("DatabaseHelper", "COPY TỪ ASSETS THÀNH CÔNG RỒI NHÉ!");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "LỖI COPY DATABASE: " + e.getMessage());
        }
    }

    /**
     * Dev A Week 12-13: Database Performance & Security Optimization
     * Runs index creation, database verification, and cleanups.
     */
    public void optimizeDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Create indexes to optimize JOIN operations in reports
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_chitiet_dathang ON Chitietdonhang(id_dathang)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_chitiet_masp ON Chitietdonhang(masp)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_sanpham_maso ON sanpham(maso)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_dathang_ngay ON Dathang(ngaydathang)");
            
            // 2. Perform integrity check
            android.database.Cursor cursor = db.rawQuery("PRAGMA integrity_check", null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String result = cursor.getString(0);
                    Log.d("DatabaseHelper", "Database Integrity Check Result: " + result);
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Optimization error: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
        
        // 3. Compact database using VACUUM outside of transactions
        try {
            db.execSQL("VACUUM");
            Log.d("DatabaseHelper", "Database compacted successfully (VACUUM).");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Vacuum error: " + e.getMessage());
        }
    }

    /**
     * Dev A Week 12-13: Database Backup & Recovery feature.
     * Backs up the current database file to external storage or a custom dir.
     */
    public boolean backupDatabase(File backupFile) {
        try {
            File currentDb = context.getDatabasePath(DATABASE_NAME);
            if (currentDb.exists()) {
                InputStream is = new java.io.FileInputStream(currentDb);
                OutputStream os = new FileOutputStream(backupFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
                Log.d("DatabaseHelper", "Database backup completed successfully.");
                return true;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Backup failed: " + e.getMessage());
        }
        return false;
    }

    public boolean restoreDatabase(File backupFile) {
        try {
            File currentDb = context.getDatabasePath(DATABASE_NAME);
            if (backupFile.exists()) {
                InputStream is = new java.io.FileInputStream(backupFile);
                OutputStream os = new FileOutputStream(currentDb);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
                Log.d("DatabaseHelper", "Database restoration completed successfully.");
                return true;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Restore failed: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Database is copied from assets, so we don't need to create tables
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrade if needed
    }
}
