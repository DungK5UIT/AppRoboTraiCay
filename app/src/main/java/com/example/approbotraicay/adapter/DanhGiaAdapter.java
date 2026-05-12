package com.example.approbotraicay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.model.DanhGia;
import java.util.List;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.ViewHolder> {
    private List<DanhGia> list;

    public DanhGiaAdapter(List<DanhGia> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_gia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhGia dg = list.get(position);
        holder.tvUser.setText(dg.getUsername());
        holder.tvDate.setText(dg.getDate());
        holder.tvComment.setText(dg.getComment());
        holder.rb.setRating(dg.getRating());
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvDate, tvComment;
        RatingBar rb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tv_rev_user);
            tvDate = itemView.findViewById(R.id.tv_rev_date);
            tvComment = itemView.findViewById(R.id.tv_rev_comment);
            rb = itemView.findViewById(R.id.rb_rev_item);
        }
    }
}
