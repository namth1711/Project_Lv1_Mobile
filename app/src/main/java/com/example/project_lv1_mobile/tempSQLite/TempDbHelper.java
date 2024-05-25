package com.example.project_lv1_mobile.tempSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TempDbHelper extends SQLiteOpenHelper {

    public TempDbHelper(@Nullable Context context) {
        super(context, "Database", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePhieuNhapCT = "CREATE TABLE PhieuNhapChiTiet(" +
                "idPhieuNhapCT TEXT PRIMARY KEY," +
                "idMember TEXT," +
                "idProduct TEXT," +
                "soLuongNhap INTEGER," +
                "soTien INTEGER);";
        db.execSQL(tablePhieuNhapCT);

        String tablePhieuXuatCT = "CREATE TABLE PhieuXuatChiTiet(" +
                "idPhieuXuatCT TEXT PRIMARY KEY," +
                "idMember TEXT," +
                "idProduct TEXT," +
                "soLuongXuat INTEGER," +
                "soTien INTEGER);";
        db.execSQL(tablePhieuXuatCT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            String dropTablePhieuNhapCT = "DROP TABLE IF EXISTS PhieuNhapChiTiet";
            String dropTablePhieuXuatCT = "DROP TABLE IF EXISTS PhieuXuatChiTiet";
            db.execSQL(dropTablePhieuNhapCT);
            db.execSQL(dropTablePhieuXuatCT);

            onCreate(db);
        }
    }
}
