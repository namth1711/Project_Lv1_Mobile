package com.example.project_lv1_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_lv1_mobile.adapter.ChonSPAdapter;
import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.tempDAO.PhieuNhapChiTietDAO;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NhapActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_nhap,
                findViewById(R.id.frameLayoutMain), true);

        frameLayoutMain.setVisibility(View.VISIBLE);
        linearLayoutHome.setVisibility(View.GONE);
        iBtnTaiKhoan.setVisibility(View.GONE);
        txtTitleToolBarMain.setText("Nhập Hàng");

        visibleItem = true;
        nhapORxuat = 0;

        Context context = NhapActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idMember = bundle.getString("idMember");

        List<PhieuNhapChiTiet> nhapChiTietList = new ArrayList<>();
        PhieuNhapChiTietDAO phieuNhapChiTietDAO = new PhieuNhapChiTietDAO(context);
        ChonSPAdapter chonSPAdapter = new ChonSPAdapter(context, nhapChiTietList,
                phieuNhapChiTietDAO, idMember);

        RecyclerView recyclerNhap = findViewById(R.id.recyclerNhap);
        TextView txtTenNguoiNhap = findViewById(R.id.txtTenNguoiNhap);
        TextView txtNgayNhap = findViewById(R.id.txtNgayNhap);
        TextView txtNhapSub = findViewById(R.id.txtNhapSub);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firestore.collection("MEMBER")
                .document(idMember);
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            Member member = snapshot.toObject(Member.class);
            txtTenNguoiNhap.setText("Người nhập: " + member.getLastName() + " " + member.getFirtName());
        });

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);

        txtNgayNhap.setText("Ngày: " + String.format("%d/%d/%d", day, month, year));

        nhapChiTietList.addAll(phieuNhapChiTietDAO.selectAllPNCT(idMember));

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerNhap.setLayoutManager(manager);
        recyclerNhap.setAdapter(chonSPAdapter);

        txtNhapSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = 0;
                for (int i = 0; i < nhapChiTietList.size(); i++) {
                    size++;
                }
                if (size == 0) {
                    Toast.makeText(context, "Vui lòng chọn sản phẩm nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, TaoPhieuNhapActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}