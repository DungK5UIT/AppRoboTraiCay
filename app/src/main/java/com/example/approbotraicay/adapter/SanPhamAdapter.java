package com.example.approbotraicay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.GioHang;
import com.example.approbotraicay.model.SanPham;
import com.example.approbotraicay.utils.Utils;
import java.text.DecimalFormat;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private List<SanPham> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SanPham sp);
    }

    public SanPhamAdapter(List<SanPham> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sp = list.get(position);
        holder.tvTenSp.setText(sp.getTenSanPham());
        
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvGiaSp.setText(decimalFormat.format(sp.getGia()) + "đ");

        Glide.with(holder.itemView.getContext())
                .load(sp.getHinhAnh())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivHinhSp);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(sp));

        holder.btnAdd.setOnClickListener(v -> {
            themVaoGioHang(sp, holder.itemView.getContext());
        });
    }

    private void themVaoGioHang(SanPham sanPham, android.content.Context context) {
        if (sanPham == null) return;
        boolean exists = false;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            if (Utils.manggiohang.get(i).getIdsp() == sanPham.getId()) {
                Utils.manggiohang.get(i).setSoluong(Utils.manggiohang.get(i).getSoluong() + 1);
                exists = true;
                break;
            }
        }
        if (!exists) {
            GioHang gioHang = new GioHang();
            gioHang.setGiasp((long) sanPham.getGia());
            gioHang.setSoluong(1);
            gioHang.setIdsp(sanPham.getId());
            gioHang.setTensp(sanPham.getTenSanPham());
            gioHang.setHinhsp(sanPham.getHinhAnh());
            Utils.manggiohang.add(gioHang);
        }
        Toast.makeText(context, "Đã thêm " + sanPham.getTenSanPham() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<SanPham> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSp, tvGiaSp;
        ImageView ivHinhSp;
        View btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSp = itemView.findViewById(R.id.tv_ten_san_pham);
            tvGiaSp = itemView.findViewById(R.id.tv_gia_san_pham);
            ivHinhSp = itemView.findViewById(R.id.iv_hinh_san_pham);
            btnAdd = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
