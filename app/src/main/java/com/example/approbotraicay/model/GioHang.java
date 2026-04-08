package com.example.approbotraicay.model;

import java.io.Serializable;

public class GioHang implements Serializable {
    private int idsp;
    private String tensp;
    private long giasp;
    private String hinhsp;
    private byte[] hinhAnhBlob;
    private int soluong;

    public GioHang() {
    }

    public GioHang(int idsp, String tensp, long giasp, String hinhsp, byte[] hinhAnhBlob, int soluong) {
        this.idsp = idsp;
        this.tensp = tensp;
        this.giasp = giasp;
        this.hinhsp = hinhsp;
        this.hinhAnhBlob = hinhAnhBlob;
        this.soluong = soluong;
    }

    public int getIdsp() { return idsp; }
    public void setIdsp(int idsp) { this.idsp = idsp; }
    public String getTensp() { return tensp; }
    public void setTensp(String tensp) { this.tensp = tensp; }
    public long getGiasp() { return giasp; }
    public void setGiasp(long giasp) { this.giasp = giasp; }
    public String getHinhsp() { return hinhsp; }
    public void setHinhsp(String hinhsp) { this.hinhsp = hinhsp; }
    public byte[] getHinhAnhBlob() { return hinhAnhBlob; }
    public void setHinhAnhBlob(byte[] hinhAnhBlob) { this.hinhAnhBlob = hinhAnhBlob; }
    public int getSoluong() { return soluong; }
    public void setSoluong(int soluong) { this.soluong = soluong; }
}
