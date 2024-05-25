package com.example.project_lv1_mobile;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_lv1_mobile.adapter.ViewPager2LichSuAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LichSuActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_lich_su,
                findViewById(R.id.frameLayoutMain), true);

        frameLayoutMain.setVisibility(View.VISIBLE);
        linearLayoutHome.setVisibility(View.GONE);
        iBtnTaiKhoan.setVisibility(View.GONE);
        txtTitleToolBarMain.setText("Lịch Sử");

        TabLayout tabLayoutLichSu = findViewById(R.id.tabLayoutLichSu);
        ViewPager2 viewPager2LichSu = findViewById(R.id.viewPager2LichSu);

        ViewPager2LichSuAdapter adapter = new ViewPager2LichSuAdapter(this);
        viewPager2LichSu.setAdapter(adapter);

        new TabLayoutMediator(tabLayoutLichSu, viewPager2LichSu, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Lịch sử xuất");
                        break;
                    case 1:
                        tab.setText("Lịch sử nhập");
                        break;
                }
            }
        }).attach();
    }
}