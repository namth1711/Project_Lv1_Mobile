package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class PhieuXuat implements Serializable {
    private String idPhieuXuat, idMember, memberName, ngayXuat;
    private int tongSoSPXuat, tongTien, thue;

    public PhieuXuat() {
    }

    public PhieuXuat(String idPhieuXuat, String idMember, String memberName, String ngayXuat, int tongSoSPXuat, int tongTien, int thue) {
        this.idPhieuXuat = idPhieuXuat;
        this.idMember = idMember;
        this.memberName = memberName;
        this.ngayXuat = ngayXuat;
        this.tongSoSPXuat = tongSoSPXuat;
        this.tongTien = tongTien;
        this.thue = thue;
    }

    public String getIdPhieuXuat() {
        return idPhieuXuat;
    }

    public PhieuXuat setIdPhieuXuat(String idPhieuXuat) {
        this.idPhieuXuat = idPhieuXuat;
        return this;
    }

    public String getIdMember() {
        return idMember;
    }

    public PhieuXuat setIdMember(String idMember) {
        this.idMember = idMember;
        return this;
    }

    public String getMemberName() {
        return memberName;
    }

    public PhieuXuat setMemberName(String memberName) {
        this.memberName = memberName;
        return this;
    }

    public String getNgayXuat() {
        return ngayXuat;
    }

    public PhieuXuat setNgayXuat(String ngayXuat) {
        this.ngayXuat = ngayXuat;
        return this;
    }

    public int getTongSoSPXuat() {
        return tongSoSPXuat;
    }

    public PhieuXuat setTongSoSPXuat(int tongSoSPXuat) {
        this.tongSoSPXuat = tongSoSPXuat;
        return this;
    }

    public int getTongTien() {
        return tongTien;
    }

    public PhieuXuat setTongTien(int tongTien) {
        this.tongTien = tongTien;
        return this;
    }

    public int getThue() {
        return thue;
    }

    public PhieuXuat setThue(int thue) {
        this.thue = thue;
        return this;
    }

    public HashMap<String, Object> objectPhieuXuat() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idPhieuXuat", this.idPhieuXuat);
        data.put("idMember", this.idMember);
        data.put("memberName", this.memberName);
        data.put("ngayXuat", this.ngayXuat);
        data.put("tongSoSPXuat", this.tongSoSPXuat);
        data.put("tongTien", this.tongTien);
        data.put("thue", this.thue);

        return data;
    }
}
