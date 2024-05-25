package com.example.project_lv1_mobile.fragment;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.PhieuXuat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class FragmentThongKeXuat extends Fragment {

    private BarChart barChartThongKeXuat;
    private TextView txtDaXuat, txtTongTienXuat, txtSoSPXuat, txtTongTienThue;

    public FragmentThongKeXuat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_thong_ke_xuat, container, false);

        barChartThongKeXuat = rootView.findViewById(R.id.barChartThongKeXuat);
        LinearLayout llListLoc = rootView.findViewById(R.id.llListLoc);
        ImageButton iBtnLocTKXuat = rootView.findViewById(R.id.iBtnLocTKXuat);
        ImageButton iBtnCancelLocTKXuat = rootView.findViewById(R.id.iBtnCancelLocTKXuat);

        txtDaXuat = rootView.findViewById(R.id.txtDaXuat);
        txtTongTienXuat = rootView.findViewById(R.id.txtTongTienXuat);
        txtSoSPXuat = rootView.findViewById(R.id.txtSoSPXuat);
        txtTongTienThue = rootView.findViewById(R.id.txtTongTienThue);

        List<PhieuXuat> phieuXuatList = new ArrayList<>();
        getDataPhieuXuat(phieuXuatList);

        iBtnLocTKXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llListLoc.setVisibility(View.VISIBLE);
                iBtnCancelLocTKXuat.setVisibility(View.VISIBLE);
                iBtnLocTKXuat.setVisibility(View.GONE);
            }
        });

        iBtnCancelLocTKXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llListLoc.setVisibility(View.GONE);
                iBtnCancelLocTKXuat.setVisibility(View.GONE);
                iBtnLocTKXuat.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    private void getDataPhieuXuat(List<PhieuXuat> phieuXuatList) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        ArrayList<BarEntry> entries = new ArrayList<>();

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
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    PhieuXuat phieuXuat = snapshot.toObject(PhieuXuat.class);
                                    phieuXuatList.add(phieuXuat);
                                }
                                for (int i = 0; i < 12; i++) {
                                    int count = 0;
                                    for (PhieuXuat px : phieuXuatList) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        try {
                                            Date date = dateFormat.parse(px.getNgayXuat());

                                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                                            String month = monthFormat.format(date);
                                            int thang = Integer.parseInt(month);

                                            if (thang == (i + 1)) {
                                                count++;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        entries.add(new BarEntry((float) i,(float) count));
                                    }
                                }
                                BarDataSet dataSet = new BarDataSet(entries, "");
                                dataSet.setColor(Color.BLUE);

                                barChartThongKeXuat.getDescription().setEnabled(false);

                                BarData barData = new BarData(dataSet);
                                barChartThongKeXuat.setData(barData);

                                barChartThongKeXuat.invalidate();

                                XAxis xAxis = barChartThongKeXuat.getXAxis();
                                ArrayList<String> monthNames = new ArrayList<>(Arrays.asList(
                                        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                                ));
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));
                                xAxis.setGranularity(0f);
                                xAxis.setLabelCount(monthNames.size());
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                                int demDaXuat = 0;
                                int demTienXuat = 0;
                                int demSoSP = 0;
                                int demThue = 0;

                                for (PhieuXuat px : phieuXuatList){
                                    demDaXuat++;
                                    demTienXuat += px.getTongTien();
                                    demSoSP += px.getTongSoSPXuat();
                                    demThue += px.getThue();
                                }

                                txtDaXuat.setText(Integer.toString(demDaXuat));
                                txtTongTienXuat.setText(Integer.toString(demTienXuat));
                                txtSoSPXuat.setText(Integer.toString(demSoSP));
                                txtTongTienThue.setText(Integer.toString(demThue));
                        }
                    }
                }
            }
        });
    }
}