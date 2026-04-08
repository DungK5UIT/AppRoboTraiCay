package com.example.approbotraicay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.NhomSanPham;
import java.util.List;

public class NhomSanPhamAdapter extends RecyclerView.Adapter<NhomSanPhamAdapter.ViewHolder> {
    private List<NhomSanPham> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NhomSanPham nhom);
    }

    public NhomSanPhamAdapter(List<NhomSanPham> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhom_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhomSanPham nhom = list.get(position);
        holder.tvTenNhom.setText(nhom.getTenNhom());
        
        Glide.with(holder.itemView.getContext())
                .load(nhom.getHinhAnhBlob())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivHinhNhom);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(nhom));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenNhom;
        ImageView ivHinhNhom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenNhom = itemView.findViewById(R.id.tv_ten_nhom);
            ivHinhNhom = itemView.findViewById(R.id.iv_hinh_nhom);
        }
    }
}
