package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_lv1_mobile.PhieuNhapChiTietActivity;
import com.example.project_lv1_mobile.PhieuXuatChiTietActivity;
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.PhieuXuat;

import java.util.List;

public class PhieuXuatAdapter extends RecyclerView.Adapter<PhieuXuatAdapter.ViewHolder> {

    private final Context context;
    private List<PhieuXuat> phieuXuatList;

    public PhieuXuatAdapter(Context context, List<PhieuXuat> phieuXuatList) {
        this.context = context;
        this.phieuXuatList = phieuXuatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_recycler_view_phieu_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtIDPhieuXuat.setText("Mã số phiếu: " + phieuXuatList.get(position).getIdPhieuXuat());
        holder.txtNgayTaoPXuat.setText("Ngày xuất hàng: " + phieuXuatList.get(position).getNgayXuat());
        holder.txtNguoiTaoPXuat.setText("Người nhập hàng: " + phieuXuatList.get(position).getMemberName());
        holder.txtTongTienPXuat.setText("Tổng thanh toán: " + phieuXuatList.get(position).getTongTien());
        holder.txtThuePXuat.setText("Thuế: " + phieuXuatList.get(position).getThue());

        String idPhieuXuat = phieuXuatList.get(position).getIdPhieuXuat();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(((Activity) context), PhieuXuatChiTietActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idPhieuXuat", idPhieuXuat);
                intent.putExtras(bundle);
                ((Activity) context).startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return phieuXuatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtIDPhieuXuat, txtNgayTaoPXuat, txtNguoiTaoPXuat, txtTongTienPXuat, txtThuePXuat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIDPhieuXuat = itemView.findViewById(R.id.txtIDPhieuXuat);
            txtNgayTaoPXuat = itemView.findViewById(R.id.txtNgayTaoPXuat);
            txtNguoiTaoPXuat = itemView.findViewById(R.id.txtNguoiTaoPXuat);
            txtTongTienPXuat = itemView.findViewById(R.id.txtTongTienPXuat);
            txtThuePXuat = itemView.findViewById(R.id.txtThuePXuat);
        }
    }
}
