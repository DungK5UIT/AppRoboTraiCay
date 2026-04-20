package com.example.approbotraicay.api;

import com.example.approbotraicay.model.NhomSanPham;
import com.example.approbotraicay.model.SanPham;
import com.example.approbotraicay.model.TaiKhoan;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // San Pham
    @GET("sanpham.json")
    Call<Map<String, SanPham>> getSanPham();

    @POST("sanpham.json")
    Call<SanPham> postSanPham(@Body SanPham sp);

    // Nhom San Pham
    @GET("nhomsanpham.json")
    Call<Map<String, NhomSanPham>> getNhomSanPham();

    // User / Auth
    @GET("taikhoan.json")
    Call<Map<String, TaiKhoan>> getUsers();

    @POST("taikhoan.json")
    Call<TaiKhoan> postUser(@Body TaiKhoan user);

    @PUT("taikhoan/{username}.json")
    Call<TaiKhoan> updateUser(@Path("username") String username, @Body TaiKhoan user);
}
