package com.example.project_lv1_mobile.tempDAO;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.tempSQLite.TempDbHelper;

import java.util.ArrayList;
import java.util.List;

public class PhieuXuatChiTietDAO {
    private final TempDbHelper tempDbHelper;

    public PhieuXuatChiTietDAO(Context context) {
        tempDbHelper = new TempDbHelper(context);
    }


    public List<PhieuXuatChiTiet> selectAllPXCT(String idMember) {
        List<PhieuXuatChiTiet>list = new ArrayList<>();

        SQLiteDatabase database = tempDbHelper.getReadableDatabase();

        try {
            String query = "SELECT * FROM PhieuXuatChiTiet WHERE idMember=? ";
            String select[] = {idMember};
            Cursor cursor = database.rawQuery(query,select);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    list.add(new PhieuXuatChiTiet(cursor.getString(0),
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

    public boolean insert(PhieuXuatChiTiet phieuXuatChiTiet) {
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("idPhieuXuatCT", phieuXuatChiTiet.getIdPhieuXuatCT());
        values.put("idMember", phieuXuatChiTiet.getIdMember());
        values.put("idProduct", phieuXuatChiTiet.getIdProduct());
        values.put("soLuongXuat", phieuXuatChiTiet.getSoLuongXuat());
        values.put("soTien", phieuXuatChiTiet.getSoTien());

        long row = database.insert("PhieuXuatChiTiet", null, values);

        return row != -1;
    }

    public boolean update(PhieuXuatChiTiet phieuXuatChiTiet) {
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("soLuongXuat", phieuXuatChiTiet.getSoLuongXuat());
        values.put("soTien", phieuXuatChiTiet.getSoTien());

        long row = database.update("PhieuXuatChiTiet", values, "idPhieuXuatCT=?",
                new String[]{phieuXuatChiTiet.getIdPhieuXuatCT()});

        return row != -1;
    }

    public boolean delete(PhieuXuatChiTiet phieuXuatChiTiet) {
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        long row = database.delete("PhieuXuatChiTiet", "idPhieuXuatCT=?",
                new String[]{phieuXuatChiTiet.getIdPhieuXuatCT()});

        return row != -1;
    }

    public boolean completeInsertFirebase(String idMember){
        SQLiteDatabase database = tempDbHelper.getWritableDatabase();

        String whereClause = "idMember = ?";
        String[] whereArgs = new String[]{idMember};

        long row = database.delete("PhieuXuatChiTiet", whereClause, whereArgs);

        return row != -1;
    }
}
