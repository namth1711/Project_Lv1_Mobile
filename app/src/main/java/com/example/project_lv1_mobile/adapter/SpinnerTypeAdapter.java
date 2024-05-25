package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.model.ProductType;

import java.util.List;

public class SpinnerTypeAdapter extends BaseAdapter {

    private final Context context;
    private List<ProductType> productTypeList;

    public SpinnerTypeAdapter(Context context, List<ProductType> productTypeList) {
        this.context = context;
        this.productTypeList = productTypeList;
    }

    @Override
    public int getCount() {
        return productTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return productTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_spinner_type, parent, false);
        }

        TextView txtTypeNameSpinner = convertView.findViewById(R.id.txtTypeNameSpinner);
        txtTypeNameSpinner.setText(productTypeList.get(position).getNameProductType());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_spinner_dropdown_type, parent, false);
        }

        ImageView ivImageTypeDropdown = convertView.findViewById(R.id.ivImageTypeDropdown);
        TextView txtTypeNameDropdown = convertView.findViewById(R.id.txtTypeNameDropdown);

        Glide.with(context).load(productTypeList.get(position).getTypeImageUri()).into(ivImageTypeDropdown);
        txtTypeNameDropdown.setText(productTypeList.get(position).getNameProductType());

        return convertView;
    }
}
