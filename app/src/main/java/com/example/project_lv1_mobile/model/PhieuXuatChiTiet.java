package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class PhieuXuatChiTiet implements Serializable {
    private String idPhieuXuatCT, idPhieuXuat, idMember, idProduct, tenSP;
    private int soLuongXuat, soTien;

    public PhieuXuatChiTiet() {
    }

    public PhieuXuatChiTiet(String idPhieuXuatCT, String idMember, String idProduct, int soLuongXuat, int soTien) {
        this.idPhieuXuatCT = idPhieuXuatCT;
        this.idMember = idMember;
        this.idProduct = idProduct;
        this.soLuongXuat = soLuongXuat;
        this.soTien = soTien;
    }

    public String getIdPhieuXuatCT() {
        return idPhieuXuatCT;
    }

    public PhieuXuatChiTiet setIdPhieuXuatCT(String idPhieuXuatCT) {
        this.idPhieuXuatCT = idPhieuXuatCT;
        return this;
    }

    public String getIdPhieuXuat() {
        return idPhieuXuat;
    }

    public PhieuXuatChiTiet setIdPhieuXuat(String idPhieuXuat) {
        this.idPhieuXuat = idPhieuXuat;
        return this;
    }

    public String getIdMember() {
        return idMember;
    }

    public PhieuXuatChiTiet setIdMember(String idMember) {
        this.idMember = idMember;
        return this;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public PhieuXuatChiTiet setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public String getTenSP() {
        return tenSP;
    }

    public PhieuXuatChiTiet setTenSP(String tenSP) {
        this.tenSP = tenSP;
        return this;
    }

    public int getSoLuongXuat() {
        return soLuongXuat;
    }

    public PhieuXuatChiTiet setSoLuongXuat(int soLuongXuat) {
        this.soLuongXuat = soLuongXuat;
        return this;
    }

    public int getSoTien() {
        return soTien;
    }

    public PhieuXuatChiTiet setSoTien(int soTien) {
        this.soTien = soTien;
        return this;
    }

    public HashMap<String, Object> objectPhieuXuatChiTiet() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idPhieuXuatCT", this.idPhieuXuatCT);
        data.put("idPhieuXuat", this.idPhieuXuat);
        data.put("tenSP", this.tenSP);
        data.put("soLuongXuat", this.soLuongXuat);
        data.put("soTien", this.soTien);

        return data;
    }
}
