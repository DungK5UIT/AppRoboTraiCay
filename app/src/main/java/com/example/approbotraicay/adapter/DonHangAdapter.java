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
        holder.tvId.setText("MĐH: #" + dh.getId());
        holder.tvNgay.setText(dh.getDate());
        
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvTong.setText(decimalFormat.format(dh.getTotal()) + "đ");

        String status;
        int color;
        switch (dh.getStatus()) {
            case DonHang.STATUS_PROCESSING:
                status = "Đang xử lý";
                color = android.graphics.Color.BLUE;
                break;
            case DonHang.STATUS_SHIPPING:
                status = "Đang giao";
                color = android.graphics.Color.CYAN;
                break;
            case DonHang.STATUS_COMPLETED:
                status = "Thành công";
                color = android.graphics.Color.GREEN;
                break;
            case DonHang.STATUS_CANCELLED:
                status = "Đã hủy";
                color = android.graphics.Color.RED;
                break;
            default:
                status = "Chờ xác nhận";
                color = android.graphics.Color.parseColor("#FF9800");
                break;
        }
        holder.tvStatus.setText(status);
        holder.tvStatus.setTextColor(color);

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
