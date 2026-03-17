package com.example.approbotraicay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppRobotTraiCay.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_TAIKHOAN = "taikhoan";
    public static final String TABLE_NHOMSANPHAM = "nhomsanpham";
    public static final String TABLE_SANPHAM = "sanpham";

    // Common column names
    public static final String KEY_ID = "id";

    // TaiKhoan Table Columns
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ROLE = "role";

    // NhomSanPham Table Columns
    public static final String KEY_TENNHOM = "tennhom";
    public static final String KEY_HINHNHOM = "hinhnhom";

    // SanPham Table Columns
    public static final String KEY_TENSANPHAM = "tensanpham";
    public static final String KEY_GIA = "gia";
    public static final String KEY_HINHSANPHAM = "hinhsanpham";
    public static final String KEY_MOTA = "mota";
    public static final String KEY_IDNHOM = "idnhom";

    // Table Create Statements
    private static final String CREATE_TABLE_TAIKHOAN = "CREATE TABLE " + TABLE_TAIKHOAN + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USERNAME + " TEXT UNIQUE,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_FULLNAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_ROLE + " INTEGER DEFAULT 0" + ")";

    private static final String CREATE_TABLE_NHOMSANPHAM = "CREATE TABLE " + TABLE_NHOMSANPHAM + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TENNHOM + " TEXT,"
            + KEY_HINHNHOM + " TEXT" + ")";

    private static final String CREATE_TABLE_SANPHAM = "CREATE TABLE " + TABLE_SANPHAM + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TENSANPHAM + " TEXT,"
            + KEY_GIA + " REAL,"
            + KEY_HINHSANPHAM + " TEXT,"
            + KEY_MOTA + " TEXT,"
            + KEY_IDNHOM + " INTEGER,"
            + "FOREIGN KEY(" + KEY_IDNHOM + ") REFERENCES " + TABLE_NHOMSANPHAM + "(" + KEY_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TAIKHOAN);
        db.execSQL(CREATE_TABLE_NHOMSANPHAM);
        db.execSQL(CREATE_TABLE_SANPHAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAIKHOAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHOMSANPHAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        onCreate(db);
    }
}
