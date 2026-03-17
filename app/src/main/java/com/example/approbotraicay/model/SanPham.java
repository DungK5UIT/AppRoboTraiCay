package com.example.approbotraicay.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int id;
    private String tenSanPham;
    private double gia;
    private String hinhAnh;
    private String moTa;
    private int idNhom;

    public SanPham(int id, String tenSanPham, double gia, String hinhAnh, String moTa, int idNhom) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.idNhom = idNhom;
    }

    public int getId() { return id; }
    public String getTenSanPham() { return tenSanPham; }
    public double getGia() { return gia; }
    public String getHinhAnh() { return hinhAnh; }
    public String getMoTa() { return moTa; }
    public int getIdNhom() { return idNhom; }
}
