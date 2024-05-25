package com.example.project_lv1_mobile.model;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private String idProduct, idProductType, productName, productImageUri;
    private int quantity, unitPrice, status;

    public Product() {
    }

    public Product(String idProduct, String idProductType, String productName, String productImageUri, int quantity, int unitPrice, int status) {
        this.idProduct = idProduct;
        this.idProductType = idProductType;
        this.productName = productName;
        this.productImageUri = productImageUri;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = status;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public Product setIdProduct(String idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public String getIdProductType() {
        return idProductType;
    }

    public Product setIdProductType(String idProductType) {
        this.idProductType = idProductType;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public Product setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductImageUri() {
        return productImageUri;
    }

    public Product setProductImageUri(String productImageUri) {
        this.productImageUri = productImageUri;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public Product setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Product setStatus(int status) {
        this.status = status;
        return this;
    }

    public HashMap<String, Object> objectProduct() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("idProduct", this.idProduct);
        data.put("idProductType", this.idProductType);
        data.put("productName", this.productName);
        data.put("productImageUri", this.productImageUri);
        data.put("quantity", this.quantity);
        data.put("unitPrice", this.unitPrice);
        data.put("status", this.status);

        return data;
    }
}
