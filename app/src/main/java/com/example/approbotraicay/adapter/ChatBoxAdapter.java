package com.example.approbotraicay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import java.util.List;

/**
 * ChatBoxAdapter - Adapter cho RecyclerView hiển thị tin nhắn chat
 * Mỗi item có thể là tin nhắn của Bot (trái) hoặc User (phải).
 * Dev B: Phụ trách hiển thị giao diện chat bong bóng.
 */
public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.ChatViewHolder> {

    public static final int TYPE_BOT = 0;
    public static final int TYPE_USER = 1;

    public static class ChatMessage {
        public String content;
        public int type; // TYPE_BOT or TYPE_USER

        public ChatMessage(String content, int type) {
            this.content = content;
            this.type = type;
        }
    }

    private List<ChatMessage> messages;

    public ChatBoxAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).type;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_bubble, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        if (msg.type == TYPE_BOT) {
            holder.llBot.setVisibility(View.VISIBLE);
            holder.llUser.setVisibility(View.GONE);
            holder.tvBotMessage.setText(msg.content);
            // Animate in
            holder.llBot.setAlpha(0f);
            holder.llBot.setTranslationX(-20f);
            holder.llBot.animate().alpha(1f).translationX(0f).setDuration(250).start();
        } else {
            holder.llUser.setVisibility(View.VISIBLE);
            holder.llBot.setVisibility(View.GONE);
            holder.tvUserMessage.setText(msg.content);
            // Animate in
            holder.llUser.setAlpha(0f);
            holder.llUser.setTranslationX(20f);
            holder.llUser.animate().alpha(1f).translationX(0f).setDuration(250).start();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llBot, llUser;
        TextView tvBotMessage, tvUserMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            llBot = itemView.findViewById(R.id.ll_bot_message);
            llUser = itemView.findViewById(R.id.ll_user_message);
            tvBotMessage = itemView.findViewById(R.id.tv_bot_message);
            tvUserMessage = itemView.findViewById(R.id.tv_user_message);
        }
    }
}
