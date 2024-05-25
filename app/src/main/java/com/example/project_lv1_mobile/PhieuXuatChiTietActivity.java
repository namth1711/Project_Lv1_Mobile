package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.project_lv1_mobile.adapter.PhieuNhapChiTietAdapter;
import com.example.project_lv1_mobile.adapter.PhieuXuatChiTietAdapter;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PhieuXuatChiTietActivity extends AppCompatActivity {

    private Context context;
    private List<PhieuXuatChiTiet> phieuXuatChiTietList;
    private PhieuXuatChiTietAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_xuat_chi_tiet);

        Bundle bundle = getIntent().getExtras();
        String idPhieuXuat = bundle.getString("idPhieuXuat");

        context = PhieuXuatChiTietActivity.this;
        phieuXuatChiTietList = new ArrayList<>();
        adapter = new PhieuXuatChiTietAdapter(context, phieuXuatChiTietList);

        ImageButton iBtnThoatCTPX = findViewById(R.id.iBtnThoatCTPX);
        TextView txtMaPhieuXuat = findViewById(R.id.txtMaPhieuXuat);
        RecyclerView recyclerPhietXuatChiTiet = findViewById(R.id.recyclerPhietXuatChiTiet);

        txtMaPhieuXuat.setText(idPhieuXuat);

        listenFirebasePXCT(idPhieuXuat);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerPhietXuatChiTiet.setLayoutManager(manager);
        recyclerPhietXuatChiTiet.setAdapter(adapter);

        iBtnThoatCTPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void listenFirebasePXCT(String idPhieuXuat) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("PHIEUXUATCHITIET").whereEqualTo("idPhieuXuat", idPhieuXuat)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "Fail", error);
                            return;
                        }
                        if (value != null) {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                switch (documentChange.getType()) {
                                    case ADDED: //  Khi chỉ có document được thêm
                                        phieuXuatChiTietList.clear();
                                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                                            PhieuXuatChiTiet pxct = snapshot.toObject(PhieuXuatChiTiet.class);
                                            phieuXuatChiTietList.add(pxct);
                                        }
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case MODIFIED:  //  Khi có 1 document được cập nhật
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case REMOVED:   // Khi có 1 document bị xóa khỏi collection
                                        documentChange.getDocument().toObject(Product.class);
                                        phieuXuatChiTietList.remove(documentChange.getOldIndex());
                                        adapter.notifyDataSetChanged();
                                        break;
                                }
                            }
                        }
                    }
                });
    }
}