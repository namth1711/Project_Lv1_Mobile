package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;

import java.util.List;

public class PhieuNhapChiTietAdapter extends RecyclerView.Adapter<PhieuNhapChiTietAdapter.ViewHolder> {

    private final Context context;
    private List<PhieuNhapChiTiet> phieuNhapChiTietList;

    public PhieuNhapChiTietAdapter(Context context, List<PhieuNhapChiTiet> phieuNhapChiTietList) {
        this.context = context;
        this.phieuNhapChiTietList = phieuNhapChiTietList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.item_recycler_phieu_nhap_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTenSPPNCT.setText("Tên sản phẩm: " + phieuNhapChiTietList.get(position).getTenSP());
        holder.txtsoLuongNhapPNCT.setText("Số lượng: " + phieuNhapChiTietList.get(position).getSoLuongNhap());
        holder.txtsoTienPNCT.setText("Tiền nhập: " + phieuNhapChiTietList.get(position).getSoTien());
    }

    @Override
    public int getItemCount() {
        return phieuNhapChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTenSPPNCT, txtsoLuongNhapPNCT, txtsoTienPNCT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTenSPPNCT = itemView.findViewById(R.id.txtTenSPPNCT);
            txtsoLuongNhapPNCT = itemView.findViewById(R.id.txtsoLuongNhapPNCT);
            txtsoTienPNCT = itemView.findViewById(R.id.txtsoTienPNCT);
        }
    }
}
