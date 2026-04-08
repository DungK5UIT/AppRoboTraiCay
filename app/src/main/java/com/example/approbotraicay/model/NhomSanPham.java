package com.example.approbotraicay.model;

import java.io.Serializable;

public class NhomSanPham implements Serializable {
    private int id; // maps to maso (converted to int if possible, or kept as is)
    private String tenNhom; // maps to tennhom
    private byte[] hinhAnhBlob; // maps to anh (BLOB)

    public NhomSanPham() {}

    public NhomSanPham(int id, String tenNhom, byte[] hinhAnhBlob) {
        this.id = id;
        this.tenNhom = tenNhom;
        this.hinhAnhBlob = hinhAnhBlob;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTenNhom() { return tenNhom; }
    public void setTenNhom(String tenNhom) { this.tenNhom = tenNhom; }
    public byte[] getHinhAnhBlob() { return hinhAnhBlob; }
    public void setHinhAnhBlob(byte[] hinhAnhBlob) { this.hinhAnhBlob = hinhAnhBlob; }
}
