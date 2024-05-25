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
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.PhieuNhap;

import java.util.List;

public class PhieuNhapAdapter extends RecyclerView.Adapter<PhieuNhapAdapter.ViewHolder> {

    private final Context context;
    private List<PhieuNhap> phieuNhapList;


    public PhieuNhapAdapter(Context context, List<PhieuNhap> phieuNhapList) {
        this.context = context;
        this.phieuNhapList = phieuNhapList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_recycler_view_phieu_nhap, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtIDPhieuNhap.setText("Mã số phiếu: " + phieuNhapList.get(position).getIdPhieuNhap());
        holder.txtNgayTaoPNhap.setText("Ngày nhập hàng: " + phieuNhapList.get(position).getNgayNhap());
        holder.txtNguoiTaoPNhap.setText("Người nhập hàng: " + phieuNhapList.get(position).getMemberName());
        holder.txtTongTienPNhap.setText("Tổng thanh toán: " + phieuNhapList.get(position).getTongTien());

        String idPhieuNhap = phieuNhapList.get(position).getIdPhieuNhap();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(((Activity) context), PhieuNhapChiTietActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idPhieuNhap", idPhieuNhap);
                intent.putExtras(bundle);
                ((Activity) context).startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return phieuNhapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtIDPhieuNhap, txtNgayTaoPNhap, txtNguoiTaoPNhap, txtTongTienPNhap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIDPhieuNhap = itemView.findViewById(R.id.txtIDPhieuNhap);
            txtNgayTaoPNhap = itemView.findViewById(R.id.txtNgayTaoPNhap);
            txtNguoiTaoPNhap = itemView.findViewById(R.id.txtNguoiTaoPNhap);
            txtTongTienPNhap = itemView.findViewById(R.id.txtTongTienPNhap);
        }
    }
}
