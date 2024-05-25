package com.example.project_lv1_mobile;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_lv1_mobile.adapter.ChonSPXuatAdapter;
import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.tempDAO.PhieuXuatChiTietDAO;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class XuatActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_xuat,
                findViewById(R.id.frameLayoutMain), true);

        frameLayoutMain.setVisibility(View.VISIBLE);
        linearLayoutHome.setVisibility(View.GONE);
        iBtnTaiKhoan.setVisibility(View.GONE);
        txtTitleToolBarMain.setText("Xuất Hàng");

        visibleItem = true;
        nhapORxuat = 1;

        Context context = XuatActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idMember = bundle.getString("idMember");

        List<PhieuXuatChiTiet> phieuXuatChiTietList = new ArrayList<>();
        PhieuXuatChiTietDAO dao = new PhieuXuatChiTietDAO(context);
        ChonSPXuatAdapter adapter = new ChonSPXuatAdapter(context, phieuXuatChiTietList, dao, idMember);

        RecyclerView recyclerXuat = findViewById(R.id.recyclerXuat);
        TextView txtTenNguoiXuat = findViewById(R.id.txtTenNguoiXuat);
        TextView txtNgayXuat = findViewById(R.id.txtNgayXuat);
        TextView txtXuatSub = findViewById(R.id.txtXuatSub);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firestore.collection("MEMBER")
                .document(idMember);
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            Member member = snapshot.toObject(Member.class);
            txtTenNguoiXuat.setText("Người xuất: " + member.getLastName() + " " + member.getFirtName());
        });

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);

        txtNgayXuat.setText("Ngày: " + String.format("%d/%d/%d", day, month, year));

        phieuXuatChiTietList.addAll(dao.selectAllPXCT(idMember));

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerXuat.setLayoutManager(manager);
        recyclerXuat.setAdapter(adapter);

        txtXuatSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = 0;
                for (int i = 0; i < phieuXuatChiTietList.size(); i++) {
                    size++;
                }
                if (size == 0) {
                    Toast.makeText(context, "Vui lòng chọn sản phẩm xuất", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, TaoPhieuXuatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}