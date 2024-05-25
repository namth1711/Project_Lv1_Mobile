package com.example.project_lv1_mobile.tempDAO;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.tempSQLite.TempDbHelper;

import java.util.ArrayList;
import java.util.List;

public class PhieuNhapChiTietDAO {
    private final TempDbHelper tempDbHelper;

    public PhieuNhapChiTietDAO(Context context) {
        tempDbHelper = new TempDbHelper(context);
    }

    public List<PhieuNhapChiTiet> selectAllPNCT(String idMember) {
        List<PhieuNhapChiTiet> list = new ArrayList<>();

        SQLiteDatabase database = tempDbHelper.getReadableDatabase();

        try {
            String query = "SELECT * FROM PhieuNhapChiTiet WHERE idMember=? ";
            String select[] = {idMember};
            Cursor cursor = database.rawQuery(query,select);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    list.add(new PhieuNhapChiTiet(cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4)));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(TAG, "BUG " + ex);
        }
        return list;
    }

    public boolean insert(PhieuNhapChiTiet phieuNhapChiTiet) {
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("idPhieuNhapCT", phieuNhapChiTiet.getIdPhieuNhapCT());
        values.put("idMember", phieuNhapChiTiet.getIdMember());
        values.put("idProduct", phieuNhapChiTiet.getIdProduct());
        values.put("soLuongNhap", phieuNhapChiTiet.getSoLuongNhap());
        values.put("soTien", phieuNhapChiTiet.getSoTien());

        long row = database.insert("PhieuNhapChiTiet", null, values);

        return row != -1;
    }

    public boolean update(PhieuNhapChiTiet phieuNhapChiTiet) {
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("soLuongNhap", phieuNhapChiTiet.getSoLuongNhap());
        values.put("soTien", phieuNhapChiTiet.getSoTien());

        long row = database.update("PhieuNhapChiTiet", values, "idPhieuNhapCT=?",
                new String[]{phieuNhapChiTiet.getIdPhieuNhapCT()});

        return row != -1;
    }

    public boolean delete(PhieuNhapChiTiet phieuNhapChiTiet) {
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        long row = database.delete("PhieuNhapChiTiet", "idPhieuNhapCT=?",
                new String[]{phieuNhapChiTiet.getIdPhieuNhapCT()});

        return row != -1;
    }

    public boolean completeInsertFirebase(String idMember){
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        String whereClause = "idMember = ?";
        String[] whereArgs = new String[]{idMember};

        long row = database.delete("PhieuNhapChiTiet", whereClause, whereArgs);

        return row != -1;
    }
}
