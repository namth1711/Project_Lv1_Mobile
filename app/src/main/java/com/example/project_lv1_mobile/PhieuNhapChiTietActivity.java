package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PhieuNhapChiTietActivity extends AppCompatActivity {

    private Context context;
    private List<PhieuNhapChiTiet> phieuNhapChiTietList;
    private PhieuNhapChiTietAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_nhap_chi_tiet);

        Bundle bundle = getIntent().getExtras();
        String idPhieuNhap = bundle.getString("idPhieuNhap");

        context = PhieuNhapChiTietActivity.this;
        phieuNhapChiTietList = new ArrayList<>();
        adapter = new PhieuNhapChiTietAdapter(context, phieuNhapChiTietList);

        ImageButton iBtnThoatCTPN = findViewById(R.id.iBtnThoatCTPN);
        TextView txtMaPhieuNhap = findViewById(R.id.txtMaPhieuNhap);
        RecyclerView recyclerPhietNhapChiTiet = findViewById(R.id.recyclerPhietNhapChiTiet);

        txtMaPhieuNhap.setText(idPhieuNhap);

        listenFirebasePNCT(idPhieuNhap);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerPhietNhapChiTiet.setLayoutManager(manager);
        recyclerPhietNhapChiTiet.setAdapter(adapter);

        iBtnThoatCTPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void listenFirebasePNCT(String idPhieuNhap) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("PHIEUNHAPCHITIET").whereEqualTo("idPhieuNhap", idPhieuNhap)
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
                                        phieuNhapChiTietList.clear();
                                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                                            PhieuNhapChiTiet pnct = snapshot.toObject(PhieuNhapChiTiet.class);
                                            phieuNhapChiTietList.add(pnct);
                                        }
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case MODIFIED:  //  Khi có 1 document được cập nhật
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case REMOVED:   // Khi có 1 document bị xóa khỏi collection
                                        documentChange.getDocument().toObject(Product.class);
                                        phieuNhapChiTietList.remove(documentChange.getOldIndex());
                                        adapter.notifyDataSetChanged();
                                        break;
                                }
                            }
                        }
                    }
                });
    }
}