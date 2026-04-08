package com.example.approbotraicay.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int id; // maps to masp
    private String tenSanPham; // maps to tensp
    private double gia; // maps to dongia
    private String moTa;
    private int idNhom; // maps to maso
    private byte[] hinhAnhBlob; // maps to anh (BLOB)

    public SanPham() {}

    public SanPham(int id, String tenSanPham, double gia, String moTa, int idNhom, byte[] hinhAnhBlob) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.moTa = moTa;
        this.idNhom = idNhom;
        this.hinhAnhBlob = hinhAnhBlob;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public int getIdNhom() { return idNhom; }
    public void setIdNhom(int idNhom) { this.idNhom = idNhom; }
    public byte[] getHinhAnhBlob() { return hinhAnhBlob; }
    public void setHinhAnhBlob(byte[] hinhAnhBlob) { this.hinhAnhBlob = hinhAnhBlob; }
}
