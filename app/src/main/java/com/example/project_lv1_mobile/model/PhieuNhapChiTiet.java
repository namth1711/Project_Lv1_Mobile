package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class PhieuNhapChiTiet implements Serializable {

    private String idPhieuNhapCT, idPhieuNhap, idMember, idProduct, tenSP;
    private int soLuongNhap, soTien;

    public PhieuNhapChiTiet() {
    }

    public PhieuNhapChiTiet(String idPhieuNhapCT,String idMember, String idProduct, int soLuongNhap, int soTien) {
        this.idPhieuNhapCT = idPhieuNhapCT;
        this.idMember = idMember;
        this.idProduct = idProduct;
        this.soLuongNhap = soLuongNhap;
        this.soTien = soTien;
    }

    public String getTenSP() {
        return tenSP;
    }

    public PhieuNhapChiTiet setTenSP(String tenSP) {
        this.tenSP = tenSP;
        return this;
    }

    public String getIdMember() {
        return idMember;
    }

    public PhieuNhapChiTiet setIdMember(String idMember) {
        this.idMember = idMember;
        return this;
    }

    public String getIdPhieuNhapCT() {
        return idPhieuNhapCT;
    }

    public PhieuNhapChiTiet setIdPhieuNhapCT(String idPhieuNhapCT) {
        this.idPhieuNhapCT = idPhieuNhapCT;
        return this;
    }

    public String getIdPhieuNhap() {
        return idPhieuNhap;
    }

    public PhieuNhapChiTiet setIdPhieuNhap(String idPhieuNhap) {
        this.idPhieuNhap = idPhieuNhap;
        return this;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public PhieuNhapChiTiet setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public int getSoLuongNhap() {
        return soLuongNhap;
    }

    public PhieuNhapChiTiet setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
        return this;
    }

    public int getSoTien() {
        return soTien;
    }

    public PhieuNhapChiTiet setSoTien(int soTien) {
        this.soTien = soTien;
        return this;
    }

    public HashMap<String, Object> objectPhieuNhapChiTiet() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idPhieuNhapCT", this.idPhieuNhapCT);
        data.put("idPhieuNhap", this.idPhieuNhap);
        data.put("tenSP", this.tenSP);
        data.put("soLuongNhap", this.soLuongNhap);
        data.put("soTien", this.soTien);

        return data;
    }
}
