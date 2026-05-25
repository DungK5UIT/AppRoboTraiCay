package com.example.approbotraicay.model;

/**
 * ChatBox - Bot hỗ trợ tự động cho ứng dụng Robot Trái Cây
 * Kế thừa và mở rộng logic phản hồi từ Appbanhang gốc.
 * Dev B / Dev C: Cung cấp logic trả lời tự động dựa trên từ khóa.
 */
public class ChatBox {

    public String getResponse(String userInput) {
        // Chuyển đổi input về chữ thường để dễ so sánh
        String input = userInput.toLowerCase().trim();

        // --- Chào hỏi ---
        if (input.contains("xin chào") || input.contains("hello") || input.contains("hi") || input.equals("chào")) {
            return "Xin chào! 👋 Tôi là Bot hỗ trợ của Robot Trái Cây. Tôi có thể giúp bạn tìm hiểu về giá cả, nguồn gốc trái cây và cách đặt hàng!";
        }

        // --- Giá trái cây ---
        if (input.contains("giá trái cây") || input.contains("giá hoa quả") || input.contains("bảng giá")) {
            return "Giá trái cây hôm nay có nhiều loại! Bạn muốn hỏi về loại nào? (cam, nho, táo, dưa hấu, kiwi, đu đủ, đào, bơ, lựu, xoài, lê...)";
        }

        // --- Từng loại trái cây ---
        if (input.contains("giá nho") || input.contains("nho đỏ") || input.contains("nho xanh")) {
            return "🍇 Giá nho hôm nay:\n• Nho đỏ: 75,000đ/kg\n• Nho xanh: 60,000đ/kg";
        }
        if (input.contains("giá cam") || input.contains("cam sành") || input.contains("cam canh")) {
            return "🍊 Giá cam hôm nay:\n• Cam sành: 30,000đ/kg\n• Cam canh: 22,000đ/kg";
        }
        if (input.contains("giá táo") || input.contains("táo mỹ") || input.contains("táo tây")) {
            return "🍎 Giá táo hôm nay:\n• Táo Tây Bắc: 23,000đ/kg\n• Táo Mỹ: 40,000đ/kg";
        }
        if (input.contains("giá dưa hấu") || input.contains("dưa hấu")) {
            return "🍉 Giá dưa hấu hôm nay: 12,000đ/kg";
        }
        if (input.contains("giá lựu") || input.contains("lựu đỏ")) {
            return "Giá lựu đỏ hôm nay: 45,000đ/kg";
        }
        if (input.contains("giá kiwi") || input.contains("kiwi")) {
            return "🥝 Giá kiwi hôm nay: 35,000đ/kg";
        }
        if (input.contains("giá đu đủ") || input.contains("đu đủ")) {
            return "Giá đu đủ hôm nay: 16,000đ/kg";
        }
        if (input.contains("giá đào") || input.contains("đào mỹ") || input.contains("đào hồng")) {
            return "🍑 Giá đào hôm nay:\n• Đào Mỹ: 55,000đ/kg\n• Đào hồng: 45,000đ/kg";
        }
        if (input.contains("giá bơ") || input.contains("bơ 34") || input.contains("bơ tròn")) {
            return "🥑 Giá bơ hôm nay:\n• Bơ 34: 21,000đ/kg\n• Bơ tròn: 17,000đ/kg";
        }
        if (input.contains("giá xoài") || input.contains("xoài cát") || input.contains("xoài")) {
            return "🥭 Giá xoài hôm nay:\n• Xoài cát Hòa Lộc: 65,000đ/kg\n• Xoài Đài Loan: 45,000đ/kg";
        }
        if (input.contains("giá lê") || input.contains("lê hàn") || input.contains("lê mỹ")) {
            return "Giá lê hôm nay:\n• Lê Hàn Quốc: 80,000đ/kg\n• Lê Mỹ: 55,000đ/kg";
        }
        if (input.contains("giá dâu") || input.contains("dâu tây")) {
            return "🍓 Giá dâu tây hôm nay: 120,000đ/kg";
        }
        if (input.contains("giá nước ép") || input.contains("nước ép")) {
            return "🧃 Giá nước ép:\n• Nước ép cam: 45,000đ/chai\n• Nước ép xoài: 40,000đ/chai\n• Nước ép dưa hấu: 35,000đ/chai";
        }

        // --- Nguồn gốc ---
        if (input.contains("hoa quả nhập từ đâu") || input.contains("nguồn gốc") || input.contains("nhập khẩu") || input.contains("nhập từ")) {
            return "🌍 Trái cây của shop được nhập từ nhiều quốc gia:\n• Mỹ: Táo, Đào, Lê\n• Úc: Cam, Kiwi\n• Thái Lan: Xoài, Dưa hấu\n• Nội địa: Bơ, Ổi, Dâu tây";
        }

        // --- Đặt hàng ---
        if (input.contains("đặt hàng") || input.contains("mua hàng") || input.contains("mua như thế nào") || input.contains("cách mua")) {
            return "🛒 Để đặt hàng, bạn có thể:\n1. Chọn sản phẩm từ Trang chủ\n2. Nhấn \"Thêm vào giỏ hàng\"\n3. Vào Giỏ hàng → Thanh toán\n4. Điền thông tin giao hàng và xác nhận!";
        }

        // --- Giao hàng ---
        if (input.contains("giao hàng") || input.contains("ship") || input.contains("phí ship") || input.contains("vận chuyển")) {
            return "🚚 Phí giao hàng: 30,000đ/đơn hàng.\nMiễn phí giao hàng cho đơn từ 300,000đ trở lên!";
        }

        // --- Thanh toán ---
        if (input.contains("thanh toán") || input.contains("trả tiền") || input.contains("phương thức")) {
            return "💳 Phương thức thanh toán:\n• Tiền mặt khi nhận hàng (COD)\n• Chuyển khoản ngân hàng\n• Ví điện tử";
        }

        // --- Thời gian ---
        if (input.contains("bao lâu") || input.contains("mấy ngày") || input.contains("thời gian giao")) {
            return "⏰ Thời gian giao hàng:\n• Nội thành: 2-4 tiếng\n• Ngoại thành: 1-2 ngày";
        }

        // --- Liên hệ ---
        if (input.contains("liên hệ") || input.contains("điện thoại") || input.contains("hotline") || input.contains("số điện thoại") || input.contains("thông tin liên hệ")) {
            return "📞 Thông tin liên hệ:\n• Hotline: 0367 456 697\n• Email: support@robotraicay.vn\n• Giờ làm việc: 7:00 - 21:00 hàng ngày";
        }

        // --- Tên bot ---
        if (input.contains("tên của") || input.contains("bạn là ai") || input.contains("mày là ai") || input.contains("ai vậy")) {
            return "🤖 Tôi là Bot hỗ trợ tự động của cửa hàng Robot Trái Cây. Chủ cửa hàng là bà Rupbis. Tôi luôn sẵn sàng giải đáp mọi thắc mắc của bạn!";
        }

        // --- Cảm ơn ---
        if (input.contains("cảm ơn") || input.contains("thank") || input.contains("thanks")) {
            return "😊 Không có gì! Rất vui được phục vụ bạn. Chúc bạn mua sắm vui vẻ tại Robot Trái Cây!";
        }

        // --- Tạm biệt ---
        if (input.contains("tạm biệt") || input.contains("bye") || input.contains("goodbye")) {
            return "👋 Tạm biệt! Hẹn gặp lại bạn lần sau. Chúc bạn một ngày tốt lành!";
        }

        // --- Mặc định ---
        return "❓ Xin lỗi, tôi chưa hiểu câu hỏi của bạn.\nBạn có thể hỏi về:\n• Giá trái cây (cam, nho, táo...)\n• Cách đặt hàng\n• Phí giao hàng\n• Thông tin liên hệ\n\nHoặc gọi hotline: 0367 456 697 để được hỗ trợ trực tiếp!";
    }
}
