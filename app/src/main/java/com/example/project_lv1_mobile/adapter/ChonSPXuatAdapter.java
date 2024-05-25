package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.tempDAO.PhieuXuatChiTietDAO;
import com.example.project_lv1_mobile.widget.CustomQuantityPicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChonSPXuatAdapter extends RecyclerView.Adapter<ChonSPXuatAdapter.ViewHolder> {

    private final Context context;
    private final List<PhieuXuatChiTiet> phieuXuatChiTietList;
    private final PhieuXuatChiTietDAO dao;
    private String idMember;

    public ChonSPXuatAdapter(Context context, List<PhieuXuatChiTiet> phieuXuatChiTietList, PhieuXuatChiTietDAO dao, String idMember) {
        this.context = context;
        this.phieuXuatChiTietList = phieuXuatChiTietList;
        this.dao = dao;
        this.idMember = idMember;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_chon_sp_xuat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        PhieuXuatChiTiet phieuXuatChiTiet = phieuXuatChiTietList.get(position);
        holder.txtTongTienSPChonXuat.setText(Integer.toString(phieuXuatChiTiet.getSoTien()));

        DocumentReference reference = firestore.collection("PRODUCT")
                .document(phieuXuatChiTiet.getIdProduct());

        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            Product product = snapshot.toObject(Product.class);
            Glide.with(context).load(product.getProductImageUri()).into(holder.ivImageSPChonXuat);
            holder.txtTenSPChonXuat.setText(product.getProductName());
            holder.txtTonKhoSPChonXuat.setText(""+product.getQuantity());
            holder.txtGiaSPChonXuat.setText(Integer.toString(product.getUnitPrice()));

            int donGia = Integer.parseInt(holder.txtGiaSPChonXuat.getText().toString());
            int gioiHanSL = product.getQuantity();

            holder.cqpSoLuongSPXuat.setQuantity(phieuXuatChiTiet.getSoLuongXuat());
            holder.cqpSoLuongSPXuat.setMaxQuantity(gioiHanSL);
            holder.cqpSoLuongSPXuat.setMainQuantityTextView(holder.txtTongTienSPChonXuat, donGia, holder.txtSoLuongSPChonXuat);

        });
        holder.txtSoLuongSPChonXuat.setText(Integer.toString(phieuXuatChiTiet.getSoLuongXuat()));

        holder.iBtnBoChonXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dao.delete(phieuXuatChiTiet)) {
                    phieuXuatChiTietList.clear();
                    phieuXuatChiTietList.addAll(dao.selectAllPXCT(idMember));
                    notifyDataSetChanged();
                }
            }
        });

        holder.txtSoLuongSPChonXuat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phieuXuatChiTiet.setSoLuongXuat(Integer.parseInt(holder.txtSoLuongSPChonXuat.getText().toString()));
                phieuXuatChiTiet.setSoTien(Integer.parseInt(holder.txtTongTienSPChonXuat.getText().toString()));
                dao.update(phieuXuatChiTiet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phieuXuatChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImageSPChonXuat;
        TextView txtTenSPChonXuat, txtTonKhoSPChonXuat, txtGiaSPChonXuat, txtSoLuongSPChonXuat,
                txtTongTienSPChonXuat;
        CustomQuantityPicker cqpSoLuongSPXuat;
        ImageButton iBtnBoChonXuat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImageSPChonXuat = itemView.findViewById(R.id.ivImageSPChonXuat);
            txtTenSPChonXuat = itemView.findViewById(R.id.txtTenSPChonXuat);
            txtTonKhoSPChonXuat = itemView.findViewById(R.id.txtTonKhoSPChonXuat);
            txtGiaSPChonXuat = itemView.findViewById(R.id.txtGiaSPChonXuat);
            txtSoLuongSPChonXuat = itemView.findViewById(R.id.txtSoLuongSPChonXuat);
            txtTongTienSPChonXuat = itemView.findViewById(R.id.txtTongTienSPChonXuat);
            cqpSoLuongSPXuat = itemView.findViewById(R.id.cqpSoLuongSPXuat);
            iBtnBoChonXuat = itemView.findViewById(R.id.iBtnBoChonXuat);
        }
    }
}
