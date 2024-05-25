package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_lv1_mobile.adapter.ViewPager2ThongKeAdapter;
import com.example.project_lv1_mobile.model.PhieuXuat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

public class DoanhThuActivity extends MainActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_doanh_thu,
                findViewById(R.id.frameLayoutMain), true);

        context = DoanhThuActivity.this;
        frameLayoutMain.setVisibility(View.VISIBLE);
        linearLayoutHome.setVisibility(View.GONE);
        iBtnTaiKhoan.setVisibility(View.GONE);
        txtTitleToolBarMain.setText("Thống Kê");

        TabLayout tabLayoutThongKe = findViewById(R.id.tabLayoutThongKe);
        ViewPager2 viewPager2ThongKe = findViewById(R.id.viewPager2ThongKe);

        ViewPager2ThongKeAdapter adapter = new ViewPager2ThongKeAdapter(this);
        viewPager2ThongKe.setAdapter(adapter);

        new TabLayoutMediator(tabLayoutThongKe, viewPager2ThongKe, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Thống kê xuất");
                        break;
                    case 1:
                        tab.setText("Thống kê nhập");
                        break;
                }
            }
        }).attach();
    }
}