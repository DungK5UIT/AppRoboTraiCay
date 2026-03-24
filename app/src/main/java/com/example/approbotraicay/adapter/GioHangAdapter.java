package com.example.approbotraicay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.GioHang;
import com.example.approbotraicay.utils.Utils;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;
    private CartUpdateListener listener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public GioHangAdapter(Context context, List<GioHang> gioHangList, CartUpdateListener listener) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.tvTen.setText(gioHang.getTensp());
        
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvGia.setText(decimalFormat.format(gioHang.getGiasp()) + "đ");
        holder.tvSoLuong.setText(String.valueOf(gioHang.getSoluong()));
        
        Glide.with(context)
                .load(gioHang.getHinhsp())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivHinh);

        holder.btnPlus.setOnClickListener(v -> {
            int sl = gioHang.getSoluong() + 1;
            gioHang.setSoluong(sl);
            holder.tvSoLuong.setText(String.valueOf(sl));
            listener.onCartUpdated();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (gioHang.getSoluong() > 1) {
                int sl = gioHang.getSoluong() - 1;
                gioHang.setSoluong(sl);
                holder.tvSoLuong.setText(String.valueOf(sl));
                listener.onCartUpdated();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            gioHangList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, gioHangList.size());
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHinh;
        TextView tvTen, tvGia, tvSoLuong;
        MaterialButton btnMinus, btnPlus;
        ImageButton btnRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinh = itemView.findViewById(R.id.iv_cart_hinh);
            tvTen = itemView.findViewById(R.id.tv_cart_ten);
            tvGia = itemView.findViewById(R.id.tv_cart_gia);
            tvSoLuong = itemView.findViewById(R.id.tv_cart_soluong);
            btnMinus = itemView.findViewById(R.id.btn_cart_minus);
            btnPlus = itemView.findViewById(R.id.btn_cart_plus);
            btnRemove = itemView.findViewById(R.id.btn_cart_remove);
        }
    }
}
