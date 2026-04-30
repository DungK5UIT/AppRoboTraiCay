package com.example.approbotraicay.model;

import java.io.Serializable;

public class DonHang implements Serializable {
    private int id;
    private String username;
    private String fullName;
    private String phone;
    private String address;
    private double total;
    private double shippingFee;
    private String date;
    private int status;

    // Status Constants
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_SHIPPING = 2;
    public static final int STATUS_COMPLETED = 3;
    public static final int STATUS_CANCELLED = 4;

    public DonHang() {
    }

    public DonHang(int id, String username, String fullName, String phone, String address, double total, double shippingFee, String date, int status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.shippingFee = shippingFee;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
