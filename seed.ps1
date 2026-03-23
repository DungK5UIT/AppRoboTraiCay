$baseUrl = "https://approbotraicay-3e3f3-default-rtdb.asia-southeast1.firebasedatabase.app"

function Post-Data ($endpoint, $body) {
    Invoke-RestMethod -Uri "$baseUrl/$endpoint" -Method Post -Body $body -ContentType "application/json"
}

Post-Data "sanpham.json" '{"tenSanPham":"Táo Envy Mỹ","gia":150000,"hinhAnh":"https://cdn.tgdd.vn/Products/Images/8788/282766/bhx/tao-envy-my-dong-hop-1kg-4-5-trai-202206101416468761_300x300.jpg","moTa":"Táo Envy nhập khẩu từ Mỹ, giòn ngọt.","idNhom":2}'
Post-Data "sanpham.json" '{"tenSanPham":"Xoài Cát Hòa Lộc","gia":85000,"hinhAnh":"https://cdn.tgdd.vn/Products/Images/8788/282542/bhx/xoai-cat-hoa-loc-1kg-2-4-trai-202206060851498877_300x300.jpg","moTa":"Xoài cát đặc sản Tiền Giang.","idNhom":1}'
Post-Data "sanpham.json" '{"tenSanPham":"Nho Mẫu Đơn","gia":450000,"hinhAnh":"https://cdn.tgdd.vn/Products/Images/8788/290822/bhx/nho-mau-don-han-quoc-hop-1kg-2-3-chum-202208221516087541_300x300.jpg","moTa":"Nho mẫu đơn Hàn Quốc cao cấp.","idNhom":2}'

Post-Data "nhomsanpham.json" '{"tenNhom":"Trái Cây Nội","hinhAnh":"ic_fruit_local"}'
Post-Data "nhomsanpham.json" '{"tenNhom":"Trái Cây Nhập","hinhAnh":"ic_fruit_import"}'
Post-Data "nhomsanpham.json" '{"tenNhom":"Combo Quà Tặng","hinhAnh":"ic_fruit_combo"}'

Write-Host "Done seeding!"
