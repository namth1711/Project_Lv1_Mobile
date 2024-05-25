package com.example.project_lv1_mobile.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_lv1_mobile.R;

import com.example.project_lv1_mobile.adapter.PhieuNhapAdapter;
import com.example.project_lv1_mobile.adapter.PhieuXuatAdapter;

import com.example.project_lv1_mobile.model.PhieuNhap;
import com.example.project_lv1_mobile.model.PhieuXuat;
import com.example.project_lv1_mobile.model.Product;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FragmentLichSuXuat extends Fragment {

    private Context context;
    private List<PhieuXuat> phieuXuatList;
    private PhieuXuatAdapter adapter;


    public FragmentLichSuXuat() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lich_su_xuat, container, false);

        phieuXuatList = new ArrayList<>();
        adapter = new PhieuXuatAdapter(context, phieuXuatList);

        listenFirebasePhieuXuat();

        RecyclerView recyclerPhieuXuat = rootView.findViewById(R.id.recyclerPhieuXuat);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerPhieuXuat.setLayoutManager(manager);
        recyclerPhieuXuat.setAdapter(adapter);

        return rootView;
    }

    private void listenFirebasePhieuXuat() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("PHIEUXUAT").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                phieuXuatList.clear();
                                List<PhieuXuat> list = new ArrayList<>();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    PhieuXuat phieuXuat = snapshot.toObject(PhieuXuat.class);
                                    list.add(phieuXuat);

                                    Collections.sort(list, new Comparator<PhieuXuat>() {
                                        @Override
                                        public int compare(PhieuXuat o1, PhieuXuat o2) {
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            try {
                                                String[] time1 = o1.getNgayXuat().split("/");
                                                String[] time2 = o2.getNgayXuat().split("/");

                                                Date date1 = dateFormat.parse(o1.getNgayXuat());
                                                Date date2 = dateFormat.parse(o2.getNgayXuat());

                                                int compareDate = date2.compareTo(date1);
                                                if (compareDate != 0) {
                                                    return compareDate;
                                                }

                                                int day1 = Integer.parseInt(time1[0]);
                                                int day2 = Integer.parseInt(time2[0]);
                                                if (day1 != day2) {
                                                    return day2 - day1;
                                                }

                                                int month1 = Integer.parseInt(time1[1]);
                                                int month2 = Integer.parseInt(time2[1]);
                                                if (month1 != month2) {
                                                    return month2 - month1;
                                                }

                                                int year1 = Integer.parseInt(time1[2]);
                                                int year2 = Integer.parseInt(time2[2]);
                                                return year2 - year1;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                return 0;
                                            }
                                        }
                                    });
                                }

                                phieuXuatList.addAll(list);
                                adapter.notifyDataSetChanged();
                                break;
                            case MODIFIED:  //  Khi có 1 document được cập nhật
                                adapter.notifyDataSetChanged();
                                break;
                            case REMOVED:   // Khi có 1 document bị xóa khỏi collection
                                documentChange.getDocument().toObject(Product.class);
                                phieuXuatList.remove(documentChange.getOldIndex());
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }
}