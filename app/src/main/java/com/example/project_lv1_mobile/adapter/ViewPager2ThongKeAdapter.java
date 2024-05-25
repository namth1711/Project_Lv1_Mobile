package com.example.project_lv1_mobile.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project_lv1_mobile.fragment.FragmentLichSuNhap;
import com.example.project_lv1_mobile.fragment.FragmentLichSuXuat;
import com.example.project_lv1_mobile.fragment.FragmentThongKeNhap;
import com.example.project_lv1_mobile.fragment.FragmentThongKeXuat;

public class ViewPager2ThongKeAdapter extends FragmentStateAdapter {

    public ViewPager2ThongKeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                FragmentThongKeXuat fragmentThongKeXuat = new FragmentThongKeXuat();
                return fragmentThongKeXuat;
            case 1:
                FragmentThongKeNhap fragmentThongKeNhap = new FragmentThongKeNhap();
                return fragmentThongKeNhap;
        }
        FragmentThongKeXuat fragmentThongKeXuat = new FragmentThongKeXuat();
        return fragmentThongKeXuat;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
