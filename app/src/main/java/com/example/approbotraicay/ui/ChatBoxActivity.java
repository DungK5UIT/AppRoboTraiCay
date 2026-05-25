package com.example.approbotraicay.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.approbotraicay.R;
import com.example.approbotraicay.adapter.ChatBoxAdapter;
import com.example.approbotraicay.model.ChatBox;
import com.example.approbotraicay.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatBoxActivity - Màn hình Chat hỗ trợ tự động
 * Dev C: Xử lý logic chat + kết nối với SessionManager.
 * Dev B: Quản lý UI - RecyclerView, toolbar, input bar.
 */
public class ChatBoxActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etInput;
    private FloatingActionButton fabSend;
    private MaterialToolbar toolbar;

    private ChatBoxAdapter adapter;
    private List<ChatBoxAdapter.ChatMessage> messageList;
    private ChatBox chatBot;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        sessionManager = new SessionManager(this);
        chatBot = new ChatBox();
        messageList = new ArrayList<>();

        initView();
        sendGreeting();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_chat);
        rvMessages = findViewById(R.id.rv_chat_messages);
        etInput = findViewById(R.id.et_chat_input);
        fabSend = findViewById(R.id.fab_chat_send);

        // Toolbar back button
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); // Title được xử lý trong layout XML
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Auto-scroll to bottom
        rvMessages.setLayoutManager(layoutManager);
        adapter = new ChatBoxAdapter(messageList);
        rvMessages.setAdapter(adapter);

        // Send button
        fabSend.setOnClickListener(v -> sendMessage());

        // Also send via keyboard "Send" action
        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendGreeting() {
        String username = sessionManager.getUserName();
        String greeting;
        if (username != null && !username.isEmpty()) {
            greeting = "Xin chào " + username + "! 👋 Tôi là Bot hỗ trợ của Robot Trái Cây.\n\nTôi có thể giúp bạn:\n• Tra giá trái cây 🍊🍇🍎\n• Hướng dẫn đặt hàng 🛒\n• Thông tin giao hàng 🚚\n• Liên hệ hỗ trợ 📞\n\nBạn cần hỏi gì nào?";
        } else {
            greeting = "Xin chào! 👋 Tôi là Bot hỗ trợ của Robot Trái Cây. Bạn cần hỏi gì không?";
        }
        addBotMessage(greeting);
    }

    private void sendMessage() {
        String userText = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(userText)) return;

        // Show user message
        addUserMessage(userText);
        etInput.setText("");

        // Get bot response with a small delay to feel more natural
        rvMessages.postDelayed(() -> {
            String response = chatBot.getResponse(userText);
            addBotMessage(response);
        }, 400);
    }

    private void addBotMessage(String text) {
        messageList.add(new ChatBoxAdapter.ChatMessage(text, ChatBoxAdapter.TYPE_BOT));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvMessages.smoothScrollToPosition(messageList.size() - 1);
    }

    private void addUserMessage(String text) {
        messageList.add(new ChatBoxAdapter.ChatMessage(text, ChatBoxAdapter.TYPE_USER));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvMessages.smoothScrollToPosition(messageList.size() - 1);
    }
}
