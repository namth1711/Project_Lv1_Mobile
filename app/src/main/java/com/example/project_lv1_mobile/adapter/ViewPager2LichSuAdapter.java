package com.example.project_lv1_mobile.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project_lv1_mobile.fragment.FragmentLichSuNhap;
import com.example.project_lv1_mobile.fragment.FragmentLichSuXuat;

public class ViewPager2LichSuAdapter extends FragmentStateAdapter {

    public ViewPager2LichSuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                FragmentLichSuXuat fragmentLichSuXuat = new FragmentLichSuXuat();
                return fragmentLichSuXuat;
            case 1:
                FragmentLichSuNhap fragmentLichSuNhap = new FragmentLichSuNhap();
                return fragmentLichSuNhap;
        }
        FragmentLichSuXuat fragmentLichSuXuat = new FragmentLichSuXuat();
        return fragmentLichSuXuat;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
