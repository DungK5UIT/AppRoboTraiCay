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
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabase();
    }

    private void copyDatabase() {
        try {
            File dbPath = context.getDatabasePath(DATABASE_NAME);
            if (!dbPath.exists()) {
                Log.d("DatabaseHelper", "Copying database from assets...");
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
                Log.d("DatabaseHelper", "Database copied successfully.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error copying database: " + e.getMessage());
        }
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
