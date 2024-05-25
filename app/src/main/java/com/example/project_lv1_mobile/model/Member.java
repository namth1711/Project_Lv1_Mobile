package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class Member implements Serializable {
    private String idMember, idAccount, firtName, lastName, email, gender, imageMember;
    private int rank, status;

    public Member() {
    }

    public Member(String idMember, String idAccount, String firtName, String lastName, String email, String gender, String imageMember, int rank, int status) {
        this.idMember = idMember;
        this.idAccount = idAccount;
        this.firtName = firtName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.imageMember = imageMember;
        this.rank = rank;
        this.status = status;
    }

    public String getIdMember() {
        return idMember;
    }

    public Member setIdMember(String idMember) {
        this.idMember = idMember;
        return this;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public Member setIdAccount(String idAccount) {
        this.idAccount = idAccount;
        return this;
    }

    public String getFirtName() {
        return firtName;
    }

    public Member setFirtName(String firtName) {
        this.firtName = firtName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Member setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Member setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Member setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getImageMember() {
        return imageMember;
    }

    public Member setImageMember(String imageMember) {
        this.imageMember = imageMember;
        return this;
    }

    public int getRank() {
        return rank;
    }

    public Member setRank(int rank) {
        this.rank = rank;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Member setStatus(int status) {
        this.status = status;
        return this;
    }

    public HashMap<String, Object> objectMember() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idMember", this.idMember);
        data.put("idAccount", this.idAccount);
        data.put("firtName", this.firtName);
        data.put("lastName", this.lastName);
        data.put("email", this.email);
        data.put("gender", this.gender);
        data.put("imageMember", this.imageMember);
        data.put("rank", this.rank);
        data.put("status", this.status);

        return data;
    }
}
