package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.TaoPhieuXuatActivity;
import com.example.project_lv1_mobile.XuatActivity;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuXuat;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.model.ProductType;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ViewHolder> {

    private final Context context;
    private List<ProductType> productTypeList;
    private final FirebaseCRUD crud;
    private String idMember;

    public ProductTypeAdapter(Context context, List<ProductType> productTypeList, FirebaseCRUD crud, String idMember) {
        this.context = context;
        this.productTypeList = productTypeList;
        this.crud = crud;
        this.idMember = idMember;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_recycler_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNameType.setText(productTypeList.get(position).getNameProductType());

        Glide.with(context).load(productTypeList.get(position).getTypeImageUri()).into(holder.ivImageType);
        ProductType productType = productTypeList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogUpdateProduct(productType);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameType;
        ImageView ivImageType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameType = itemView.findViewById(R.id.txtNameType);
            ivImageType = itemView.findViewById(R.id.ivImageType);
        }
    }

    private void openDialogUpdateProduct(ProductType productType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_update_type, null);
        builder.setView(view);
        Dialog dialogDialogUp = builder.create();
        dialogDialogUp.show();

        EditText edtUpdateTypeName = view.findViewById(R.id.edtUpdateTypeName);
        TextView txtTypeUpSub = view.findViewById(R.id.txtTypeUpSub);
        TextView txtCancelTypeUp = view.findViewById(R.id.txtCancelTypeUp);

        edtUpdateTypeName.setText(productType.getNameProductType());

        txtCancelTypeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDialogUp.dismiss();
            }
        });

        txtTypeUpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUpdateTypeName.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Không được để trống tên loại sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = edtUpdateTypeName.getText().toString();
                productType.setNameProductType(name);

                crud.updateType(productType);
                notifyDataSetChanged();
                dialogDialogUp.dismiss();
            }
        });
    }

}
