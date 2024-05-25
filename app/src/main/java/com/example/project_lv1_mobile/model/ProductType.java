package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class ProductType implements Serializable {

    private String idType, nameProductType, typeImageUri;

    public ProductType() {
    }

    public ProductType(String idType, String nameProductType, String typeImageUri) {
        this.idType = idType;
        this.nameProductType = nameProductType;
        this.typeImageUri = typeImageUri;
    }

    public String getIdType() {
        return idType;
    }

    public ProductType setIdType(String idType) {
        this.idType = idType;
        return this;
    }

    public String getNameProductType() {
        return nameProductType;
    }

    public ProductType setNameProductType(String nameProductType) {
        this.nameProductType = nameProductType;
        return this;
    }

    public String getTypeImageUri() {
        return typeImageUri;
    }

    public ProductType setTypeImageUri(String typeImageUri) {
        this.typeImageUri = typeImageUri;
        return this;
    }

    public HashMap<String, Object> objectType() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idType", this.idType);
        data.put("nameProductType", this.nameProductType);
        data.put("typeImageUri", this.typeImageUri);

        return data;
    }
}
