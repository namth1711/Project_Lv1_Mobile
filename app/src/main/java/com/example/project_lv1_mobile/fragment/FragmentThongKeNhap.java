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
import com.example.project_lv1_mobile.model.PhieuNhap;
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


public class FragmentThongKeNhap extends Fragment {

    private BarChart barChartThongKeNhap;
    private TextView txtDaNhap, txtTongTienNhap, txtSoSPNhap;
    public FragmentThongKeNhap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_thong_ke_nhap, container, false);

        barChartThongKeNhap = rootView.findViewById(R.id.barChartThongKeNhap);
        LinearLayout llListLoc = rootView.findViewById(R.id.llListLoc);
        ImageButton iBtnLocTKNhap = rootView.findViewById(R.id.iBtnLocTKNhap);
        ImageButton iBtnCancelLocTKNhap = rootView.findViewById(R.id.iBtnCancelLocTKNhap);

        txtDaNhap = rootView.findViewById(R.id.txtDaNhap);
        txtTongTienNhap = rootView.findViewById(R.id.txtTongTienNhap);
        txtSoSPNhap = rootView.findViewById(R.id.txtSoSPNhap);

        List<PhieuNhap> phieuNhapList = new ArrayList<>();
        getDataPhieuNhap(phieuNhapList);

        iBtnLocTKNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llListLoc.setVisibility(View.VISIBLE);
                iBtnCancelLocTKNhap.setVisibility(View.VISIBLE);
                iBtnLocTKNhap.setVisibility(View.GONE);
            }
        });

        iBtnCancelLocTKNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llListLoc.setVisibility(View.GONE);
                iBtnCancelLocTKNhap.setVisibility(View.GONE);
                iBtnLocTKNhap.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    private void getDataPhieuNhap(List<PhieuNhap> phieuNhapList) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        ArrayList<BarEntry> entries = new ArrayList<>();

        firestore.collection("PHIEUNHAP").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                phieuNhapList.clear();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    PhieuNhap phieuNhap = snapshot.toObject(PhieuNhap.class);
                                    phieuNhapList.add(phieuNhap);
                                }
                                for (int i = 0; i < 12; i++) {
                                    int count = 0;
                                    for (PhieuNhap pn : phieuNhapList) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        try {
                                            Date date = dateFormat.parse(pn.getNgayNhap());

                                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                                            String month = monthFormat.format(date);
                                            int thang = Integer.parseInt(month);

                                            if (thang == (i + 1)) {
                                                count++;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        entries.add(new BarEntry((float) i, (float) count));
                                    }
                                }
                                BarDataSet dataSet = new BarDataSet(entries, "");
                                dataSet.setColor(Color.BLUE);

                                barChartThongKeNhap.getDescription().setEnabled(false);

                                BarData barData = new BarData(dataSet);
                                barChartThongKeNhap.setData(barData);

                                barChartThongKeNhap.invalidate();

                                XAxis xAxis = barChartThongKeNhap.getXAxis();
                                ArrayList<String> monthNames = new ArrayList<>(Arrays.asList(
                                        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                                ));
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(monthNames));
                                xAxis.setGranularity(0f);
                                xAxis.setLabelCount(monthNames.size());
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                                int demDaNhap = 0;
                                int demTienNhap = 0;
                                int demSoSP = 0;


                                for (PhieuNhap pn : phieuNhapList){
                                    demDaNhap++;
                                    demTienNhap += pn.getTongTien();
                                    demSoSP += pn.getTongSoSPNhap();

                                }

                                txtDaNhap.setText(Integer.toString(demDaNhap));
                                txtTongTienNhap.setText(Integer.toString(demTienNhap));
                                txtSoSPNhap.setText(Integer.toString(demSoSP));

                        }
                    }
                }
            }
        });
    }
}