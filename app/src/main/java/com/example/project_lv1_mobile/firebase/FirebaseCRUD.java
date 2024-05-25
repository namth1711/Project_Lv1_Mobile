package com.example.project_lv1_mobile.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuNhap;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.PhieuXuat;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.model.ProductType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FirebaseCRUD {
    private final FirebaseFirestore firestore;
    private final Context context;
    private final String COLLECTION_MEMBER = "MEMBER";
    private final String COLLECTION_PRODUCT = "PRODUCT";
    private final String COLLECTION_TYPE = "TYPE";
    private final String COLLECTION_PHIEU_NHAP = "PHIEUNHAP";
    private final String COLLECTION_PHIEU_XUAT = "PHIEUXUAT";
    private final String COLLECTION_PHIEU_XUAT_CT = "PHIEUXUATCHITIET";
    private final String COLLECTION_PHIEU_NHAP_CT = "PHIEUNHAPCHITIET";


    public FirebaseCRUD(FirebaseFirestore firestore, Context context) {
        this.firestore = firestore;
        this.context = context;
    }

    //  MEMBER
    public void addMember(Member member) {
        HashMap<String, Object> mapMember = member.objectMember();
        firestore.collection(COLLECTION_MEMBER).document(member.getIdMember())
                .set(mapMember).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateMember(Member member) {
        firestore.collection(COLLECTION_MEMBER).document(member.getIdMember()).update(member.objectMember())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    // PRODUCT TYPE
    public void addProductType(ProductType type) {
        HashMap<String, Object> mapType = type.objectType();
        firestore.collection(COLLECTION_TYPE).document(type.getIdType())
                .set(mapType).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm loại sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    public void updateType(ProductType type) {
        firestore.collection(COLLECTION_TYPE).document(type.getIdType()).update(type.objectType())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    //  PRODUCT
    public void addProduct(Product product) {
        HashMap<String, Object> mapProduct = product.objectProduct();
        firestore.collection(COLLECTION_PRODUCT).document(product.getIdProduct())
                .set(mapProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    public void updateProduct(Product product) {
        firestore.collection(COLLECTION_PRODUCT).document(product.getIdProduct()).update(product.objectProduct())
                .addOnSuccessListener(aVoid -> {

                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    //  PHIEU NHAP
    public void taoPhieuNhap(PhieuNhap phieuNhap) {
        HashMap<String, Object> mapPhieuNhap = phieuNhap.objectPhieuNhap();
        firestore.collection(COLLECTION_PHIEU_NHAP).document(phieuNhap.getIdPhieuNhap())
                .set(mapPhieuNhap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    public void updatePhieuNhap(PhieuNhap phieuNhap) {
        firestore.collection(COLLECTION_PHIEU_NHAP).document(phieuNhap.getIdPhieuNhap()).update(phieuNhap.objectPhieuNhap())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    //  PHIEUXUAT
    public void taoPhieuXuat(PhieuXuat phieuXuat) {
        HashMap<String, Object> mapPhieuXuat = phieuXuat.objectPhieuXuat();
        firestore.collection(COLLECTION_PHIEU_XUAT).document(phieuXuat.getIdPhieuXuat())
                .set(mapPhieuXuat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    public void updatePhieuXuat(PhieuXuat phieuXuat) {
        firestore.collection(COLLECTION_PHIEU_XUAT).document(phieuXuat.getIdPhieuXuat()).update(phieuXuat.objectPhieuXuat())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    //  PHIEUXUATCHITIET
    public void taoPhieuXuatChiTiet(PhieuXuatChiTiet phieuXuatChiTiet){
        HashMap<String, Object> mapPhieuXuatCT = phieuXuatChiTiet.objectPhieuXuatChiTiet();
        firestore.collection(COLLECTION_PHIEU_XUAT_CT).document(phieuXuatChiTiet.getIdPhieuXuatCT())
                .set(mapPhieuXuatCT).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }

    //  PHIEUNHAPCHITIET
    public void taoPhieuNhapChiTiet(PhieuNhapChiTiet phieuNhapChiTiet){
        HashMap<String, Object> mapPhieuNhapCT = phieuNhapChiTiet.objectPhieuNhapChiTiet();
        firestore.collection(COLLECTION_PHIEU_NHAP_CT).document(phieuNhapChiTiet.getIdPhieuNhapCT())
                .set(mapPhieuNhapCT).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(e -> {
                    Log.e("Error", "Fail" + e);
                });
    }
}
