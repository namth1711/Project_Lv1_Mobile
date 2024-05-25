package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class PhieuNhap implements Serializable {
    private String idPhieuNhap, idMember, memberName, ngayNhap;
    private int tongSoSPNhap, tongTien;

    public PhieuNhap() {
    }

    public int getTongSoSPNhap() {
        return tongSoSPNhap;
    }

    public PhieuNhap setTongSoSPNhap(int tongSoSPNhap) {
        this.tongSoSPNhap = tongSoSPNhap;
        return this;
    }

    public PhieuNhap(String idPhieuNhap, String idMember, String memberName, String ngayNhap, int tongSoSPNhap, int tongTien) {
        this.idPhieuNhap = idPhieuNhap;
        this.idMember = idMember;
        this.memberName = memberName;
        this.ngayNhap = ngayNhap;
        this.tongSoSPNhap = tongSoSPNhap;
        this.tongTien = tongTien;
    }

    public String getIdPhieuNhap() {
        return idPhieuNhap;
    }

    public PhieuNhap setIdPhieuNhap(String idPhieuNhap) {
        this.idPhieuNhap = idPhieuNhap;
        return this;
    }

    public String getMemberName() {
        return memberName;
    }

    public PhieuNhap setMemberName(String memberName) {
        this.memberName = memberName;
        return this;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public PhieuNhap setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
        return this;
    }

    public String getIdMember() {
        return idMember;
    }

    public PhieuNhap setIdMember(String idMember) {
        this.idMember = idMember;
        return this;
    }

    public int getTongTien() {
        return tongTien;
    }

    public PhieuNhap setTongTien(int tongTien) {
        this.tongTien = tongTien;
        return this;
    }

    public HashMap<String, Object> objectPhieuNhap() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idPhieuNhap", this.idPhieuNhap);
        data.put("idMember", this.idMember);
        data.put("memberName", this.memberName);
        data.put("ngayNhap", this.ngayNhap);
        data.put("tongSoSPNhap", this.tongSoSPNhap);
        data.put("tongTien", this.tongTien);

        return data;
    }
}
