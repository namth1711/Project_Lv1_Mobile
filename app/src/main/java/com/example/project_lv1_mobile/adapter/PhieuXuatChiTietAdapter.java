package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;

import java.util.List;

public class PhieuXuatChiTietAdapter extends RecyclerView.Adapter<PhieuXuatChiTietAdapter.ViewHolder> {

    private final Context context;
    private List<PhieuXuatChiTiet> phieuXuatChiTietList;

    public PhieuXuatChiTietAdapter(Context context, List<PhieuXuatChiTiet> phieuXuatChiTietList) {
        this.context = context;
        this.phieuXuatChiTietList = phieuXuatChiTietList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.item_recycler_phieu_xuat_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTenSPPXCT.setText("Tên sản phẩm: "+phieuXuatChiTietList.get(position).getTenSP());
        holder.txtsoLuongXuatPXCT.setText("Số lượng "+phieuXuatChiTietList.get(position).getSoLuongXuat());
        holder.txtsoTienPXCT.setText("Tiền xuất: "+phieuXuatChiTietList.get(position).getSoTien());
    }

    @Override
    public int getItemCount() {
        return phieuXuatChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTenSPPXCT, txtsoLuongXuatPXCT, txtsoTienPXCT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSPPXCT = itemView.findViewById(R.id.txtTenSPPXCT);
            txtsoLuongXuatPXCT = itemView.findViewById(R.id.txtsoLuongXuatPXCT);
            txtsoTienPXCT = itemView.findViewById(R.id.txtsoTienPXCT);
        }
    }
}
