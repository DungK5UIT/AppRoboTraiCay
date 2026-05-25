package com.example.approbotraicay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.DonHang;
import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {
    private List<DonHang> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DonHang dh);
    }

    public DonHangAdapter(List<DonHang> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonHang dh = list.get(position);
        holder.tvId.setText("Đơn hàng #" + dh.getId());
        holder.tvNgay.setText(dh.getDate());
        
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvTong.setText(decimalFormat.format(dh.getTotal()) + "đ");

        String status;
        int bgColor;
        switch (dh.getStatus()) {
            case DonHang.STATUS_PROCESSING:
                status = "Đang xử lý";
                bgColor = android.graphics.Color.parseColor("#FF9800"); // Orange
                break;
            case DonHang.STATUS_SHIPPING:
                status = "Đang giao 🚚";
                bgColor = android.graphics.Color.parseColor("#2196F3"); // Blue
                break;
            case DonHang.STATUS_COMPLETED:
                status = "✅ Đã giao";
                bgColor = android.graphics.Color.parseColor("#4CAF50"); // Green
                break;
            case DonHang.STATUS_CANCELLED:
                status = "❌ Đã hủy";
                bgColor = android.graphics.Color.parseColor("#F44336"); // Red
                break;
            default:
                status = "Chờ xác nhận";
                bgColor = android.graphics.Color.parseColor("#9C27B0"); // Purple
                break;
        }
        holder.tvStatus.setText(status);
        // Set rounded badge background color
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setColor(bgColor);
        bg.setCornerRadius(32f);
        holder.tvStatus.setBackground(bg);
        holder.tvStatus.setTextColor(android.graphics.Color.WHITE);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(dh));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvNgay, tvTong, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_item_don_hang_id);
            tvNgay = itemView.findViewById(R.id.tv_item_don_hang_ngay);
            tvTong = itemView.findViewById(R.id.tv_item_don_hang_tongtien);
            tvStatus = itemView.findViewById(R.id.tv_item_don_hang_trangthai);
        }
    }
}
