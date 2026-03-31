package com.example.approbotraicay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.ChiTietDonHang;
import java.text.DecimalFormat;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ViewHolder> {
    private List<ChiTietDonHang> list;

    public ChiTietDonHangAdapter(List<ChiTietDonHang> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tiet_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChiTietDonHang ctdh = list.get(position);
        holder.tvTen.setText(ctdh.getProductName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String giaSl = decimalFormat.format(ctdh.getPrice()) + "đ x " + ctdh.getQuantity();
        holder.tvGiaSl.setText(giaSl);
        
        holder.tvThanhTien.setText(decimalFormat.format(ctdh.getPrice() * ctdh.getQuantity()) + "đ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGiaSl, tvThanhTien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tv_item_ctdh_ten);
            tvGiaSl = itemView.findViewById(R.id.tv_item_ctdh_gia_sl);
            tvThanhTien = itemView.findViewById(R.id.tv_item_ctdh_thanhtien);
        }
    }
}
