package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.tempDAO.PhieuNhapChiTietDAO;
import com.example.project_lv1_mobile.widget.CustomQuantityPicker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ChonSPAdapter extends RecyclerView.Adapter<ChonSPAdapter.ViewHolder> {

    private final Context context;
    private final List<PhieuNhapChiTiet> phieuNhapChiTietList;
    private final PhieuNhapChiTietDAO dao;
    private String idMember;

    public ChonSPAdapter(Context context, List<PhieuNhapChiTiet> phieuNhapChiTietList, PhieuNhapChiTietDAO dao, String idMember) {
        this.context = context;
        this.phieuNhapChiTietList = phieuNhapChiTietList;
        this.dao = dao;
        this.idMember = idMember;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_chon_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        holder.txtTongTienSPChon.setText(""+phieuNhapChiTietList.get(position).getSoTien());
        PhieuNhapChiTiet phieuNhapChiTiet = phieuNhapChiTietList.get(position);
        DocumentReference reference = firestore.collection("PRODUCT")
                .document(phieuNhapChiTietList.get(position).getIdProduct());

        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            Product product = snapshot.toObject(Product.class);
            Glide.with(context).load(product.getProductImageUri()).into(holder.ivImageSPChon);
            holder.txtTenSPChon.setText(product.getProductName());
            holder.txtGiaSPChon.setText(Integer.toString(product.getUnitPrice()));

            int donGia = Integer.parseInt(holder.txtGiaSPChon.getText().toString());

            holder.cqpSoLuongSP.setQuantity(phieuNhapChiTiet.getSoLuongNhap());
            holder.cqpSoLuongSP.setMaxQuantity(99);
            holder.cqpSoLuongSP.setMainQuantityTextView(holder.txtTongTienSPChon, donGia, holder.txtSoLuongSPChon);

        });

        holder.txtSoLuongSPChon.setText(Integer.toString(phieuNhapChiTietList.get(position).getSoLuongNhap()));

        holder.iBtnBoChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dao.delete(phieuNhapChiTiet)) {
                    phieuNhapChiTietList.clear();
                    phieuNhapChiTietList.addAll(dao.selectAllPNCT(idMember));
                    notifyDataSetChanged();
                }
            }
        });

        holder.txtSoLuongSPChon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phieuNhapChiTiet.setSoLuongNhap(
                        Integer.parseInt(holder.txtSoLuongSPChon.getText().toString()));
                phieuNhapChiTiet.setSoTien(
                        Integer.parseInt(holder.txtTongTienSPChon.getText().toString()));
                dao.update(phieuNhapChiTiet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phieuNhapChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImageSPChon, iBtnBoChon;
        TextView txtTenSPChon, txtTongTienSPChon, txtSoLuongSPChon, txtGiaSPChon;
        CustomQuantityPicker cqpSoLuongSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImageSPChon = itemView.findViewById(R.id.ivImageSPChon);
            iBtnBoChon = itemView.findViewById(R.id.iBtnBoChon);
            txtTenSPChon = itemView.findViewById(R.id.txtTenSPChon);
            txtTongTienSPChon = itemView.findViewById(R.id.txtTongTienSPChon);
            txtSoLuongSPChon = itemView.findViewById(R.id.txtSoLuongSPChon);
            txtGiaSPChon = itemView.findViewById(R.id.txtGiaSPChon);
            cqpSoLuongSP = itemView.findViewById(R.id.cqpSoLuongSP);

        }
    }
}
