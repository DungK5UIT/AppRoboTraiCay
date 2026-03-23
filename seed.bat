@echo off
set BASE_URL=https://approbotraicay-3e3f3-default-rtdb.asia-southeast1.firebasedatabase.app

curl -X POST -H "Content-Type: application/json" -d "{\"tenSanPham\":\"Táo Envy Mỹ\",\"gia\":150000,\"hinhAnh\":\"https://cdn.tgdd.vn/Products/Images/8788/282766/bhx/tao-envy-my-dong-hop-1kg-4-5-trai-202206101416468761_300x300.jpg\",\"moTa\":\"Táo Envy nhập khẩu từ Mỹ, giòn ngọt.\",\"idNhom\":2}" %BASE_URL%/sanpham.json
curl -X POST -H "Content-Type: application/json" -d "{\"tenSanPham\":\"Xoài Cát Hòa Lộc\",\"gia\":85000,\"hinhAnh\":\"https://cdn.tgdd.vn/Products/Images/8788/282542/bhx/xoai-cat-hoa-loc-1kg-2-4-trai-202206060851498877_300x300.jpg\",\"moTa\":\"Xoài cát đặc sản Tiền Giang.\",\"idNhom\":1}" %BASE_URL%/sanpham.json
curl -X POST -H "Content-Type: application/json" -d "{\"tenSanPham\":\"Nho Mẫu Đơn\",\"gia\":450000,\"hinhAnh\":\"https://cdn.tgdd.vn/Products/Images/8788/290822/bhx/nho-mau-don-han-quoc-hop-1kg-2-3-chum-202208221516087541_300x300.jpg\",\"moTa\":\"Nho mẫu đơn Hàn Quốc cao cấp.\",\"idNhom\":2}" %BASE_URL%/sanpham.json

curl -X POST -H "Content-Type: application/json" -d "{\"tenNhom\":\"Trái Cây Nội\",\"hinhAnh\":\"ic_fruit_local\"}" %BASE_URL%/nhomsanpham.json
curl -X POST -H "Content-Type: application/json" -d "{\"tenNhom\":\"Trái Cây Nhập\",\"hinhAnh\":\"ic_fruit_import\"}" %BASE_URL%/nhomsanpham.json
curl -X POST -H "Content-Type: application/json" -d "{\"tenNhom\":\"Combo Quà Tặng\",\"hinhAnh\":\"ic_fruit_combo\"}" %BASE_URL%/nhomsanpham.json
