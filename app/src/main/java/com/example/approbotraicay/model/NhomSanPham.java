package com.example.approbotraicay.model;

import java.io.Serializable;

public class NhomSanPham implements Serializable {
    private int id;
    private String tenNhom;
    private String hinhAnh;
    public NhomSanPham() {}

    public NhomSanPham(int id, String tenNhom, String hinhAnh) {
        this.id = id;
        this.tenNhom = tenNhom;
        this.hinhAnh = hinhAnh;
    }

    public int getId() { return id; }
    public String getTenNhom() { return tenNhom; }
    public String getHinhAnh() { return hinhAnh; }
}
